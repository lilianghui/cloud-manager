package com.lilianghui.spring.starter.netty.rpc.common.loadbalance;


import java.util.*;

public class PollWeightRibbon extends IpAddressRibbon {

    Integer pos = 0;


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
        String serverName = null;
        if (pos >= ipArrayList.size()) {
            pos = 0;
        }
        serverName = ipArrayList.get(pos);
        //轮询+1
        pos++;


        return serverName;
    }

}
