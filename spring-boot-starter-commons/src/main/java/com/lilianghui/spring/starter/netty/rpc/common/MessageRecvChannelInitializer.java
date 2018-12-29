/**
 * @filename:MessageRecvChannelInitializer.java Newland Co. Ltd. All rights reserved.
 * @Description:Rpc服务端管道初始化
 * @author tangjie
 * @version 1.0
 */
package com.lilianghui.spring.starter.netty.rpc.common;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.springframework.context.ApplicationContext;

public class MessageRecvChannelInitializer extends ChannelInitializer<SocketChannel> {

    //ObjectDecoder 底层默认继承半包解码器LengthFieldBasedFrameDecoder处理粘包问题的时候，
    //消息头开始即为长度字段，占据4个字节。这里出于保持兼容的考虑
    final public static int MESSAGE_LENGTH = 4;
    private ApplicationContext applicationContext = null;

    public MessageRecvChannelInitializer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //ObjectDecoder的基类半包解码器LengthFieldBasedFrameDecoder的报文格式保持兼容。因为底层的父类LengthFieldBasedFrameDecoder
        //的初始化参数即为super(maxObjectSize, 0, 4, 0, 4); 
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, MessageRecvChannelInitializer.MESSAGE_LENGTH, 0, MessageRecvChannelInitializer.MESSAGE_LENGTH));
        //利用LengthFieldPrepender回填补充ObjectDecoder消息报文头
        pipeline.addLast(new LengthFieldPrepender(MessageRecvChannelInitializer.MESSAGE_LENGTH));
        pipeline.addLast(new ObjectEncoder());
        //考虑到并发性能，采用weakCachingConcurrentResolver缓存策略。一般情况使用:cacheDisabled即可
        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
        pipeline.addLast(new InputServerHandler(this.applicationContext));
    }
}