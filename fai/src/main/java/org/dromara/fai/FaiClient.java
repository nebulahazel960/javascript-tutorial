package org.dromara.fai;

import cn.hutool.cache.impl.LRUCache;
import com.dtflys.forest.utils.Validations;
import org.dromara.fai.config.FaiConfiguration;

public class FaiClient {
    
    private final static LRUCache<String, FaiClient> clientCache = new LRUCache<>(128);

    private final FaiConfiguration config;
    
    private final String apiKey;
    
    private final String baseUrl;
    
    public static FaiClient get(FaiConfiguration config, String apiKey, String baseUrl) {
        Validations.assertParamNotEmpty(apiKey, "apiKey");
        Validations.assertParamNotEmpty(baseUrl, "baseUrl");
        final String key = apiKey + "@" + baseUrl;
        return clientCache.get(key, () -> new FaiClient(config, key, baseUrl));
    }

    private FaiClient(FaiConfiguration config, String apiKey, String baseUrl) {
        this.config = config;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    public FaiConfiguration config() {
        return config;
    }

    public String apiKey() {
        return apiKey;
    }


    public String baseUrl() {
        return baseUrl;
    }
    
    public FaiModel model(String model) {
        return new FaiModel(this, model);
    }

}
