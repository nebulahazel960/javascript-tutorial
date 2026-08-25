package org.dromara.fai.mcp.message;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FaiMcpMethodMessage extends FaiMcpMessage {
    
    private final String method;

    private volatile Map<String, Object> params;
    
    @JsonIgnore
    private Lock lock = new ReentrantLock();
    
    public static FaiMcpMethodMessage of(String method) {
        return new FaiMcpMethodMessage(method);
    }

    public static FaiMcpMethodMessage of(FaiMcpMethod method) {
        return new FaiMcpMethodMessage(method.getMethod());
    }

    public FaiMcpMethodMessage(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public FaiMcpMethodMessage setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }
    
    public FaiMcpMethodMessage setParam(String key, Object value) {
        if (params == null) {
            lock.lock();
            try {
                if (params == null) {
                    params = new ConcurrentHashMap<>();
                }
            } finally {
                lock.unlock();
            }
        }
        params.put(key, value);
        return this;
    }
}
