package org.dromara.fai.param;

import com.dtflys.forest.utils.ReflectUtils;

import java.io.Serializable;

public class FunctionParameterPropertyParam implements Serializable {
    
    private final String type;
    
    private final String description;

    public FunctionParameterPropertyParam(Class<?> clazz, String description) {
        this.type = classToToolType(clazz);
        this.description = description;
    }

    private static String classToToolType(Class<?> clazz) {
        if (ReflectUtils.isPrimaryType(clazz)) {
            if (clazz.isArray()) {
                return "array";
            } else if (CharSequence.class.isAssignableFrom(clazz)) {
                return "string";
            } else if (Number.class.isAssignableFrom(clazz)
                    || int.class.isAssignableFrom(clazz)
                    || long.class.isAssignableFrom(clazz)
                    || float.class.isAssignableFrom(clazz)
                    || double.class.isAssignableFrom(clazz)) {
                return "number";
            }
        } 
        return "object";
    }

    public FunctionParameterPropertyParam(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

}
