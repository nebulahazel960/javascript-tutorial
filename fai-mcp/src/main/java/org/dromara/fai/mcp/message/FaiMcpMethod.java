package org.dromara.fai.mcp.message;

public enum FaiMcpMethod {
    INITIALIZE("initialize"),
    TOOLS_CALL("tools/call"),
    TOOLS_LIST("tools/list"),
    NOTIFICATION_CANCELLED("notifications/cancelled"),
    NOTIFICATION_INITIALIZED("notifications/initialized"),
    PING("ping"),
    RESOURCES_LIST("resources/list"),
    RESOURCES_READ("resources/read"),
    RESOURCES_TEMPLATES_LIST("resources/templates/list"),
    PROMPTS_LIST("prompts/list"),
    PROMPTS_GET("prompts/get"),
    NOTIFICATION_ROOTS_LIST_CHANGED("notifications/roots/list_changed"),
    ;
    
    private final String method;

    FaiMcpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
