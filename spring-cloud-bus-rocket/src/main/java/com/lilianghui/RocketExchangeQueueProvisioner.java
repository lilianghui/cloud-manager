package com.lilianghui;

import com.lilianghui.config.RocketConsumerDestination;
import com.lilianghui.config.RocketConsumerProperties;
import com.lilianghui.config.RocketProducerDestination;
import com.lilianghui.config.RocketProducerProperties;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.cloud.stream.provisioning.ProvisioningException;
import org.springframework.cloud.stream.provisioning.ProvisioningProvider;

public class RocketExchangeQueueProvisioner implements ProvisioningProvider<ExtendedConsumerProperties<RocketConsumerProperties>, ExtendedProducerProperties<RocketProducerProperties>> {
    @Override
    public ProducerDestination provisionProducerDestination(String name, ExtendedProducerProperties<RocketProducerProperties> properties) throws ProvisioningException {
        return new RocketProducerDestination(name);
    }

    @Override
    public ConsumerDestination provisionConsumerDestination(String name, String group, ExtendedConsumerProperties<RocketConsumerProperties> properties) throws ProvisioningException {
        return new RocketConsumerDestination(name, group);
    }
}
