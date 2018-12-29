package com.lilianghui.spring.starter.netty.rpc.common;

import com.lilianghui.spring.starter.netty.rpc.entity.MessageCallBack;
import com.lilianghui.spring.starter.netty.rpc.entity.MessageRequest;
import com.lilianghui.spring.starter.netty.rpc.entity.MessageResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OutputClientHandler extends ChannelInboundHandlerAdapter {//implements Callable

    private ChannelHandlerContext context;
    public Map<String, MessageCallBack> STRING_OBJECT_MAP = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        context = ctx;
    }

    /**
     * 收到服务端数据，唤醒等待线程
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) {
        MessageResponse response = (MessageResponse) msg;
        String messageId = response.getMessageId();
        MessageCallBack callBack = STRING_OBJECT_MAP.get(messageId);
        if (callBack != null) {
            STRING_OBJECT_MAP.remove(messageId);
            callBack.over(response);
        }
//        result = msg.toString();
//        notify();
    }

    /**
     * 写出数据，开始等待唤醒
     */
//    @Override
//    public synchronized Object call() throws InterruptedException {
//        context.writeAndFlush();
//        wait();
//        return result;
//    }
    public MessageCallBack sendRequest(MessageRequest request, int httpTimeout) {
        MessageCallBack callBack = new MessageCallBack(request,httpTimeout);
        STRING_OBJECT_MAP.put(request.getMessageId(), callBack);
        context.writeAndFlush(request);
        return callBack;
    }

}