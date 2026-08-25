package org.dromara.fai.config;

import cn.hutool.core.util.StrUtil;
import com.dtflys.forest.utils.StringUtils;
import org.dromara.fai.FaiClient;
import org.dromara.fai.FaiModel;
import org.dromara.fai.chat.FaiChat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class FaiConfiguration extends FaiPlatformConfig {

    private static final String DEFAULT_FAI_YAML_FILE_NAME = "fai.yml";

    private volatile static FaiGlobalConfiguration globalConfig;

    private final static Map<String, String> ENV_VARS = new ConcurrentHashMap<>();
    
    private final static Lock LOCK = new ReentrantLock();
    
    static {
        final Map<String, String> envVars = System.getenv();
        for (final String key : envVars.keySet()) {
            ENV_VARS.put("%" + key, envVars.get(key));
        }
    }
    
    protected static String format(String str) {
        return str == null ? null : StrUtil.format(str, ENV_VARS);
    }
    
    protected String platform;
    
    protected FaiChildConfiguration platformConfig;
    
    protected String model;
    
    public static FaiGlobalConfiguration globalConfig() {
        if (globalConfig == null) {
            LOCK.lock(); 
            try {
                if (globalConfig == null) {
                    try {
                        globalConfig = loadConfig();
                    } catch (Throwable e) {
                    }
                    if (globalConfig == null) {
                        globalConfig = new FaiGlobalConfiguration();
                        globalConfig.init();
                    }
                }
            } finally {
                LOCK.unlock();
            }
        }
        return globalConfig;
    }
    
    
    public static FaiConfiguration config(String id) {
        return globalConfig().modelMap.get(id);
    }
    
    public static FaiGlobalConfiguration loadConfig() {
        return loadConfig(DEFAULT_FAI_YAML_FILE_NAME);
    }

    public static FaiGlobalConfiguration loadConfig(String filepath) {
        final File file = new File(filepath);
        if (file.exists()) {
            return loadConfig(new File(filepath));
        }
        final InputStream in = FaiGlobalConfiguration.class.getClassLoader().getResourceAsStream(filepath);
        if (in != null) {
            return loadConfig(in);
        }
        throw new RuntimeException(new FileNotFoundException(filepath));
    }

    public static FaiGlobalConfiguration loadConfig(File file) {
        try {
            return loadConfig(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static FaiGlobalConfiguration loadConfig(InputStream in) {
        FaiGlobalConfiguration fai = FaiYaml.loadConfig(in);
        globalConfig = fai;
        globalConfig.init();
        return fai;
    }


    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public void setApiKey(String apiKey) {
        this.apiKey = format(apiKey);
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = format(baseUrl);
    }

    @Override
    public String getApiBasePath() {
        return apiBasePath;
    }

    @Override
    public void setApiBasePath(String apiBasePath) {
        this.apiBasePath = format(apiBasePath);
    }
    
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = format(model);
    }

    public FaiClient client(String apiKey, String baseUrl) {
        return FaiClient.get(this, apiKey, baseUrl);
    }

    public FaiClient client() {
        return client(apiKey, baseUrl);
    }

    public FaiModel model(String platform, String apiKey, String baseUrl, String model) {
        if (StringUtils.isNotEmpty(platform)) {
            return FaiModel.ofPlatform(this, platform)
                    .apiKey(apiKey)
                    .baseUrl(baseUrl)
                    .model(model)
                    .apiBasePath(getApiBasePath())
                    .apiChatPath(getApiChatPath());
        }
        return new FaiModel(this, apiKey, baseUrl, model)
                .apiBasePath(getApiBasePath())
                .apiChatPath(getApiChatPath());
    }


    public FaiModel model(String apiKey, String baseUrl, String model) {
        return new FaiModel(this, apiKey, baseUrl, model)
                .apiBasePath(getApiBasePath())
                .apiChatPath(getApiChatPath());
    }

    public FaiModel model() {
        return model(getPlatform(), getApiKey(), getBaseUrl(), getModel());
    }

    public FaiModel model(String model) {
        return model(getPlatform(), getApiKey(), getBaseUrl(), model);
    }

    public FaiModel apiKey(String apiKey) {
        return model(getPlatform(), apiKey, getBaseUrl(), getModel());
    }

    public FaiModel baseUrl(String baseUrl) {
        return model(getPlatform(), getApiKey(), baseUrl, getModel());
    }
    
    public FaiChat chat() {
        return model().chat();
    }

    public <R extends FaiChat> R chat(Class<R> clazz) {
        return model().chat(clazz);
    }

}
