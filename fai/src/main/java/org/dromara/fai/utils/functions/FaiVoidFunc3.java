package org.dromara.fai.utils.functions;

@FunctionalInterface
public interface FaiVoidFunc3<P1, P2, P3> extends FaiFunction {

    void call(P1 p1, P2 p2, P3 p3);

    @Override
    default int numberOfParameters() {
        return 2;
    }

}
