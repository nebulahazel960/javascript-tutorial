package org.dromara.fai.test.structured;

import lombok.Data;
import org.dromara.fai.annotation.AIDesc;

import java.io.Serializable;

@Data
public class Plant implements Serializable {

    @AIDesc("植物名称")
    private String name;
    
    @AIDesc("植物类型")
    private String type;
    
    @AIDesc("植物简介")
    private String description;
    
    @AIDesc("是否可以食用，true: 可食用，false：不可食用")
    private Boolean eatable;
    
    @AIDesc("口感")
    private String taste;
}
