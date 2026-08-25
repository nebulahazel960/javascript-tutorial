package org.dromara.fai.runner;

import com.dtflys.forest.sse.SSESink;

public class DefaultFaiSink implements FaiSink {

    private final SSESink sseSink;

    public DefaultFaiSink(SSESink sseSink) {
        this.sseSink = sseSink;
    }

    @Override
    public FaiSink next(Object data) {
        sseSink.next(data);
        return this;
    }
}
