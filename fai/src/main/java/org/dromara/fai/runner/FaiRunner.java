package org.dromara.fai.runner;

import com.dtflys.forest.utils.TypeReference;
import org.dromara.fai.FaiModel;
import org.dromara.fai.chat.FaiChat;
import org.dromara.fai.config.FaiConfiguration;

import java.util.List;

public interface FaiRunner {

    FaiConfiguration config();
    
    static FaiChatRunner chatRunner(FaiChat completions) {
        return new FaiChatRunner(completions);
    }
    
    FaiModel model();

    FaiChat chat();

    FaiRunner run();
    
    void stop();
    
    void loop();
    
    String content();
    
    <R> R content(Class<R> clazz);

    <R> R content(TypeReference<R> typeReference);
    
    Object toolCallsResult();
    
    <R> R toolCallsResult(Class<R> clazz);

    <R> R toolCallsResult(TypeReference<R> typeReference);
    
    Object get();
    
    <R> R get(Class<R> clazz);
    
    <R> R get(TypeReference<R> typeReference);
    
    <R> List<R> list(Class<R> clazz);
    
    default String text() {
        return get(String.class);
    }
    
}
