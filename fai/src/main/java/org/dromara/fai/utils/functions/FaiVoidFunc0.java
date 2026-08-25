package org.dromara.fai.utils.functions;

@FunctionalInterface
public interface FaiVoidFunc0 extends FaiFunction {

    void call();

    @Override
    default int numberOfParameters() {
        return 0;
    }

}
