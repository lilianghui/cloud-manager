package com.lilianghui.framework.core.rocketmq.transaction;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Maps;
import com.lilianghui.framework.core.entity.MergeEntity;
import com.lilianghui.framework.core.jackson.JacksonUtils;
import com.lilianghui.framework.core.protobuf.ProtocbufRedisSerializer;
import com.lilianghui.framework.core.rocketmq.RocketService;
import com.lilianghui.framework.core.rocketmq.entity.RocketMQConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
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
            try {
                String className = messageExt.getUserProperty(SpringTransactionMQProducer.CLASS_NAME);
                if (StringUtils.isNotBlank(className)) {
                    String[] split = className.split(",");
                    Class<?> mainClass = Class.forName(split[0]);
                    JavaType javaType = JacksonUtils.getObjectMapper().getTypeFactory().constructParametricType(MergeEntity.class, mainClass, Class.forName(split[1]), Class.forName(split[2]));
                    MergeEntity mergeEntity = JacksonUtils.getObjectMapper().readValue(new String(messageExt.getBody(), RemotingHelper.DEFAULT_CHARSET), javaType);
                    RocketService rocketService = (RocketService) applicationContext.getBean(rocketMQConfig.getEntityBeanNameMap().get(mainClass));
                    rocketService.processMergeEntity(mergeEntity, messageExt, context);
                } else {
                    Object object = protocbufRedisSerializer.deserialize(messageExt.getBody());
                    RocketService rocketService = (RocketService) applicationContext.getBean(rocketMQConfig.getEntityBeanNameMap().get(object.getClass()));
                    rocketService.process(object, messageExt, context);
                }
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
