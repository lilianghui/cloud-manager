package com.lilianghui.spring.starter.netty.rpc.common.loadbalance;

import java.util.Map;

public abstract class IpAddressRibbon {


    public abstract String getIpAddress(Map<String, Integer> ipMap, String clientIp);


}
