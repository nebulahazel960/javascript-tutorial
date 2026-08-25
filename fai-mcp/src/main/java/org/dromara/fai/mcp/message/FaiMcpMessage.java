package org.dromara.fai.mcp.message;

public class FaiMcpMessage {
    
    private String jsonrpc = "2.0";

    private Long id;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
