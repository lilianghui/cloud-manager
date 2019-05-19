/*
 * Copyright 2015-2018 The OpenZipkin Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.lilianghui.application.server;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zipkin2.Callback;
import zipkin2.Span;
import zipkin2.codec.SpanBytesDecoder;
import zipkin2.collector.Collector;
import zipkin2.collector.CollectorMetrics;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Consumes spans from Kafka messages, ignoring malformed input
 */
@Slf4j
final class RocketCollectorWorker implements Runnable, MessageListenerConcurrently, MessageListenerOrderly {
    static final Callback<Void> NOOP =
            new Callback<Void>() {
                @Override
                public void onSuccess(Void value) {
                }

                @Override
                public void onError(Throwable t) {
                    log.error(t.getMessage(), t);
                }
            };

    final ZipkinRocketCollectorProperties properties;
    final Collector collector;
    final CollectorMetrics metrics;

    private ObjectMapper objectMapper = new ObjectMapper();


    RocketCollectorWorker(RocketCollector.Builder builder) {
        properties = builder.properties;
        collector = builder.delegate.build();
        metrics = builder.metrics;
    }

    @Override
    public void run() {
        try {
            ZipkinRocketCollectorProperties.ConsumerProperties consumerProperties = properties.getConsumer();
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerProperties.getConsumerGroup());
            consumer.setNamesrvAddr(properties.getNameServer());
            int consumeThreadMax = consumerProperties.getConsumeThreadMax();
//            consumer.setConsumeThreadMax(consumeThreadMax);

//            if (consumeThreadMax < consumer.getConsumeThreadMin()) {
//                consumer.setConsumeThreadMin(consumeThreadMax);
//            }

//            consumer.setMessageModel(consumerProperties.getMessageModel());
            //从消息队列头部开始消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

            //设置广播消费模式
            consumer.setMessageModel(MessageModel.CLUSTERING);


            consumer.setMessageListener(this);
            consumer.registerMessageListener((MessageListenerConcurrently) this);
            //订阅主题
            consumer.subscribe(consumerProperties.getTopic(), consumerProperties.getTag());
            consumer.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /* span key or trace ID key */
    static boolean protobuf3(byte[] bytes) {
        return bytes[0] == 10 && bytes[1] != 0; // varint follows and won't be zero
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        processMessage(msgs);
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        processMessage(msgs);
        return ConsumeOrderlyStatus.SUCCESS;
    }

    private void processMessage(List<MessageExt> msgs) {
        msgs.forEach(messageExt -> {
//            try {
//                final byte[] bytes = messageExt.getBody();
//                JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Span.class);
//                List<Span> spans = objectMapper.readValue(new String(bytes), javaType);
//                spans.forEach(span -> collector.accept(Collections.singletonList(span), NOOP));
//            } catch (IOException e) {
//                log.error(e.getMessage(), e);
//            }
            metrics.incrementMessages();
            final byte[] bytes = messageExt.getBody();
            if (bytes.length < 2) { // need two bytes to check if protobuf
                metrics.incrementMessagesDropped();
            } else {
                // If we received legacy single-span encoding, decode it into a singleton list
                if (!protobuf3(bytes) && bytes[0] <= 16 && bytes[0] != 12 /* thrift, but not list */) {
                    metrics.incrementBytes(bytes.length);
                    try {
                        Span span = SpanBytesDecoder.THRIFT.decodeOne(bytes);
                        collector.accept(Collections.singletonList(span), NOOP);
                    } catch (RuntimeException e) {
                        metrics.incrementMessagesDropped();
                    }
                } else {
                    collector.acceptSpans(bytes, NOOP);
                }
            }


        });
    }
}
