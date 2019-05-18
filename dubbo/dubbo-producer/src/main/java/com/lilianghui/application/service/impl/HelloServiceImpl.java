package com.lilianghui.application.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.lilianghui.application.mapper.UserMapper;
import com.lilianghui.entity.User;
import com.lilianghui.service.HelloService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public List<User> selectByRowBounds(int offset, int limit) {
        return userMapper.selectByRowBounds(new User(),new RowBounds(offset,limit));
    }
}
