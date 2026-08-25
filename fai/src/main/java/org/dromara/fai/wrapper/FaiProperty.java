package org.dromara.fai.wrapper;

import com.dtflys.forest.utils.ReflectUtils;
import com.dtflys.forest.utils.StringUtils;
import org.dromara.fai.annotation.AIDesc;
import org.dromara.fai.exception.FaiRuntimeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FaiProperty {

    private final static Object[] GETTER_ARGS = new Object[0];
    
    private final static FaiAnnotationValueWrapper[] ANNOTATION_VALUE_WRAPPERS = new FaiAnnotationValueWrapper[] {
            new FaiAnnotationValueWrapper("com.fasterxml.jackson.annotation.JsonProperty", "value"),
            new FaiAnnotationValueWrapper("com.alibaba.fastjson2.annotation.JSONField", "name"),
            new FaiAnnotationValueWrapper("com.alibaba.fastjson.annotation.JSONField", "name"),
    };

    private String name;
    
    String alias;

    Field field;

    Method getter;

    Method setter;
    
    Type type;
    
    String desc;

    FaiProperty(String name) {
        this.name = name;
    }


    FaiProperty(String name, Method getter) {
        this.name = name;
        this.getter = getter;
    }

    FaiProperty(String name, Field field) {
        this.name = name;
        this.field = field;
    }

    FaiProperty(String name, Field field, Method getter, Method setter) {
        this.name = name;
        this.field = field;
        this.getter = getter;
        this.setter = setter;
        
    }
    
    void init() {
        if (getter != null) {
            type = getter.getGenericReturnType();
        } else if (field != null) {
            type = field.getGenericType();
        }

        final List<Annotation> annotations = new ArrayList<>();
        if (field != null) {
            annotations.addAll(Arrays.asList(field.getDeclaredAnnotations()));
        }
        if (getter != null) {
            annotations.addAll(Arrays.asList(getter.getDeclaredAnnotations()));
        }
        if (setter != null) {
            annotations.addAll(Arrays.asList(setter.getDeclaredAnnotations()));
        }

        for (final Annotation annotation : annotations) {
            if (annotation instanceof AIDesc) {
                AIDesc desc = (AIDesc) annotation;
                this.desc = desc.value();
            } else if (StringUtils.isBlank(this.alias)) {
                initNameByAnnotation(annotation);
            }
        }
    }
    
    private void initNameByAnnotation(final Annotation annotation) {
        for (FaiAnnotationValueWrapper faiAnnotationValueWrapper : ANNOTATION_VALUE_WRAPPERS) {
            String value = faiAnnotationValueWrapper.getValueAsString(annotation);
            if (StringUtils.isNotEmpty(value)) {
                this.alias = value;
                break;
            }
        }
    }
    
    public boolean isAccessible() {
        return (getter != null && Modifier.isPublic(getter.getModifiers())) || (field != null && Modifier.isPublic(field.getModifiers()));
    }

    public String name() {
        return name;
    }
    
    public String alias() {
        return alias;
    }
    
    public String displayName() {
        if (StringUtils.isNotEmpty(alias)) {
            return alias;
        }
        return name;
    }

    public Field field() {
        return field;
    }

    public Method getter() {
        return getter;
    }

    public Method setter() {
        return setter;
    }

    public String desc() {
        return desc;
    }
    
    public Type type() {
        return type;
    }
    
    public Class<?> propClass() {
        return ReflectUtils.toClass(type);
    }
    
    public Object value(Object instance) {
        if (getter != null) {
            try {
                return getter.invoke(instance, GETTER_ARGS);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new FaiRuntimeException(e);
            }
        }
        if (field != null) {
            field.setAccessible(true);
            try {
                return field.get(instance);
            } catch (IllegalAccessException e) {
                throw new FaiRuntimeException(e);
            }
        }
        return null;
    }
    
    public void setValue(Object instance, Object value) {
        if (setter != null) {
            try {
                setter.invoke(instance, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new FaiRuntimeException(e);
            }
        }
        if (field != null) {
            field.setAccessible(true);
            try {
                field.set(instance, value);
            } catch (IllegalAccessException e) {
                throw new FaiRuntimeException(e);
            }
        }
    }
}
