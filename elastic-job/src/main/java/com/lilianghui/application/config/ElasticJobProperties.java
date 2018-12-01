package com.lilianghui.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = ElasticJobProperties.PREFIX)
public class ElasticJobProperties {
    public static final String PREFIX = "elasticjob";

    private ZookeeperProperties zookeeper = new ZookeeperProperties();

    @Data
    public static class ZookeeperProperties {
        private String servers;
        private String namespace;
    }
}
