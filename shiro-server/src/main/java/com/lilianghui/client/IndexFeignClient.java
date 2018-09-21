package com.lilianghui.client;

import com.lilianghui.client.fallback.IndexFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "service",url = "https://www.sojson.com/open/api/weather"
        ,fallbackFactory = IndexFeignClientFallbackFactory.class)
public interface IndexFeignClient {

    @RequestMapping("json.shtml")
    Map<String, Object> weather(@RequestParam("city") String city);

}
