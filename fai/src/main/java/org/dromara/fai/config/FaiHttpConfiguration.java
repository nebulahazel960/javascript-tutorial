package org.dromara.fai.config;

import cn.hutool.core.util.RandomUtil;
import com.dtflys.forest.Forest;
import com.dtflys.forest.backend.HttpBackend;
import com.dtflys.forest.callback.AddressSource;
import com.dtflys.forest.callback.RetryWhen;
import com.dtflys.forest.callback.SuccessWhen;
import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.converter.ForestConverter;
import com.dtflys.forest.converter.json.ForestJsonConverter;
import com.dtflys.forest.http.ForestAsyncMode;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.interceptor.Interceptor;
import com.dtflys.forest.logging.ForestLogHandler;
import com.dtflys.forest.reflection.ForestVariable;
import com.dtflys.forest.utils.ForestDataType;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class FaiHttpConfiguration {

    private final ForestConfiguration forestConfiguration;

    public FaiHttpConfiguration() {
        final String forestConfigurationId = "fai-" + RandomUtil.randomString(6);
        forestConfiguration = Forest.config(forestConfigurationId);
        forestConfiguration.setConnectTimeout(Duration.ofSeconds(20));
        forestConfiguration.setReadTimeout(Integer.MAX_VALUE);
        forestConfiguration.setLogHandler(new FaiHttpLogHandler());
    }


    public String getId() {
        return forestConfiguration.getId();
    }

    public int getMaxConnections() {
        return forestConfiguration.getMaxRetryCount();
    }

    public void setMaxConnections(int maxConnections) {
        forestConfiguration.setMaxRetryCount(maxConnections);
    }

    public int getMaxRouteConnections() {
        return forestConfiguration.getMaxRouteConnections();
    }

    public void setMaxRouteConnections(int maxRouteConnections) {
        forestConfiguration.setMaxRouteConnections(maxRouteConnections);
    }

    public int getMaxRequestQueueSize() {
        return forestConfiguration.getMaxRequestQueueSize();
    }

    public void setMaxRequestQueueSize(int maxRequestQueueSize) {
        forestConfiguration.setMaxRequestQueueSize(maxRequestQueueSize);
    }

    public int getMaxAsyncThreadSize() {
        return forestConfiguration.getMaxAsyncThreadSize();
    }

    public void setMaxAsyncThreadSize(int maxAsyncThreadSize) {
        forestConfiguration.setMaxAsyncThreadSize(maxAsyncThreadSize);
    }

    public int getMaxAsyncQueueSize() {
        return forestConfiguration.getMaxAsyncQueueSize();
    }

    public void setMaxAsyncQueueSize(int maxAsyncQueueSize) {
        forestConfiguration.setMaxAsyncQueueSize(maxAsyncQueueSize);
    }

    public ForestAsyncMode getAsyncMode() {
        return forestConfiguration.getAsyncMode();
    }

    public void setAsyncMode(ForestAsyncMode asyncMode) {
        forestConfiguration.setAsyncMode(asyncMode);
    }

    public Integer getConnectTimeout() {
        return forestConfiguration.getConnectTimeout();
    }

    public void setConnectTimeout(Integer connectTimeout) {
        forestConfiguration.setConnectTimeout(connectTimeout);
    }

    public Integer getReadTimeout() {
        return forestConfiguration.getReadTimeout();
    }

    public void setReadTimeout(Integer readTimeout) {
        forestConfiguration.setReadTimeout(readTimeout);
    }

    public String getCharset() {
        return forestConfiguration.getCharset();
    }

    public void setCharset(String charset) {
        forestConfiguration.setCharset(charset);
    }

    public void setBaseAddressScheme(String baseAddressScheme) {
        forestConfiguration.setBaseAddressScheme(baseAddressScheme);
    }


    public void setBaseAddressHost(String baseAddressHost) {
        forestConfiguration.setBaseAddressHost(baseAddressHost);
    }


    public void setBaseAddressPort(Integer baseAddressPort) {
        forestConfiguration.setBaseAddressPort(baseAddressPort);
    }

    public AddressSource getBaseAddressSource() {
        return forestConfiguration.getBaseAddressSource();
    }

    public void setBaseAddressSource(Class<? extends AddressSource> baseAddressSource) {
        forestConfiguration.setBaseAddressSourceClass(baseAddressSource);
    }

    public Class getRetryer() {
        return forestConfiguration.getRetryer();
    }

    public void setRetryer(Class retryer) {
        forestConfiguration.setRetryer(retryer);
    }

    public Integer getMaxRetryCount() {
        return forestConfiguration.getMaxRetryCount();
    }

    public void setMaxRetryCount(Integer maxRetryCount) {
        forestConfiguration.setMaxRetryCount(maxRetryCount);
    }

    public long getMaxRetryInterval() {
        return forestConfiguration.getMaxRetryInterval();
    }

    public void setMaxRetryInterval(long maxRetryInterval) {
        forestConfiguration.setMaxRetryInterval(maxRetryInterval);
    }

    public boolean isAutoRedirection() {
        return forestConfiguration.isAutoRedirection();
    }

    public void setAutoRedirection(boolean autoRedirection) {
        forestConfiguration.setAutoRedirection(autoRedirection);
    }

    public boolean isLogEnabled() {
        return forestConfiguration.isLogEnabled();
    }

    public void setLogEnabled(boolean logEnabled) {
        forestConfiguration.setLogEnabled(logEnabled);
    }

    public boolean isLogRequest() {
        return forestConfiguration.isLogRequest();
    }

    public void setLogRequest(boolean logRequest) {
        forestConfiguration.setLogRequest(logRequest);
    }

    public boolean isLogResponseStatus() {
        return forestConfiguration.isLogResponseStatus();
    }

    public void setLogResponseStatus(boolean logResponseStatus) {
        forestConfiguration.setLogResponseStatus(logResponseStatus);
    }

    public boolean isLogResponseContent() {
        return forestConfiguration.isLogResponseContent();
    }

    public void setLogResponseContent(boolean logResponseContent) {
        forestConfiguration.setLogResponseContent(logResponseContent);
    }

    public ForestLogHandler getLogHandler() {
        return forestConfiguration.getLogHandler();
    }

    public void setLogHandler(Class<? extends ForestLogHandler> logHandlerClass) {
        forestConfiguration.setLogHandler(forestConfiguration.getForestObject(logHandlerClass));
    }


    public String getSslProtocol() {
        return forestConfiguration.getSslProtocol();
    }

    public void setSslProtocol(String sslProtocol) {
        forestConfiguration.setSslProtocol(sslProtocol);
    }

    public HttpBackend getBackend() {
        return forestConfiguration.getBackend();
    }

    public void setBackend(String backend) {
        forestConfiguration.setBackendName(backend);
    }

    public void setBackend(HttpBackend backend) {
        forestConfiguration.setBackend(backend);
    }

    public Integer getBackendClientCacheMaxSize() {
        return forestConfiguration.getBackendClientCacheMaxSize();
    }

    public void setBackendClientCacheMaxSize(Integer backendClientCacheMaxSize) {
        forestConfiguration.setBackendClientCacheMaxSize(backendClientCacheMaxSize);
    }

    public Duration getBackendClientCacheExpireTime() {
        return forestConfiguration.getBackendClientCacheExpireTime();
    }

    public void setBackendClientCacheExpireTime(Duration backendClientCacheExpireTime) {
        forestConfiguration.setBackendClientCacheExpireTime(backendClientCacheExpireTime);
    }

    public Map<String, ForestVariable> getVariables() {
        return forestConfiguration.getVariables();
    }

    public void setVariables(Map<String, Object> variables) {
        forestConfiguration.setVariables(variables);
    }

    public List<Class<? extends Interceptor>> getInterceptors() {
        return forestConfiguration.getInterceptors();
    }

    public void setInterceptors(List<Class<? extends Interceptor>> interceptors) {
        forestConfiguration.setInterceptors(interceptors);
    }

    public SuccessWhen getSuccessWhen() {
        return forestConfiguration.getSuccessWhen();
    }

    public void setSuccessWhen(Class<? extends SuccessWhen> successWhen) {
        forestConfiguration.setSuccessWhenClass(successWhen);
    }

    public RetryWhen getRetryWhen() {
        return forestConfiguration.getRetryWhen();
    }

    public void setRetryWhen(Class<? extends RetryWhen> retryWhen) {
        forestConfiguration.setRetryWhenClass(retryWhen);
    }

    public ForestConverter<?> getConverter(ForestDataType dataType) {
        return forestConfiguration.getConverter(dataType);
    }

    public ForestJsonConverter getJsonConverter() {
        return forestConfiguration.getJsonConverter();
    }

    public <T> T client(Class<T> clazz) {
        return forestConfiguration.client(clazz);
    }

    public ForestRequest<?> request() {
        return forestConfiguration.request();
    }

    public <R> ForestRequest<R> request(Class<R> clazz) {
        return forestConfiguration.request(clazz);
    }

    public ForestRequest<?> get(String url) {
        return forestConfiguration.get(url);
    }

    public ForestRequest<?> get(String url, Object ...args) {
        return forestConfiguration.get(url, args);
    }

    public ForestRequest<?> post(String url) {
        return forestConfiguration.post(url);
    }

    public ForestRequest<?> post(String url, Object ...args) {
        return forestConfiguration.post(url, args);
    }

    public ForestRequest<?> put(String url) {
        return forestConfiguration.put(url);
    }

    public ForestRequest<?> put(String url, Object ...args) {
        return forestConfiguration.put(url, args);
    }

    public ForestRequest<?> delete(String url) {
        return forestConfiguration.delete(url);
    }

    public ForestRequest<?> delete(String url, Object ...args) {
        return forestConfiguration.delete(url, args);
    }

    public ForestRequest<?> head(String url) {
        return forestConfiguration.head(url);
    }

    public ForestRequest<?> head(String url, Object ...args) {
        return forestConfiguration.head(url, args);
    }

    public ForestRequest<?> patch(String url) {
        return forestConfiguration.patch(url);
    }

    public ForestRequest<?> patch(String url, Object ...args) {
        return forestConfiguration.patch(url, args);
    }


    public ForestRequest<?> options(String url) {
        return forestConfiguration.options(url);
    }

    public ForestRequest<?> options(String url, Object ...args) {
        return forestConfiguration.options(url, args);
    }

    public ForestRequest<?> trace(String url) {
        return forestConfiguration.trace(url);
    }

    public ForestRequest<?> trace(String url, Object ...args) {
        return forestConfiguration.trace(url, args);
    }

}
