package com.lilianghui.service;

import com.lilianghui.entity.Item;
import com.lilianghui.entity.User;
import com.lilianghui.mapper.db01.UserMapper;
import com.lilianghui.mapper.db02.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class IndexService {

    @Resource
    private ApplicationContext applicationContext;
    @Resource
//    @Autowired(required = false)
    private UserMapper userMapper;
    @Resource
//    @Autowired(required = false)
    private ItemMapper itemMapper;

    public void saveData(User user, Item item){
        UserMapper u = applicationContext.getBean(UserMapper.class);
        userMapper.insert(user);
        itemMapper.insert(item);
    }
}
