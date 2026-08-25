package org.dromara.fai.chat;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import com.dtflys.forest.Forest;
import com.dtflys.forest.utils.ForestDataType;
import com.dtflys.forest.utils.StringUtils;
import com.dtflys.forest.utils.TypeReference;

import javax.crypto.NullCipher;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FaiChatResponse implements Serializable {
    
    private final static int CACHE_KEY_DEFAULT_CONTENT = 0;

    private final static int CACHE_KEY_REASONING_CONTENT = 1;
    
    private List<FaiChoiceResponse> EMPTY_CHOICES = ListUtil.of(FaiChoiceResponse.EMPTY);
    
    private List<FaiChoiceResponse> choices;
    
    private String rawData;
    
    private String id;
    
    private Date created;
    
    private String model;
    
    private String object;

    private Object toolCallsResult;
    
    private volatile String cachedContent;
    
    private volatile String cachedReasoningContent;
    
    private final Lock lock = new ReentrantLock();
    
    public boolean hasContent() {
        return null != content();
    }

    public boolean hasReasoning() {
        return null != reasoningContent();
    }


    public String reasoningContent() {
        return getContentText(FaiMessageResponse::getReasoningContent);
    }

    public String content() {
        return getContentText(FaiMessageResponse::getContent);
    }
    
    public String allContent() {
        final StringBuilder builder = new StringBuilder();
        final String reasoningContent = reasoningContent();
        if (StringUtils.isNotEmpty(reasoningContent)) {
            builder.append(reasoningContent);
        }
        final String content = content();
        if (StringUtils.isNotEmpty(content)) {
            builder.append(content);
        }
        return builder.toString();
    }

    private String getContentText(Function<FaiMessageResponse, String> func) {
        final StringBuilder contentBuilder = new StringBuilder();
        final List<FaiMessageResponse> messages = deltasOrMessages();
        if (messages != null) {
            for (final FaiMessageResponse message : messages) {
                if (message != null) {
                    final String content = func.apply(message);
                    contentBuilder.append(content == null ? "" : content);
                }
            }
        }
        return contentBuilder.toString();
    }


    public <R> R resultAs(Class<R> clazz) {
        final String str = content();
        Object obj = Forest.config().getConverter(ForestDataType.AUTO).convertToJavaObject(str, clazz);
        if (obj == null) {
            return null;
        }
        return clazz.cast(obj);
    }

    public <R> R resultAs(TypeReference<R> typeReference) {
        final String str = content();
        if (str == null) {
            return null;
        }
        Object obj = Forest.config().getConverter(ForestDataType.AUTO).convertToJavaObject(str, typeReference.getType());
        if (obj == null) {
            return null;
        }
        return (R) obj;
    }


    public List<FaiMessageResponse> deltas() {
        return choices().stream()
                .map(FaiChoiceResponse::delta)
                .collect(Collectors.toList());
    }

    private List<FaiMessageResponse> deltasOrMessages() {
        return choices().stream()
                .map(FaiChoiceResponse::deltaOrMessage)
                .collect(Collectors.toList());
    }


    public List<FaiChoiceResponse> choices() {
        return getChoices();
    }
    
    public List<FaiChoiceResponse> getChoices() {
        if (choices == null) {
            return EMPTY_CHOICES;
        }
        return choices;
    }


    public void setChoices(List<FaiChoiceResponse> choices) {
        this.choices = choices;
    }
    
    public String rawData() {
        return getRawData();
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public String id() {
        return getId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public Date created() {
        return getCreated();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
    
    public String model() {
        return getModel();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
        
    public String object() {
        return getObject();
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
    
    public boolean isToolCalls() {
        if (CollectionUtil.isEmpty(choices)) {
            return false;
        }
        for (final FaiChoiceResponse choice : choices) {
            if (choice.isToolCalls()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isFinished() {
        if (choices != null) {
            for (final FaiChoiceResponse choice : choices) {
                if (choice.isFinished()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public String finishReason() {
        if (choices != null) {
            for (final FaiChoiceResponse choice : choices) {
                if (choice.isFinished()) {
                    return choice.finishReason();
                }
            }
        }
        return "";
    }

    public Object toolCallsResult() {
        return toolCallsResult;
    }

    public <T> T toolCallsResult(Class<T> clazz) {
        if (toolCallsResult == null) {
            return null;
        }
        return Convert.convert(clazz, toolCallsResult);
    }

    public <T> T toolCallsResult(TypeReference<T> typeReference) {
        if (toolCallsResult == null) {
            return null;
        }
        return Convert.convert(typeReference.getType(), toolCallsResult);
    }


    public void setToolCallsResult(Object toolCallsResult) {
        this.toolCallsResult = toolCallsResult;
    }
}
