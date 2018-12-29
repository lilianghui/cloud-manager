package com.lilianghui.spring.starter.netty.rpc.zookeeper.loadbalance;

import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class IpHashRibbon extends IpAddressRibbon {

    public IpHashRibbon(ZooKeeper zooKeeper) {
        super(zooKeeper);
    }

    @Override
    public String getIpAddress(String clientIp) {
        //    2.取出来key,放到set中
        Set<String> ipset = ipMap.keySet();

        //    3.set放到list，要循环list取出
        ArrayList<String> iplist = new ArrayList<String>();
        iplist.addAll(ipset);

        //对ip的hashcode值取余数，每次都一样的
        int hashCode = clientIp.hashCode();
        int serverListsize = iplist.size();
        int pos = hashCode % serverListsize;
        return iplist.get(pos);

    }

}
