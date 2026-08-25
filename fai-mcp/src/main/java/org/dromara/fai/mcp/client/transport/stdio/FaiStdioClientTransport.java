package org.dromara.fai.mcp.client.transport.stdio;

import org.dromara.fai.mcp.client.transport.FaiMcpClientTransport;
import org.dromara.fai.mcp.message.FaiMcpMessage;
import org.dromara.fai.tools.FaiTool;

import java.util.List;

public class FaiStdioClientTransport implements FaiMcpClientTransport {

    @Override
    public void connect(FaiMcpMessage message, Long id) {
        
    }

    @Override
    public void execute(FaiMcpMessage message) {
        
    }

    @Override
    public List<FaiTool> listTools() {
        return null;
    }

    @Override
    public void close() {
        
    }
}
