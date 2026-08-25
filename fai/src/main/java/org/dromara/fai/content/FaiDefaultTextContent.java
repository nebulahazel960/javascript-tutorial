package org.dromara.fai.content;

public class FaiDefaultTextContent implements FaiTextContent {
    
    private String content;
    
    
    static FaiDefaultTextContent from(String content) {
        return new FaiDefaultTextContent(content);
    }
    
    public FaiDefaultTextContent(String content) {
        this.content = content;
    }

    @Override
    public String text() {
        return content;
    }
}
