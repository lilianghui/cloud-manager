package com.lilianghui.spring.starter.feign;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfiguration {
    @Bean
    public Logger.Level logger() {
        return Logger.Level.FULL;
    }
}
