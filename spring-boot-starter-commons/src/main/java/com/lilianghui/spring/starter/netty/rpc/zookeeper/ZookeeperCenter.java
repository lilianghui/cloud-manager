package com.lilianghui.spring.starter.netty.rpc.zookeeper;

import com.lilianghui.spring.starter.netty.rpc.zookeeper.loadbalance.IpAddressRibbon;
import com.lilianghui.spring.starter.netty.rpc.zookeeper.loadbalance.PollRibbon;
import org.apache.zookeeper.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ZookeeperCenter implements Watcher {
    private static final Map<String, IpAddressRibbon> SERVICE_ADDRESS_ROBIN = new HashMap<>();
    private static final String ROOT = "/registry";
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    ZooKeeper zooKeeper = null;

    public ZookeeperCenter(String address, int sessionTimeout) throws Exception {
        zooKeeper = new ZooKeeper(address, sessionTimeout, this);
        countDownLatch.await();
    }

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
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            countDownLatch.countDown();
        }
    }

    public String getServiceAddress(String serviceName,String clientIp) throws Exception {
        serviceName = serviceName.toLowerCase();
        IpAddressRibbon ipAddressRobin = SERVICE_ADDRESS_ROBIN.get(serviceName);
        if (ipAddressRobin == null) {
            String serviceRoot = ROOT + "/" + serviceName;
            ipAddressRobin = new PollRibbon(zooKeeper);
            List<String> list = zooKeeper.getChildren(serviceRoot, ipAddressRobin);
            ipAddressRobin.setIpMap(list);
            SERVICE_ADDRESS_ROBIN.put(serviceName, ipAddressRobin);
        }
        return ipAddressRobin.getIpAddress(clientIp);
    }

    public static void main(String[] args) throws Exception {
        ZookeeperCenter zookeeperCenter = new ZookeeperCenter("127.0.0.1:2181", 1000);
        zookeeperCenter.register("api-gate-way", "192.168.1.123:8801");
        zookeeperCenter.register("api-gate-way", "192.168.1.124:8802");
        zookeeperCenter.register("api-gate-way", "192.168.1.125:8803");
        new Thread(() -> {
            for (int i = 4; i < 15; i++) {
                try {
                    zookeeperCenter.register("api-gate-way", "192.168.1.125:880" + i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        for (int i = 0; i < 50; i++) {
            Thread.sleep(1000);
            System.err.println(zookeeperCenter.getServiceAddress("api-gate-way",null));
        }
    }
}
