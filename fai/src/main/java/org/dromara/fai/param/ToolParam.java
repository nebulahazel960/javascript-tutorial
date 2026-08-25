package org.dromara.fai.param;

import java.io.Serializable;

public abstract class ToolParam implements Serializable {
    
    protected final String type;

    public ToolParam(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
