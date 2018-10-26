package com.lilianghui.client;


import com.lilianghui.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("shiro-server")
public interface ShiroFeignClient {


    @RequestMapping("/user/selectByPrimaryKey")
    User selectByPrimaryKey(@RequestBody User user);
}
