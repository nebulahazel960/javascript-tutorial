package org.dromara.fai.config.builtin.openai;

import org.dromara.fai.FaiModel;
import org.dromara.fai.config.FaiApiPath;
import org.dromara.fai.config.builtin.volcengine.FaiVolcengineConfiguration;

public class FaiOpenAIV1Configuration extends FaiApiPath {
    
    public FaiOpenAIV1Configuration(FaiOpenAIConfiguration configuration) {
        super(configuration, "/v1", "/chat/completions");
    }
    
    
}
