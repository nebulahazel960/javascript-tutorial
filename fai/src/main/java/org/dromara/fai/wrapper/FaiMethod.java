package org.dromara.fai.wrapper;

import cn.hutool.core.map.MapUtil;
import com.dtflys.forest.utils.ReflectUtils;
import com.dtflys.forest.utils.StringUtils;
import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.Paranamer;
import org.dromara.fai.annotation.AIParam;
import org.dromara.fai.annotation.AITool;
import org.dromara.fai.param.FunctionParam;
import org.dromara.fai.param.FunctionParameterPropertyParam;
import org.dromara.fai.param.FunctionParametersParam;
import org.dromara.fai.param.ToolFunctionParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FaiMethod {

    private final static char NAME_SEPARATOR = '-';

    private String prefix = "";
    
    private String name;
    
    private final Class<?> declaringClass;
    
    private final Method method;
    
    private final List<FaiParameter> parameterWrappers = new ArrayList<>();
    
    private final boolean isStatic;
    
    private boolean isTool = false;

    private ToolFunctionParam param;
    
    private String desc;
    
    public static FaiMethod of(final Method method) {
        return new FaiMethod(method);
    }

    private FaiMethod(Method method) {
        this.method = method;
        this.declaringClass = method.getDeclaringClass();
        this.isStatic = Modifier.isStatic(method.getModifiers());
        init();
    }
    
    private void init() {
        this.prefix = this.declaringClass.getName().replace('.', NAME_SEPARATOR);
        this.name = method.getName();
        final Annotation[] annotations = method.getDeclaredAnnotations();
        for (final Annotation annotation : annotations) {
            if (annotation instanceof AITool) {
                final AITool tool = (AITool) annotation;
                initToolParam(tool);
            }
        }
    }
    
    private void initToolParam(final AITool toolAnno) {
        final String toolName = toolAnno.name();
        this.isTool = true;
        this.desc = toolAnno.desc();
        if (StringUtils.isNotEmpty(toolName)) {
            this.name = toolName;
        }
        Paranamer paranamer = new AdaptiveParanamer();
        String[] parameterNames = paranamer.lookupParameterNames(method);

        final FunctionParametersParam functionParametersParam = new FunctionParametersParam("object");
        final FunctionParam functionParam = new FunctionParam(name(), desc, functionParametersParam);
        param = new ToolFunctionParam(functionParam);

        final Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            final Parameter parameter = parameters[i];
            String parameterName = i < parameterNames.length ? parameterNames[i] : null;
            boolean isRequired = false;
            String parameterDesc = "";
            final Annotation[] parameterAnnotations = parameter.getDeclaredAnnotations();
            boolean isToolParam = false;
            for (final Annotation parameterAnno : parameterAnnotations) {
                if (parameterAnno instanceof AIParam) {
                    final AIParam aiParam = (AIParam) parameterAnno;
                    final String aiParamName = aiParam.name();
                    isToolParam = true;
                    isRequired = aiParam.required();
                    parameterDesc = aiParam.desc();
                    if (StringUtils.isNotEmpty(aiParamName)) {
                        parameterName = aiParamName;
                    }
                } else {
                    final Map<String, Object> attributes = ReflectUtils.getAttributesFromAnnotation(parameterAnno);
                    if (!attributes.isEmpty()) {
                        final String annoName = MapUtil.getStr(attributes, "name");
                        if (StringUtils.isEmpty(parameterName) && StringUtils.isNotEmpty(annoName)) {
                            parameterName = annoName;
                        }
                    }
                }
            }
            if (isToolParam) {
                final FunctionParameterPropertyParam propertyParam = new FunctionParameterPropertyParam(parameter.getType(), parameterDesc);
                functionParametersParam.appendProperty(parameterName, propertyParam, isRequired);
            }
            
            final FaiParameter parameterWrapper = new FaiParameter(parameterName, isToolParam, parameter);
            parameterWrappers.add(parameterWrapper);
        }
    }

    public String name() {
        return name;
    }

    public String fullName() {
        return prefix + NAME_SEPARATOR + name;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public Method javaMethod() {
        return method;
    }

    public Class<?> declaringClass() {
        return declaringClass;
    }

    public String desc() {
        return desc;
    }

    public boolean isTool() {
        return isTool;
    }

    public ToolFunctionParam param() {
        return param;
    }

    public List<FaiParameter> parameters() {
        return parameterWrappers;
    }
}
