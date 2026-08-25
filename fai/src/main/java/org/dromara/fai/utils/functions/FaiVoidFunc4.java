package org.dromara.fai.utils.functions;

@FunctionalInterface
public interface FaiVoidFunc4<P1, P2, P3, P4> extends FaiFunction {

    void call(P1 p1, P2 p2, P3 p3, P4 p4);

    @Override
    default int numberOfParameters() {
        return 2;
    }

}
