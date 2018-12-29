package com.lilianghui.spring.starter.netty.rpc.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Random;


@Data
@ConfigurationProperties(prefix = NettyRpcProperties.PREFIX)
public class NettyRpcProperties {
    public static final String PREFIX = "spring.netty.rpc";

    private String zookeeperAddress = "127.0.0.1:2181";
    private String host = "0.0.0.0";
    private Random random = new Random();
    private int sessionTimeout = 30000;
    private int httpTimeout = 30000;
    //0-1023: BSD保留端口,也叫系统端口,这些端口只有系统特许的进程才能使用;
    //1024-5000: BSD临时端口,一般的应用程序使用1024到4999来进行通讯;
    //5001-65535: BSD服务器(非特权)端口,用来给用户自定义端口.
    private int port = random(5001, 65535);

    public int random(int start, int end) {
        return random.nextInt(Math.abs(end - start)) + start;
    }

}
