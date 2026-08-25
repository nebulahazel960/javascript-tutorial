package org.dromara.fai.utils.functions;

@FunctionalInterface
public interface FaiVoidFunc2<P1, P2> extends FaiFunction {

    void call(P1 p1, P2 p2);

    @Override
    default int numberOfParameters() {
        return 2;
    }

}
