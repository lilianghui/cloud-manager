/**
 * Superid.menkor.com Inc.
 * Copyright (c) 2012-2018 All Rights Reserved.
 */
package org.springframework.cloud.stream.binder.rocket.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageListener;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.messaging.Message;

/**
 *
 * @author libing
 * @version $Id: RocketInboundChannelAdapter.java, v 0.1 2018年10月27日 下午4:05 zt Exp $
 */
public class RocketInboundChannelAdapter extends MessageProducerSupport {
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private SimpleRocketMessageListenerContainer listenerContainer;

    public RocketInboundChannelAdapter(SimpleRocketMessageListenerContainer container) {
        this.listenerContainer = container;
    }

    @Override
    protected void onInit() {
        super.onInit();
        Listener messageListener = new ListenerImpl();
        this.listenerContainer.setMessageListener(messageListener);
    }

    @Override
    protected void doStart() {
        listenerContainer.start();
    }

    @Override
    protected void doStop() {
    }

    public class ListenerImpl implements Listener, MessageListener {
        public void onMessage(Message message) throws Exception {
            try {
                this.createAndSend(message);
            } catch (RuntimeException e) {
            }
        }

        @Override
        public void onMessage(org.springframework.amqp.core.Message message) {

        }

        private void createAndSend(Message message) {
            RocketInboundChannelAdapter.this.sendMessage(message);
        }
    }
}