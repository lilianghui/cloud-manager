package com.lilianghui.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.stream.binder.ExtendedBindingProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("spring.cloud.stream.rocket")
public class RocketExtendedBindingProperties implements ExtendedBindingProperties<RocketConsumerProperties, RocketProducerProperties> {

    private Map<String, RocketBindingProperties> bindings = new HashMap<>();

    public Map<String, RocketBindingProperties> getBindings() {
        return bindings;
    }

    public void setBindings(Map<String, RocketBindingProperties> bindings) {
        this.bindings = bindings;
    }

    @Override
    public synchronized RocketConsumerProperties getExtendedConsumerProperties(String channelName) {
        RocketConsumerProperties properties;
        if (bindings.containsKey(channelName)) {
            if (bindings.get(channelName).getConsumer() != null) {
                properties = bindings.get(channelName).getConsumer();
            } else {
                properties = new RocketConsumerProperties();
                this.bindings.get(channelName).setConsumer(properties);
            }
        } else {
            properties = new RocketConsumerProperties();
            RocketBindingProperties rbp = new RocketBindingProperties();
            rbp.setConsumer(properties);
            bindings.put(channelName, rbp);
        }
        return properties;
    }

    @Override
    public synchronized RocketProducerProperties getExtendedProducerProperties(String channelName) {
        RocketProducerProperties properties;
        if (bindings.containsKey(channelName)) {
            if (bindings.get(channelName).getProducer() != null) {
                properties = bindings.get(channelName).getProducer();
            } else {
                properties = new RocketProducerProperties();
                this.bindings.get(channelName).setProducer(properties);
            }
        } else {
            properties = new RocketProducerProperties();
            RocketBindingProperties rbp = new RocketBindingProperties();
            rbp.setProducer(properties);
            bindings.put(channelName, rbp);
        }
        return properties;
    }
}
