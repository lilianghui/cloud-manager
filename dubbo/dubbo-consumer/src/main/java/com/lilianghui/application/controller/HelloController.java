package com.lilianghui.application.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lilianghui.service.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Reference(version = "1.0.0")
    private HelloService helloService;

    @GetMapping("hello/{name}")
    public String sayHello(@PathVariable String name) {
        return helloService.sayHello(name);
    }

    @GetMapping("goodbye/{name}")
    public String sayGoodbye(@PathVariable String name) {
        return helloService.sayGoodbye(name);
    }

}