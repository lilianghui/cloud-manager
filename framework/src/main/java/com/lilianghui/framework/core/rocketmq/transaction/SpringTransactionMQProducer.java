package com.lilianghui.framework.core.rocketmq.transaction;

import com.google.common.collect.Maps;
import com.lilianghui.framework.core.entity.MergeEntity;
import com.lilianghui.framework.core.jackson.JacksonUtils;
import com.lilianghui.framework.core.rocketmq.entity.RocketMQConfig;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.UUID;

public class SpringTransactionMQProducer extends TransactionMQProducer implements DisposableBean, InitializingBean, TransactionListener {
    private final static Map<String, LocalTransactionState> LOCAL_TRANSACTION_STATE_MAP = Maps.newConcurrentMap();
    private final static Map<String, TransactionListener> ABSTRACT_TRANSACTION_LISTENER_MAP = Maps.newConcurrentMap();
    public final static String CLASS_NAME = "CLASS_NAME";

    private RocketMQConfig rocketMQConfig;

    public SpringTransactionMQProducer() {
        super();
        setTransactionListener(this);
    }

    public SpringTransactionMQProducer(String producerGroup) {
        super(producerGroup);
        setTransactionListener(this);
    }


    public TransactionSendResult sendMessageInTransaction(final MergeEntity mergeEntity, Object arg,
                                                          TransactionListener listener) throws Exception {
        String keys = UUID.randomUUID().toString();
        mergeEntity.setIdentity(keys);
        Message msg = new Message(getRocketMQConfig().getTopic(),
                getRocketMQConfig().getTag(), keys, JacksonUtils.writeValue(mergeEntity).getBytes(RemotingHelper.DEFAULT_CHARSET));
        String className = mergeEntity.getMainEntity().getClass().getName() + ","
                + mergeEntity.getInsertEntity().iterator().next().getClass().getName() + "," + mergeEntity.getExtra().getClass().getName();
        msg.putUserProperty(CLASS_NAME, className);
        putListener(msg.getKeys(), listener);
        TransactionSendResult transactionSendResult = super.sendMessageInTransaction(msg, arg);
        return transactionSendResult;
    }

    public TransactionSendResult sendMessageInTransaction(final Message msg, Object arg,
                                                          TransactionListener listener) throws Exception {
        putListener(msg.getKeys(), listener);
        TransactionSendResult transactionSendResult = super.sendMessageInTransaction(msg, arg);
        return transactionSendResult;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.start();
    }

    @Override
    public void destroy() throws Exception {
        this.shutdown();
    }

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        LocalTransactionState value = LocalTransactionState.ROLLBACK_MESSAGE;
        try {
            value = getListener(msg.getKeys()).executeLocalTransaction(msg, arg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOCAL_TRANSACTION_STATE_MAP.put(msg.getTransactionId(), value);
//        if (value == LocalTransactionState.COMMIT_MESSAGE) {
//            removeListener(msg.getTransactionId());
//        }
        return value;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        LocalTransactionState value = LOCAL_TRANSACTION_STATE_MAP.get(msg.getTransactionId());
        if (null == value) {
            value = getListener(msg.getKeys()).checkLocalTransaction(msg);
        }
        if (value == LocalTransactionState.COMMIT_MESSAGE) {
            removeListener(msg.getKeys());
        }
        return value;
    }


    private TransactionListener getListener(String transactionId) {
        TransactionListener listener = ABSTRACT_TRANSACTION_LISTENER_MAP.get(transactionId);
        if (listener == null) {
            throw new RuntimeException();
        }
        return listener;
    }

    private void putListener(String transactionId, TransactionListener listener) {
        ABSTRACT_TRANSACTION_LISTENER_MAP.put(transactionId, listener);
    }

    private void removeListener(String transactionId) {
        ABSTRACT_TRANSACTION_LISTENER_MAP.remove(transactionId);
    }

    public RocketMQConfig getRocketMQConfig() {
        return rocketMQConfig;
    }

    public void setRocketMQConfig(RocketMQConfig rocketMQConfig) {
        this.rocketMQConfig = rocketMQConfig;
    }
}
