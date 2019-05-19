package com.lilianghui.application.server;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;
import zipkin2.collector.CollectorMetrics;
import zipkin2.collector.CollectorSampler;
import zipkin2.storage.StorageComponent;


@Configuration
@EnableConfigurationProperties(ZipkinRocketCollectorProperties.class)
@Conditional(ZipkinRocketCollectorAutoConfiguration.RocketBootstrapServersSet.class)
public class ZipkinRocketCollectorAutoConfiguration {


    @Bean(initMethod = "start")
    public RocketCollector rocket(
            ZipkinRocketCollectorProperties properties,
            CollectorSampler sampler,
            CollectorMetrics metrics,
            StorageComponent storage) {
        return RocketCollector.builder(properties).sampler(sampler).metrics(metrics).storage(storage).build();
    }

    static final class RocketBootstrapServersSet implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata a) {
            return !isEmpty(context.getEnvironment().getProperty("zipkin.collector.rocket.nameServer"));
        }

        private static boolean isEmpty(String s) {
            return s == null || s.isEmpty();
        }
    }
}
