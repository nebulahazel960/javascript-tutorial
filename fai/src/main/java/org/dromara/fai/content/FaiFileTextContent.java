package org.dromara.fai.content;

import cn.hutool.core.io.FileUtil;
import com.dtflys.forest.http.ForestURL;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class FaiFileTextContent implements FaiTextContent {
    
    private File file;
    
    static FaiFileTextContent from(String filepath) {
        if (filepath == null) {
            return null;
        }
        if (filepath.contains("://")) {
            try {
                return from(new URL(filepath));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        final File f = FileUtil.file(filepath);
        return new FaiFileTextContent(f);
    }

    static FaiFileTextContent from(URI uri) {
        if (uri == null) {
            return null;
        }
        return new FaiFileTextContent(FileUtil.file(uri));
    }

    static FaiFileTextContent from(URL url) {
        if (url == null) {
            return null;
        }
        return new FaiFileTextContent(FileUtil.file(url));
    }

    public FaiFileTextContent(File file) {
        this.file = file;
    }


    @Override
    public String text() {
        if (file != null) {
            try (final FileInputStream fileInputStream = new FileInputStream(file)) {
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                return IOUtils.toString(bufferedReader);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
