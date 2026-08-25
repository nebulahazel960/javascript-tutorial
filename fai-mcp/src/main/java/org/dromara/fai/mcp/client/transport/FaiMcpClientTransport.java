package org.dromara.fai.mcp.client.transport;

import org.dromara.fai.mcp.message.FaiMcpMessage;
import org.dromara.fai.tools.FaiTool;

import java.util.List;

public interface FaiMcpClientTransport {

    void connect(FaiMcpMessage message, Long id);
    
    void execute(FaiMcpMessage message);
    
    List<FaiTool> listTools();
    
    void close();
}
