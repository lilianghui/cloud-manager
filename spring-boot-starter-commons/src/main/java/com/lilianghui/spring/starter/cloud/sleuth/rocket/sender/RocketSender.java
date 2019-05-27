/*
 * Copyright 2016-2018 The OpenZipkin Authors
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
package com.lilianghui.spring.starter.cloud.sleuth.rocket.sender;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.starter.RocketMQProperties;
import org.apache.rocketmq.spring.starter.core.RocketMQTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.Assert;
import zipkin2.Call;
import zipkin2.Callback;
import zipkin2.CheckResult;
import zipkin2.codec.Encoding;
import zipkin2.reporter.AwaitableCallback;
import zipkin2.reporter.BytesMessageEncoder;
import zipkin2.reporter.Sender;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * This sends (usually json v2) encoded spans to a Kafka topic.
 *
 * <p>This sender is thread-safe.
 *
 * <p>This sender is linked against Kafka 0.10.2+, which allows it to work with Kafka 0.10+ brokers
 */
@Slf4j
public final class RocketSender extends Sender {

    final Encoding encoding;
    final BytesMessageEncoder encoder;
    final int messageMaxBytes;
    volatile boolean closeCalled;

    private String topic;
    private RocketMQSender rocketMQSender;

    public RocketSender(String topic) {
        this(null, topic);
    }

    public RocketSender(RocketMQTemplate rocketMQTemplate, String topic) {
        this(rocketMQTemplate, null, topic);
    }

    public RocketSender(RocketMQTemplate rocketMQTemplate, RocketMQProperties rocketMQProperties, String topic) {
        this.rocketMQSender = createRocketMQSender(rocketMQTemplate, rocketMQProperties);
        this.topic = topic;
        this.messageMaxBytes = 1000000;
        this.encoding = Encoding.JSON;
        this.encoder = BytesMessageEncoder.forEncoding(this.encoding);
    }


    private RocketMQSender createRocketMQSender(RocketMQTemplate rocketMQTemplate, RocketMQProperties rocketMQProperties) {

        if (Objects.isNull(rocketMQTemplate)) {
            return (topic, message, callBack) -> {
                createMQProducer(rocketMQProperties).send(new Message(topic, message), new SendCallbackEx(callBack));
            };
        } else {
            return (topic, message, callBack) ->
                    rocketMQTemplate.asyncSend(topic, new GenericMessage<>(new String(message)), new SendCallbackEx(callBack));
        }
    }


    @Override
    public int messageSizeInBytes(List<byte[]> encodedSpans) {
        return encoding.listSizeInBytes(encodedSpans);
    }

    @Override
    public int messageSizeInBytes(int encodedSizeInBytes) {
        return encoding.listSizeInBytes(encodedSizeInBytes);
    }

    @Override
    public Encoding encoding() {
        return encoding;
    }

    @Override
    public int messageMaxBytes() {
        return messageMaxBytes;
    }

    /**
     * This sends all of the spans as a single message.
     *
     * <p>NOTE: this blocks until the metadata server is available.
     */
    @Override
    public zipkin2.Call<Void> sendSpans(List<byte[]> encodedSpans) {
        if (closeCalled) throw new IllegalStateException("closed");
        byte[] message = encoder.encode(encodedSpans);
        return new KafkaCall(message);
    }

    /**
     * Ensures there are no problems reading metadata about the topic.
     */
    @Override
    public CheckResult check() {
        try {
            return CheckResult.OK;
        } catch (RuntimeException e) {
            return CheckResult.failed(e);
        }
    }

    @Override
    public synchronized void close() {
        if (closeCalled) return;
        closeCalled = true;
    }


    class KafkaCall extends Call.Base<Void> { // KafkaFuture is not cancelable
        private final byte[] message;

        KafkaCall(byte[] message) {
            this.message = message;
        }

        @Override
        protected Void doExecute() throws IOException {
            AwaitableCallback callback = new AwaitableCallback();
            publish(callback);
            callback.await();
            return null;
        }

        @Override
        protected void doEnqueue(Callback<Void> callback) {
            publish(callback);
        }

        @Override
        public Call<Void> clone() {
            return new KafkaCall(message);
        }


        private void publish(Callback<Void> callback) {
            try {
                rocketMQSender.send(topic, message, callback);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private DefaultMQProducer createMQProducer(RocketMQProperties rocketMQProperties) throws MQClientException {
        RocketMQProperties.Producer producerConfig = rocketMQProperties.getProducer();
        String groupName = producerConfig.getGroup();
        Assert.hasText(groupName, "[spring.rocketmq.producer.group] must not be null");

        DefaultMQProducer producer = new DefaultMQProducer(producerConfig.getGroup());
        producer.setNamesrvAddr(rocketMQProperties.getNameServer());
        producer.setSendMsgTimeout(producerConfig.getSendMsgTimeout());
        producer.setRetryTimesWhenSendFailed(producerConfig.getRetryTimesWhenSendFailed());
        producer.setRetryTimesWhenSendAsyncFailed(producerConfig.getRetryTimesWhenSendAsyncFailed());
        producer.setMaxMessageSize(producerConfig.getMaxMessageSize());
        producer.setCompressMsgBodyOverHowmuch(producerConfig.getCompressMsgBodyOverHowmuch());
        producer.setRetryAnotherBrokerWhenNotStoreOK(producerConfig.isRetryAnotherBrokerWhenNotStoreOk());
        producer.start();
        return producer;
    }

    public interface RocketMQSender {
        void send(String topic, byte[] message, Callback<Void> callback) throws Exception;
    }

    public class SendCallbackEx implements SendCallback {
        private Callback<Void> callback;

        public SendCallbackEx(Callback<Void> callback) {
            this.callback = callback;
        }

        @Override
        public void onSuccess(SendResult sendResult) {
            System.out.println(sendResult);
            callback.onSuccess(null);
        }

        @Override
        public void onException(Throwable e) {
            log.error(e.getMessage(), e);
            callback.onError(e);
        }
    }


}
