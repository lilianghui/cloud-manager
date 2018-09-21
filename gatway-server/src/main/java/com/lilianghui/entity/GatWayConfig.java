package com.lilianghui.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = GatWayConfig.PREFIX)
@RefreshScope
@Data
@Builder
@Component
public class GatWayConfig {
    public static final String PREFIX = "gatway";

    private String name;
    private int age;

}
