package com.lilianghui.client;


import com.lilianghui.entity.Contract;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("elasticsearch-server")
public interface ElasticFeignClient {


    @RequestMapping("/elastic/contract/initContract")
    boolean initContract(@RequestBody List<Contract> contracts);
}
