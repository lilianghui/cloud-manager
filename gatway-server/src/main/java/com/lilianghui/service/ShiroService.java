package com.lilianghui.service;

import com.lilianghui.client.ShiroFeignClient;
import com.lilianghui.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ShiroService {

    @Resource
    private ShiroFeignClient shiroFeignClient;

    public User selectByPrimaryKey(User user) {
        return shiroFeignClient.selectByPrimaryKey(user);
    }
}
