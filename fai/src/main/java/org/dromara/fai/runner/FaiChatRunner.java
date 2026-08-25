package org.dromara.fai.runner;

import com.dtflys.forest.Forest;
import com.dtflys.forest.auth.BearerAuth;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestSSE;
import com.dtflys.forest.mapping.MappingTemplate;
import com.dtflys.forest.sse.SSELinesMode;
import com.dtflys.forest.utils.StringUtils;
import com.dtflys.forest.utils.TypeReference;
import org.dromara.fai.Fai;
import org.dromara.fai.FaiModel;
import org.dromara.fai.callback.OnMessage;
import org.dromara.fai.callback.OnMessageStart;
import org.dromara.fai.callback.OnMessageStop;
import org.dromara.fai.callback.OnToolCalls;
import org.dromara.fai.chat.FaiChoiceResponse;
import org.dromara.fai.chat.FaiMessageResponse;
import org.dromara.fai.chat.FaiChat;
import org.dromara.fai.chat.FaiChatResponse;
import org.dromara.fai.chat.FaiToolCallsHandler;
import org.dromara.fai.config.FaiConfiguration;
import org.dromara.fai.param.ChatCompletionsParam;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class FaiChatRunner implements FaiRunner {
    
    public final static int STATE_START = 0;
    
    public final static int STATE_RUNNING = 1;
    
    public final static int STATE_STOP = 2;

    protected volatile int state = STATE_START;

    protected final FaiChat chat;

    protected volatile FaiChatResponse resultResponse;

    protected volatile FaiToolCallsHandler toolCallsHandler;
    
    protected volatile ForestSSE sse;

    public FaiChatRunner(FaiChat chat) {
        this.chat = chat;
    }


    @Override
    public FaiConfiguration config() {
        return chat.config();
    }

    @Override
    public FaiModel model() {
        return chat.model();
    }

    @Override
    public FaiChat chat() {
        return chat;
    }

    protected ForestSSE createSSE(final ForestRequest<?> request) {
        return request.sse();
    }

    protected void doSseListen() {
        if (sse != null) {
            sse.listen(SSELinesMode.SINGLE_LINE);
        }
    }

    @Override
    public FaiChatRunner run() {
        final FaiChat prompts = chat();
        final Consumer<FaiRunner> onPrepare = prompts.onPrepareCallback();
        final OnMessageStart onStart = prompts.onStartCallback();
        final OnMessage onMessage = Optional.ofNullable(prompts.onMessageCallback())
                .orElse((runner, res, sink) -> {
                    final String content = res.content();
                    if (null != content) {
                        sink.next(content);
                    }
                });
        final OnMessageStop onStop = prompts.onStopCallback();
        final OnToolCalls onToolCalls = prompts.onToolCallsCallback();
        if (onPrepare != null) {
            onPrepare.accept(this);
        }

        if (state == STATE_STOP) {
            return this;
        }

        final StringBuffer contentBuffer = new StringBuffer();
        final AtomicReference<String> responseRoleRef = new AtomicReference<>(null);
        final FaiModel model = model();
        final String baseUrl = model.baseUrl();
        final String apiBasePath = model.apiBasePath();
        final String path = model.apiChatPath();
        final ChatCompletionsParam param = prompts.param();

        final ForestRequest<?> request = Fai.http().post(path);

        for (final Map.Entry<String, Object> entry : prompts.env().entrySet()) {
            request.setVariable(entry.getKey(), entry.getValue());
        }

        final String bodyJson = request.getConfiguration().getJsonConverter().encodeToString(param);
        final MappingTemplate bodyTemplate = MappingTemplate.create(bodyJson);
        final String body = bodyTemplate.render(request, request.arguments(), true);

        request.readTimeout(Duration.ofHours(6)).logEnabled(chat().isLogEnabled())
                .authenticator(BearerAuth.token(model.apiKey()))
                .logEnabled(prompts.isLogEnabled())
                .basePath(baseUrl)
                .basePath(apiBasePath)
                .contentTypeJson()
                .addHeader("Accept", "application/json")
                .addBody(body);

        if (prompts.isStream()) {
            sse = createSSE(request)
                    .setOnOpen(event -> {
                        if (onStart != null) {
                            onStart.onMessageStart(this);
                            if (state == STATE_STOP) {
                                event.close();
                                return;
                            }
                        }
                        resultResponse = new FaiChatResponse();
                        state = STATE_RUNNING;
                    })
                    .setOnClose(event -> {
                        List<FaiChoiceResponse> choices = new ArrayList<>();
                        resultResponse.setChoices(choices);
                        final FaiMessageResponse delta = new FaiMessageResponse();
                        delta.setRole(responseRoleRef.get());
                        delta.setContent(contentBuffer.toString());
                        final FaiChoiceResponse choice = new FaiChoiceResponse();
                        choice.setDelta(delta);
                        choices.add(choice);
                        if (onStop != null) {
                            onStop.onMessageStop(this, resultResponse);
                        }
                        if (state == STATE_STOP) {
                            event.close();
                        }
                    })
                    .setOnMessage((event, sink) -> {
                        final String value = event.value();
                        if (value != null && !value.isEmpty() && value.charAt(0) == '{' && value.charAt(value.length() - 1) == '}') {
                            final FaiChatResponse response = event.value(FaiChatResponse.class);
                            response.setRawData(value);
                            if (response.isToolCalls()) {
                                if (toolCallsHandler == null) {
                                    synchronized (this) {
                                        if (toolCallsHandler == null) {
                                            toolCallsHandler = new FaiToolCallsHandler(this);
                                        }
                                    }
                                }
                                toolCallsHandler.add(response);
                            } else if (response.isFinished()) {
                                final String finishReason = response.finishReason();
                                if ("tool_calls".equals(finishReason) && toolCallsHandler != null) {
                                    final Object toolCallsResult = onToolCalls != null ?
                                            onToolCalls.onToolCalls(this, toolCallsHandler) :
                                            toolCallsHandler.call();
                                    toolCallsHandler = null;
                                    if (toolCallsResult != null) {
                                        response.setToolCallsResult(toolCallsResult);
                                        final String resultContent = 
                                                (toolCallsResult instanceof Map || toolCallsResult instanceof Collection || !(toolCallsResult instanceof CharSequence)) ?
                                                        Forest.config().getJsonConverter().encodeToString(toolCallsResult) :
                                                        String.valueOf(toolCallsResult);
                                        response.choices().get(0).delta().setContent(String.valueOf(resultContent));
                                        resultResponse = response;
                                    }
                                }
                            }
                            
                            if (!response.isToolCalls()) {
                                resultResponse.setId(response.getId());
                                resultResponse.setCreated(response.created());
                                resultResponse.setObject(response.object());
                                resultResponse.setModel(response.getModel());
                                
                                for (final FaiChoiceResponse choice : response.choices()) {
                                    final FaiMessageResponse delta = choice.delta();
                                    if (responseRoleRef.get() == null && StringUtils.isNotEmpty(delta.getRole())) {
                                        responseRoleRef.set(delta.getRole());
                                    }
                                    final String content = delta.getContent();
                                    contentBuffer.append(content == null ? "" : content);
                                }
                                if (onMessage != null) {
                                    onMessage.onMessage(this, response, new DefaultFaiSink(sink));
                                }
                                if (state == STATE_STOP) {
                                    event.close();
                                }
                            }
                        }
                    });
                    doSseListen();
        } else {
            String text = request.executeAsString();
            System.out.println(text);
            resultResponse = Forest.config().getJsonConverter().convertToJavaObject(text, FaiChatResponse.class);
            System.out.println(resultResponse);
        }
        return this;
    }

    @Override
    public void stop() {
        state = STATE_STOP;
        if (sse != null) {
            sse.close();
        }
    }

    @Override
    public void loop() {
        do {
            run();
        } while (state == STATE_RUNNING);
    }

    @Override
    public String content() {
        if (resultResponse == null) {
            return null;
        }
        return resultResponse.content();
    }


    public FaiChatResponse response() {
        return resultResponse;
    }

    @Override
    public <R> R content(Class<R> clazz) {
        if (resultResponse == null) {
            return null;
        }
        return resultResponse.resultAs(clazz);
    }

    @Override
    public <R> R content(TypeReference<R> clazz) {
        if (resultResponse == null) {
            return null;
        }
        return resultResponse.resultAs(clazz);

    }


    @Override
    public Object toolCallsResult() {
        if (resultResponse == null) {
            return null;
        }
        return resultResponse.toolCallsResult();
    }

    @Override
    public <R> R toolCallsResult(Class<R> clazz) {
        if (resultResponse == null) {
            return null;
        }
        return resultResponse.toolCallsResult(clazz);
    }

    @Override
    public <R> R toolCallsResult(TypeReference<R> typeReference) {
        if (resultResponse == null) {
            return null;
        }
        return resultResponse.toolCallsResult(typeReference);

    }

    @Override
    public Object get() {
        final Object result = toolCallsResult();
        if (result != null) {
            return result;
        }
        return content();
    }

    @Override
    public <R> R get(Class<R> clazz) {
        final R result = toolCallsResult(clazz);
        if (result != null) {
            return result;
        }
        return content(clazz);
    }

    @Override
    public <R> R get(TypeReference<R> typeReference) {
        final R result = toolCallsResult(typeReference);
        if (result != null) {
            return result;
        }
        return content(typeReference);
    }

    @Override
    public <R> List<R> list(Class<R> clazz) {
        return get(new TypeReference<List<R>>() {});
    }
}
