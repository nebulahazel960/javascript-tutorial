package org.dromara.fai.wrapper;

import cn.hutool.core.util.ClassUtil;
import com.dtflys.forest.utils.NameUtils;
import com.dtflys.forest.utils.ReflectUtils;
import com.dtflys.forest.utils.StringUtils;
import org.dromara.fai.Fai;
import org.dromara.fai.annotation.AIDesc;
import org.dromara.fai.exception.FaiDuplicateToolNameException;
import org.dromara.fai.tools.FaiStaticFunctionTool;
import org.dromara.fai.tools.FaiTool;
import org.dromara.fai.tools.FaiToolProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FaiType implements FaiToolProvider {
    
    private final static Map<Type, FaiType> TYPES_CACHE = new ConcurrentHashMap<>();
    
    private final Type type;
    
    private final Class<?> rawClass;
    
    private FaiType childType;
    
    private final Map<String, FaiProperty> props = new HashMap<>();
    
    private final List<FaiMethod> staticMethods = new ArrayList<>();
    
    private final List<FaiMethod> dynamicMethods = new ArrayList<>();

    private final Map<String, FaiTool> staticTools = new ConcurrentHashMap<>();

    private String desc;
    
    private boolean isArray = false;
    
    public static FaiType of(Class<?> clazz) {
        return of((Type) clazz);
    }

    public static FaiType of(Type type) {
        return TYPES_CACHE.computeIfAbsent(type, t -> new FaiType(type));
    }


    private FaiType(Class<?> clazz) {
        this(ReflectUtils.toType(clazz));
    }

    private FaiType(Type type) {
        final Class<?> clazz = ReflectUtils.toClass(type);
        if (clazz.isArray()) {
            this.type = clazz.getComponentType();
            this.rawClass = clazz.getComponentType();
            isArray = true;
        } else if (type instanceof ParameterizedType) {
            final Type paramType = ReflectUtils.getGenericArgument(type, 0);
            if (Collection.class.isAssignableFrom(clazz)) {
                this.type = paramType;
                this.rawClass = ReflectUtils.toClass(paramType);
                this.isArray = true;
            } else {
                this.type = type;
                this.rawClass = clazz;
            }
        } else {
            this.type = type;
            this.rawClass = clazz;
        }
        this.childType = isArray ? of(this.type) : null;
        init();
    }
    
    private void init() {
        final List<Annotation> annotations = new ArrayList<>();
        annotations.addAll(Arrays.asList(rawClass.getAnnotations()));
        for (final Annotation annotation : annotations) {
            if (annotation instanceof AIDesc) {
                AIDesc descAnno = (AIDesc) annotation;
                desc = descAnno.value();
                break;
            }
        }

        final Class<?>[] interfaceClasses = rawClass.getInterfaces();
        for (final Class<?> interfaceClass : interfaceClasses) {
            final Method[] interfaceMethods = ReflectUtils.getMethods(interfaceClass);
            for (final Method interfaceMethod : interfaceMethods) {
                if (ClassUtil.isPublic(interfaceClass)) {
                    final FaiMethod methodWrapper = FaiMethod.of(interfaceMethod);
                    if (Modifier.isStatic(interfaceMethod.getModifiers())) {
                        staticMethods.add(methodWrapper);
                    } else {
                        dynamicMethods.add(methodWrapper);
                    }
                }
            }
        }

        final Method[] methods = ReflectUtils.getMethods(rawClass);
        final Map<String, FaiProperty> newProps = new LinkedHashMap<>();
        for (final Method method : methods) {
            String methodName = method.getName();
            int paramCount = method.getParameterCount();
            if (paramCount == 0 && NameUtils.isGetter(methodName)) {
                final String name = NameUtils.propNameFromGetter(methodName);
                if (StringUtils.isNotEmpty(name)) {
                    FaiProperty prop = newProps.computeIfAbsent(name,
                            k -> new FaiProperty(name));
                    prop.getter = method;
                }
            } else if (paramCount == 1 && NameUtils.isSetter(methodName)) {
                final String name = NameUtils.propNameFromSetter(methodName);
                if (StringUtils.isNotEmpty(name)) {
                    FaiProperty prop = newProps.computeIfAbsent(name,
                            k -> new FaiProperty(name));
                    prop.setter = method;
                }
            }
            if (ClassUtil.isPublic(method)) {
                final FaiMethod methodWrapper = FaiMethod.of(method);
                if (Modifier.isStatic(method.getModifiers())) {
                    staticMethods.add(methodWrapper);
                } else {
                    dynamicMethods.add(methodWrapper);
                }
            }
        }

        final Field[] fields = ReflectUtils.getFields(rawClass);
        for (final Field field : fields) {
            final String name = field.getName();
            FaiProperty prop = newProps.computeIfAbsent(name,
                    k -> new FaiProperty(name));
            prop.field = field;
        }

        newProps.values().stream()
                .filter(FaiProperty::isAccessible)
                .forEach(prop -> {
                    prop.init();
                    this.props.put(prop.name(), prop);
                });

    }

    private void initStaticTools() {
        for (final FaiMethod staticMethod : staticMethods) {
            final String name = staticMethod.name();
            if (staticMethod.isTool()) {
                if (staticTools.containsKey(name)) {
                    throw new FaiDuplicateToolNameException(name);
                }
            }
        }
    }


    public List<FaiMethod> dynamicMethods() {
        return dynamicMethods;
    }

    public List<FaiMethod> staticMethods() {
        return staticMethods;
    }

    public FaiMethod staticMethod(final String name) {
        for (final FaiMethod staticMethod : staticMethods) {
            if (staticMethod.name().equals(name)) {
                return staticMethod;
            }
        }
        return null;
    }

    public FaiMethod staticMethod(final String name, int paramsNumber) {
        for (final FaiMethod staticMethod : staticMethods) {
            if (staticMethod.name().equals(name) && staticMethod.parameters().size() == paramsNumber) {
                return staticMethod;
            }
        }
        return null;
    }


    private FaiMethod findToolMethod(final String name, final List<FaiMethod> methods) {
        for (final FaiMethod method : methods) {
            if (method.name().equals(name) && method.isTool()) {
                return method;
            }
        }
        return null;
    }

    private FaiMethod findToolMethod(final String name, final List<FaiMethod> methods, int paramsNumber) {
        for (final FaiMethod method : methods) {
            if (method.name().equals(name) && method.isTool() && paramsNumber == method.parameters().size()) {
                return method;
            }
        }
        return null;
    }


    public FaiMethod staticToolMethod(final String name) {
        return findToolMethod(name, staticMethods);
    }

    public FaiMethod dynamicToolMethod(final String name) {
        return findToolMethod(name, dynamicMethods);
    }

    public FaiMethod dynamicToolMethod(final String name, final int paramsNumber) {
        return findToolMethod(name, dynamicMethods, paramsNumber);
    }


    private List<FaiMethod> findToolMethods(final List<FaiMethod> methods) {
        List<FaiMethod> tools = new ArrayList<>();
        for (final FaiMethod staticMethod : methods) {
            if (staticMethod.isTool()) {
                tools.add(staticMethod);
            }
        }
        return tools;
    }

    public List<FaiMethod> staticToolMethods() {
        return findToolMethods(staticMethods);
    }


    public List<FaiMethod> staticToolMethods(String... names) {
        return Arrays.stream(names)
                .map(this::staticToolMethod)
                .collect(Collectors.toList());
    }

    public FaiStaticFunctionTool staticTool(final String name) {
        FaiMethod toolMethod = staticToolMethod(name);
        if (toolMethod == null) {
            return null;
        }
        return new FaiStaticFunctionTool(toolMethod);
    }

    private List<FaiTool> findTools(final List<FaiMethod> methods) {
        List<FaiTool> tools = new ArrayList<>();
        for (final FaiMethod method : methods) {
            if (method.isTool()) {
                tools.add(new FaiStaticFunctionTool(method));
            }
        }
        return tools;
    }

    public List<FaiTool> staticTools() {
        return findTools(staticMethods);
    }


    public Class<?> javaClass() {
        return rawClass;
    }

    public Map<String, Object> toDescMap() {
        return toDescMap(this);
    }
    
    private Map<String, Object> toDescMap(FaiType faiTypeWrapper) {
        Map<String, Object> descMap = new LinkedHashMap<>();
        for (final Map.Entry<String, FaiProperty> entry : faiTypeWrapper.props.entrySet()) {
            final FaiProperty prop = entry.getValue();
            if (!prop.isAccessible()) {
                continue;
            }
            final String name = prop.displayName();
            final Class<?> propClass = prop.propClass();
            final String desc = prop.desc;
            if (ReflectUtils.isPrimaryType(propClass)) {
                if (StringUtils.isNotEmpty(desc)) {
                    descMap.put(name, "<" + desc + ">");
                } else {
                    descMap.put(name, "<" + propClass.getSimpleName() + " value>");
                }
            } else {
                if (Collection.class.isAssignableFrom(propClass)) {
                    Type[] genTypes = ReflectUtils.getGenericTypeArguments(prop.type);
                    if (genTypes != null && genTypes.length > 0) {
                        Class<?> childTypeClass = ReflectUtils.toClass(genTypes[0]);
                        FaiType childTypeWrapper = FaiType.of(childTypeClass);
                        Map<String, Object> childDescMap = toDescMap(childTypeWrapper);
                        descMap.put(name, Collections.singletonList(childDescMap));
                    }
                } else {
                    FaiType childPropType = FaiType.of(propClass);
                    Map<String, Object> childDescMap = toDescMap(childPropType);
                    descMap.put(name, childDescMap);
                }
            }
        }
        return descMap;
    }
    
    public List<Object> toDescArray() {
        List<Object> descArray = new LinkedList<>();
        if (childType != null) {
            descArray.add(childType.toDescMap());
        }
        return descArray;
    }

    private String toSimpleJSONPrompt(Object obj) {
        return Fai.http().getJsonConverter().encodeToString(obj);
    }


    public String toSimpleJSONPrompt() {
        if (isArray) {
            return toSimpleJSONPrompt(toDescArray());
        }
        return toSimpleJSONPrompt(toDescMap());
    }
    
    public String toJSONPrompt() {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotEmpty(desc)) {
            builder.append("\n请以 JSON 的形式输出").append(desc).append("的数据，输出的 JSON 需遵守以下的格式:\n");
        } else {
            builder.append("\nPlease output in JSON format. The output JSON must comply with the following format:\n");
        }
        builder.append(toSimpleJSONPrompt());
        return builder.toString();
    }

    @Override
    public List<FaiTool> tools() {
        return staticTools();
    }
}

// 2ce0c3
