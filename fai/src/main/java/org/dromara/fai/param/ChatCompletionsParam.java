package org.dromara.fai.param;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class ChatCompletionsParam implements Serializable {
    
    private final String model;
    
    private final Boolean stream;
    
    private final List<ChatMessageParam> messages;
    
    @JsonProperty("response_format")
    private ResponseFormatParam responseFormat;
    
    private List<ToolParam> tools;
    
    public ChatCompletionsParam(String model, Boolean stream, List<ChatMessageParam> messages, ResponseFormatParam responseFormat, List<ToolParam> tools) {
        this.model = model;
        this.stream = stream;
        this.messages = messages;
        this.responseFormat = responseFormat;
        this.tools = tools;
    }

    public String getModel() {
        return model;
    }

    public Boolean getStream() {
        return stream;
    }

    public List<ChatMessageParam> getMessages() {
        return messages;
    }
    
    public ResponseFormatParam getResponseFormat() {
        return responseFormat;
    }

    public List<ToolParam> getTools() {
        return tools;
    }

    public void setTools(List<ToolParam> tools) {
        this.tools = tools;
    }
}
