package org.dromara.fai.config;

import org.dromara.fai.FaiModel;

public class FaiApiPath {
    
    private final FaiConfiguration configuration;
    
    private String apiBasePath;

    private String apiChatPath;
    
    public FaiApiPath(FaiConfiguration configuration, String apiBasePath) {
        this(configuration, apiBasePath, null);
    }

    public FaiApiPath(FaiConfiguration configuration, String apiBasePath, String apiChatPath) {
        this.configuration = configuration;
        this.apiBasePath = apiBasePath;
        this.apiChatPath = apiChatPath;
    }


    public FaiApiPath(FaiConfiguration configuration) {
        this.configuration = configuration;
    }


    public FaiConfiguration configuration() {
        return configuration;
    }

    public String getApiBasePath() {
        return apiBasePath;
    }

    public FaiApiPath apiPath(String apiPath) {
        this.apiBasePath = apiPath;
        return this;
    }

    public String getApiChatPath() {
        return apiChatPath;
    }

    public FaiApiPath apiChatPath(String apiChatPath) {
        this.apiChatPath = apiChatPath;
        return this;
    }

    public String getApiKey() {
        return configuration.getApiKey();
    }
    
    public String getBaseUrl() {
        return configuration.getBaseUrl();
    }
    
    public String getModel() {
        return configuration.getModel();
    }

    public static FaiModel model(FaiConfiguration config, String apiKey, String baseUrl, String model) {
        return new FaiModel(config, apiKey, baseUrl, model);
    }

    public FaiModel model() {
        final FaiModel retModel = model(configuration, getApiKey(), getBaseUrl(), getModel())
                .apiBasePath(getApiBasePath());
        if (apiChatPath != null) {
            retModel.apiChatPath(getApiChatPath());
        }
        return retModel;
    }

    public FaiModel model(String model) {
        final FaiModel retModel = model(configuration, getApiKey(), getBaseUrl(), model)
                .apiBasePath(getApiBasePath());
        if (apiChatPath != null) {
            retModel.apiChatPath(getApiChatPath());
        }
        return retModel;
    }


    public FaiModel apiKey(String apiKey) {
        final FaiModel retModel = model(configuration, apiKey, getBaseUrl(), getModel())
                .apiBasePath(getApiBasePath());
        if (apiChatPath != null) {
            retModel.apiChatPath(getApiChatPath());
        }
        return retModel;
    }

    public FaiModel baseUrl(String baseUrl) {
        final FaiModel retModel = model(configuration, getApiKey(), baseUrl, getModel())
                .apiBasePath(getApiBasePath());
        if (apiChatPath != null) {
            retModel.apiChatPath(getApiChatPath());
        }
        return retModel;
    }

}
