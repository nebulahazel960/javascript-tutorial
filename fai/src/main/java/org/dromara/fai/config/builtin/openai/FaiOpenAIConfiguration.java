package org.dromara.fai.config.builtin.openai;

import org.dromara.fai.config.FaiChildConfiguration;
import org.dromara.fai.config.FaiGlobalConfiguration;

public class FaiOpenAIConfiguration extends FaiChildConfiguration {
    
    public FaiOpenAIConfiguration() {
        this(null);
        this.baseUrl = "https://api.openai.com";
        this.id = "openAI";
        this.apiChatPath = "/chat/completions";
    }

    public FaiOpenAIConfiguration(FaiGlobalConfiguration parent) {
        this.parent = parent;
    }

    public FaiOpenAIV1Configuration v1() {
        return new FaiOpenAIV1Configuration(this);
    }
}
