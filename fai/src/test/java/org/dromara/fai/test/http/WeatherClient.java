package org.dromara.fai.test.http;

import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Query;
import org.dromara.fai.annotation.AIParam;
import org.dromara.fai.annotation.AITool;

import java.util.Map;

public interface WeatherClient {
    
    @AITool(desc = "根据城市编号查询天气")
    @Get("https://restapi.amap.com/v3/weather/weatherInfo?extensions=base&output=JSON&key=785484bfc6b1fba3c1bb9e67c19b5458")
    Map<String, Object> getBaseWeather(
            @AIParam(desc = "城市编号，以下是各个城市对应的编号，北京: 110000、上海: 310000、杭州: 330100、苏州: 320500")
            @Query("city") String city
    );

}
