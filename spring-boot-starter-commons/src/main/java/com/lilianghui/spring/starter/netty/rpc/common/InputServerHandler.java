package com.lilianghui.spring.starter.netty.rpc.common;

import com.lilianghui.spring.starter.netty.rpc.entity.MessageRequest;
import com.lilianghui.spring.starter.netty.rpc.entity.MessageResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 用于处理请求数据
 */
public class InputServerHandler extends ChannelInboundHandlerAdapter {
    private final ApplicationContext applicationContext;
    private static ThreadPoolExecutor threadPoolExecutor;

    public InputServerHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MessageRequest request = (MessageRequest) msg;
        MessageResponse response = new MessageResponse();
        MessageRecvInitializeTask recvTask = new MessageRecvInitializeTask(request, response, applicationContext, ctx);
        //不要阻塞nio线程，复杂的业务逻辑丢给专门的线程池
        submit(recvTask);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //网络有异常要关闭通道
        ctx.close();
    }

    public static void submit(Runnable task) {
        if (threadPoolExecutor == null) {
            synchronized (InputServerHandler.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = (ThreadPoolExecutor) RpcThreadPool.getExecutor(16, -1);
                }
            }
        }
        threadPoolExecutor.submit(task);
    }
}