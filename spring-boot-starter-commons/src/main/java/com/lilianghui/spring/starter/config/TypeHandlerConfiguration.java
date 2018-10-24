package com.lilianghui.spring.starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = TypeHandlerConfiguration.PREFIX)
public class TypeHandlerConfiguration {
    public static final String PREFIX = "mybatis";

    private String typeHandlerPackage;

}
