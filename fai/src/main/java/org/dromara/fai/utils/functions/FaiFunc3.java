package org.dromara.fai.utils.functions;

@FunctionalInterface
public interface FaiFunc3<R, P1, P2, P3> extends FaiFunction {

    R call(P1 p1, P2 p2, P3 p3);

    @Override
    default int numberOfParameters() {
        return 3;
    }
}
