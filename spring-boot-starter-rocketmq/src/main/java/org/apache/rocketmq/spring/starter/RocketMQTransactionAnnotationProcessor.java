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

package org.apache.rocketmq.spring.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.spring.starter.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.starter.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.starter.config.TransactionHandler;
import org.apache.rocketmq.spring.starter.config.TransactionHandlerRegistry;
import org.apache.rocketmq.spring.starter.core.DefaultRocketMQListenerContainer;
import org.apache.rocketmq.spring.starter.core.RocketMQListener;
import org.apache.rocketmq.spring.starter.enums.ConsumeMode;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.StandardEnvironment;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.apache.rocketmq.spring.starter.core.DefaultRocketMQListenerContainerConstants.*;


@Slf4j
public class RocketMQTransactionAnnotationProcessor
        implements BeanPostProcessor, Ordered, BeanFactoryAware, SmartInitializingSingleton {
    private BeanFactory beanFactory;
    private BeanExpressionResolver resolver = new StandardBeanExpressionResolver();
    private final Set<Class<?>> nonAnnotatedClasses =
            Collections.newSetFromMap(new ConcurrentHashMap<Class<?>, Boolean>(64));

    private final Set<Class<?>> nonRocketAnnotatedClasses =
            Collections.newSetFromMap(new ConcurrentHashMap<Class<?>, Boolean>(64));

    @Autowired(required = false)
    private TransactionHandlerRegistry transactionHandlerRegistry;

    private AtomicLong counter = new AtomicLong(0);

    @Resource
    private StandardEnvironment environment;

    @Resource
    private RocketMQProperties rocketMQProperties;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        if (beanFactory instanceof ConfigurableListableBeanFactory) {
            this.resolver = ((ConfigurableListableBeanFactory) beanFactory).getBeanExpressionResolver();
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!this.nonAnnotatedClasses.contains(bean.getClass())) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            RocketMQTransactionListener listener = AnnotationUtils.findAnnotation(targetClass, RocketMQTransactionListener.class);
            this.nonAnnotatedClasses.add(bean.getClass());
            if (listener == null) { // for quick search
                log.trace("No @RocketMQTransactionListener annotations found on bean type: {}", bean.getClass());
            } else {
                try {
                    processTransactionListenerAnnotation(listener, bean, beanName);
                } catch (MQClientException e) {
                    log.error("failed to process annotation " + listener, e);
                    throw new BeanCreationException("failed to process annotation " + listener, e);
                }
            }
        }

        if (!this.nonRocketAnnotatedClasses.contains(bean.getClass())) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            RocketMQMessageListener listener = AnnotationUtils.findAnnotation(targetClass, RocketMQMessageListener.class);
            this.nonRocketAnnotatedClasses.add(bean.getClass());
            if (listener == null) { // for quick search
                log.trace("No @RocketMQTransactionListener annotations found on bean type: {}", bean.getClass());
            } else {
                try {
                    processListenerAnnotation(listener, bean, beanName);
                } catch (MQClientException e) {
                    log.error("failed to process annotation " + listener, e);
                    throw new BeanCreationException("failed to process annotation " + listener, e);
                }
            }
        }

        return bean;
    }

    private void processListenerAnnotation(RocketMQMessageListener annotation, Object bean, String beanName) throws MQClientException {
        RocketMQListener rocketMQListener = (RocketMQListener) bean;

        validate(annotation);
        BeanDefinitionBuilder beanBuilder = BeanDefinitionBuilder.rootBeanDefinition(DefaultRocketMQListenerContainer.class);
        beanBuilder.addPropertyValue(PROP_NAMESERVER, rocketMQProperties.getNameServer());
        beanBuilder.addPropertyValue(PROP_TOPIC, environment.resolvePlaceholders(annotation.topic()));

        beanBuilder.addPropertyValue(PROP_CONSUMER_GROUP, environment.resolvePlaceholders(annotation.consumerGroup()));
        beanBuilder.addPropertyValue(PROP_CONSUME_MODE, annotation.consumeMode());
        beanBuilder.addPropertyValue(PROP_CONSUME_THREAD_MAX, annotation.consumeThreadMax());
        beanBuilder.addPropertyValue(PROP_MESSAGE_MODEL, annotation.messageModel());
        beanBuilder.addPropertyValue(PROP_SELECTOR_EXPRESS, environment.resolvePlaceholders(annotation.selectorExpress()));
        beanBuilder.addPropertyValue(PROP_SELECTOR_TYPE, annotation.selectorType());
        beanBuilder.addPropertyValue(PROP_ROCKETMQ_LISTENER, rocketMQListener);
        beanBuilder.addPropertyValue("beanFactory", beanFactory);
        if (Objects.nonNull(objectMapper)) {
            beanBuilder.addPropertyValue(PROP_OBJECT_MAPPER, objectMapper);
        }
        beanBuilder.setDestroyMethodName(METHOD_DESTROY);

        String containerBeanName = String.format("%s_%s", DefaultRocketMQListenerContainer.class.getName(), counter.incrementAndGet());
        ((DefaultListableBeanFactory) beanFactory).registerBeanDefinition(containerBeanName, beanBuilder.getBeanDefinition());

        DefaultRocketMQListenerContainer container = beanFactory.getBean(containerBeanName, DefaultRocketMQListenerContainer.class);

        if (!container.isStarted()) {
            try {
                container.start();
            } catch (Exception e) {
                log.error("started container failed. {}", container, e);
                throw new RuntimeException(e);
            }
        }

        log.info("register rocketMQ listener to container, listenerBeanName:{}, containerBeanName:{}", beanName, containerBeanName);

    }

    private void validate(RocketMQMessageListener annotation) {
        if (annotation.consumeMode() == ConsumeMode.ORDERLY &&
                annotation.messageModel() == MessageModel.BROADCASTING)
            throw new BeanDefinitionValidationException("Bad annotation definition in @RocketMQMessageListener, messageModel BROADCASTING does not support ORDERLY message!");
    }

    private void processTransactionListenerAnnotation(RocketMQTransactionListener anno, Object bean, String beanName) throws MQClientException {
        if (transactionHandlerRegistry == null) {
            throw new MQClientException("Bad usage of @RocketMQTransactionListener, the class must work with producer rocketMQTemplate", null);
        }
        if (!TransactionListener.class.isAssignableFrom(bean.getClass())) {
            throw new MQClientException("Bad usage of @RocketMQTransactionListener, the class must implements interface org.apache.rocketmq.client.producer.TransactionListener", null);
        }
        TransactionHandler transactionHandler = new TransactionHandler();
        transactionHandler.setBeanFactory(this.beanFactory);
        transactionHandler.setName(anno.txProducerGroup());
        transactionHandler.setBeanName(bean.getClass().getName());
        transactionHandler.setListener((TransactionListener) bean);
        transactionHandler.setCheckExecutor(anno.corePoolSize(), anno.maximumPoolSize(),
                anno.keepAliveTime(), anno.blockingQueueSize());

        transactionHandlerRegistry.registerTransactionHandler(transactionHandler);
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    @Override
    public void afterSingletonsInstantiated() {
        // Do nothing
    }
}
