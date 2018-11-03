package com.lilianghui.endpoint;

import org.apache.rocketmq.spring.starter.RocketMQProperties;
import org.apache.rocketmq.spring.starter.core.RocketMQTemplate;
import org.springframework.context.Lifecycle;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.messaging.Message;

public class RocketOutboundEndpoint extends AbstractReplyProducingMessageHandler
        implements Lifecycle {
    private RocketMQTemplate rocketMQTemplate;
    private RocketMQProperties rocketMQProperties;
    private String destination;
    protected volatile boolean running = false;

    public RocketOutboundEndpoint(RocketMQTemplate rocketMQTemplate, RocketMQProperties rocketMQProperties, String destination) {
        this.rocketMQTemplate = rocketMQTemplate;
        this.rocketMQProperties = rocketMQProperties;
        this.destination = destination;
    }

    @Override
    public void start() {
        this.running = true;
    }

    @Override
    public void stop() {
        this.running = false;
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    protected Object handleRequestMessage(Message<?> message) {
        rocketMQTemplate.syncSend(destination, new String((byte[]) message.getPayload()));
        return null;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
