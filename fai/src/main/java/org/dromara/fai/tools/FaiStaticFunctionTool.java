package org.dromara.fai.tools;

import org.dromara.fai.exception.FaiRuntimeException;
import org.dromara.fai.param.ToolParam;
import org.dromara.fai.runner.FaiRunner;
import org.dromara.fai.wrapper.FaiMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class FaiStaticFunctionTool extends FaiAbstractFunctionTool {
    
    public FaiStaticFunctionTool(FaiMethod methodWrapper) {
        super(methodWrapper);
    }

    public FaiStaticFunctionTool(Method method) {
        super(FaiMethod.of(method));
    }


    @Override
    public String name() {
        return methodWrapper.name();
    }

    @Override
    public ToolParam param() {
        return methodWrapper.param();
    }

    @Override
    public Object call(Object... args) {
        try {
            return methodWrapper.javaMethod().invoke(methodWrapper.declaringClass(), args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new FaiRuntimeException(e);
        }
    }



    @Override
    public Object call(final FaiRunner runner, Map<String, Object> argsMap) {
        Object[] args = arguments(runner, argsMap);
        return call(args);
    }


}
