package org.dromara.fai.tools;

import org.dromara.fai.param.ToolParam;
import org.dromara.fai.runner.FaiRunner;
import org.dromara.fai.utils.functions.FaiFunction;
import org.dromara.fai.utils.LambdaUtils;
import org.dromara.fai.wrapper.FaiMethod;

import java.util.List;
import java.util.Map;

public interface FaiTool {

    static FaiTool tool(FaiFunction function) {
        return tool(LambdaUtils.getMethod(function));
    }
    
    static FaiTool tool(FaiMethod methodWrapper) {
        return FaiAbstractFunctionTool.staticTool(methodWrapper);
    }

    static FaiTool tool(Object instance, FaiMethod methodWrapper) {
        return FaiAbstractFunctionTool.tool(instance, methodWrapper);
    }
    
    static FaiStaticFunctionTool staticTool(Class<?> clazz, String methodName) {
        return FaiAbstractFunctionTool.staticTool(clazz, methodName);
    }

    static List<FaiStaticFunctionTool> staticTools(Class<?> clazz, String ...methodNames) {
        return FaiAbstractFunctionTool.staticTools(clazz, methodNames);
    }


    static List<FaiStaticFunctionTool> staticTools(Class<?> clazz) {
        return FaiAbstractFunctionTool.allStaticTools(clazz);
    }
    
    String name();

    ToolParam param();
    
    FaiToolType type();
    
    Object call(Object... args);

    Object call(final FaiRunner runner, Map<String, Object> argsMap);
}
