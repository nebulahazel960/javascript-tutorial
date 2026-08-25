package org.dromara.fai.content;

import java.net.URI;
import java.net.URL;

public interface FaiTextContent extends FaiContent {
    
    static FaiDefaultTextContent fromText(String content) {
        return FaiDefaultTextContent.from(content);
    }

    static FaiFileTextContent fromFile(String filepath) {
        return FaiFileTextContent.from(filepath);
    }

    static FaiNetworkTextContent fromURI(String uri) {
        return FaiNetworkTextContent.from(uri);
    }

    static FaiNetworkTextContent fromURI(URI uri) {
        return FaiNetworkTextContent.from(uri);
    }

    static FaiNetworkTextContent fromURL(URL url) {
        return FaiNetworkTextContent.from(url);
    }
    
    String text();
    
}
