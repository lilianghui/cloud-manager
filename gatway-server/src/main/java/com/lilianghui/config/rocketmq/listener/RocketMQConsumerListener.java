package com.lilianghui.config.rocketmq.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.starter.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.starter.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@RocketMQMessageListener(consumerGroup = "${spring.application.name}", topic = "topic")
@Component
public class RocketMQConsumerListener implements RocketMQListener {


    @Override
    public void onMessage(Object message) {
        System.out.println(message);
    }
}
