package com.lilianghui.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.spring.starter.RocketMQProperties;
import org.apache.rocketmq.spring.starter.core.RocketMQTemplate;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.support.DefaultMessageBuilderFactory;
import org.springframework.integration.support.MessageBuilderFactory;
import org.springframework.messaging.support.GenericMessage;

import java.util.UUID;

@Slf4j
public class RocketInboundEndpoint extends MessageProducerSupport {

    private MessageBuilderFactory messageBuilderFactory;
    private RocketMQTemplate rocketMQTemplate;
    private RocketMQProperties rocketMQProperties;
    private String destination;
    private String applicationName;

    public RocketInboundEndpoint(RocketMQTemplate rocketMQTemplate, RocketMQProperties rocketMQProperties, String destination, String applicationName) {
        this.rocketMQTemplate = rocketMQTemplate;
        this.rocketMQProperties = rocketMQProperties;
        this.destination = destination;
        this.applicationName = applicationName;
        this.messageBuilderFactory = new DefaultMessageBuilderFactory();

    }


    @Override
    protected void onInit() {
        super.onInit();
        doStart();
        initializeConsumers();
    }

    private void initializeConsumers() {
        try {
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(applicationName + "-" + UUID.randomUUID().toString().replaceAll("-",""));
            System.out.println(applicationName + "-" + UUID.randomUUID().toString().replaceAll("-",""));
            consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            consumer.setMessageModel(MessageModel.BROADCASTING);
            consumer.subscribe(this.destination, (String) null);
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                msgs.forEach(messageExt -> {
                    try {
                        GenericMessage message = new GenericMessage(messageExt.getBody());
                        sendMessage(message);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                });
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
        } catch (MQClientException e) {
            logger.error(e.getMessage(), e);
        }
    }


}
