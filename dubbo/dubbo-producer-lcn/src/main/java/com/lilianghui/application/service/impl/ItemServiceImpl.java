package com.lilianghui.application.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.lilianghui.application.mapper.ItemMapper;
import com.lilianghui.entity.Item;
import com.lilianghui.service.ItemService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service(version = "1.0.0", interfaceClass = ItemService.class)
@org.springframework.stereotype.Service
public class ItemServiceImpl implements ItemService {

    @Resource
    private ItemMapper itemMapper;


    @Override
    @LcnTransaction //分布式事务注解
    @Transactional //本地事务注解
    public int save(Item item) {
        int count = itemMapper.insert(item);
        item = itemMapper.selectByPrimaryKey(item.getId());
        System.out.println(item);
        return count;
    }

    @Override
    public Item selectByPrimaryKey(Long itemId) {
        return itemMapper.selectByPrimaryKey(itemId);
    }
}
