package com.lilianghui.spring.starter.netty.rpc.zookeeper;

import com.lilianghui.spring.starter.netty.rpc.DiscoveryService;
import com.lilianghui.spring.starter.netty.rpc.entity.NettyRpcProperties;
import com.lilianghui.spring.starter.netty.rpc.common.loadbalance.IpAddressRibbon;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ZookeeperService extends DiscoveryService implements Watcher {
    private static final String ROOT = "/registry";
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    ZooKeeper zooKeeper = null;
    private AtomicBoolean init = new AtomicBoolean(false);

    //    1.定义map, key-ip,value-weight
    protected Map<String, Integer> ipMap = new LinkedHashMap<>();

    public ZookeeperService(NettyRpcProperties nettyRpcProperties) throws Exception {
        super(nettyRpcProperties);
        zooKeeper = new ZooKeeper(nettyRpcProperties.getZookeeperAddress(), nettyRpcProperties.getSessionTimeout(), this);
        countDownLatch.await();
    }

    @Override
    public void register(String serviceName, String registration) throws Exception {
        serviceName = serviceName.toLowerCase();
        if (zooKeeper.exists(ROOT, false) == null) {
            zooKeeper.create(ROOT, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        String serviceRoot = ROOT + "/" + serviceName;
        if (zooKeeper.exists(serviceRoot, false) == null) {
            zooKeeper.create(serviceRoot, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        String serviceInstance = serviceRoot + "/" + registration + "_";
        if (zooKeeper.exists(serviceInstance, false) == null) {
            zooKeeper.create(serviceInstance, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        }
    }

    @Override
    public Map<String, Integer> getIp(String serviceName) throws Exception {
        if (!init.get()) {
            final Watcher watcher = new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    try {
                        if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged && StringUtils.isNotBlank(watchedEvent.getPath())) {
                            List<String> list = null;
                            list = zooKeeper.getChildren(watchedEvent.getPath(), this);
                            setIpMap(list);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            String serviceRoot = ROOT + "/" + serviceName;
            List<String> list = zooKeeper.getChildren(serviceRoot, watcher);
            setIpMap(list);
            init.set(true);
        }
        return ipMap;
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                countDownLatch.countDown();
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    public void setIpMap(List<String> ipServer) {
        synchronized (ipMap) {
            this.ipMap.clear();
            ipServer.forEach(ip -> this.ipMap.put(ip.split("_")[0], 1));
        }

    }

}
