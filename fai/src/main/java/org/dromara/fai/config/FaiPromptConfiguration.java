package org.dromara.fai.config;

import java.io.Serializable;
import java.util.List;

public class FaiPromptConfiguration implements Serializable {
    
    private String id;
    
    private List<FaiMessageConfiguration> messages;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<FaiMessageConfiguration> getMessages() {
        return messages;
    }

    public void setMessages(List<FaiMessageConfiguration> messages) {
        this.messages = messages;
    }
}
