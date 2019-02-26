package com.lilianghui.application.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lilianghui.application.mapper.UserMapper;
import com.lilianghui.entity.User;
import com.lilianghui.service.HelloService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service(version = "1.0.0")
@org.springframework.stereotype.Service
public class HelloServiceImpl implements HelloService {

    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional
    public int save(User user) {
        return userMapper.insert(user);
    }
}
