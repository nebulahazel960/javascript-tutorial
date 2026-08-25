package org.dromara.fai.utils.functions;

@FunctionalInterface
public interface FaiFunc1<R, P> extends FaiFunction {

    R call(P param);

    @Override
    default int numberOfParameters() {
        return 1;
    }

}
