package org.dromara.fai.wrapper;

import java.io.Serializable;
import java.lang.reflect.Parameter;

public class FaiParameter implements Serializable {
    
    private final Parameter parameter;
    
    private final String name;
    
    private final boolean isToolPram;
    
    public FaiParameter(String name, boolean isToolPram, Parameter parameter) {
        this.parameter = parameter;
        this.name = name;
        this.isToolPram = isToolPram;
    }
    
    public String name() {
        if (name != null) {
            return name;
        }
        return parameter.getName();
    }

    public Parameter javaParameter() {
        return parameter;
    }

    public Class<?> type() {
        return parameter.getType();
    }

    public boolean isToolPram() {
        return isToolPram;
    }
}
