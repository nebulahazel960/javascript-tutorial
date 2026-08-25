package org.dromara.fai.param;

import java.io.Serializable;

public class FunctionParam implements Serializable {
    
    private final String name;
    
    private final String description;
    
    private final FunctionParametersParam parameters;

    public FunctionParam(String name, String description, FunctionParametersParam parameters) {
        this.name = name;
        this.description = description;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }


    public FunctionParametersParam getParameters() {
        return parameters;
    }

}
