package org.dromara.fai.reactor;

import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestSSE;
import com.dtflys.forest.reactor.sse.ReactorSSE;
import org.dromara.fai.chat.FaiChat;
import org.dromara.fai.runner.FaiChatRunner;
import reactor.core.publisher.Flux;

public class ReactorRunner extends FaiChatRunner {

    public ReactorRunner(FaiChat chat) {
        super(chat);
    }

    @Override
    protected ForestSSE createSSE(ForestRequest<?> request) {
        return request.sse(ReactorSSE.class);
    }

    @Override
    protected void doSseListen() {
    }

    public Flux<String> toFlux() {
        return toFlux(String.class);
    }


    public <R> Flux<R> toFlux(Class<R> clazz) {
        if (sse == null) {
            return null;
        }
        if (sse instanceof ReactorSSE) {
            return ((ReactorSSE) sse).toFlux(clazz);
        }
        return null;
    }

    @Override
    public ReactorRunner run() {
        super.run();
        return this;
    }

}
