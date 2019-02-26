package com.lilianghui.application.controller;

import com.lilianghui.application.service.TransactionalService;
import com.lilianghui.entity.Item;
import com.lilianghui.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
public class HelloController {

    @Resource
    private TransactionalService transactionalService;

    @GetMapping("hello/{name}")
    public int sayHello(@PathVariable String name) {
        Date date = new Date();
        User user = new User();
        user.setName(name);
        user.setIndate(date);

        Item item = new Item();
        item.setIndate(date);
        item.setValue(1);
        return transactionalService.save(user, item);
    }


}