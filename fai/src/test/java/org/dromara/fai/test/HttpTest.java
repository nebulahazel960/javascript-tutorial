package org.dromara.fai.test;

import com.dtflys.forest.Forest;
import com.dtflys.forest.utils.TypeReference;
import org.dromara.fai.Fai;
import org.dromara.fai.test.http.WeatherClient;
import org.dromara.fai.tools.FaiTool;
import org.dromara.fai.wrapper.FaiObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class HttpTest {
    
    private final WeatherClient weatherClient = Forest.client(WeatherClient.class);


    @Test
    public void testForestClientObjectTool() {
        FaiObject object = Fai.object(weatherClient);
        List<FaiTool> tools = object.tools();
        assertThat(tools).isNotNull();
        assertThat(tools.size()).isEqualTo(1);
    }


    @Test
    public void testWeatherWithDeepSeek_getContent() {
        final Map<String, Object> vars = new HashMap<>();
        vars.put("role", "AI天气助手");
        vars.put("city", "苏州");

        final String content = Fai.deepSeek().v3()
                .chat()
                .logEnabled(true)
                .stream()
                .system("你是一个{role}")
                .user("{city}今天的天气如何")
                .env(vars)
                .tool(weatherClient)
                .run()
                .content();
        System.out.println("Content: " + content);

        assertThat(content).isNotNull();
        assertThat(content.contains("status")).isTrue();
        assertThat(content.contains("lives")).isTrue();
    }


    @Test
    public void testWeatherWithDeepSeek_getContent_Lambda() {
        final Map<String, Object> vars = new HashMap<>();
        vars.put("ai-role", "AI天气助手");
        vars.put("city", "苏州");

        String content = Fai.deepSeek().v3()
                .chat()
                .stream()
                .system("你是一个{ai-role}")
                .user("{city}今天的天气如何")
                .tool(weatherClient::getBaseWeather)
                .env(vars)
                .run()
                .content();
        System.out.println("Content: " + content);
    }


    @Test
    public void testWeatherWithDeepSeek_getToolCallsResult() {
        final Map<String, Object> vars = new HashMap<>();
        vars.put("ai-role", "AI天气助手");
        vars.put("city", "苏州");

        Map<String, Object> content = Fai.deepSeek().v3()
                .chat()
                .stream()
                .system("你是一个{ai-role}")
                .user("{city}今天的天气如何")
                .tool(weatherClient::getBaseWeather)
                .env(vars)
                .run()
                .get(new TypeReference<Map<String, Object>>() {});
        System.out.println("Content: " + content);
        
        assertThat(content).isNotNull();
        assertThat(content.get("status")).isEqualTo("1");
        assertThat(content.get("info")).isEqualTo("OK");
    }


    @Test
    public void testWeatherWithDoubao_getContent() {
        final Map<String, Object> vars = new HashMap<>();
        vars.put("ai-role", "AI天气助手");
        vars.put("city", "苏州");

        String content = Fai.volcengine().v3().model("doubao-1-5-pro-32k-250115")
                .chat()
                .stream()
                .system("你是一个{ai-role}")
                .user("{city}今天的天气如何")
                .tool(weatherClient::getBaseWeather)
                .env(vars)
                .run()
                .content();
        System.out.println("Content: " + content);
    }

    @Test
    public void testWeatherWithDoubao_getToolCallsResult() {
        final Map<String, Object> vars = new HashMap<>();
        vars.put("ai-role", "AI天气助手");
        vars.put("city", "苏州");

        Map<String, Object> content = Fai.volcengine().v3().model("doubao-1-5-pro-32k-250115")
                .chat()
                .stream()
                .system("你是一个{ai-role}")
                .user("{city}今天的天气如何")
                .tool(weatherClient::getBaseWeather)
                .env(vars)
                .run()
                .toolCallsResult(new TypeReference<Map<String, Object>>() {});
        System.out.println("Content: " + content);
    }

}
