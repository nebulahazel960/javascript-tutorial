package org.dromara.fai.test;

import org.dromara.fai.Fai;
import org.dromara.fai.content.FaiTextContent;
import org.junit.Test;

import java.util.Scanner;

public class ChatTest {
    
    @Test
    public void testAnswerLoopOfDeepSeek() {
        Fai.chat()
                .stream()
                .logEnabled()
                .onBeforeRequest(runner -> {
                    System.out.println("\n\n我：");
                    runner.chat().user(new Scanner(System.in).nextLine());
                })
                .onMessageStart(runner -> System.out.println("\nAI机器人:"))
                .onMessage((runner, res) -> System.out.print(res.content()))
                .onMessageStop((runner, res) -> runner.chat().prompt(res.deltas()))
                .runLoop();
    }

    @Test
    public void testAnswerLoopOfGPT() {
        Fai.openAI().v1().model("gpt-4.1")
                .chat()
                .stream()
                .logDisabled()
                .onBeforeRequest(runner -> {
                    System.out.println("\n\n我：");
                    runner.chat().user(new Scanner(System.in).nextLine());
                })
                .onMessageStart(runner -> System.out.println("\nAI机器人:"))
                .onMessage((runner, res) -> System.out.print(res.content()))
                .onMessageStop((runner, res) -> runner.chat().prompt(res.deltas()))
                .runLoop();
    }


    @Test
    public void testAnswerLoopOfDoubao() {
        Fai.volcengine().v3().model("doubao-1-5-pro-32k-250115")
                .chat()
                .stream()
                .logEnabled(false)
                .onBeforeRequest(runner -> {
                    System.out.println("\n\n我：");
                    runner.chat().user(new Scanner(System.in).nextLine());
                })
                .onMessageStart(runner -> System.out.println("\nAI机器人:"))
                .onMessage((runner, res) -> System.out.print(res.content()))
                .onMessageStop((runner, res) -> runner.chat().prompt(res.deltas()))
                .runLoop();
    }


    @Test
    public void testChatOfDoubao() {
        Fai.volcengine().v3().model("doubao-1-5-pro-32k-250115")
                .chat()
                .logEnabled()
                .stream()
                .system("你是一个AI助手，你的名字叫\"智能体Fai\"")
                .user("常见的十字花科植物有哪些？")
                .onMessage((runner, res) -> {
                    System.out.print(res.choices().get(0).delta().content());
                })
                .run();
    }


    @Test
    public void testChatOfGPT() {
        Fai.openAI().v1().model("gpt-4.1")
                .chat()
                .stream()
                .system("你是一个AI助手，你的名字叫\"智能体Fai\"")
                .user("常见的十字花科植物有哪些？")
                .onMessage((runner, res) -> {
                    System.out.print(res.choices().get(0).delta().content());
                })
                .run();
    }


    @Test
    public void testChatOfGiteeAIDeepSeekR1() {
        Fai.modelOf("giteeAI")
                .stream()
                .system("你是一个AI助手，你的名字叫\"智能体Fai\"")
                .user("常见的十字花科植物有哪些？")
                .onMessage((runner, res) -> {
                    System.out.print(res.allContent());
                })
                .run();
    }
    
    @Test
    public void testChatOfConfigPrompts() {
        String text = Fai.chat()
                .promptId("hello-world")
                .promptId("services-description")
                .text();
        System.out.println(text);
    }

    @Test
    public void testChatOfConfigTemplatePrompts() {
        Fai.deepSeek().v3().chat()
                .stream()
                .promptId("template-message")
                .env("name", "Fai智能体")
                .onMessage((runner, res) -> {
                    System.out.print(res.choices().get(0).delta().content());
                })
                .run();
    }

    @Test
    public void testChatOfConfigFilePrompts() {
        Fai.deepSeek().v3().chat()
                .stream()
                .user(FaiTextContent.fromFile("prompt.txt"))
                .env("lang", "Java")
                .env("name", "公子骏")
                .onMessage((runner, res) -> {
                    System.out.print(res.content());
                })
                .run();
    }


}
