package org.dromara.fai.param;

import com.dtflys.forest.utils.StringUtils;

import java.io.Serializable;

public class ResponseFormatParam implements Serializable {
    
    private final String type;
    
    public static ResponseFormatParam get(String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        return new ResponseFormatParam(type);
    }

    public ResponseFormatParam(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
