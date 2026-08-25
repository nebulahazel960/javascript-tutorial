package org.dromara.fai.callback;

import org.dromara.fai.chat.FaiChatResponse;
import org.dromara.fai.runner.FaiRunner;
import org.dromara.fai.runner.FaiSink;

@FunctionalInterface
public interface OnMessage {
    
    void onMessage(FaiRunner runner, FaiChatResponse res, FaiSink sink);
}
