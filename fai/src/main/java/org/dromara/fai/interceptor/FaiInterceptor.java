package org.dromara.fai.interceptor;

import org.dromara.fai.callback.OnMessage;
import org.dromara.fai.callback.OnMessageStart;
import org.dromara.fai.callback.OnMessageStop;
import org.dromara.fai.callback.OnToolCalls;
import org.dromara.fai.chat.FaiChatResponse;
import org.dromara.fai.chat.FaiToolCallsHandler;
import org.dromara.fai.runner.FaiRunner;
import org.dromara.fai.runner.FaiSink;

public interface FaiInterceptor extends OnMessageStart, OnMessage, OnMessageStop, OnToolCalls {

    @Override
    default void onMessageStart(FaiRunner runner) {
    }

    @Override
    default void onMessage(FaiRunner runner, FaiChatResponse res, FaiSink sink) {

    }

    @Override
    default void onMessageStop(FaiRunner runner, FaiChatResponse res) {

    }

    @Override
    default Object onToolCalls(FaiRunner runner, FaiToolCallsHandler handler) {
        return handler.call();
    }
}
