package org.dromara.fai.test.structured;

import lombok.Data;
import org.dromara.fai.annotation.AIDesc;

import java.util.List;

@Data
public class PlantSet {
    
    @AIDesc("植物列表")
    private List<Plant> plants;
    
}
