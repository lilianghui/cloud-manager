package com.lilianghui.controller;

import com.hazelcast.client.AuthenticationException;
import com.lilianghui.entity.CellAttr;
import com.lilianghui.entity.GatWayConfig;
import com.lilianghui.entity.User;
import com.lilianghui.entity.MxCellEx;
import com.lilianghui.interfaces.HelloService;
import com.lilianghui.service.ContractService;
import com.lilianghui.service.ShiroService;
import com.lilianghui.shiro.spring.starter.core.IncorrectCaptchaException;
import com.lilianghui.spring.starter.utils.RedissLockUtils;
import com.mxgraph.io.*;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Document;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
public class IndexController {

    @Resource
    private ContractService contractService;
    @Resource
    private ShiroService shiroService;
    @Resource
    private HelloService helloService;
//    @Resource
//    private RedisTemplate redisTemplate;
//    @Resource
//    private RedisTemplate protocbufRedisTemplate;

    @Resource
    private GatWayConfig gatWayConfig;

    @Value("${extra.user-name}")
    private String userName;


    public IndexController() {

        mxCodecRegistry.register(new mxCellCodec(new MxCellEx(), null, new String[]{"parent", "source", "target"}, null) {
            @Override
            public String getName() {
                return "mxCell";
            }
        });
        mxCodecRegistry.register(new mxObjectCodec(new CellAttr()) {
            @Override
            public String getName() {
                return "Object";
            }
        });
    }

    @RequestMapping("/")
    public ModelAndView index(Model model) {
       /* String key=DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss:S");
        redisTemplate.boundListOps("list").leftPush(key);
        byte[] val = ProtobufUtils.serialize(User.builder().account("account").name("lilianghui").build(), User.class);
        protocbufRedisTemplate.opsForValue().set(key, User.builder().account("account--").name("lilianghui--").age(784).build());
        User user = (User) protocbufRedisTemplate.opsForValue().get(key);
//        User user = ProtobufUtils.deSerialize((byte[]) redisTemplate.opsForValue().get(key), User.class);
        System.out.println(user);*/
//        System.out.println(gatWayConfig);
//        model.addAttribute("name", "spring cloud" + gatWayConfig);
//        Contract contract = new Contract();
//        List<Contract> list = contractService.selectContract(contract);
//        shiroService.selectByPrimaryKey(new User());
        model.addAttribute("action", "/login");
        return new ModelAndView("index");
    }

    @RequestMapping("mxgraph")
    public ModelAndView mxgraph(Model model) {
        return new ModelAndView("MXGraph");
    }

    @ResponseBody
    @RequestMapping("grpc")
    public Map<String, Object> grpc(String name) {
        Map<String, Object> result = new HashMap<>();
        try {
            shiroService.sendMessage(name);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }


    @ResponseBody
    @RequestMapping("rpc")
    public Map<String, Object> rpc() {
        Map<String, Object> result = new HashMap<>();
        try {
            for (int i = 0; i < 100; i++) {
                long start = System.currentTimeMillis();
                System.err.println("-" + i + "-----rpc--" + helloService.hello("aaaaaaaaaaaaaaa") + "------" + new Date(System.currentTimeMillis() - start).toLocaleString());
            }
            for (int i = 0; i < 100; i++) {
                long start = System.currentTimeMillis();
                User user = new User();
                user.setId("1");
                user = shiroService.selectByPrimaryKey(user);
                System.err.println("------http--------" + new Date(System.currentTimeMillis() - start).toLocaleString());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("modal")
    public Map<String, Object> modal(String graphXml) {
        Map<String, Object> result = new HashMap<>();
        try {
            mxGraph graph = new mxGraph();
            mxCodec codec = new mxCodec();

            Document doc = mxXmlUtils.parseXml(graphXml);
            codec.decode(doc.getDocumentElement(), graph.getModel());
            mxCell root = (mxCell) graph.getDefaultParent();
            System.out.println(root);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }


    @RequestMapping("transactional")
    public ModelAndView transactional(Model model) {
        try {
            contractService.transactional();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        model.addAttribute("name", "transactional");
        return new ModelAndView("index");
    }


    /**
     * 用户登录
     *
     * @param user
     * @param attributes
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ModelAndView login(HttpSession session, User user, RedirectAttributes attributes) {
        ModelAndView mv = new ModelAndView();
        try {

            boolean login = shiroService.login(user);
            if (login) {
                session.setAttribute("account", user);
                mv.setViewName("redirect:/success");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            mv.setViewName("redirect:/");
        }
        return mv;
    }


    @RequestMapping("success")
    public ModelAndView success(Model model, HttpSession session) {
        try {
            User user = (User) session.getAttribute("account");
            model.addAttribute("certificateCode", user == null ? "" : user.getCertificateCode());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ModelAndView("success");
    }

    @RequestMapping("logout")
    public ModelAndView logout(Model model, HttpSession session) {
        try {
            session.invalidate();
            SecurityUtils.getSubject().logout();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ModelAndView("redirect:/");
    }


    @RequestMapping("initContract")
    public ModelAndView initContract(Model model) {
        try {
            contractService.initContract();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ModelAndView("success");
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

                RedissLockUtils.lock(_lock, TimeUnit.MINUTES, 50);

                System.out.println(getCurrentDate() + " " + name + " begin...");
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println(getCurrentDate() + " " + name + " " + i);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                System.out.println(getCurrentDate() + " " + name + " end...");

                RedissLockUtils.unlock(_lock);
            }
        }).start();
        model.addAttribute("name", "lock");
        return new ModelAndView("index");
    }

    @RequestMapping("zkplock")
    public ModelAndView zkplock(Model model) {
        String name = "";
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
                    CuratorFramework client =
                            CuratorFrameworkFactory.builder()
                                    .connectString("127.0.0.1:2181")
                                    .sessionTimeoutMs(5000)
                                    .connectionTimeoutMs(5000)
                                    .retryPolicy(retryPolicy)
                                    .namespace("base")
                                    .build();
                    client.start();
                    InterProcessMutex mutex = new InterProcessMutex(client, "/curator/lock");
                    mutex.acquire();
                    System.out.println(getCurrentDate() + " " + name + " begin...");
                    for (int i = 0; i < 100; i++) {
                        try {
                            Thread.sleep(1000);
                            System.out.println(getCurrentDate() + " " + name + " " + i);
                        } catch (InterruptedException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                    System.out.println(getCurrentDate() + " " + name + " end...");
                    mutex.release();
                    client.close();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }).start();
        model.addAttribute("name", "lock");
        return new ModelAndView("index");
    }
}
