package org.dromara.fai.test.function;

import lombok.extern.slf4j.Slf4j;
import org.dromara.fai.annotation.AIParam;
import org.dromara.fai.annotation.AITool;
import org.dromara.fai.chat.FaiChatResponse;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MyCounter {
    
    private final AtomicInteger counter = new AtomicInteger(0);

    @AITool(desc = "计数器自增累加")
    public int incrementNumber(@AIParam(desc = "要累加的数量") int value, FaiChatResponse response) {
        System.out.println("\n\n\tmodel: " + response.model() + "\n");
        int oldValue = counter.get();
        while (!counter.compareAndSet(oldValue, oldValue + value)) {
            oldValue = counter.get();
        }
        return counter.get();
    }


    @AITool(desc = "计数器自增累加")
    public int increment(@AIParam(desc = "要累加的数量") int value) {
        int oldValue = counter.get();
        while (!counter.compareAndSet(oldValue, oldValue + value)) {
            oldValue = counter.get();
        }
        return counter.get();
    }

    @AITool(name = "incrementOne", desc = "计数器自增加一")
    public int increment() {
        return counter.incrementAndGet();
    }


    public int getCounter() {
        return counter.get();
    }
}
