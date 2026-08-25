package org.dromara.fai.param;

import java.io.Serializable;

public class ChatMessageParam implements Serializable {

    private final String role;

    private final String content;

    public ChatMessageParam(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

}
