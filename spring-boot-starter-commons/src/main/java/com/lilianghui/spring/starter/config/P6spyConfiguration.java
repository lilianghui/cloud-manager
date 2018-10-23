package com.lilianghui.spring.starter.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = P6spyConfiguration.PREFIX)
public class P6spyConfiguration {
    public static final String PREFIX = "spring.p6spy";

}
