/**
 * Title:轮询
 * Description:
 *
 * @author Created by Julie
 * @version 1.0
 * @date on 15:49 2017/10/26
 */
package com.lilianghui.spring.starter.netty.rpc.zookeeper.loadbalance;


import org.apache.zookeeper.ZooKeeper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class PollRibbon extends IpAddressRibbon {
    //    Integer sum=0;
    Integer pos = 0;

    public PollRibbon(ZooKeeper zooKeeper) {
        super(zooKeeper);
    }

    public String getIpAddress(String clientIp) {


        //    2.取出来key,放到set中
        Set<String> ipset = ipMap.keySet();

        //    3.set放到list，要循环list取出
        ArrayList<String> iplist = new ArrayList<String>();
        iplist.addAll(ipset);

        String serverName = null;

        //    4.定义一个循环的值，如果大于set就从0开始
        synchronized (pos) {
            if (pos >= ipset.size()) {
                pos = 0;
            }
            serverName = iplist.get(pos);
            //轮询+1
            pos++;
        }
//        System.err.println(String.format("clientIp=%s---iplist=%s---serverName=%s",clientIp,iplist,serverName));
        return serverName;

    }


}
