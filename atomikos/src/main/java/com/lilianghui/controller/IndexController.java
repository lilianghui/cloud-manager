package com.lilianghui.controller;

import com.lilianghui.entity.Item;
import com.lilianghui.entity.User;
import com.lilianghui.service.IndexService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;

@RestController
public class IndexController {

    @Resource
    private IndexService indexService;

    @RequestMapping("saveData")
    public String saveData() {
        Item item = new Item();
        int id = new Random().nextInt();
        item.setId(id);
        item.setIndate(new Date());
        item.setValue(id);
        User user = new User();
        user.setId(id);
        user.setIndate(new Date());
        user.setName("name-" + id);
        indexService.saveData(user, item);
        return "success";
    }
}
