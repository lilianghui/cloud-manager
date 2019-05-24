package org.apache.rocketmq.spring.starter.core;

import org.apache.rocketmq.client.producer.TransactionMQProducer;

public interface ProducerBeanFactory {

    TransactionMQProducer createTransactionMQProducer(TransactionMQProducer txProducer);
}
