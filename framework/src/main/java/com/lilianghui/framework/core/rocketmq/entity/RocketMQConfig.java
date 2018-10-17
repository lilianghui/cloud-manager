package com.lilianghui.framework.core.rocketmq.entity;

import com.google.common.collect.Maps;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString(callSuper = true)
@ConfigurationProperties(prefix = RocketMQConfig.PREFIX)
public class RocketMQConfig {
    public static final String PREFIX = "rocketmq";

    private String group;
    private String namesrvAddr;
    private String instanceName;
    private String topic;
    private String tag;
    private Map<Class, String> entityBeanNameMap = Maps.newHashMap();
}
