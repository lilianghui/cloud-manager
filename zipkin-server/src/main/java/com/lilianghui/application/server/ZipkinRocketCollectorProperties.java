package com.lilianghui.application.server;

import lombok.Data;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("zipkin.collector.rocket")
public class ZipkinRocketCollectorProperties {

    private String nameServer;

    private ConsumerProperties consumer = new ConsumerProperties();

    @Data
    public static class ConsumerProperties {
        private String consumerGroup;

        private String topic = "zipkin";

        private String tag = "*";

        private String charset = "UTF-8";

        int consumeThreadMax;

    }


}
