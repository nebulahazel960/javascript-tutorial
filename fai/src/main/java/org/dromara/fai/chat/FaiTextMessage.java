package org.dromara.fai.chat;

import org.dromara.fai.FaiMessage;
import org.dromara.fai.FaiRole;
import org.dromara.fai.content.FaiTextContent;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FaiTextMessage implements FaiMessage, Serializable {
    
    private final String role;
    
    private final FaiTextContent content;

    public static FaiTextMessage create(String role, FaiTextContent content) {
        return new FaiTextMessage(role, content);
    }

    public static FaiTextMessage create(FaiRole role, FaiTextContent content) {
        return new FaiTextMessage(role, content);
    }

    public static FaiTextMessage create(String role, String content) {
        return new FaiTextMessage(role, FaiTextContent.fromText(content));
    }

    public static FaiTextMessage create(FaiRole role, String content) {
        return new FaiTextMessage(role, FaiTextContent.fromText(content));
    }

    public static List<FaiTextMessage> listText(String role, List<String> content) {
        return content.stream().map(item -> create(role, item))
                .collect(Collectors.toList());
    }

    public static List<FaiTextMessage> listText(String role, String ...content) {
        return listText(role, Arrays.asList(content));
    }

    public static List<FaiTextMessage> listText(FaiRole role, List<String> content) {
        return content.stream().map(item -> create(role, item))
                .collect(Collectors.toList());
    }

    public static List<FaiTextMessage> list(FaiRole role, List<FaiTextContent> content) {
        return content.stream().map(item -> create(role, item))
                .collect(Collectors.toList());
    }


    public static List<FaiTextMessage> listText(FaiRole role, String ...content) {
        return listText(role, Arrays.asList(content));
    }

    public static List<FaiTextMessage> listText(FaiRole role, FaiTextContent ...content) {
        return list(role, Arrays.asList(content));
    }


    public static FaiTextMessage system(String content) {
        return FaiTextMessage.create(FaiRole.SYSTEM, content);
    }

    public static FaiTextMessage system(FaiTextContent content) {
        return FaiTextMessage.create(FaiRole.SYSTEM, content);
    }

    public static List<FaiTextMessage> system(List<String> content) {
        return FaiTextMessage.listText(FaiRole.SYSTEM, content);
    }

    public static List<FaiTextMessage> system(String ...content) {
        return FaiTextMessage.listText(FaiRole.SYSTEM, content);
    }

    public static List<FaiTextMessage> system(FaiTextContent ...content) {
        return FaiTextMessage.listText(FaiRole.SYSTEM, content);
    }

    public static FaiTextMessage user(String content) {
        return create(FaiRole.USER, content);
    }

    public static FaiTextMessage user(FaiTextContent content) {
        return create(FaiRole.USER, content);
    }

    public static List<FaiTextMessage> user(List<String> content) {
        return FaiTextMessage.listText(FaiRole.USER, content);
    }

    public static List<FaiTextMessage> user(String ...content) {
        return FaiTextMessage.listText(FaiRole.USER, content);
    }

    public static List<FaiTextMessage> user(FaiTextContent ...content) {
        return FaiTextMessage.listText(FaiRole.USER, content);
    }

    public static FaiTextMessage assistant(String content) {
        return create(FaiRole.ASSISTANT, content);
    }

    public static FaiTextMessage assistant(FaiTextContent content) {
        return create(FaiRole.ASSISTANT, content);
    }

    public static List<FaiTextMessage> assistant(List<String> content) {
        return FaiTextMessage.listText(FaiRole.ASSISTANT, content);
    }

    public static List<FaiTextMessage> assistant(String ...content) {
        return FaiTextMessage.listText(FaiRole.ASSISTANT, content);
    }

    public static List<FaiTextMessage> assistant(FaiTextContent ...content) {
        return FaiTextMessage.listText(FaiRole.ASSISTANT, content);
    }


    private FaiTextMessage(String role, FaiTextContent content) {
        this.role = role;
        this.content = content;
    }


    private FaiTextMessage(FaiRole role, FaiTextContent content) {
        this.role = role.getName();
        this.content = content;
    }

    @Override
    public String role() {
        return role;
    }


    @Override
    public String content() {
        return content.text();
    }

}
