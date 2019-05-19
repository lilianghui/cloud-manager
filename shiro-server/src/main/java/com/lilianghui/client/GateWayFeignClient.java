package com.lilianghui.client;

import com.lilianghui.client.fallback.IndexFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient("gatway-server")
public interface GateWayFeignClient {

    @RequestMapping(value = "rpc" ,method = RequestMethod.POST)
    public Map<String, Object> rpc();

}
