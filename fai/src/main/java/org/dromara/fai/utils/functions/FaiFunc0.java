package org.dromara.fai.utils.functions;

@FunctionalInterface
public interface FaiFunc0<R> extends FaiFunction {

    R call();

    @Override
    default int numberOfParameters() {
        return 0;
    }
}
