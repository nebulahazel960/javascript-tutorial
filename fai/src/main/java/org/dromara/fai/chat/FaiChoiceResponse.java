package org.dromara.fai.chat;

import com.dtflys.forest.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class FaiChoiceResponse implements Serializable {
    
    final static FaiChoiceResponse EMPTY = emptyChoice();
    
    private Integer index;

    private FaiMessageResponse message;
    
    private String logprobs;
    
    @JsonProperty("finish_reason")
    private String finishReason;
    
    private FaiMessageResponse delta;
    
    private static FaiChoiceResponse emptyChoice() {
        FaiChoiceResponse choice = new FaiChoiceResponse();
        choice.index = 0;
        choice.message = null;
        choice.logprobs = null;
        choice.finishReason = null;
        choice.delta = FaiMessageResponse.EMPTY_DELTA;
        return choice;
    }
    
    public Integer index() {
        return getIndex();
    }

    public Integer getIndex() {
        return index;
    }

    public FaiMessageResponse message() {
        return getMessage();
    }

    public FaiMessageResponse getMessage() {
        return message;
    }

    public void setMessage(FaiMessageResponse message) {
        this.message = message;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }


    public String logprobs() {
        return getLogprobs();
    }

    public String getLogprobs() {
        return logprobs;
    }

    public void setLogprobs(String logprobs) {
        this.logprobs = logprobs;
    }
    
    public String finishReason() {
        return getFinishReason();
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }
    
    public FaiMessageResponse delta() {
        return getDelta();
    }

    public FaiMessageResponse getDelta() {
        return delta;
    }

    public void setDelta(FaiMessageResponse delta) {
        this.delta = delta;
    }
    
    public boolean isToolCalls() {
        return delta != null && delta().isToolCalls();
    }
    
    public boolean isFinished() {
        return StringUtils.isNotEmpty(finishReason);
    }

    public FaiMessageResponse deltaOrMessage() {
        return delta != null ? delta : message;
    }
    
}
