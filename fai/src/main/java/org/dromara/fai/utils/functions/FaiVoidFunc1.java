package org.dromara.fai.utils.functions;

@FunctionalInterface
public interface FaiVoidFunc1<P> extends FaiFunction {

    void call(P param);

    @Override
    default int numberOfParameters() {
        return 1;
    }

}
