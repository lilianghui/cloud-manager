package com.lilianghui.spring.starter.netty.rpc.server;

import com.lilianghui.spring.starter.netty.rpc.DiscoveryService;
import com.lilianghui.spring.starter.netty.rpc.entity.NettyRpcProperties;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.lang.reflect.Proxy;

public class NettyRpcClientFactoryBean implements FactoryBean {

    private Class nettyInterface;

    @Resource
    private DiscoveryService discoveryService;
    @Resource
    private NettyRpcProperties nettyRpcProperties;

    public NettyRpcClientFactoryBean() {
    }

    public NettyRpcClientFactoryBean(Class nettyInterface) {
        this.nettyInterface = nettyInterface;
    }

    @Override
    public Object getObject() throws Exception {
        Assert.notNull(nettyInterface, "Property nettyInterface is required");
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{nettyInterface}, new NettyProxy(nettyInterface, discoveryService, nettyRpcProperties));
    }

    @Override
    public Class<?> getObjectType() {
        return this.nettyInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
