package org.dromara.fai.tools;

public enum FaiToolType {
    FUNCTION("function"),
    ;
    
    private final String name;

    FaiToolType(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }
}
