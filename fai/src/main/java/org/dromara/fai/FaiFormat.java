package org.dromara.fai;

public enum FaiFormat {
    TEXT("text"),
    JSON("json_object"),
    ;

    private final String name;

    FaiFormat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
