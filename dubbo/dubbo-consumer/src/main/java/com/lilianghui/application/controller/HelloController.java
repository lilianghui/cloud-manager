package com.lilianghui.application.controller;

import com.lilianghui.application.service.TransactionalService;
import com.lilianghui.entity.Item;
import com.lilianghui.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class HelloController {

    @Resource
    private TransactionalService transactionalService;

    @GetMapping("hello/{name}")
    public int sayHello(@PathVariable String name, Integer id) {
        Date date = new Date();
        User user = new User();
        user.setName(name);
        user.setIndate(date);
        user.setId(System.currentTimeMillis());

        Item item = new Item();
        item.setIndate(date);
        item.setValue(1);
        return transactionalService.save(user, item);
    }

    @GetMapping("selectByPrimaryKey/{id}/{itemId}")
    public Object selectByPrimaryKey(@PathVariable Long id,@PathVariable Long itemId) {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("success", false);
            result.put("data", transactionalService.selectByPrimaryKey(id));
            result.put("result", transactionalService.selectItemByPrimaryKey(itemId));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }


}