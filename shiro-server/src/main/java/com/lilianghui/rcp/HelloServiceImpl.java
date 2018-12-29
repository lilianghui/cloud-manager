package com.lilianghui.rcp;


import com.lilianghui.interfaces.HelloService;
import com.lilianghui.spring.starter.annotation.NettyRpcService;
import org.springframework.stereotype.Service;

/**
 * 实现类
 */
@Service
@NettyRpcService
public class HelloServiceImpl implements HelloService {
    public String hello(String msg) {
        return msg+"ccccccccccccccccccc";
    }
}