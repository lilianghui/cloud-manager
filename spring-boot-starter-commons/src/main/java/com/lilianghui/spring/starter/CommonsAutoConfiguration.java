package com.lilianghui.spring.starter;

import brave.Tracing;
import com.lilianghui.spring.starter.brave.rocket.SleuthRocketProducerAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CommonsAutoConfiguration {

    @Bean
    public SleuthRocketProducerAspect producerAspect(Tracing tracing) {
        return new SleuthRocketProducerAspect(tracing);
    }

}
