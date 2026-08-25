package org.dromara.fai.memory;

import org.dromara.fai.FaiMessage;
import org.dromara.fai.chat.FaiTextMessage;

import java.util.List;

public interface FaiMemory {

    void add(FaiTextMessage message);

    List<FaiMessage> messages();
}
