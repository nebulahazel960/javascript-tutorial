package org.dromara.fai.test;

import com.dtflys.forest.utils.TypeReference;
import org.dromara.fai.Fai;
import org.dromara.fai.test.structured.Plant;
import org.dromara.fai.test.structured.PlantSet;
import org.dromara.fai.wrapper.FaiType;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class StructuredTest {
    
    @Test
    public void testJSON() {
        FaiType typeWrapper = FaiType.of(Plant.class);
        System.out.println(typeWrapper.toJSONPrompt());
    }
    
    @Test
    public void testJSONResponseWithDeepSeek() {
        final PlantSet plantSet = Fai.deepSeek().v3()
                .chat()
                .stream()
                .system("你是一个植物学家，并可以回答植物学相关问题。")
                .user("常见的十字花科植物有哪些？请列出至少20个,")
                .user("其中可食用的列出10个，不可食用的也列出10个。")
                .onMessage((runner, res) -> {
                    System.out.print(res.content());
                })
                .get(PlantSet.class);
        System.out.println("\nContent: " + plantSet);
        
        assertThat(plantSet).isNotNull();
        assertThat(plantSet.getPlants()).isNotNull();
        assertThat(plantSet.getPlants().size()).isEqualTo(20);
        assertThat(plantSet.getPlants().stream()
                .filter(plant -> plant.getEatable())
                .collect(Collectors.toList())
                .size())
                .isEqualTo(10);
    }

    @Test
    public void testJSONResponseWithDeepSeek2() {
        final PlantSet plantSet = Fai.deepSeek().v3()
                .chat()
                .stream()
                .system("你是一个植物学家，并可以回答植物学相关问题。")
                .user("常见的十字花科植物有哪些？请列出至少20个,")
                .user("其中可食用的列出10个，不可食用的也列出10个。")
                .onMessage((runner, res) -> {
                    System.out.print(res.content());
                })
                .get(PlantSet.class);
        System.out.println("\nContent: " + plantSet);

        assertThat(plantSet).isNotNull();
        assertThat(plantSet.getPlants()).isNotNull();
        assertThat(plantSet.getPlants().size()).isEqualTo(20);
        assertThat(plantSet.getPlants().stream()
                .filter(plant -> plant.getEatable())
                .collect(Collectors.toList())
                .size())
                .isEqualTo(10);
    }


    @Test
    public void testArrayJSONResponseWithDeepSeek() {
        final List<Plant> plantList = Fai.deepSeek().v3()
                .chat()
                .stream()
                .system("你是一个植物学家，并可以回答植物学相关问题。")
                .user("常见的十字花科植物有哪些？请列出至少20个,")
                .user("其中可食用的列出10个，不可食用的也列出10个。")
                .onMessage((runner, res) -> {
                    System.out.print(res.content());
                })
                .get(new TypeReference<List<Plant>>() {});
        System.out.println("\nContent: " + plantList);

        assertThat(plantList).isNotNull();
        assertThat(plantList.size()).isEqualTo(20);
        assertThat(plantList.stream()
                .filter(plant -> plant.getEatable())
                .collect(Collectors.toList())
                .size())
                .isEqualTo(10);
    }


    @Test
    public void testJSONResponseWithDeepSeek_withoutStream() {
        final int total = 4, eatable = 2, uneatable = 2;

        final PlantSet plantSet = Fai.deepSeek().v3()
                .chat()
                .logEnabled(true)
                .responseJson(PlantSet.class)
                .system("你是一个植物学家，并可以回答植物学相关问题。")
                .user("常见的十字花科植物有哪些？请列出至少{total}个,")
                .user("其中可食用的列出{eatable}个，不可食用的也列出{uneatable}个。")
                .assistant("{\n")
                .onMessage((runner, res) -> {
                    System.out.print(res.content());
                })
                .env("total", total)
                .env("eatable", eatable)
                .env("uneatable", uneatable)
                .run()
                .get(PlantSet.class);
        System.out.println("\nContent: " + plantSet);

        assertThat(plantSet).isNotNull();
        assertThat(plantSet.getPlants()).isNotNull();
        assertThat(plantSet.getPlants().size()).isEqualTo(total);
        assertThat((int) plantSet.getPlants().stream()
                .filter(Plant::getEatable)
                .count())
                .isEqualTo(eatable);
    }


    @Test
    public void testJSONResponseWithDoubao() {
        Fai.volcengine().v3().model("doubao-1-5-pro-32k-250115")
                .chat()
                .stream()
                .responseJson(PlantSet.class)
                .system("你是一个AI助手，并可以回答植物学相关问题。")
                .user("常见的十字花科植物有哪些？请列出至少10个")
                .user("其中可食用的列出10个，不可食用的也列出10个。")
                .assistant("{\n")
                .onMessageStart(runner -> {
                    System.out.print("{\n");
                })
                .onMessage((runner, res) -> {
                    System.out.print(res.content());
                })
                .run();
    }

}
