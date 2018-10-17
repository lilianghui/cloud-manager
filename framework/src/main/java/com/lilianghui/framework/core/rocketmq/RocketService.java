package com.lilianghui.framework.core.rocketmq;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

public interface RocketService<T> {
    void process(T object, MessageExt messageExt, ConsumeConcurrentlyContext context);
}
