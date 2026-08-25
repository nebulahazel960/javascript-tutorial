package org.dromara.fai.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class FaiPlatformConfig implements Serializable {

    @JsonProperty("api-key")
    protected String apiKey;

    @JsonProperty("base-url")
    protected String baseUrl;

    @JsonProperty("api-base-path")
    protected String apiBasePath;

    @JsonProperty("api-chat-path")
    protected String apiChatPath = "/chat/completions";

    public FaiPlatformConfig() {
    }

    private FaiPlatformConfig(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiBasePath() {
        return apiBasePath;
    }

    public void setApiBasePath(String apiBasePath) {
        this.apiBasePath = apiBasePath;
    }

    public String getApiChatPath() {
        return apiChatPath;
    }

    public void setApiChatPath(String apiChatPath) {
        this.apiChatPath = apiChatPath;
    }
}
