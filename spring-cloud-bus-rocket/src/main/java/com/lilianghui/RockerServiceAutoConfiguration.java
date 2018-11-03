package com.lilianghui;

import org.apache.rocketmq.spring.starter.RocketMQProperties;
import org.apache.rocketmq.spring.starter.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.stream.binder.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@Configuration
@ConditionalOnMissingBean({Binder.class})
@ConditionalOnBean(RocketMQTemplate.class)
public class RockerServiceAutoConfiguration {
    @Resource
    private Environment environment;

    @Bean
    public RocketMessageChannelBinder rabbitMessageChannelBinder(@Autowired RocketMQTemplate rocketMQTemplate
            , @Autowired RocketMQProperties rocketMQProperties) throws Exception {
        String applicationName = environment.getProperty("spring.application.name");
        RocketMessageChannelBinder binder = new RocketMessageChannelBinder(new String[0],applicationName, provisioningProvider(), rocketMQTemplate, rocketMQProperties);
        return binder;
    }

    @Bean
    public RocketExchangeQueueProvisioner provisioningProvider() {
        return new RocketExchangeQueueProvisioner();
    }

}
