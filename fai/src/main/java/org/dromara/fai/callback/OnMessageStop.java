package org.dromara.fai.callback;

import org.dromara.fai.chat.FaiChatResponse;
import org.dromara.fai.runner.FaiRunner;

@FunctionalInterface
public interface OnMessageStop {
    
    void onMessageStop(FaiRunner runner, FaiChatResponse res);
}
