package com.lilianghui.spring.starter.netty.rpc.common.loadbalance;

import java.util.*;

/**
 * Title:
 * Description:加权随机
 *
 * @author Created by Julie
 * @version 1.0
 * @date on 18:42 2017/10/26
 */
public class RandomWeightRibbon extends IpAddressRibbon {


    @Override
    public String getIpAddress(Map<String, Integer> ipMap, String clientIp) {


        Set<String> ipSet = ipMap.keySet();
        Iterator<String> ipIterator = ipSet.iterator();

        //定义一个list放所有server
        ArrayList<String> ipArrayList = new ArrayList<String>();

        //循环set，根据set中的可以去得知map中的value，给list中添加对应数字的server数量
        while (ipIterator.hasNext()) {
            String serverName = ipIterator.next();
            Integer weight = ipMap.get(serverName);
            for (int i = 0; i < weight; i++) {
                ipArrayList.add(serverName);
            }
        }

        //循环随机数
        Random random = new Random();
        //随机数在list数量中取（1-list.size）
        int pos = random.nextInt(ipArrayList.size());
        String serverNameReturn = ipArrayList.get(pos);
        return serverNameReturn;
    }


}
