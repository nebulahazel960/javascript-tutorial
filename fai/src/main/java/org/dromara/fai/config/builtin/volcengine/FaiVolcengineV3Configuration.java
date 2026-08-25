package org.dromara.fai.config.builtin.volcengine;

import org.dromara.fai.config.FaiApiPath;

public class FaiVolcengineV3Configuration extends FaiApiPath {
    
    public FaiVolcengineV3Configuration(FaiVolcengineConfiguration configuration) {
        super(configuration, "/api/v3", "/chat/completions");
    }

}
