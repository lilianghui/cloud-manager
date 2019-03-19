package com.lilianghui.spring.starter.netty.rpc;

public interface DiscoveryService {

    void register(String serviceName, String address) throws Exception;

    String getServiceAddress(String serviceName, String clientIp) throws Exception;

}
