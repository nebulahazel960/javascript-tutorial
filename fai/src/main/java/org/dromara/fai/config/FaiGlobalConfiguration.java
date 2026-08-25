package org.dromara.fai.config;

import org.dromara.fai.config.builtin.deepseek.FaiDeepSeekConfiguration;
import org.dromara.fai.config.builtin.openai.FaiOpenAIConfiguration;
import org.dromara.fai.config.builtin.volcengine.FaiVolcengineConfiguration;
import org.dromara.fai.chat.FaiTextMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FaiGlobalConfiguration extends FaiConfiguration {
    
    private FaiHttpConfiguration http = new FaiHttpConfiguration();
    
    private Map<String, FaiChildConfiguration> platforms = new ConcurrentHashMap<>();

    private List<FaiChildConfiguration> models = new ArrayList<>();
    
    Map<String, FaiChildConfiguration> modelMap = new ConcurrentHashMap<>();
    
    private List<FaiPromptConfiguration> prompts = new ArrayList<>();

    Map<String, FaiPromptConfiguration> promptMap = new ConcurrentHashMap<>();
    
    void init() {
        for (final FaiChildConfiguration platform : platforms.values()) {
            platform.parent = this;
        }
        for (final FaiChildConfiguration model : modelMap.values()) {
            model.init(this);
        }
    }

    public FaiOpenAIConfiguration getOpenAI() {
        return (FaiOpenAIConfiguration) platform("openAI");
    }

    public void setOpenAI(FaiOpenAIConfiguration openAI) {
        platforms.put("openAI", openAI);
    }

    public FaiDeepSeekConfiguration getDeepSeek() {
        return (FaiDeepSeekConfiguration) platform("deepSeek");
    }

    public void setDeepSeek(FaiDeepSeekConfiguration deepSeek) {
        platforms.put("deepSeek", deepSeek);
    }

    public FaiVolcengineConfiguration getVolcengine() {
        return (FaiVolcengineConfiguration) platform("volcengine");
    }

    public void setVolcengine(FaiVolcengineConfiguration volcengine) {
        platforms.put("volcengine", volcengine);
    }
    
    public FaiChildConfiguration platform(String platfom) {
        return platforms.computeIfAbsent(platfom, k -> {
            switch (platfom) {
                case "openAI": return new FaiOpenAIConfiguration(this);
                case "deepSeek": return new FaiDeepSeekConfiguration(this);
                case "volcengine": return new FaiVolcengineConfiguration(this);
                default: return new FaiChildConfiguration(this);
            }
        });
    }

    public List<FaiChildConfiguration> getModels() {
        return models;
    }

    public void setModels(List<FaiChildConfiguration> models) {
        this.models = models;
    }

    public List<FaiPromptConfiguration> getPrompts() {
        return prompts;
    }

    public void setPrompts(List<FaiPromptConfiguration> prompts) {
        this.prompts = prompts;
    }

    public FaiHttpConfiguration getHttp() {
        return http;
    }

    public void setHttp(FaiHttpConfiguration http) {
        this.http = http;
    }

    public FaiHttpConfiguration http() {
        return getHttp();
    }

    public List<FaiMessageConfiguration> promptMessageConfigsOf(String id) {
        final FaiPromptConfiguration promptConfig = promptMap.get(id);
        if (promptConfig == null) {
            return new ArrayList<>();
        }
        return promptConfig.getMessages();
    }
    
    public List<FaiTextMessage> promptMessagesOf(String id) {
        final List<FaiMessageConfiguration> messageConfigs = promptMessageConfigsOf(id);
        final List<FaiTextMessage> messages = new ArrayList<>();
        for (FaiMessageConfiguration msgConfig : messageConfigs) {
            final String sys = msgConfig.getSystem();
            final String user = msgConfig.getUser();
            if (sys != null) {
                messages.add(FaiTextMessage.system(sys));
            }
            if (user != null) {
                messages.add(FaiTextMessage.user(user));
            }
        }
        return messages;
    }

}
