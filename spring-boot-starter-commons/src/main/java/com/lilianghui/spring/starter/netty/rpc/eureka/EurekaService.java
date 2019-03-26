package com.lilianghui.spring.starter.netty.rpc.eureka;

import com.lilianghui.spring.starter.netty.rpc.DiscoveryService;
import com.lilianghui.spring.starter.netty.rpc.entity.NettyRpcProperties;
import com.netflix.appinfo.ApplicationInfoManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class EurekaService extends DiscoveryService {
    private final static String GRPC_ADDRESS_KEY = "GRPC_ADDRESS_KEY";
    private AtomicBoolean init = new AtomicBoolean(false);
    private ApplicationInfoManager applicationInfoManager;
    //    1.定义map, key-ip,value-weight
    protected Map<String, Integer> ipMap = new LinkedHashMap<>();
    @Resource
    private EurekaDiscoveryClient eurekaDiscoveryClient;

    private String applicationName;

    public EurekaService(NettyRpcProperties nettyRpcProperties) throws Exception {
        super(nettyRpcProperties);
    }

    @Override
    public void register(String serviceName, String port) {
        Map<String, String> appMetadata = new HashMap<>();
        appMetadata.put(GRPC_ADDRESS_KEY, port);
        applicationInfoManager.registerAppMetadata(appMetadata);
    }

    @Override
    public Map<String, Integer> getIp(String serviceName) throws Exception {
        if (!init.get()) {
            List<String> list = new ArrayList<>();
            eurekaDiscoveryClient.getInstances(serviceName).forEach(serviceInstance -> {
                String host = serviceInstance.getHost();
                String port = serviceInstance.getMetadata().get(GRPC_ADDRESS_KEY);
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

    public void setApplicationInfoManager(ApplicationInfoManager applicationInfoManager) {
        this.applicationInfoManager = applicationInfoManager;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
