package com.lilianghui.controller;

import com.lilianghui.entity.GatWayConfig;
import com.lilianghui.entity.User;
import com.lilianghui.utils.ProtobufUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;

@Controller
public class IndexController {

    private Logger logger= LoggerFactory.getLogger(IndexController.class);
//    @Resource
//    private RedisTemplate redisTemplate;
//    @Resource
//    private RedisTemplate protocbufRedisTemplate;

    @Resource
    private GatWayConfig gatWayConfig;

    @RequestMapping("/")
    public ModelAndView index(Model model){
       /* String key=DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss:S");
        redisTemplate.boundListOps("list").leftPush(key);
        byte[] val = ProtobufUtils.serialize(User.builder().account("account").name("lilianghui").build(), User.class);
        protocbufRedisTemplate.opsForValue().set(key, User.builder().account("account--").name("lilianghui--").age(784).build());
        User user = (User) protocbufRedisTemplate.opsForValue().get(key);
//        User user = ProtobufUtils.deSerialize((byte[]) redisTemplate.opsForValue().get(key), User.class);
        System.out.println(user);*/
        System.out.println(gatWayConfig);
        model.addAttribute("name","spring cloud"+gatWayConfig);
        return new ModelAndView("index");
    }
}
