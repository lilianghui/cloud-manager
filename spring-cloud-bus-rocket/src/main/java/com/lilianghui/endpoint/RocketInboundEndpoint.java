package com.lilianghui.endpoint;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.spring.starter.core.RocketMQTemplate;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.support.DefaultMessageBuilderFactory;
import org.springframework.integration.support.MessageBuilderFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import java.util.List;

public class RocketInboundEndpoint extends MessageProducerSupport {
    private RocketMQTemplate rocketMQTemplate;
    private String destination;
    private MessageBuilderFactory messageBuilderFactory;


    public RocketInboundEndpoint(RocketMQTemplate rocketMQTemplate, String destination) {
        this.rocketMQTemplate = rocketMQTemplate;
        this.destination = destination;
        this.messageBuilderFactory = new DefaultMessageBuilderFactory();

    }


    @Override
    protected void onInit() {
        super.onInit();
        initConsumer();
    }

    private void initConsumer() {
        try {
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumerGroup");
            consumer.setNamesrvAddr("127.0.0.1:9876");
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            consumer.setMessageModel(MessageModel.BROADCASTING);
            consumer.subscribe(this.destination, (String) null);
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    msgs.forEach(messageExt -> {
                        try {
//                            byte[] payload = messageExt.getBody();
//                            MessageValues mv = EmbeddedHeaderUtils.extractHeaders(payload);
//                            org.springframework.messaging.Message<?> internalMsgObject = messageBuilderFactory.withPayload((byte[]) mv.getPayload())
//                                    .copyHeaders(mv.getHeaders()).build();
                            Message message = new GenericMessage(messageExt.getBody());
                            sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

    }


}
