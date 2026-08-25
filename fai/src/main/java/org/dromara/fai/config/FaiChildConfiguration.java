package org.dromara.fai.config;

import com.dtflys.forest.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.dromara.fai.exception.FaiRuntimeException;

public class FaiChildConfiguration extends FaiConfiguration {

    @JsonIgnore
    protected FaiGlobalConfiguration parent;
    
    protected String id;

    public FaiChildConfiguration() {
    }

    public FaiChildConfiguration(FaiGlobalConfiguration parent) {
        this.parent = parent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = format(id);
    }

    public FaiGlobalConfiguration getParent() {
        return parent;
    }

    void setParent(FaiGlobalConfiguration parent) {
        this.parent = parent;
    }

    void init(FaiGlobalConfiguration parent) {
        this.parent = parent;
        if (StringUtils.isNotEmpty(platform)) {
            platformConfig = FaiConfiguration.globalConfig().platform(platform);
            if (this.platformConfig == null) {
                throw new FaiRuntimeException("Platform not found: " + platform);
            }
            if (StringUtils.isEmpty(apiKey)) {
                apiKey = platformConfig.getApiKey();
            }
            if (StringUtils.isEmpty(baseUrl)) {
                baseUrl = platformConfig.getBaseUrl();
            }
        }
    }


    @Override
    public String getApiKey() {
        if (apiKey == null && parent != null) {
            return parent.getApiKey();
        }
        return apiKey;
    }

    @Override
    public String getBaseUrl() {
        if (baseUrl == null && parent != null) {
            return parent.getBaseUrl();
        }
        return baseUrl;
    }

    @Override
    public String getModel() {
        if (model == null && parent != null) {
            return parent.getModel();
        }
        return model;
    }
}
