package org.dromara.fai.mcp.client;

import org.dromara.fai.mcp.client.transport.FaiMcpClientTransport;
import org.dromara.fai.tools.FaiToolProvider;

public interface FaiMcpClient extends FaiToolProvider {
    
    static FaiMcpClient create(FaiMcpClientTransport transport) {
        final FaiMcpClient mcpClient = new DefaultMcpClient();
        mcpClient.transport(transport);
        return mcpClient;
    }
    
    void transport(FaiMcpClientTransport transport);

}
