package com.lilianghui.spring.starter.netty.rpc.eureka;

import com.lilianghui.spring.starter.netty.rpc.DiscoveryService;
import com.lilianghui.spring.starter.netty.rpc.entity.NettyRpcProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class EurekaService extends DiscoveryService implements ApplicationContextAware {
    private AtomicBoolean init = new AtomicBoolean(false);
    private ApplicationContext applicationContext;
    //    1.定义map, key-ip,value-weight
    protected Map<String, Integer> ipMap = new LinkedHashMap<>();
    @Resource
    private EurekaDiscoveryClient eurekaDiscoveryClient;

    @Value("${spring.application.name}")
    private String applicationName;

    public EurekaService(NettyRpcProperties nettyRpcProperties) throws Exception {
        super(nettyRpcProperties);
    }

    @Override
    public void register(String serviceName, String address) {
        System.out.println(applicationContext);
    }

    @Override
    public Map<String, Integer> getIp(String serviceName) throws Exception {
        if (!init.get()) {
            List<String> list = new ArrayList<>();
            eurekaDiscoveryClient.getInstances(serviceName).forEach(serviceInstance -> {
                String host = serviceInstance.getHost();
                int port = Integer.valueOf(serviceInstance.getMetadata().get("gRPC.port"));
                list.add(String.format("%s:%s", host, port));
            });
            setIpMap(list);
            init.set(true);
        }
        return ipMap;
    }

    @EventListener
    public void listener(ApplicationEvent event) {
        if ((event instanceof EurekaInstanceCanceledEvent) || event instanceof EurekaInstanceRegisteredEvent) {
            init.set(false);
        }
    }


    public void setIpMap(List<String> ipServer) {
        synchronized (ipMap) {
            this.ipMap.clear();
            ipServer.forEach(ip -> this.ipMap.put(ip.split("_")[0], 1));
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       this.applicationContext=applicationContext;
    }
}
