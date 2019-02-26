package com.lilianghui.application.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lilianghui.service.HelloService;

@Service(version = "1.0.0")
@org.springframework.stereotype.Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return String.format("--Hello---%s-------",name);
    }

    @Override
    public String sayGoodbye(String name) {
        return String.format("--Goodbye---%s-------",name);
    }

}
