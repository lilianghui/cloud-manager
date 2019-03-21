package com.lilianghui.spring.starter.netty.rpc;

import com.lilianghui.spring.starter.netty.rpc.entity.NettyRpcProperties;
import com.lilianghui.spring.starter.netty.rpc.common.loadbalance.IpAddressRibbon;
import com.lilianghui.spring.starter.netty.rpc.common.loadbalance.PollRibbon;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class DiscoveryService {
    protected NettyRpcProperties nettyRpcProperties;

    public DiscoveryService(NettyRpcProperties nettyRpcProperties) throws Exception {
        this.nettyRpcProperties = nettyRpcProperties;
    }

    private static final Map<String, IpAddressRibbon> SERVICE_ADDRESS_ROBIN = new ConcurrentHashMap<>();

    public abstract void register(String serviceName, String address) throws Exception;

    public abstract Map<String, Integer> getIp(String serviceName) throws Exception;

    public IpAddressRibbon getIpAddressRibbon() {
        return new PollRibbon();
    }

    public String getServiceAddress(String serviceName, String clientIp) throws Exception {
        serviceName = serviceName.toLowerCase();
        IpAddressRibbon ipAddressRobin = SERVICE_ADDRESS_ROBIN.get(serviceName);
        if (ipAddressRobin == null) {
            ipAddressRobin = getIpAddressRibbon();
            SERVICE_ADDRESS_ROBIN.put(serviceName, ipAddressRobin);
        }
        return ipAddressRobin.getIpAddress(getIp(serviceName), clientIp);
    }

}
