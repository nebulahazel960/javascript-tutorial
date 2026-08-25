package org.dromara.fai.memory;

import org.dromara.fai.FaiMessage;
import org.dromara.fai.chat.FaiTextMessage;

import java.util.ArrayList;
import java.util.List;

public class FaiFullMemory implements FaiMemory {

    private final List<FaiMessage> messages = new ArrayList<>();

    @Override
    public void add(FaiTextMessage message) {
        messages.add(message);
    }

    @Override
    public List<FaiMessage> messages() {
        return messages;
    }

}
