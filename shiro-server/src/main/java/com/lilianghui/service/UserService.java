package com.lilianghui.service;

import com.lilianghui.entity.User;
import com.lilianghui.framework.core.rocketmq.RocketService;
import com.lilianghui.framework.core.service.impl.AbstractBaseMapperService;
import com.lilianghui.mapper.UserMapper;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService extends AbstractBaseMapperService<User, UserMapper> implements RocketService<User> {

    public List<User> findAll() {
        return mapper.findAll();
    }

    @Override
    public void process(User user, MessageExt messageExt, ConsumeConcurrentlyContext context) {
        mapper.insert(user);
        System.out.println(user);
    }
}
