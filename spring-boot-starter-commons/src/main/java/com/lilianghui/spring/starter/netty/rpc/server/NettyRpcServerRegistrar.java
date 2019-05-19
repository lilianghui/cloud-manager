package com.lilianghui.spring.starter.netty.rpc.server;

import com.lilianghui.spring.starter.annotation.NettyRpcService;
import com.lilianghui.spring.starter.config.OpenReplicatorProperties;
import com.lilianghui.spring.starter.netty.rpc.DiscoveryService;
import com.lilianghui.spring.starter.netty.rpc.common.MessageRecvChannelInitializer;
import com.lilianghui.spring.starter.netty.rpc.common.NamedThreadFactory;
import com.lilianghui.spring.starter.netty.rpc.entity.NettyRpcProperties;
import com.lilianghui.spring.starter.netty.rpc.eureka.EurekaService;
import com.lilianghui.spring.starter.netty.rpc.zookeeper.ZookeeperService;
import com.lilianghui.spring.starter.utils.WebUtils;
import com.netflix.appinfo.ApplicationInfoManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

import javax.annotation.Resource;
import java.nio.channels.spi.SelectorProvider;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

@Slf4j
@Configuration
@EnableConfigurationProperties(NettyRpcProperties.class)
public class NettyRpcServerRegistrar implements ApplicationContextAware, InitializingBean, DisposableBean {


    private ApplicationContext applicationContext;
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private ChannelFuture future;

    @Value("${spring.application.name}")
    private String applicationName;

    @Resource
    private NettyRpcProperties nettyRpcProperties;


    @Bean
    @ConditionalOnClass(name = "com.netflix.discovery.DiscoveryClient")
    @ConditionalOnProperty(prefix = NettyRpcProperties.PREFIX,value = "registerEureka",havingValue = "true",matchIfMissing=true)
    public DiscoveryService eurekaService(@Autowired ApplicationInfoManager applicationInfoManager) throws Exception {
            EurekaService eurekaService = new EurekaService(nettyRpcProperties);
            eurekaService.setApplicationInfoManager(applicationInfoManager);
            eurekaService.register(applicationName, String.valueOf(nettyRpcProperties.getPort()));
            return eurekaService;
    }

    @Bean
    @ConditionalOnClass(name = "org.apache.zookeeper.ZooKeeper")
    @ConditionalOnProperty(prefix = NettyRpcProperties.PREFIX,value = "registerEureka",havingValue = "false")
    public DiscoveryService discoveryService() throws Exception {
        ZookeeperService zookeeperService = new ZookeeperService(nettyRpcProperties);
        zookeeperService.register(applicationName, String.format("%s:%s", WebUtils.getLocalIp(), nettyRpcProperties.getPort()));
        return zookeeperService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> handlerMap = applicationContext.getBeansWithAnnotation(NettyRpcService.class);
        if (MapUtils.isNotEmpty(handlerMap)) {
            try {
                //netty的线程池模型设置成主从线程池模式，这样可以应对高并发请求
                //当然netty还支持单线程、多线程网络IO模型，可以根据业务需求灵活配置
                ThreadFactory threadRpcFactory = new NamedThreadFactory("NettyRPC ThreadFactory");

                //方法返回到Java虚拟机的可用的处理器数量
                int parallel = Runtime.getRuntime().availableProcessors() * 2;

                boss = new NioEventLoopGroup();
                worker = new NioEventLoopGroup(parallel, threadRpcFactory, SelectorProvider.provider());

                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                        .childHandler(new MessageRecvChannelInitializer(applicationContext))
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);
                future = bootstrap.bind(nettyRpcProperties.getHost(), nettyRpcProperties.getPort()).sync();
                log.info("Netty RPC Server start success ip:{} port:{}", nettyRpcProperties.getHost(), nettyRpcProperties.getPort());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        try {
            if (future != null) {
                future.channel().closeFuture().sync();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        try {
            if (worker != null) {
                worker.shutdownGracefully();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        try {
            if (boss != null) {
                boss.shutdownGracefully();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    public static class ZookeeperCenterCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Map<String, Object> handlerMap = context.getBeanFactory().getBeansWithAnnotation(NettyRpcService.class);
            return MapUtils.isNotEmpty(handlerMap);
        }
    }
}
