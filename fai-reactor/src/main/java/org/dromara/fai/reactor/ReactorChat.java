package org.dromara.fai.reactor;

import org.dromara.fai.FaiModel;
import org.dromara.fai.callback.OnMessage;
import org.dromara.fai.callback.OnMessageWithoutSink;
import org.dromara.fai.chat.FaiChat;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

public class ReactorChat extends FaiChat {

    public ReactorChat(FaiModel model) {
        super(model);
    }

    public ReactorChat(FaiModel model, Boolean stream) {
        super(model, stream);
    }

    @Override
    public ReactorChat onMessage(OnMessage onMessageCallback) {
        super.onMessage(onMessageCallback);
        return this;
    }

    @Override
    public ReactorChat onMessage(OnMessageWithoutSink onMessageCallbackWithoutSink) {
        super.onMessage(onMessageCallbackWithoutSink);
        return this;
    }



    @Override
    public ReactorChat system(String content) {
        super.system(content);
        return this;
    }

    @Override
    public ReactorChat system(List<String> content) {
        super.system(content);
        return this;
    }

    @Override
    public ReactorChat system(String ...content) {
        super.system(content);
        return this;
    }

    @Override
    public ReactorChat user(String content) {
        super.user(content);
        return this;
    }

    @Override
    public ReactorChat user(List<String> content) {
        super.user(content);
        return this;
    }

    @Override
    public ReactorChat user(String ...content) {
        super.user(content);
        return this;
    }

    @Override
    public ReactorChat assistant(String content) {
        super.assistant(content);
        return this;
    }

    @Override
    public ReactorChat assistant(List<String> content)  {
        super.assistant(content);
        return this;
    }

    @Override
    public ReactorChat assistant(String ...content) {
        super.assistant(content);
        return this;
    }


    @Override
    public ReactorChat stream(boolean stream) {
        super.stream(stream);
        return this;
    }

    @Override
    public ReactorChat stream() {
        return stream(true);
    }

    @Override
    public ReactorRunner runner() {
        return new ReactorRunner(this);
    }

    @Override
    public ReactorRunner run() {
        return runner().run();
    }
    
    
    public <R> Flux<R> toFlux(Class<R> clazz) {
        return stream().run().toFlux(clazz);
    }


    public Flux<String> toFlux() {
        return stream().run().toFlux();
    }

    @Override
    public ReactorChat env(String name, Object value) {
        super.env(name, value);
        return this;
    }

    @Override
    public ReactorChat env(Map<String, Object> variables) {
        super.env(variables);
        return this;
    }



}
