package org.dromara.fai.tools;

import org.dromara.fai.FaiModel;
import org.dromara.fai.chat.FaiChatResponse;
import org.dromara.fai.chat.FaiChat;
import org.dromara.fai.config.FaiConfiguration;
import org.dromara.fai.exception.FaiToolException;
import org.dromara.fai.runner.FaiChatRunner;
import org.dromara.fai.runner.FaiRunner;
import org.dromara.fai.wrapper.FaiMethod;
import org.dromara.fai.wrapper.FaiParameter;
import org.dromara.fai.wrapper.FaiType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class FaiAbstractFunctionTool implements FaiTool {
    
    private final static FaiMethod[] EMPTY_WRAPPER_ARRAY = new FaiMethod[0];

    protected FaiAbstractFunctionTool(FaiMethod methodWrapper) {
        this.methodWrapper = methodWrapper;
    }

    public static List<FaiStaticFunctionTool> allStaticTools(Class<?> clazz) {
        return staticTools(FaiType.of(clazz).staticToolMethods());
    }

    public static List<FaiStaticFunctionTool> staticTools(Class<?> clazz, String ...methodNames) {
        return staticTools(FaiType.of(clazz).staticToolMethods(methodNames));
    }

    public static FaiStaticFunctionTool staticTool(Class<?> clazz, String methodName) {
        return staticTool(FaiType.of(clazz).staticToolMethod(methodName));
    }

    public static List<FaiStaticFunctionTool> staticTools(List<FaiMethod> methodWrappers) {
        return methodWrappers.stream()
                .map(FaiStaticFunctionTool::new)
                .collect(Collectors.toList());
    }


    public static List<FaiStaticFunctionTool> staticTools(FaiMethod...methodWrapper) {
        return Arrays.stream(methodWrapper)
                .map(FaiAbstractFunctionTool::staticTool)
                .collect(Collectors.toList());
    }
    
    public static FaiStaticFunctionTool staticTool(FaiMethod methodWrapper) {
        if (methodWrapper == null) {
            throw new FaiToolException(new IllegalArgumentException("methodWrapper must not be null"));
        }
        if (!methodWrapper.isTool()) {
            throw new FaiToolException(new IllegalArgumentException("methodWrapper is not a tool"));
        }
        if (methodWrapper.isStatic()) {
            return new FaiStaticFunctionTool(methodWrapper);
        }
        throw new FaiToolException(new IllegalArgumentException("methodWrapper is not static"));
    }

    public static FaiFunctionTool tool(Object instance, FaiMethod methodWrapper) {
        if (methodWrapper == null) {
            throw new FaiToolException(new IllegalArgumentException("methodWrapper must not be null"));
        }
        if (!methodWrapper.isTool()) {
            throw new FaiToolException(new IllegalArgumentException("methodWrapper is not a tool"));
        }
        if (methodWrapper.isStatic()) {
            throw new FaiToolException(new IllegalArgumentException("methodWrapper is static"));
        }
        return new FaiFunctionTool(instance, methodWrapper);
    }

    protected final FaiMethod methodWrapper;

    public Object[] arguments(final FaiRunner runner, Map<String, Object> argsMap) {
        final List<FaiParameter> parameters = methodWrapper.parameters();
        Object[] arguments = new Object[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            final FaiParameter parameter = parameters.get(i);
            final Object arg = toArgument(runner, argsMap, parameter);
            arguments[i] = arg;
        }
        return arguments;
    }

    private Object toArgument(final FaiRunner runner, final Map<String, Object> argsMap, final FaiParameter parameter) {
        if (!parameter.isToolPram()) {
            final Class<?> paramType = parameter.javaParameter().getType();
            if (FaiChatResponse.class.isAssignableFrom(paramType) && runner instanceof FaiChatRunner) {
                return ((FaiChatRunner) runner).response();
            }
            if (FaiChat.class.isAssignableFrom(paramType)) {
                return runner.chat();
            }
            if (FaiConfiguration.class.isAssignableFrom(paramType)) {
                return runner.config();
            }
            if (FaiModel.class.isAssignableFrom(paramType)) {
                return runner.model();
            }
            if (FaiTool.class.isAssignableFrom(paramType)) {
                return this;
            }
        }
        return argsMap.get(parameter.name());
    }


    @Override
    public FaiToolType type() {
        return FaiToolType.FUNCTION;
    }

    @Override
    public abstract Object call(Object ...args);
}
