package com.lilianghui;

import com.lilianghui.config.RocketConsumerProperties;
import com.lilianghui.config.RocketExtendedBindingProperties;
import com.lilianghui.config.RocketProducerProperties;
import com.lilianghui.endpoint.RocketInboundEndpoint;
import com.lilianghui.endpoint.RocketOutboundEndpoint;
import org.apache.rocketmq.spring.starter.core.RocketMQTemplate;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.cloud.stream.binder.AbstractMessageChannelBinder;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.binder.ExtendedPropertiesBinder;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.integration.core.MessageProducer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

public class RocketMessageChannelBinder extends AbstractMessageChannelBinder<ExtendedConsumerProperties<RocketConsumerProperties>, ExtendedProducerProperties<RocketProducerProperties>, RocketExchangeQueueProvisioner> implements ExtendedPropertiesBinder<MessageChannel, RocketConsumerProperties, RocketProducerProperties>, DisposableBean {


    private RocketExtendedBindingProperties extendedBindingProperties = new RocketExtendedBindingProperties();
    private RocketMQTemplate rocketMQTemplate;

    public RocketMessageChannelBinder(String[] headersToEmbed, RocketExchangeQueueProvisioner provisioningProvider, RocketMQTemplate rocketMQTemplate) {
        super(headersToEmbed, provisioningProvider);
        this.rocketMQTemplate = rocketMQTemplate;
    }

    @Override
    protected MessageHandler createProducerMessageHandler(ProducerDestination destination, ExtendedProducerProperties<RocketProducerProperties> producerProperties, MessageChannel errorChannel) throws Exception {
        RocketOutboundEndpoint endpoint = new RocketOutboundEndpoint(rocketMQTemplate,destination.getName());
        endpoint.setBeanFactory(this.getBeanFactory());
        return endpoint;
    }

    @Override
    protected MessageProducer createConsumerEndpoint(ConsumerDestination destination, String group, ExtendedConsumerProperties<RocketConsumerProperties> properties) throws Exception {
        RocketInboundEndpoint endpoint = new RocketInboundEndpoint(rocketMQTemplate,destination.getName());
        endpoint.setBeanFactory(this.getBeanFactory());
        return endpoint;
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public RocketConsumerProperties getExtendedConsumerProperties(String channelName) {
        return this.extendedBindingProperties.getExtendedConsumerProperties(channelName);
    }

    @Override
    public RocketProducerProperties getExtendedProducerProperties(String channelName) {
        return this.extendedBindingProperties.getExtendedProducerProperties(channelName);
    }
}