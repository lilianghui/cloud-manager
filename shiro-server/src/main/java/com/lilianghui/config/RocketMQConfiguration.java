/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lilianghui.config;

import com.lilianghui.entity.User;
import com.lilianghui.framework.core.protobuf.ProtocbufRedisSerializer;
import com.lilianghui.framework.core.rocketmq.entity.RocketMQConfig;
import com.lilianghui.framework.core.rocketmq.transaction.SpringDefaultMQPushConsumer;
import com.lilianghui.framework.core.rocketmq.transaction.SpringTransactionMQProducer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.*;

@Configuration
@EnableConfigurationProperties(RocketMQConfig.class)
public class RocketMQConfiguration {
    @Resource
    private RocketMQConfig rocketMQConfig;


    @Bean
    public DefaultMQPushConsumer MQPushConsumer() throws Exception {
        SpringDefaultMQPushConsumer consumer = new SpringDefaultMQPushConsumer(rocketMQConfig.getGroup());
        consumer.setNamesrvAddr(rocketMQConfig.getNamesrvAddr());
        consumer.setVipChannelEnabled(false);
        consumer.setInstanceName(rocketMQConfig.getInstanceName());
        consumer.subscribe(rocketMQConfig.getTopic(), rocketMQConfig.getTag());
        consumer.setRocketMQConfig(rocketMQConfig);
        return consumer;
    }

}