package org.apache.rocketmq.spring.starter.core;

import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.Lifecycle;

public class DefaultProducerBeanFactory implements ProducerBeanFactory {

    @Override
    public TransactionMQProducer createTransactionMQProducer(TransactionMQProducer txProducer) {
        return txProducer;
    }
}
