package com.lilianghui.client;


import com.lilianghui.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("shiro-server")
public interface ShiroFeignClient {


    @RequestMapping(value = "/user/selectByPrimaryKey" ,method = RequestMethod.POST)
    User selectByPrimaryKey(@RequestBody(required = false) User user);
}
