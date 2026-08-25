package org.dromara.fai;

import com.dtflys.forest.exceptions.ForestRuntimeException;
import org.dromara.fai.chat.FaiChat;
import org.dromara.fai.config.FaiChildConfiguration;
import org.dromara.fai.config.FaiConfiguration;
import org.dromara.fai.exception.FaiRuntimeException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class FaiModel {

    private final FaiConfiguration config;
    
    private String id;
    
    private String apiKey;

    private String baseUrl;
    
    private String apiBasePath;

    private String apiChatPath;

    private String model;
    
    private Boolean stream;
    
    public static FaiModel ofPlatform(FaiConfiguration config, String platform) {
        return new FaiModel(config).platform(platform);
    }


    public FaiModel(FaiConfiguration config, String apiKey, String baseUrl, String model) {
        this.config = config;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.model = model;
    }

    public FaiModel(FaiClient client, String model) {
        this.config = client.config();
        this.apiKey = client.apiKey();
        this.baseUrl = client.baseUrl();
        this.model = model;
    }

    public FaiModel(FaiConfiguration config, String model) {
        this.config = config;
        this.model = model;
    }

    public FaiModel(FaiConfiguration config) {
        this.config = config;
    }
    
    public FaiModel platform(String platfom) {
        final FaiChildConfiguration config = FaiConfiguration.globalConfig().platform(platfom);
        if (config == null) {
            throw new FaiRuntimeException("Platform not found: " + platfom);
        }
        this.apiKey = config.getApiKey();
        this.baseUrl = config.getBaseUrl();
        return this;
    }

    public String id() {
        return id;
    }

    public FaiModel id(String id) {
        this.id = id;
        return this;
    }

    public FaiConfiguration config() {
        return config;
    }

    public FaiClient client() {
        return FaiClient.get(config, apiKey, baseUrl);
    }
    
    public FaiModel stream(boolean stream) {
        this.stream = stream;
        return this;
    }
    
    public FaiModel stream() {
        return stream(true);
    }

    public FaiModel model(String model) {
        this.model = model;
        return this;
    }

    public FaiModel apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }
    
    public String apiKey() {
        return apiKey;
    }
    
    public FaiModel baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }
    
    public String baseUrl() {
        return baseUrl;
    }

    public String apiBasePath() {
        return apiBasePath;
    }

    public FaiModel apiBasePath(String apiPath) {
        this.apiBasePath = apiPath;
        return this;
    }

    public String apiChatPath() {
        return apiChatPath != null ? apiChatPath : config().getApiChatPath();
    }

    public FaiModel apiChatPath(String apiChatPath) {
        if (apiChatPath != null) {
            this.apiChatPath = apiChatPath;
        }
        return this;
    }


    public String model() {
        return model;
    }
    
    public FaiChat chat() {
        return new FaiChat(this, stream);
    }

    public <R extends FaiChat> R chat(Class<R> clazz) {
        try {
            final Constructor<R> constructor = clazz.getConstructor(FaiModel.class, Boolean.class);
            return constructor.newInstance(this, stream);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ForestRuntimeException(e);
        }
    }


    public FaiChat chat(String role, String ...content) {
        return new FaiChat(this, stream).prompt(role, content);
    }

    public FaiChat chat(FaiRole role, String ...content) {
        return new FaiChat(this, stream).prompt(role, content);
    }

    public FaiChat chat(String content) {
        return user(content);
    }

    public FaiChat chat(String ...content) {
        return user(content);
    }

    public FaiChat chat(List<String> messages) {
        return user(messages);
    }

    public <R extends FaiChat> R chat(String content, Class<R> clazz) {
        return (R) chat(clazz).user(content);
    }
    
    public <R extends FaiChat> R chat(List<String> messages, Class<R> clazz) {
        return (R) chat(clazz).user(messages);
    }

    public <R extends FaiChat> R chat(FaiRole role, String content, Class<R> clazz) {
        return (R) chat(clazz).prompt(role, content);
    }

    public <R extends FaiChat> R chat(FaiRole role, List<String> messages, Class<R> clazz) {
        return (R) chat(clazz).prompt(role, messages);
    }



    public FaiChat promptOf(String id) {
        return chat().promptId(id);
    }
    
    public FaiChat message(FaiMessage...messages) {
        return chat().prompt(messages);
    }
    
    public <T extends FaiMessage> FaiChat message(List<T> messages) {
        return chat().prompt(messages);
    }
    
    public FaiChat message(FaiMessage message) {
        return chat().prompt(message);
    }

    public FaiChat message(FaiRole role, String content) {
        return chat().prompt(role, content);
    }

    public FaiChat message(FaiRole role, List<String> content) {
        return chat().prompt(role, content);
    }

    public FaiChat message(FaiRole role, String ...content) {
        return chat().prompt(role, content);
    }


    public FaiChat message(String role, String content) {
        return chat().prompt(role, content);
    }

    public FaiChat message(String role, List<String> content) {
        return chat().prompt(role, content);
    }

    public FaiChat message(String role, String ...content) {
        return chat().prompt(role, content);
    }

    public FaiChat system(String content) {
        return chat().system(content);
    }

    public FaiChat system(List<String> content) {
        return chat().system(content);
    }

    public FaiChat system(String ...content) {
        return chat().system(content);
    }

    public FaiChat user(String content) {
        return chat().user(content);
    }

    public FaiChat user(List<String> content) {
        return chat().user(content);
    }

    public FaiChat user(String ...content) {
        return chat().user(content);
    }

    public FaiChat assistant(String content) {
        return chat().assistant(content);
    }

    public FaiChat assistant(List<String> content) {
        return chat().assistant(content);
    }

    public FaiChat assistant(String ...content) {
        return chat().assistant(content);
    }

}
