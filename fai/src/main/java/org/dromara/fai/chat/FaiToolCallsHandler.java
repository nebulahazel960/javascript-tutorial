package org.dromara.fai.chat;

import cn.hutool.core.convert.Convert;
import com.dtflys.forest.utils.StringUtils;
import org.dromara.fai.Fai;
import org.dromara.fai.runner.FaiRunner;
import org.dromara.fai.tools.FaiTool;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FaiToolCallsHandler {
    
    private final FaiRunner runner;
    
    private final Queue<FaiChatResponse> responseBuffer = new ConcurrentLinkedQueue<>();
    
    private final StringBuffer argumentsBuffer = new StringBuffer();
    
    private String functionName;

    private volatile FaiTool tool;

    private volatile Map<String, Object> argumentsMap;

    public FaiToolCallsHandler(FaiRunner runner) {
        this.runner = runner;
    }

    public void add(FaiChatResponse response) {
        if (response != null && response.isToolCalls()) {
            responseBuffer.add(response);
            for (final FaiChoiceResponse choice : response.choices()) {
                for (final FaiToolCallResponse toolCall : choice.delta().getToolCalls()) {
                    String type = toolCall.getType();
                    if ("function".equals(type) || toolCall.getFunction() != null) {
                        processFunction(response, choice, toolCall);
                    }
                }
            }
        }
    }
    
    private void processFunction(FaiChatResponse response, FaiChoiceResponse choice, FaiToolCallResponse toolCall) {
        final FaiFunctionResponse function =  toolCall.getFunction();
        if (function != null) {
            final String name = function.getName();
            if (StringUtils.isNotEmpty(name)) {
                functionName = function.getName();
            }
            argumentsBuffer.append(function.getArguments());
        }
    }

    public FaiTool tool() {
        if (tool == null) {
            tool = runner.chat().tool(functionName);
        }
        return tool;
    }

    public Map<String, Object> arguments() {
        if (argumentsMap == null) {
            argumentsMap = Fai.http().getJsonConverter().convertObjectToMap(argumentsBuffer.toString());
        }
        return argumentsMap;
    }

    public <T> T call(Class<T> clazz, final Map<String, Object> args) {
        final FaiTool tool = tool();
        if (tool != null) {
            Object ret = tool.call(runner, args);
            if (ret != null) {
                return Convert.convert(clazz, ret);
            }
        }
        return null;
    }

    public <T> T call(Class<T> clazz) {
        return call(clazz, arguments());
    }


    public Object call() {
        return call(Object.class, arguments());
    }
    
}
