package com.lilianghui.endpoint;

import org.apache.rocketmq.spring.starter.core.RocketMQTemplate;
import org.springframework.context.Lifecycle;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.messaging.Message;

public class RocketOutboundEndpoint extends AbstractReplyProducingMessageHandler
        implements Lifecycle {
    private RocketMQTemplate rocketMQTemplate;
    private String destination;

    public RocketOutboundEndpoint(RocketMQTemplate rocketMQTemplate, String destination) {
        this.rocketMQTemplate = rocketMQTemplate;
        this.destination = destination;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
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
