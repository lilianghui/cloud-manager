package com.lilianghui.config;

import org.springframework.cloud.stream.provisioning.ConsumerDestination;

public class RocketConsumerDestination implements ConsumerDestination {
    private String destination;
    private String group;

    public RocketConsumerDestination() {
    }

    public RocketConsumerDestination(String destination, String group) {
        this.destination = destination;
        this.group = group;
    }

    @Override
    public String getName() {
        return destination;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
