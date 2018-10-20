package com.lilianghui.framework.core.rocketmq;

import com.lilianghui.framework.core.entity.MergeEntity;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

public interface RocketService<T> {
    void process(T object, MessageExt messageExt, ConsumeConcurrentlyContext context);

    void processMergeEntity(MergeEntity<T, ?> object, MessageExt messageExt, ConsumeConcurrentlyContext context);
}
