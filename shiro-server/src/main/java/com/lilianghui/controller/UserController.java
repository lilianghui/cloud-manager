package com.lilianghui.controller;

import com.lilianghui.client.IndexFeignClient;
import com.lilianghui.entity.User;
import com.lilianghui.mapper.UserMapper;
import com.lilianghui.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private IndexFeignClient indexFeignClient;

    @RequestMapping("list")
    public List<User> list() {
        System.out.println(indexFeignClient.weather("北京"));
        return userService.selectAll();
    }

    @RequestMapping("view")
    public ModelAndView index(Model model) {
        model.addAttribute("list", userService.findAll());
        return new ModelAndView("index");
    }
}
