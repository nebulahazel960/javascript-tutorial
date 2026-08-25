package org.dromara.fai.chat;

@FunctionalInterface
public interface FaiChatOnMessageCallback {
    
    void onMessage(FaiChatResponse response);
    
}
