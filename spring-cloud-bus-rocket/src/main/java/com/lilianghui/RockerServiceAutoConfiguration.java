package com.lilianghui;

import org.apache.rocketmq.spring.starter.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.stream.binder.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean({Binder.class})
@ConditionalOnBean(RocketMQTemplate.class)
public class RockerServiceAutoConfiguration {

    @Bean
    public RocketMessageChannelBinder rabbitMessageChannelBinder(@Autowired RocketMQTemplate rocketMQTemplate) throws Exception {
        RocketMessageChannelBinder binder = new RocketMessageChannelBinder(new String[0], provisioningProvider(), rocketMQTemplate);
        return binder;
    }

    @Bean
    public RocketExchangeQueueProvisioner provisioningProvider() {
        return new RocketExchangeQueueProvisioner();
    }

}
