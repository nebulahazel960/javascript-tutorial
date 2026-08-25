package org.dromara.fai.mcp.client;

import org.dromara.fai.mcp.client.transport.FaiMcpClientTransport;
import org.dromara.fai.tools.FaiTool;

import java.util.ArrayList;
import java.util.List;

public class DefaultMcpClient implements FaiMcpClient {
    
    private FaiMcpClientTransport transport;
    
    @Override
    public List<FaiTool> tools() {
        return new ArrayList<>();
    }

    @Override
    public void transport(FaiMcpClientTransport transport) {
        this.transport = transport;
    }
}
