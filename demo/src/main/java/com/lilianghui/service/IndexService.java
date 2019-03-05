package com.lilianghui.service;

import com.lilianghui.entity.Item;
import com.lilianghui.entity.User;
import com.lilianghui.mapper.db01.UserMapper;
import com.lilianghui.mapper.db02.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.annotation.Resource;

@Service
@Transactional
public class IndexService {

//    @Resource
    @Autowired
    private UserMapper userMapper;
//    @Resource
    @Autowired
    private ItemMapper itemMapper;
    @Resource
    private JtaTransactionManager transactionManager;

    public void saveData(User user, Item item){
        userMapper.insert(user);
        itemMapper.insert(item);
    }
}
