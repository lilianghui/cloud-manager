package com.lilianghui.spring.starter.netty.rpc.common;

import com.lilianghui.spring.starter.netty.rpc.entity.MessageRequest;
import com.lilianghui.spring.starter.netty.rpc.entity.MessageResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.MethodUtils;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MessageRecvInitializeTask implements Runnable {

    private MessageRequest request = null;
    private MessageResponse response = null;
    private ApplicationContext applicationContext = null;
    private ChannelHandlerContext ctx = null;
    private static final Map<String, Object> BEAN_MAP = new ConcurrentHashMap<>();

    public MessageResponse getResponse() {
        return response;
    }

    public MessageRequest getRequest() {
        return request;
    }

    public void setRequest(MessageRequest request) {
        this.request = request;
    }

    public MessageRecvInitializeTask(MessageRequest request, MessageResponse response, ApplicationContext applicationContext, ChannelHandlerContext ctx) {
        this.request = request;
        this.response = response;
        this.applicationContext = applicationContext;
        this.ctx = ctx;
    }

    public void run() {
        try {
            response.setMessageId(request.getMessageId());
            Object result = reflect(request);
            response.setResult(result);
        } catch (Exception e) {
            response.setError(e.getMessage());
            log.error(e.getMessage(), e);
        }

        ctx.writeAndFlush(response).addListener((ChannelFutureListener) channelFuture -> log.info("RPC Server Send message-id respone:" + request.getMessageId()));
    }

    private Object reflect(MessageRequest request) throws Exception {
        String className = request.getClassName();
        String methodName = request.getMethodName();
        Object[] parameters = request.getParameters();
        return MethodUtils.invokeMethod(resolveBean(className), methodName, parameters);
    }

    private Object resolveBean(String className) throws ClassNotFoundException {
        Object serviceBean = BEAN_MAP.get(className);
        if (serviceBean == null) {
            synchronized (className.intern()) {
                if (BEAN_MAP.get(className) == null) {
                    BEAN_MAP.put(className, applicationContext.getBean(Class.forName(className)));
                    serviceBean = BEAN_MAP.get(className);
                }
            }
        }
        return serviceBean;
    }
}