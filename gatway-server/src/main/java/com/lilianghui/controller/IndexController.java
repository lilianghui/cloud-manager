package com.lilianghui.controller;

import com.lilianghui.entity.GatWayConfig;
import com.lilianghui.framework.core.lock.redisson.RedissLockUtil;
import com.lilianghui.service.ContractService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Resource
    private ContractService contractService;
//    @Resource
//    private RedisTemplate redisTemplate;
//    @Resource
//    private RedisTemplate protocbufRedisTemplate;

    @Resource
    private GatWayConfig gatWayConfig;

    @RequestMapping("/")
    public ModelAndView index(Model model) {
       /* String key=DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss:S");
        redisTemplate.boundListOps("list").leftPush(key);
        byte[] val = ProtobufUtils.serialize(User.builder().account("account").name("lilianghui").build(), User.class);
        protocbufRedisTemplate.opsForValue().set(key, User.builder().account("account--").name("lilianghui--").age(784).build());
        User user = (User) protocbufRedisTemplate.opsForValue().get(key);
//        User user = ProtobufUtils.deSerialize((byte[]) redisTemplate.opsForValue().get(key), User.class);
        System.out.println(user);*/
        System.out.println(gatWayConfig);
        model.addAttribute("name", "spring cloud" + gatWayConfig);
        return new ModelAndView("index");
    }

    @RequestMapping("transactional")
    public ModelAndView transactional(Model model) {
        try {
            contractService.transactional();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("name", "transactional");
        return new ModelAndView("index");
    }

    private final String _lock = "_lock";

    public String getCurrentDate() {
        return DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }


    @RequestMapping("lock")
    public ModelAndView lock(Model model) {
        String name = "";
        new Thread(new Runnable() {

            @Override
            public void run() {

                RedissLockUtil.lock(_lock, TimeUnit.MINUTES, 50);

                System.out.println(getCurrentDate() + " " + name + " begin...");
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println(getCurrentDate() + " " + name + " " + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(getCurrentDate() + " " + name + " end...");

                RedissLockUtil.unlock(_lock);
            }
        }).start();
        model.addAttribute("name", "lock");
        return new ModelAndView("index");
    }
}
