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

package org.apache.rocketmq.spring.starter.config;

import io.netty.util.internal.ConcurrentSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.spring.starter.RocketMQConfigUtils;
import org.apache.rocketmq.spring.starter.RocketMQProperties;
import org.apache.rocketmq.spring.starter.core.ProducerBeanFactory;
import org.apache.rocketmq.spring.starter.core.RocketMQTemplate;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutorService;

@Slf4j
public class TransactionHandlerRegistry implements DisposableBean {
//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private ProducerBeanFactory producerBeanFactory;

    @Resource
    private RocketMQProperties rocketMQProperties;


    private final Set<String> listenerContainers = new ConcurrentSet<>();

    public TransactionHandlerRegistry() {
    }

    public Collection<String> getAllTrans() {
        return Collections.unmodifiableSet(listenerContainers);
    }

    @Override
    public void destroy() throws Exception {
        listenerContainers.clear();
    }

    public void registerTransactionHandler(TransactionHandler handler) throws MQClientException {
        if (listenerContainers.contains(handler.getName())) {
            throw new MQClientException(-1,
                String.format("The transaction name [%s] has been defined in TransactionListener [%s]", handler.getName(),
                    handler.getBeanName()));
        }
        listenerContainers.add(handler.getName());
        TransactionMQProducer transactionMQProducer = createAndStartTransactionMQProducer(handler.getName(), handler.getListener(), handler.getCheckExecutor());
        if(handler.getBeanFactory() instanceof DefaultListableBeanFactory){
            ((DefaultListableBeanFactory) handler.getBeanFactory()).registerSingleton(handler.getName(),transactionMQProducer);
        }
    }
    private String getTxProducerGroupName(String name) {
        return  name == null ? RocketMQConfigUtils.ROCKETMQ_TRANSACTION_DEFAULT_GLOBAL_NAME : name;
    }

    public TransactionMQProducer createAndStartTransactionMQProducer(String txProducerGroup, TransactionListener transactionListener,
                                                                     ExecutorService executorService) throws MQClientException {
        txProducerGroup = getTxProducerGroupName(txProducerGroup);
        if (RocketMQTemplate.cache().containsKey(txProducerGroup)) {
            log.info(String.format("get TransactionMQProducer '%s' from cache", txProducerGroup));
            return null;
        }

        TransactionMQProducer txProducer = createTransactionMQProducer(txProducerGroup, transactionListener, executorService);
        txProducer.start();
        RocketMQTemplate.cache().put(txProducerGroup, txProducer);

        return txProducer;
    }
    private TransactionMQProducer createTransactionMQProducer(String name, TransactionListener transactionListener,
                                                              ExecutorService executorService) {
        RocketMQProperties.Producer producer = rocketMQProperties.getProducer();
        Assert.notNull(producer, "Property 'producer' is required");
        Assert.notNull(transactionListener, "Parameter 'transactionListener' is required");
        TransactionMQProducer txProducer = new TransactionMQProducer(name);
        txProducer.setTransactionListener(transactionListener);

        txProducer.setNamesrvAddr(rocketMQProperties.getNameServer());
        if (executorService != null) {
            txProducer.setExecutorService(executorService);
        }

        txProducer.setSendMsgTimeout(producer.getSendMsgTimeout());
        txProducer.setRetryTimesWhenSendFailed(producer.getRetryTimesWhenSendFailed());
        txProducer.setRetryTimesWhenSendAsyncFailed(producer.getRetryTimesWhenSendAsyncFailed());
        txProducer.setMaxMessageSize(producer.getMaxMessageSize());
        txProducer.setCompressMsgBodyOverHowmuch(producer.getCompressMsgBodyOverHowmuch());
        txProducer.setRetryAnotherBrokerWhenNotStoreOK(producer.isRetryAnotherBrokerWhenNotStoreOk());
        return producerBeanFactory.createTransactionMQProducer(txProducer);
    }
}
