package org.dromara.fai.runner;

public interface FaiSink {

    FaiSink next(Object data);
}
