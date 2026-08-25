package org.dromara.fai.config;

import com.dtflys.forest.converter.json.ForestJsonConverter;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.logging.DefaultLogHandler;
import com.dtflys.forest.logging.LogBodyMessage;
import com.dtflys.forest.logging.RequestLogMessage;
import com.dtflys.forest.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FaiHttpLogHandler extends DefaultLogHandler {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public void logContent(String content) {
        getLogger().info("[Fai] " + content);
    }

    @Override
    protected String requestLoggingContent(RequestLogMessage requestLogMessage) {
        final ForestRequest request = requestLogMessage.getRequest();
        final StringBuilder builder = new StringBuilder();
        builder.append("Request ");
        builder.append(asyncModeContent(requestLogMessage));
        builder.append(": \n\t");
        builder.append(retryContent(requestLogMessage));
        builder.append(redirection(requestLogMessage));
        builder.append(proxyContent(requestLogMessage));
        builder.append(requestTypeChangeHistory(requestLogMessage));
        builder.append(requestLogMessage.getRequestLine());
        if (request.isLogRequestHeaders()) {
            final String headers = requestLoggingHeaders(requestLogMessage);
            if (StringUtils.isNotEmpty(headers)) {
                builder.append("\n\tHeaders: \n");
                builder.append(headers);
            }
        }
        if (request.isLogRequestBody()) {
            final String body = requestLoggingBody(requestLogMessage);
            if (StringUtils.isNotEmpty(body)) {
                builder.append("\n\tBody: \n");
                builder.append(body);
            }
        }
        return builder.toString();
    }

    @Override
    protected String requestLoggingBody(RequestLogMessage requestLogMessage) {
        final ForestJsonConverter converter = requestLogMessage.getRequest().getConfiguration().getJsonConverter();
        final LogBodyMessage logBodyMessage = requestLogMessage.getBody();
        if (logBodyMessage == null) {
            return "";
        }
        final String bodyContent = logBodyMessage.getBodyString();
        if (StringUtils.isNotEmpty(bodyContent)) {
            if (bodyContent.startsWith("{") && bodyContent.endsWith("}")) {
                final Map<String,  Object> map = converter.convertObjectToMap(bodyContent);
                try {
                    final String jsonStr = objectMapper.writeValueAsString(map);
                    try (final BufferedReader reader = new BufferedReader(new StringReader(jsonStr))) {
                        return reader.lines()
                                .map(line -> "\t\t" + line + "\n")
                                .collect(Collectors.joining());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (JsonProcessingException e) {
                    return "\t\t" + bodyContent;
                }
            }
        }
        return "\t\t" + bodyContent;
    }
}
