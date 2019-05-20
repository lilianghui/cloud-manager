package org.apache.rocketmq.spring.starter.core;

import org.apache.rocketmq.client.producer.TransactionMQProducer;

public class ProducerBeanFactory {

    public TransactionMQProducer createTransactionMQProducer(TransactionMQProducer txProducer) {
        return txProducer;
    }
}
