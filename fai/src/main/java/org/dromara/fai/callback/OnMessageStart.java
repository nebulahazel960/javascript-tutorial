package org.dromara.fai.callback;

import org.dromara.fai.runner.FaiRunner;

@FunctionalInterface
public interface OnMessageStart {
    
    void onMessageStart(FaiRunner runner);
}
