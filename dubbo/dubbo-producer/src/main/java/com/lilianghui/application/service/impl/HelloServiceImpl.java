package com.lilianghui.application.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.lilianghui.application.mapper.UserMapper;
import com.lilianghui.entity.User;
import com.lilianghui.service.HelloService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service(version = "1.0.0",interfaceClass = HelloService.class)
@org.springframework.stereotype.Service
public class HelloServiceImpl implements HelloService {

    @Resource
    private UserMapper userMapper;

    @Override
    @LcnTransaction //分布式事务注解
    @Transactional //本地事务注解
    public int save(User user) {
        return userMapper.insert(user);
    }

    @Override
    @Transactional
    public User selectByPrimaryKey(Object id) {
        userMapper.deleteByPrimaryKey("1");
        return userMapper.selectByPrimaryKey(id);
    }
}
