package com.lilianghui.application.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EurekaServerListener {
    private Logger logger = LoggerFactory.getLogger(EurekaServerListener.class);

    @EventListener
    public void listener(ApplicationEvent event) {
        if (event instanceof EurekaInstanceCanceledEvent) {
            logger.info("{}服务下线",((EurekaInstanceCanceledEvent) event).getServerId());
        } else if (event instanceof EurekaInstanceRegisteredEvent) {
            logger.info("{}服务上线",((EurekaInstanceRegisteredEvent) event).getInstanceInfo().getAppName());
        } else if (event instanceof EurekaInstanceRenewedEvent) {
            logger.info("{}服务续约",((EurekaInstanceRenewedEvent) event).getServerId());
        }
    }
}
