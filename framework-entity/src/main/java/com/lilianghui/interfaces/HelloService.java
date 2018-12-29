package com.lilianghui.interfaces;

import com.lilianghui.spring.starter.annotation.NettyRpcClient;

@NettyRpcClient(value = "shiro-server")
public interface HelloService {
    String hello(String msg);
}