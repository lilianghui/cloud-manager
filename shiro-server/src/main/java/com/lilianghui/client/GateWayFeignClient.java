package com.lilianghui.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient("fastdfs-server")
public interface GateWayFeignClient {

    @RequestMapping(value = "rpc", method = RequestMethod.POST)
    Map<String, Object> rpc();

}
