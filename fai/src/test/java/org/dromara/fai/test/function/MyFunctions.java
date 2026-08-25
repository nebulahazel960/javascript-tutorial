package org.dromara.fai.test.function;

import org.dromara.fai.annotation.AITool;
import org.dromara.fai.annotation.AIParam;

public class MyFunctions {
    
    @AITool(desc = "根据用户名和用户的心情生成一段鼓励的话语")
    public static String getEncourageMessage(
            @AIParam(desc = "用户名") String name,
            @AIParam(desc = "用户的心情，只有以下几个选项：开心，难过，压力大，疲倦") String mood) {
        String message = "";
        switch (mood) {
            case "开心":
                message = "请继续保持积极的心态，做得好！";
                break;
            case "难过":
                message = "记住，即使在最黑暗的日子里，也会有阳光等着你。";
                break;
            case "压力大":
                message = "你做得足够好，现在是时候休息一下了。";
                break;
            case "疲倦":
                message = "深呼吸，一切都会好起来。";
                break;
        }
        return "亲爱的" + name + "，" + message;
    }

}
