package org.dromara.fai.chat;

import java.io.Serializable;

public class FaiToolCallResponse implements Serializable {
    
    private Integer index;
    
    private String id;
    
    private String type;
    
    private FaiFunctionResponse function;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FaiFunctionResponse getFunction() {
        return function;
    }

    public void setFunction(FaiFunctionResponse function) {
        this.function = function;
    }
}
