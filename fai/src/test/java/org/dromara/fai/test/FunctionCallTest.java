package org.dromara.fai.test;

import org.dromara.fai.Fai;
import org.dromara.fai.test.function.MyCounter;
import org.dromara.fai.test.function.MyFunctions;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class FunctionCallTest {
    
    @Test
    public void testFunctionCallingWithDeepSeek_encourageMessage_lambda() {
        StringBuilder builder = new StringBuilder();
        Fai.deepSeek().v3()
                .chat()
                .logEnabled()
                .stream()
                .tool(MyFunctions::getEncourageMessage)
                .system("你是一个AI鼓励师")
                .user("小红的情绪有点低落，请鼓励她一下")
                .onMessage((runner, res) -> {
                    System.out.print(res.content());
                    builder.append(res.content());
                })
                .run();
        assertThat(builder.toString()).contains("亲爱的小红，记住，即使在最黑暗的日子里，也会有阳光等着你。");
    }

    @Test
    public void testFunctionCallingWithOpenAI_encourageMessage_lambda() {
        StringBuilder builder = new StringBuilder();
        Fai.openAI().v1().model("gpt-4.1")
                .chat()
                .logEnabled()
                .stream()
                .tool(MyFunctions::getEncourageMessage)
                .system("你是一个AI鼓励师")
                .user("小红的情绪有点低落，请鼓励她一下")
                .onMessage((runner, res) -> {
                    System.out.print(res.content());
                    builder.append(res.content());
                })
                .run();
        assertThat(builder.toString()).contains("亲爱的小红，记住，即使在最黑暗的日子里，也会有阳光等着你。");
    }



    @Test
    public void testFunctionCallingWithDeepSeek_incrementCounter() {
        MyCounter counter = new MyCounter();
        Fai.deepSeek().v3()
                .chat()
                .stream()
                .tool(counter)
                .system("你是一个AI计数器")
                .user("让计数器加三")
                .onMessage((runner, res) -> {
                    System.out.print(res.content());
                })
                .run();
        assertThat(counter.getCounter()).isEqualTo(3);
    }


    @Test
    public void testFunctionCallingWithOpenAI_incrementCounter() {
        MyCounter counter = new MyCounter();
        Fai.deepSeek().v3()
                .chat()
                .logEnabled()
                .stream()
                .tool(counter)
                .system("你是一个AI计数器")
                .user("让计数器加三")
                .onMessage((runner, res) -> {
                    System.out.print(res.content());
                })
                .run();
        assertThat(counter.getCounter()).isEqualTo(3);
    }



    @Test
    public void testFunctionCallingWithDoubao_encourageMessage() {
        StringBuilder builder = new StringBuilder();
        Fai.volcengine().v3().model("doubao-1-5-pro-32k-250115")
                .chat()
                .stream()
                .tool(MyFunctions.class)
                .system("你是一个AI鼓励师")
                .user("小红的情绪有点低落，请鼓励她一下")
                .onMessage((runner, res) -> {
                    System.out.print(res.content());
                    builder.append(res.content());
                })
                .run();
        assertThat(builder.toString()).contains("亲爱的小红，记住，即使在最黑暗的日子里，也会有阳光等着你。");
    }

    @Test
    public void testFunctionCallingWithDoubao_incrementCounter() {
        MyCounter counter = new MyCounter();
        AtomicReference<String> funcName = new AtomicReference<>();
        Fai.volcengine().v3().model("doubao-1-5-pro-32k-250115")
                .chat()
                .stream()
                .tool(counter)
                .system("你是一个AI计数器")
                .user("让计数器累加三")
                .onToolCalls((runner, handler) -> {
                    System.out.println("\n调用了函数 " + handler.tool().name());
                    funcName.set(handler.tool().name());
                    return handler.call();
                })
                .onMessage((runner, res) -> {
                    System.out.print(res.content());
                })
                .run();
        assertThat(counter.getCounter()).isEqualTo(3);
        assertThat(funcName.get()).isEqualTo("increment");
    }


    @Test
    public void testFunctionCallingWithDoubao_incrementCounter_lambda() {
        MyCounter counter = new MyCounter();
        StringBuilder builder = new StringBuilder();
        Fai.volcengine().v3().model("doubao-1-5-pro-32k-250115")
                .chat()
                .stream()
                .tool(counter::increment, int.class)
                .system("你是一个AI计数器")
                .user("让计数器累加{num}")
                .env("num", "三")
                .onToolCalls((runner, handler) -> {
                    Integer ret = handler.call(Integer.class, handler.arguments());
                    System.out.println(ret);
                    return "count: " + ret;
                })
                .onMessage((runner, res) -> {
                    if (res.toolCallsResult() != null) {
                        System.out.println(res.toolCallsResult());
                        builder.append(res.toolCallsResult());
                    }
                })
                .run();
        assertThat(counter.getCounter()).isEqualTo(3);
        assertThat(builder.toString()).isEqualTo("count: 3");
    }

}
