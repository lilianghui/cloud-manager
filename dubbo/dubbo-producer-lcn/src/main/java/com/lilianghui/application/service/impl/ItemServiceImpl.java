package com.lilianghui.application.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lilianghui.application.mapper.ItemMapper;
import com.lilianghui.entity.Item;
import com.lilianghui.service.ItemService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service(version = "1.0.0")
@org.springframework.stereotype.Service
public class ItemServiceImpl implements ItemService {

    @Resource
    private ItemMapper itemMapper;


    @Override
    @Transactional
    public int save(Item item) {
        return itemMapper.insert(item);
    }
}
