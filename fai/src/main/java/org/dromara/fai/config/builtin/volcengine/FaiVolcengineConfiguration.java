package org.dromara.fai.config.builtin.volcengine;

import org.dromara.fai.config.FaiChildConfiguration;
import org.dromara.fai.config.FaiGlobalConfiguration;

public class FaiVolcengineConfiguration extends FaiChildConfiguration {

    public FaiVolcengineConfiguration(FaiGlobalConfiguration parent) {
        this.parent = parent;
        this.baseUrl = "https://ark.cn-beijing.volces.com";
        this.apiChatPath = "/chat/completions";
        this.id = "volcengine";
    }


    public FaiVolcengineConfiguration() {
        this(null);
    }
    
    public FaiVolcengineV3Configuration v3() {
        return new FaiVolcengineV3Configuration(this);
    }
}
