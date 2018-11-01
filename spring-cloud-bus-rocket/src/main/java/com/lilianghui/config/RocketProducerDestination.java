package com.lilianghui.config;

import org.springframework.cloud.stream.provisioning.ProducerDestination;

public class RocketProducerDestination implements ProducerDestination {

    private String destination;

    public RocketProducerDestination() {
    }

    public RocketProducerDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String getName() {
        return destination;
    }

    @Override
    public String getNameForPartition(int partition) {
        return destination;
    }
}
