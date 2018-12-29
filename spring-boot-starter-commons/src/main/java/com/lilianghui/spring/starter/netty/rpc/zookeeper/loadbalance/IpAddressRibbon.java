package com.lilianghui.spring.starter.netty.rpc.zookeeper.loadbalance;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class IpAddressRibbon implements Watcher {
    protected ZooKeeper zooKeeper = null;
    //    1.定义map, key-ip,value-weight
    protected Map<String, Integer> ipMap = new LinkedHashMap<>();

    public IpAddressRibbon(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public abstract String getIpAddress(String clientIp);

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged && StringUtils.isNotBlank(watchedEvent.getPath())) {
                List<String> list = zooKeeper.getChildren(watchedEvent.getPath(), this);
                setIpMap(list);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void setIpMap(List<String> ipServer) {
        synchronized (ipMap) {
            this.ipMap.clear();
            ipServer.forEach(ip -> this.ipMap.put(ip.split("_")[0], 1));
        }

    }

}
