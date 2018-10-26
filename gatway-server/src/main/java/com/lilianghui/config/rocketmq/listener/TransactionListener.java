package com.lilianghui.config.rocketmq.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.starter.annotation.RocketMQTransactionListener;

import static com.lilianghui.service.ContractService.TX_PRODUCER_GROUP;

@Slf4j
@RocketMQTransactionListener(txProducerGroup = TX_PRODUCER_GROUP)
public class TransactionListener implements org.apache.rocketmq.client.producer.TransactionListener {

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        try {

        } catch (Exception e) {
            log.error(e.getMessage(),e);
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
