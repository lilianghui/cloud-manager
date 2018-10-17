package com.lilianghui.framework.core.rocketmq.transaction;

import com.google.common.collect.Maps;
import com.lilianghui.framework.core.protobuf.ProtocbufRedisSerializer;
import com.lilianghui.framework.core.rocketmq.RocketService;
import com.lilianghui.framework.core.rocketmq.entity.RocketMQConfig;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.Map;

public class SpringDefaultMQPushConsumer extends DefaultMQPushConsumer implements ApplicationContextAware, DisposableBean, InitializingBean, MessageListenerConcurrently {
    private ProtocbufRedisSerializer protocbufRedisSerializer = new ProtocbufRedisSerializer();
    private RocketMQConfig rocketMQConfig;
    private ApplicationContext applicationContext;

    public SpringDefaultMQPushConsumer() {
        registerMessageListener(this);
    }

    public SpringDefaultMQPushConsumer(String consumerGroup) {
        super(consumerGroup);
        registerMessageListener(this);
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        ConsumeConcurrentlyStatus value = ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        for (MessageExt messageExt : msgs) {
            Object object = protocbufRedisSerializer.deserialize(messageExt.getBody());
            try {
                RocketService rocketService = (RocketService) applicationContext.getBean(rocketMQConfig.getEntityBeanNameMap().get(object.getClass()));
                rocketService.process(object,messageExt,context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.start();
    }

    @Override
    public void destroy() throws Exception {
        this.shutdown();
    }

    public RocketMQConfig getRocketMQConfig() {
        return rocketMQConfig;
    }

    public void setRocketMQConfig(RocketMQConfig rocketMQConfig) {
        this.rocketMQConfig = rocketMQConfig;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
