package com.lilianghui.application.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.lilianghui.application.service.TransactionalService;
import com.lilianghui.entity.Item;
import com.lilianghui.entity.User;
import com.lilianghui.service.HelloService;
import com.lilianghui.service.ItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionalImpl implements TransactionalService {

    @Reference(version = "1.0.0")
    private HelloService helloService;

    @Reference(version = "1.0.0")
    private ItemService lcnService;


    @Override
    @LcnTransaction
    @Transactional
    public int save(User user, Item item) {
        helloService.save(user);
        lcnService.save(item);
        return 0;
    }

}
