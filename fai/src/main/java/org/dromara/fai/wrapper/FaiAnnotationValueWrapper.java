package org.dromara.fai.wrapper;

import cn.hutool.core.util.ReflectUtil;
import org.dromara.fai.exception.FaiRuntimeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FaiAnnotationValueWrapper {
    
    private final String annotationTypeName;
    
    private final String methodName;

    public FaiAnnotationValueWrapper(String annotationTypeName, String methodName) {
        this.annotationTypeName = annotationTypeName;
        this.methodName = methodName;
    }

    public String annotationTypeName() {
        return annotationTypeName;
    }

    public String methodName() {
        return methodName;
    }
    
    public Object getValue(Annotation annotation) {
        Class<?> annoType = annotation.annotationType();
        if (annotationTypeName.equals(annoType.getName())) {
            final Method valueMethod = ReflectUtil.getMethod(annoType, methodName);
            if (valueMethod != null) {
                try {
                    return valueMethod.invoke(annotation);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new FaiRuntimeException(e);
                }
            }
        }
        return null;
    }
    
    public String getValueAsString(Annotation annotation) {
        Object value = getValue(annotation);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return String.valueOf(value);
    }
}
