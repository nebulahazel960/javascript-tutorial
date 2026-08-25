package org.dromara.fai.utils.functions;


import org.dromara.fai.tools.FaiTool;
import org.dromara.fai.tools.FaiToolProvider;
import org.dromara.fai.utils.LambdaUtils;
import org.dromara.fai.wrapper.FaiMethod;
import org.dromara.fai.wrapper.FaiObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface FaiFunction extends FaiToolProvider, Serializable {

    @Override
    default List<FaiTool> tools() {
        final List<FaiTool> tools = new ArrayList<>(1);
        final FaiMethod method = LambdaUtils.getMethod(this);
        if (method == null) {
            return tools;
        }
        if (method.isStatic()) {
            tools.add(FaiTool.tool(method));
            return tools;
        }
        final Object invoker = LambdaUtils.getInvoker(this);
        if (invoker == null) {
            return tools;
        }
        tools.add(FaiObject.of(invoker).tool(method));
        return tools;
    }

    int numberOfParameters();
}
