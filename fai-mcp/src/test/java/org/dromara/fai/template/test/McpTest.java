package org.dromara.fai.template.test;

import com.dtflys.forest.Forest;
import com.dtflys.forest.auth.BearerAuth;
import com.dtflys.forest.auth.ForestAuthenticator;
import org.dromara.fai.Fai;
import org.dromara.fai.callback.OnMessage;
import org.dromara.fai.chat.FaiChatResponse;
import org.dromara.fai.mcp.client.DefaultMcpClient;
import org.dromara.fai.mcp.client.FaiMcpClient;
import org.dromara.fai.mcp.client.transport.FaiMcpClientTransport;
import org.dromara.fai.mcp.client.transport.sse.FaiSseMcpClientTransport;
import org.dromara.fai.runner.FaiRunner;
import org.dromara.fai.runner.FaiSink;
import org.junit.Test;

public class McpTest {

    
    @Test
    public void testMcp2() {
        FaiMcpClientTransport transport = FaiSseMcpClientTransport.url("http://localhost:8080");
        FaiMcpClient client = FaiMcpClient.create(transport);
        Fai.modelOf("giteeAI-DeepSeek-V3.2-Exp")
                .stream()
                .chat("你好，你是谁？")
                .tool(client)
                .onMessage((runner, res) -> {
                    System.out.print(res.allContent());
                })
                .run();
    }
    
    
    @Test
    public void testSseMcp() {
        Forest.get("https://dashscope.aliyuncs.com/api/v1/mcps/amap-maps/sse")
                .authenticator(BearerAuth.token("xxxxx"))
                .addHeader("Accept", "application/json,text/event-stream")
                .sse()
                .setOnMessage((event, sink) -> {
                    System.out.println(event.rawData());
                })
                .listen();
    }

}
