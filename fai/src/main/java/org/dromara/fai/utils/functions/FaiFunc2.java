package org.dromara.fai.utils.functions;

@FunctionalInterface
public interface FaiFunc2<R, P1, P2> extends FaiFunction {

    R call(P1 p1, P2 p2);

    @Override
    default int numberOfParameters() {
        return 2;
    }

}
