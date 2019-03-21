package com.lilianghui.spring.starter.netty.rpc.common.loadbalance;

import java.util.*;

/**
 * Title:
 * Description:随机
 *
 * @author Created by Julie
 * @version 1.0
 * @date on 18:25 2017/10/26
 */
public class RandomRibbon extends IpAddressRibbon {


    @Override
    public String getIpAddress(Map<String, Integer> ipMap, String clientIp) {

        Set<String> ipSet = ipMap.keySet();

        //定义一个list放所有server
        ArrayList<String> ipArrayList = new ArrayList<String>();
        ipArrayList.addAll(ipSet);

        //循环随机数
        Random random = new Random();
        //随机数在list数量中取（1-list.size）
        int pos = random.nextInt(ipArrayList.size());
        String serverNameReturn = ipArrayList.get(pos);
        return serverNameReturn;
    }


}
