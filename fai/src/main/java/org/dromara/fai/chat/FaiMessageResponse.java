package org.dromara.fai.chat;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.dromara.fai.FaiMessage;

import java.io.Serializable;
import java.util.List;

public class FaiMessageResponse implements FaiMessage, Serializable {
    
    final static FaiMessageResponse EMPTY_DELTA = new FaiMessageResponse();
    
    private String role;
    
    private String content;

    @JsonProperty(value = "reasoning_content")
    private String reasoningContent;
    
    @JsonProperty(value = "tool_calls")
    private List<FaiToolCallResponse> toolCalls;
    
    @Override
    public String role() {
        return getRole();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    @Override
    public String content() {
        return getContent();
    }

    public String getContent() {
        return content;
    }

    public String getReasoningContent() {
        return reasoningContent;
    }

    public void setReasoningContent(String reasoningContent) {
        this.reasoningContent = reasoningContent;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<FaiToolCallResponse> getToolCalls() {
        return toolCalls;
    }

    public void setToolCalls(List<FaiToolCallResponse> toolCalls) {
        this.toolCalls = toolCalls;
    }
    
    public boolean isToolCalls() {
        return CollectionUtil.isNotEmpty(toolCalls);
    }
}
