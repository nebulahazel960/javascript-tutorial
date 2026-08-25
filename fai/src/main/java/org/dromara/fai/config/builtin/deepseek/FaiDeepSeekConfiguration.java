package org.dromara.fai.config.builtin.deepseek;

import org.dromara.fai.FaiModel;
import org.dromara.fai.config.FaiChildConfiguration;
import org.dromara.fai.config.FaiGlobalConfiguration;

public class FaiDeepSeekConfiguration extends FaiChildConfiguration {

    public FaiDeepSeekConfiguration(FaiGlobalConfiguration parent) {
        this.parent = parent;
        this.baseUrl = "https://api.deepseek.com";
        this.apiChatPath = "/chat/completions";
        this.id = "deepSeek";
    }


    public FaiDeepSeekConfiguration() {
        this(null);
    }
    
    public FaiModel v3() {
        return model("deepseek-chat");
    }
    
    public FaiModel r1() {
        return model("deepseek-r1");
    }
}
