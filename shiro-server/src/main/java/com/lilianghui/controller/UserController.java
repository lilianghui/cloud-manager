package com.lilianghui.controller;

import com.lilianghui.client.GateWayFeignClient;
import com.lilianghui.client.IndexFeignClient;
import com.lilianghui.entity.User;
import com.lilianghui.mapper.UserMapper;
import com.lilianghui.service.UserService;
import com.lilianghui.spring.starter.Interceptor.RequestContextHolder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private IndexFeignClient indexFeignClient;
    @Resource
    private GateWayFeignClient gateWayFeignClient;

    @RequestMapping("list")
    public List<User> list(String name) {
        Example example = new Example(User.class);
        if (StringUtils.isNotBlank(name)) {
            example.createCriteria().andLike("customer", "%" + name + "%");
        }
        return userService.selectByExample(example);
    }

    @RequestMapping(value = "selectByPrimaryKey" ,method = RequestMethod.POST)
    public User selectByPrimaryKey(@RequestBody User user) {
        return userService.selectByPrimaryKey(user);
    }

    @RequestMapping("view")
    public ModelAndView index(Model model) {
        System.out.println(indexFeignClient.weather("北京"));
        model.addAttribute("list", userService.findAll());
        return new ModelAndView("index");
    }
}
