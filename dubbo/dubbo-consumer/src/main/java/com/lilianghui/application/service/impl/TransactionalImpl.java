package com.lilianghui.application.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fescar.spring.annotation.GlobalTransactional;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.lilianghui.application.service.TransactionalService;
import com.lilianghui.entity.Item;
import com.lilianghui.entity.User;
import com.lilianghui.service.HelloService;
import com.lilianghui.service.ItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionalImpl implements TransactionalService {

    @Reference(version = "1.0.0")
    private HelloService helloService;

    @Reference(version = "1.0.0")
    private ItemService lcnService;


    @Override
    @LcnTransaction
    @Transactional
//    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-gts-fescar-example")
    public int save(User user, Item item) {
        lcnService.save(item);
        helloService.save(user);
        return 0;
    }

    @Override
    public User selectByPrimaryKey(Long id) {
        return helloService.selectByPrimaryKey(id);
    }

    @Override
    public Item selectItemByPrimaryKey(Long itemId) {
        return lcnService.selectByPrimaryKey(itemId);
    }

    @Override
    public List<User> selectByRowBounds(int offset, int limit) {
        return helloService.selectByRowBounds(offset,limit);
    }

}
