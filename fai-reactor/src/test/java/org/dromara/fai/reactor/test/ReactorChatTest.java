package org.dromara.fai.reactor.test;

import org.dromara.fai.Fai;
import org.dromara.fai.FaiRole;
import org.dromara.fai.reactor.ReactorChat;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ReactorChatTest {

    @Test
    public void testFastChat() {
        Map<String, Object> map = new HashMap<>();
        map.put("language", "中文");
        
        Fai.chat(FaiRole.SYSTEM, "你是一位翻译专家", ReactorChat.class)
                .user("请将下列的英文翻译成{language ?? '西班牙语'}")
                .user("Hello")
                .env(map)
                .onMessage((runner, res, sink) -> {
                    sink.next(res.content());
                })
                .toFlux(String.class)
                .subscribe(System.out::print);
    }

}
