package org.dromara.fai.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionParametersParam implements Serializable {
    
    private final String type;
    
    private Map<String, FunctionParameterPropertyParam> properties = new HashMap<>();
    
    private List<String> required = new ArrayList<>();

    public FunctionParametersParam(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Map<String, FunctionParameterPropertyParam> getProperties() {
        return properties;
    }

    public List<String> getRequired() {
        return required;
    }

    public void appendProperty(String name, FunctionParameterPropertyParam property, boolean required) {
        if (properties.containsKey(name)) {
            return;
        }
        properties.put(name, property);
        if (required) {
            this.required.add(name);
        }
    }
    
}
