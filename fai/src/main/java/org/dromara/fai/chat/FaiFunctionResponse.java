package org.dromara.fai.chat;

import java.io.Serializable;

public class FaiFunctionResponse implements Serializable {
    
    private String name;
    
    private String arguments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }
}
