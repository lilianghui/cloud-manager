package com.lilianghui.spring.starter.cloud.sleuth.rocket.sender;

import org.apache.rocketmq.spring.starter.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin2.reporter.Sender;

@Configuration
//@ConditionalOnBean(RocketMQTemplate.class)
@ConditionalOnMissingBean(Sender.class)
@ConditionalOnProperty(value = "spring.zipkin.sender.type", havingValue = "rocket")
public class ZipkinRocketSenderConfiguration {
    @Value("${spring.zipkin.rocket.topic:zipkin}")
    private String topic;


    @Bean
    public Sender rocketSender(RocketMQTemplate rocketMQTemplate) {
        return new RocketSender(rocketMQTemplate, topic);
    }

}
