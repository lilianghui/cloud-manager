package com.lilianghui.spring.starter.netty.rpc.server;

import com.google.common.collect.Sets;
import com.lilianghui.spring.starter.annotation.NettyRpcClient;
import com.lilianghui.spring.starter.netty.rpc.common.OutputClientHandler;
import com.lilianghui.spring.starter.netty.rpc.entity.MessageCallBack;
import com.lilianghui.spring.starter.netty.rpc.common.MessageRecvChannelInitializer;
import com.lilianghui.spring.starter.netty.rpc.entity.MessageRequest;
import com.lilianghui.spring.starter.netty.rpc.entity.NettyRpcProperties;
import com.lilianghui.spring.starter.netty.rpc.zookeeper.ZookeeperCenter;
import com.lilianghui.spring.starter.utils.WebUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NettyProxy implements InvocationHandler {

    private static final Map<String, OutputClientHandler> CHANNEL_FUTURE_MAP = new ConcurrentHashMap<>();
    private static final Set<String> IGNORE_METHOD_NAME = Sets.newHashSet("toString", "hashCode", "equals", "wait");
    private Class nettyInterface;
    private NettyRpcClient nettyRpcClient;
    private ZookeeperCenter zookeeperCenter;
    private NettyRpcProperties nettyRpcProperties;

    public NettyProxy(Class<? extends Annotation> nettyInterface, ZookeeperCenter zookeeperCenter, NettyRpcProperties nettyRpcProperties) throws Exception {
        this.nettyInterface = nettyInterface;
        this.zookeeperCenter = zookeeperCenter;
        this.nettyRpcProperties = nettyRpcProperties;
        nettyRpcClient = nettyInterface.getDeclaredAnnotation(NettyRpcClient.class);
    }

    public OutputClientHandler createNettyServer(String host, int port) throws Exception {
        OutputClientHandler outputClientHandler = new OutputClientHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
//                        pipeline.addLast(new StringDecoder());
//                        pipeline.addLast(new StringEncoder());
                        //ObjectDecoder的基类半包解码器LengthFieldBasedFrameDecoder的报文格式保持兼容。因为底层的父类LengthFieldBasedFrameDecoder
                        //的初始化参数即为super(maxObjectSize, 0, 4, 0, 4);
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, MessageRecvChannelInitializer.MESSAGE_LENGTH, 0, MessageRecvChannelInitializer.MESSAGE_LENGTH));
                        //利用LengthFieldPrepender回填补充ObjectDecoder消息报文头
                        pipeline.addLast(new LengthFieldPrepender(MessageRecvChannelInitializer.MESSAGE_LENGTH));
                        pipeline.addLast(new ObjectEncoder());
                        //考虑到并发性能，采用weakCachingConcurrentResolver缓存策略。一般情况使用:cacheDisabled即可
                        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));

                        pipeline.addLast(outputClientHandler);
                    }
                });
        ChannelFuture channelFuture = b.connect(host, port).sync();
        return outputClientHandler;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (IGNORE_METHOD_NAME.contains(methodName)) {
            return null;
        }
        String clientIp = null;
        try {
            clientIp = WebUtils.getRemoteHost(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String address = getIpAddress(clientIp);
        if (!CHANNEL_FUTURE_MAP.containsKey(address)) {
            String[] addressSplit = address.split(":");
            CHANNEL_FUTURE_MAP.put(address, createNettyServer(addressSplit[0], Integer.valueOf(addressSplit[1])));
        }
        MessageRequest request = new MessageRequest();
        request.setMessageId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(methodName);
        request.setTypeParameters(method.getParameterTypes());
        request.setParameters(args);
        MessageCallBack callBack = CHANNEL_FUTURE_MAP.get(address).sendRequest(request,nettyRpcProperties.getHttpTimeout());
        return callBack.start();
    }

    public String getIpAddress(String clientIp) throws Exception {
        String address = nettyRpcClient.address();
        if (StringUtils.isBlank(address)) {
            address = zookeeperCenter.getServiceAddress(nettyRpcClient.value(), clientIp);
        }
        return address;
    }
}
