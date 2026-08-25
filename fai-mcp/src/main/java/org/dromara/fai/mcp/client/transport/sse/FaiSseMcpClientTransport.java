package org.dromara.fai.mcp.client.transport.sse;

import com.dtflys.forest.Forest;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestSSE;
import com.dtflys.forest.utils.StringUtils;
import org.dromara.fai.mcp.client.transport.FaiMcpClientTransport;
import org.dromara.fai.mcp.message.FaiMcpMessage;
import org.dromara.fai.tools.FaiTool;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class FaiSseMcpClientTransport implements FaiMcpClientTransport {

    private final ForestRequest<?> request;
    
    private final AtomicReference<ForestSSE> sseRef = new AtomicReference<>();
    
    private final AtomicReference<String> sessionIdRef = new AtomicReference<>();
    
    private final AtomicReference<List<FaiTool>> toolsRef = new AtomicReference<>();

    public FaiSseMcpClientTransport(ForestRequest<?> request) {
        this.request = request;
    }
    
    public static FaiSseMcpClientTransport url(String url) {
        return post(url);
    }

    public static FaiSseMcpClientTransport get(String url) {
        return new FaiSseMcpClientTransport(Forest.get(url));
    }

    public static FaiSseMcpClientTransport post(String url) {
        return new FaiSseMcpClientTransport(Forest.post(url));
    }

    public static FaiSseMcpClientTransport put(String url) {
        return new FaiSseMcpClientTransport(Forest.post(url));
    }

    public static FaiSseMcpClientTransport delete(String url) {
        return new FaiSseMcpClientTransport(Forest.delete(url));
    }

    public static FaiSseMcpClientTransport head(String url) {
        return new FaiSseMcpClientTransport(Forest.head(url));
    }

    public static FaiSseMcpClientTransport options(String url) {
        return new FaiSseMcpClientTransport(Forest.options(url));
    }

    public static FaiSseMcpClientTransport trace(String url) {
        return new FaiSseMcpClientTransport(Forest.trace(url));
    }

    public FaiSseMcpClientTransport query(String queryString) {
        request.addQuery(queryString);
        return this;
    }

    public FaiSseMcpClientTransport query(String name, String value) {
        request.addQuery(name, value);
        return this;
    }
    
    public FaiSseMcpClientTransport header(String name, String value) {
        request.addHeader(name, value);
        return this;
    }

    public FaiSseMcpClientTransport header(Map<String, String> headers) {
        request.addHeader(headers);
        return this;
    }

    private ForestRequest<?> setupRequest(FaiMcpMessage message) {
        final String sessionId = sessionIdRef.get();
        return this.request
                .contentTypeJson()
                .addHeader("Accept", "application/json,text/event-stream")
                .cond(sessionId != null, q -> q.addHeader("Mcp-Session-Id", sessionId))
                .addBody(message);
    }

    @Override
    public void connect(FaiMcpMessage message, Long id) {
        final ForestSSE sse = setupRequest(message).sse()
                .setOnOpen(event -> {
                    final String sessionId = event.response().getHeaderValue("Mcp-Session-Id");
                    if (StringUtils.isNotEmpty(sessionId)) {
                        sessionIdRef.set(sessionId);
                    }
                });
        sseRef.set(sse);
        sse.listen(data -> {
            
        });
    }

    @Override
    public void execute(FaiMcpMessage message) {
        
    }

    @Override
    public List<FaiTool> listTools() {
        
        return null;
    }
    
    private void fetchTools() {
        
    }

    @Override
    public void close() {
        final ForestSSE sse = sseRef.get();
        if (sse != null) {
            sse.close();
        }
    }
}
