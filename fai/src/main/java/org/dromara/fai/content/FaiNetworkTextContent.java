package org.dromara.fai.content;

import com.dtflys.forest.http.ForestURL;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FaiNetworkTextContent implements FaiTextContent {

    private ForestURL url;

    static FaiNetworkTextContent from(String urlstring) {
        if (urlstring == null) {
            return null;
        }
        return new FaiNetworkTextContent(ForestURL.fromUrl(urlstring));
    }

    static FaiNetworkTextContent from(URI uri) {
        if (uri == null) {
            return null;
        }
        return new FaiNetworkTextContent(ForestURL.fromUrl(uri.toString()));
    }

    static FaiNetworkTextContent from(URL url) {
        if (url == null) {
            return null;
        }
        return new FaiNetworkTextContent(ForestURL.fromUrl(url.toString()));
    }


    public FaiNetworkTextContent(ForestURL url) {
        this.url = url;
    }

    @Override
    public String text() {
        if (url != null) {
            try {
                return IOUtils.toString(url.toJavaURL(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
