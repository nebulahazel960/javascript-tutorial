package org.dromara.fai;

public enum FaiRole {
    
    SYSTEM("system"),
    
    USER("user"),
    
    ASSISTANT("assistant"),
    
    ;

    private String name;

    FaiRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
