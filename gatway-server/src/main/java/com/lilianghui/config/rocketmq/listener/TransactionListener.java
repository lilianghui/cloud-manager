package com.lilianghui.config.rocketmq.listener;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.starter.annotation.RocketMQTransactionListener;

import static com.lilianghui.service.ContractService.TX_PRODUCER_GROUP;

@RocketMQTransactionListener(txProducerGroup = TX_PRODUCER_GROUP)
public class TransactionListener implements org.apache.rocketmq.client.producer.TransactionListener {

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        System.out.println(messageExt);
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
