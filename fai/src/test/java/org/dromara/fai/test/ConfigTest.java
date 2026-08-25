package org.dromara.fai.test;

import org.dromara.fai.config.FaiConfiguration;
import org.junit.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigTest {
    
    @Test
    public void testConfigWithInputStream() {
        InputStream in = getClass().getClassLoader().getResourceAsStream("fai.yml");
        FaiConfiguration configuration = FaiConfiguration.loadConfig(in);
        assertThat(configuration).isNotNull();
        assertThat(configuration.getApiKey()).isEqualTo(System.getenv("API_KEY_DEEP_SEEK"));
        assertThat(configuration.getBaseUrl()).isEqualTo("https://api.deepseek.com");
        assertThat(configuration.getModel()).isEqualTo("deepseek-chat");
    }

    @Test
    public void testConfigWithFilepath() {
        FaiConfiguration configuration = FaiConfiguration.loadConfig("fai.yml");
        assertThat(configuration).isNotNull();
        assertThat(configuration.getApiKey()).isEqualTo(System.getenv("API_KEY_DEEP_SEEK"));
        assertThat(configuration.getBaseUrl()).isEqualTo("https://api.deepseek.com");
        assertThat(configuration.getModel()).isEqualTo("deepseek-chat");
    }

    @Test
    public void testConfigWithDefaultFileName() {
        FaiConfiguration configuration = FaiConfiguration.globalConfig();
        assertThat(configuration).isNotNull();
        assertThat(configuration.getApiKey()).isEqualTo(System.getenv("API_KEY_DEEP_SEEK"));
        assertThat(configuration.getBaseUrl()).isEqualTo("https://api.deepseek.com");
        assertThat(configuration.getModel()).isEqualTo("deepseek-chat");
    }


    @Test
    public void testConfigByID() {
        FaiConfiguration configuration = FaiConfiguration.config("model1");
        assertThat(configuration).isNotNull();
        assertThat(configuration.getApiKey()).isEqualTo("API_KEY_1");
        assertThat(configuration.getBaseUrl()).isEqualTo("BASE_URL_1");
        assertThat(configuration.getModel()).isEqualTo("model-test");
    }

    @Test
    public void testConfigByID2() {
        FaiConfiguration configuration = FaiConfiguration.config("model2");
        assertThat(configuration).isNotNull();
        assertThat(configuration.getApiKey()).isEqualTo(System.getenv("API_KEY_VOLCENGINE"));
        assertThat(configuration.getBaseUrl()).isEqualTo("https://ark.cn-beijing.volces.com");
        assertThat(configuration.getModel()).isEqualTo("doubao-1-5-pro-32k-250115");
    }


    @Test
    public void testConfigByID3() {
        FaiConfiguration configuration = FaiConfiguration.config("model3");
        assertThat(configuration).isNotNull();
        assertThat(configuration.getApiKey()).isEqualTo(System.getenv("API_KEY_DEEP_SEEK"));
        assertThat(configuration.getBaseUrl()).isEqualTo("https://openai.com");
        assertThat(configuration.getModel()).isEqualTo("deepseek-chat");
    }


}
