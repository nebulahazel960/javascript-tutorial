package org.dromara.fai.callback;

import org.dromara.fai.chat.FaiToolCallsHandler;
import org.dromara.fai.runner.FaiRunner;

public interface OnToolCalls {
    
    Object onToolCalls(FaiRunner runner, FaiToolCallsHandler handler);
    
}
