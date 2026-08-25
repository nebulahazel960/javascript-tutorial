package org.dromara.fai.param;

public class ToolFunctionParam extends ToolParam {
    
    private final FunctionParam function;

    public ToolFunctionParam(FunctionParam function) {
        super("function");
        this.function = function;
    }

    public FunctionParam getFunction() {
        return function;
    }

}
