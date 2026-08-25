package org.dromara.fai;

import org.dromara.fai.config.FaiHttpConfiguration;
import org.dromara.fai.config.builtin.openai.FaiOpenAIConfiguration;
import org.dromara.fai.config.builtin.volcengine.FaiVolcengineConfiguration;
import org.dromara.fai.chat.FaiChat;
import org.dromara.fai.chat.FaiTextMessage;
import org.dromara.fai.config.FaiConfiguration;
import org.dromara.fai.config.FaiGlobalConfiguration;
import org.dromara.fai.config.builtin.deepseek.FaiDeepSeekConfiguration;
import org.dromara.fai.runner.FaiChatRunner;
import org.dromara.fai.runner.FaiRunner;
import org.dromara.fai.utils.functions.FaiNoParams;
import org.dromara.fai.wrapper.FaiObject;
import org.dromara.fai.wrapper.FaiType;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Fai 调用接口及相关工具方法的入口
 *
 * @since 1.0.beta
 */
public class Fai {

    /**
     * 获取或创建 Fai 全局配置对象
     *
     * @return Fai 全局配置对象
     * @since 1.0.beta
     */
    public static FaiGlobalConfiguration config() {
        return FaiConfiguration.globalConfig();
    }

    /**
     * 通过配置 ID 获取或创建 Fai 配置对象
     *
     * @param id 配置 ID
     * @return Fai 配置对象
     */
    public static FaiConfiguration config(String id) {
        return FaiConfiguration.config(id);
    }

    public static FaiHttpConfiguration http() {
        return config().http();
    }
    
    public static FaiOpenAIConfiguration openAI() {
        return FaiConfiguration.globalConfig().getOpenAI();
    }
    
    public static FaiDeepSeekConfiguration deepSeek() {
        return FaiConfiguration.globalConfig().getDeepSeek();
    }

    public static FaiVolcengineConfiguration volcengine() {
        return FaiConfiguration.globalConfig().getVolcengine();
    }

    public static FaiClient client(String apiKey, String baseUrl) {
        return config().client(apiKey, baseUrl);
    }

    public static FaiClient client() {
        return config().client();
    }
    
    public static FaiModel modelOf(String id) {
        return config(id).model();
    }

    public static FaiModel model(String apiKey, String baseUrl, String model) {
        return config().model(apiKey, baseUrl, model);
    }

    public static FaiModel globalModel() {
        return config().model();
    }

    public static FaiModel model(String model) {
        return config().model(model);
    }

    public static FaiModel apiKey(String apiKey) {
        return config().apiKey(apiKey);
    }

    public static FaiModel baseUrl(String baseUrl) {
        return config().baseUrl(baseUrl);
    }
    
    public static FaiTextMessage message(FaiRole role, String content) {
        return FaiTextMessage.create(role, content);
    }
    
    public static FaiTextMessage message(String role, String content) {
        return FaiTextMessage.create(role, content);
    }
    
    public static FaiTextMessage system(String content) {
        return FaiTextMessage.system(content);
    }

    public static List<FaiTextMessage> system(List<String> content) {
        return FaiTextMessage.system(content);
    }

    public static List<FaiTextMessage> system(String ...content) {
        return FaiTextMessage.system(content);
    }

    public static FaiTextMessage user(String content) {
        return FaiTextMessage.user(content);
    }

    public static List<FaiTextMessage> user(List<String> content) {
        return FaiTextMessage.user(content);
    }

    public static List<FaiTextMessage> user(String ...content) {
        return FaiTextMessage.user(content);
    }


    public static FaiTextMessage assistant(String content) {
        return FaiTextMessage.assistant(content);
    }


    public static FaiChat chat() {
        return config().chat();
    }

    public static FaiChat chat(String ...content) {
        return config().model().chat(content);
    }

    public static FaiChat chat(String content) {
        return config().model().chat(content);
    }

    public static FaiChat chat(List<String> messages) {
        return config().model().chat(messages);
    }

    public static <C extends FaiChat> C chat(Class<C> clazz) {
        return config().chat(clazz);
    }

    public static <C extends FaiChat> C chat(String content, Class<C> clazz) {
        return config().model().chat(content, clazz);
    }

    public static <C extends FaiChat> C chat(List<String> messages, Class<C> clazz) {
        return config().model().chat(messages, clazz);
    }

    public static <C extends FaiChat> C chat(FaiRole role, String content, Class<C> clazz) {
        return config().model().chat(role, content, clazz);
    }

    public static <C extends FaiChat> C chat(FaiRole role, List<String> messages, Class<C> clazz) {
        return config().model().chat(role, messages, clazz);
    }


    public static FaiChat chat(FaiConfiguration config) {
        return config.chat();
    }

    public static FaiChat chat(FaiModel model) {
        return new FaiChat(model);
    }
    
    public static FaiChatRunner runner(FaiChat chatCompletions) {
        return FaiRunner.chatRunner(chatCompletions);
    }
    
    public static FaiType type(Class<?> clazz) {
        return FaiType.of(clazz);
    }

    public static FaiObject object(String name, Object object) {
        return FaiObject.of(name, object);
    }


    public static FaiObject object(Object object) {
        return FaiObject.of(object);
    }

    public static <T> T caller(T object) {
        return object;
    }

    public static <T> T anyInput(Class<T> clazz) {
        return (T) null;
    }

    public static <T> T[] arrayInput(Class<T> clazz) {
        return (T[]) Array.newInstance(clazz, 0);
    }

    public static byte byteInput() {
        return 0;
    }

    public static byte[] byteArrayInput() {
        return new byte[0];
    }

    public static char charInput() {
        return 0;
    }

    public static char[] charArrayInput() {
        return new char[0];
    }

    public static short shortInput() {
        return 0;
    }

    public static short[] shortArrayInput() {
        return new short[0];
    }


    public static int intInput() {
        return 0;
    }

    public static int[] anyIntArray() {
        return new int[0];
    }

    public static long anyLong() {
        return 0;
    }

    public static long[] longArrayInput() {
        return new long[0];
    }

    public static float floatInput() {
        return 0.0F;
    }

    public static float[] floatArrayInput() {
        return new float[0];
    }


    public static double doubleInput() {
        return 0.0D;
    }

    public static double[] doubleArrayInput() {
        return new double[0];
    }

    public static String stringInput() {
        return anyInput(String.class);
    }

    public static String[] stringArrayInput() {
        return arrayInput(String.class);
    }

    public static FaiNoParams noParams() {
        return null;
    }


}
