package org.dromara.fai.utils.functions;

@FunctionalInterface
public interface FaiFunc5<R, P1, P2, P3, P4, P5> extends FaiFunction {

    R call(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5);

    @Override
    default int numberOfParameters() {
        return 3;
    }
}
