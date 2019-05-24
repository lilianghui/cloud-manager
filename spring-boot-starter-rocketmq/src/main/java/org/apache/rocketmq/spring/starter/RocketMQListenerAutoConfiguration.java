package org.apache.rocketmq.spring.starter;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.impl.MQClientAPIImpl;
import org.apache.rocketmq.spring.starter.config.TransactionHandlerRegistry;
import org.apache.rocketmq.spring.starter.core.RocketMQTemplate;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.Order;

//@Configuration
//@ConditionalOnClass(MQClientAPIImpl.class)
public class RocketMQListenerAutoConfiguration {

//    @Bean
//    @ConditionalOnBean(RocketMQTemplate.class)
//    @ConditionalOnMissingBean(TransactionHandlerRegistry.class)
//    @Order(3)
//    public TransactionHandlerRegistry transactionHandlerRegistry() {
//        return new TransactionHandlerRegistry();
//    }
//
//
//    @SuppressWarnings("rawtypes")
//    @Bean(name = RocketMQConfigUtils.ROCKETMQ_TRANSACTION_ANNOTATION_PROCESSOR_BEAN_NAME)
//    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
//    public RocketMQTransactionAnnotationProcessor rocketMQTransactionAnnotationProcessor() {
//        return new RocketMQTransactionAnnotationProcessor();
//    }

}
