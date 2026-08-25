package org.dromara.fai.chat;

import cn.hutool.core.collection.CollectionUtil;
import com.dtflys.forest.utils.ReflectUtils;
import com.dtflys.forest.utils.TypeReference;
import org.dromara.fai.*;
import org.dromara.fai.callback.*;
import org.dromara.fai.config.FaiConfiguration;
import org.dromara.fai.content.FaiTextContent;
import org.dromara.fai.param.ChatCompletionsParam;
import org.dromara.fai.param.ChatMessageParam;
import org.dromara.fai.param.ResponseFormatParam;
import org.dromara.fai.runner.FaiChatRunner;
import org.dromara.fai.runner.FaiRunner;
import org.dromara.fai.tools.FaiTool;
import org.dromara.fai.utils.*;
import org.dromara.fai.utils.functions.*;
import org.dromara.fai.wrapper.FaiMethod;
import org.dromara.fai.wrapper.FaiObject;
import org.dromara.fai.wrapper.FaiType;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FaiChat implements Serializable {
    
    private final transient FaiModel model;
    
    private final List<FaiMessage> messages = new ArrayList<>();
    
    private String responseFormat;
    
    private FaiType responseFormatType;
    
    private boolean stream = false;
    
    private final Map<String, FaiTool> tools = new LinkedHashMap<>();
    
    private boolean logEnabled = true;
    
    private final Map<String, Object> environment = new ConcurrentHashMap<>();
    
    private Consumer<FaiRunner> onBeforeRequestCallback;

    private OnMessageStart onMessageStartCallback;

    private OnMessage onMessageCallback;
    
    private OnMessageStop onMessageStopCallback;

    private OnToolCalls onToolCallsCallback;
    

    public FaiChat(FaiModel model) {
        this(model, null);
    }

    public FaiChat(FaiModel model, Boolean stream) {
        this.model = model;
        if (stream != null) {
            this.stream = stream;
        }
    }

    public FaiConfiguration config() {
        return model.config();
    }

    public FaiChat tool(FaiVoidFunc0 func) {
        return lambda(func);
    }

    public FaiChat tool(FaiVoidFunc0 func, FaiNoParams noParams) {
        return lambda(func);
    }


    public <P> FaiChat tool(FaiVoidFunc1<P> func) {
        return lambda(func);
    }

    public <P1, P2> FaiChat tool(FaiVoidFunc2<P1, P2> func) {
        return lambda(func);
    }

    public <P1, P2, P3> FaiChat tool(FaiVoidFunc3<P1, P2, P3> func) {
        return lambda(func);
    }

    public <P1, P2, P3, P4> FaiChat tool(FaiVoidFunc4<P1, P2, P3, P4> func) {
        return lambda(func);
    }

    public <P1, P2, P3, P4, P5> FaiChat tool(FaiVoidFunc5<P1, P2, P3, P4, P5> func) {
        return lambda(func);
    }

    public <R> FaiChat tool(FaiFunc0<R> func) {
        return lambda(func);
    }

    public <R, P> FaiChat tool(FaiFunc1<R, P> func) {
        return lambda(func);
    }

    public <R, P> FaiChat tool(FaiFunc1<R, P> func, Class<P> type) {
        return lambda(func);
    }

    public <R, P1, P2> FaiChat tool(FaiFunc2<R, P1, P2> func) {
        return lambda(func);
    }

    public <R, P1, P2> FaiChat tool(FaiFunc2<R, P1, P2> func, Class<P1> type1, Class<P2> type2) {
        return lambda(func);
    }

    public <R, P1, P2, P3> FaiChat tool(FaiFunc3<R, P1, P2, P3> func) {
        return lambda(func);
    }

    public <R, P1, P2, P3> FaiChat tool(FaiFunc3<R, P1, P2, P3> func, Class<P1> type1, Class<P2> type2, Class<P3> type3) {
        return lambda(func);
    }


    public <R, P1, P2, P3, P4> FaiChat tool(FaiFunc4<R, P1, P2, P3, P4> func) {
        return lambda(func);
    }

    public <R, P1, P2, P3, P4, P5> FaiChat tool(FaiFunc5<R, P1, P2, P3, P4, P5> func) {
        return lambda(func);
    }

    private FaiChat lambda(FaiFunction func) {
        final FaiMethod method = LambdaUtils.getMethod(func);
        if (method == null) {
            return this;
        }
        if (method.isStatic()) {
            return tool(method);
        }
        final Object invoker = LambdaUtils.getInvoker(func);
        if (invoker == null) {
            return this;
        }
        return tool(invoker, method);
    }


    public FaiChat tool(Object object) {
        if (object != null) {
            if (object instanceof Class) {
                return tool((Class<?>) object);
            }
            if (object instanceof FaiObject) {
                return tool(((FaiObject) object).tools());
            }
            return toolsOf(Fai.object(object));
        }
        return this;
    }

    public <T extends FaiTool> FaiChat tool(String name, Object object) {
        if (object != null) {
            return toolsOf(Fai.object(name, object));
        }
        return this;
    }

    public <T extends FaiTool> FaiChat toolsOf(FaiObject object) {
        if (object != null) {
            return tool(object.tools());
        }
        return this;
    }

    public FaiChat tool(Class<?> clazz) {
        if (clazz != null) {
            return toolsOf(Fai.type(clazz));
        }
        return this;
    }

    public <T extends FaiTool> FaiChat toolsOf(FaiType type) {
        if (type != null) {
            return tool(type.staticTools());
        }
        return this;
    }

    public FaiChat tool(FaiType type) {
        return toolsOf(type);
    }

    public <T extends FaiTool> FaiChat tool(FaiMethod method) {
        if (method != null) {
            return tool(FaiTool.tool(method));
        }
        return this;
    }

    public <T extends FaiTool> FaiChat tool(Object invoker, FaiMethod method) {
        if (invoker != null && method != null) {
            return tool(FaiObject.of(invoker).tool(method));
        }
        return this;
    }


    public FaiChat tool(FaiTool tool) {
        if (tool != null) {
            this.tools.put(tool.name(), tool);
        }
        return this;
    }

    public <T extends FaiTool> FaiChat tool(List<T> tools) {
        if (tools != null) {
            for (final T tool : tools) {
                this.tools.put(tool.name(), tool);
            }
        }
        return this;
    }

    public List<FaiTool> tools() {
        return new ArrayList<>(tools.values());
    }
    
    public FaiTool tool(String name) {
        return tools.get(name);
    }

    public boolean isLogEnabled() {
        return logEnabled;
    }
    
    public FaiChat logEnabled(boolean logEnabled) {
        this.logEnabled = logEnabled;
        return this;
    }
    
    public FaiChat logEnabled() {
        return logEnabled(true);
    }
    
    public FaiChat logDisabled() {
        return logEnabled(false);
    }

    public FaiModel model() {
        return model;
    }

    public FaiChat prompt(FaiMessage...messages) {
        return prompt(Arrays.asList(messages));
    }

    public <T extends FaiMessage> FaiChat prompt(List<T> messages) {
        this.messages.addAll(messages);
        return this;
    }

    public String responseFormat() {
        return responseFormat;
    }

    public FaiChat responseFormat(String responseFormat) {
        this.responseFormat = responseFormat;
        return this;
    }

    public FaiChat responseFormat(FaiFormat responseFormat) {
        return responseFormat(responseFormat.getName());
    }

    public FaiChat responseText() {
        return responseFormat(FaiFormat.TEXT);
    }


    public FaiChat responseJson() {
        return responseFormat(FaiFormat.JSON);
    }

    public FaiChat responseJson(Class<?> clazz) {
        this.responseFormatType = FaiType.of(clazz);
        return responseFormat(FaiFormat.JSON);
    }

    public FaiChat responseJson(Type type) {
        this.responseFormatType = FaiType.of(type);
        return responseFormat(FaiFormat.JSON);
    }


    public FaiType responseFormatType() {
        return responseFormatType;
    }

    public boolean isStream() {
        return stream;
    }

    public FaiChat stream(boolean stream) {
        this.stream = stream;
        return this;
    }
    
    public FaiChat onBeforeRequest(Consumer<FaiRunner> onPrepareCallback) {
        this.onBeforeRequestCallback = onPrepareCallback;
        return this;
    }
    
    public Consumer<FaiRunner> onPrepareCallback() {
        return onBeforeRequestCallback;
    }
    
    public FaiChat onMessageStart(OnMessageStart onStartCallback) {
        this.onMessageStartCallback = onStartCallback;
        return this;
    }

    public OnMessageStart onStartCallback() {
        return onMessageStartCallback;
    }

    public FaiChat onMessage(OnMessage onMessageCallback) {
        this.onMessageCallback = onMessageCallback;
        return this;
    }

    public FaiChat onMessage(OnMessageWithoutSink onMessageCallbackWithoutSink) {
        this.onMessageCallback = (runner, res, sink) -> {
            if (onMessageCallbackWithoutSink != null) {
                onMessageCallbackWithoutSink.onMessage(runner, res);
            }
        };
        return this;
    }


    public OnMessage onMessageCallback() {
        return onMessageCallback;
    }
    
    public FaiChat onMessageStop(OnMessageStop onStopCallback) {
        this.onMessageStopCallback = onStopCallback;
        return this;
    }
    
    public OnMessageStop onStopCallback() {
        return onMessageStopCallback;
    }

    public OnToolCalls onToolCallsCallback() {
        return onToolCallsCallback;
    }

    public FaiChat onToolCalls(OnToolCalls onToolCalls) {
        this.onToolCallsCallback = onToolCalls;
        return this;
    }

    public FaiChat stream() {
        return stream(true);
    }

    public FaiChat promptId(String id) {
        return prompt(Fai.config().promptMessagesOf(id));
    }
    
    public List<FaiMessage> messages() {
        return messages;
    }
    
    public List<FaiMessage> messages(String role) {
        return messages.stream()
                .filter(faiChatMessage -> faiChatMessage.role().equals(role))
                .collect(Collectors.toList());
    }
    
    public List<FaiMessage> messages(FaiRole role) {
        return messages(role.getName());
    }
    
    public List<FaiMessage> systemMessages() {
        return messages(FaiRole.SYSTEM);
    }
    
    public List<FaiMessage> userMessages() {
        return messages(FaiRole.USER);
    }
    
    public List<FaiMessage> assistantMessages() {
        return messages(FaiRole.ASSISTANT);
    }
    
    public FaiChat prompt(FaiTextMessage message) {
        this.messages.add(message);
        return this;
    }
    
    public FaiChat prompt(String role, String content) {
        return prompt(FaiTextMessage.create(role, content));
    }

    public FaiChat prompt(String role, List<String> content) {
        return prompt(FaiTextMessage.listText(role, content));
    }

    public FaiChat prompt(String role, String ...content) {
        return prompt(FaiTextMessage.listText(role, content));
    }

    public FaiChat prompt(FaiRole role, String content) {
        return prompt(FaiTextMessage.create(role, content));
    }

    public FaiChat prompt(FaiRole role, List<String> content) {
        return prompt(FaiTextMessage.listText(role, content));
    }

    public FaiChat prompt(FaiRole role, String ...content) {
        return prompt(FaiTextMessage.listText(role, content));
    }
    
    public FaiChat prompt(FaiMessageResponse delta) {
        return prompt(delta.getRole(), delta.getContent());
    }
    
    public FaiChat system(String content) {
        return prompt(FaiTextMessage.system(content));
    }

    public FaiChat system(FaiTextContent content) {
        return prompt(FaiTextMessage.system(content));
    }

    public FaiChat system(List<String> content) {
        return prompt(FaiTextMessage.system(content));
    }

    public FaiChat system(String ...content) {
        return prompt(FaiTextMessage.system(content));
    }

    public FaiChat system(FaiTextContent ...content) {
        return prompt(FaiTextMessage.system(content));
    }

    public FaiChat user(String content) {
        return prompt(FaiTextMessage.user(content));
    }

    public FaiChat user(FaiTextContent content) {
        return prompt(FaiTextMessage.user(content));
    }

    public FaiChat user(List<String> content) {
        return prompt(FaiTextMessage.user(content));
    }

    public FaiChat user(String ...content) {
        return prompt(FaiTextMessage.user(content));
    }

    public FaiChat user(FaiTextContent ...content) {
        return prompt(FaiTextMessage.user(content));
    }

    public FaiChat assistant(String content) {
        return prompt(FaiTextMessage.assistant(content));
    }

    public FaiChat assistant(FaiTextContent content) {
        return prompt(FaiTextMessage.assistant(content));
    }

    public FaiChat assistant(List<String> content) {
        return prompt(FaiTextMessage.assistant(content));
    }

    public FaiChat assistant(String ...content) {
        return prompt(FaiTextMessage.assistant(content));
    }
    
    public FaiChat assistant(FaiTextContent ...content) {
        return prompt(FaiTextMessage.assistant(content));
    }

    public ChatCompletionsParam param() {
        final List<ChatMessageParam> messageParams = new ArrayList<>();
        boolean userMessageAppended = false;
        for (final FaiMessage message : messages) {
            if (FaiRole.SYSTEM.getName().equals(message.role())) {
                messageParams.add(new ChatMessageParam(message.role(), message.content()));
            } else {
                if (!userMessageAppended && responseFormatType != null) {
                    if (FaiFormat.JSON.getName().equals(responseFormat)) {
                        String jsonPrompt = responseFormatType.toJSONPrompt();
                        messageParams.add(new ChatMessageParam(FaiRole.SYSTEM.getName(), jsonPrompt));
                    }
                }
                userMessageAppended = true;
                messageParams.add(new ChatMessageParam(message.role(), message.content()));
            }
        }

        return new ChatCompletionsParam(
                model.model(),
                stream,
                messageParams,
                ResponseFormatParam.get(responseFormat),
                CollectionUtil.isEmpty(tools) ?
                        null :
                        tools.values().stream().map(FaiTool::param).collect(Collectors.toList()));
    }

    public FaiChat env(String name, Object value) {
        environment.put(name, value);
        return this;
    }

    public FaiChat env(Map<String, Object> variables) {
        this.environment.putAll(variables);
        return this;
    }

    public Map<String, Object> env() {
        return this.environment;
    }

    public FaiChatRunner runner() {
        return FaiRunner.chatRunner(this);
    }
    
    public FaiChatRunner run() {
        return runner().run();
    }

    public void runLoop() {
        runner().loop();
    }

    public String content() {
        return run().content();
    }
    
    public String text() {
        return run().text();
    }

    public <R> R get(TypeReference<R> typeReference) {
        final Type type = typeReference.getType();
        final Class<R> clazz = (Class<R>) ReflectUtils.toClass(type);
        if (ReflectUtils.isPrimaryType(clazz)) {
            return run().get(clazz);
        }
        return responseJson(type).run().get(typeReference);

    }

    public <R> R get(Class<R> clazz) {
        if (ReflectUtils.isPrimaryType(clazz)) {
            return run().get(clazz);
        }
        return responseJson(clazz).run().get(clazz);
    }


}
