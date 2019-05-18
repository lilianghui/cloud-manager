package com.lilianghui.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

public class DefaultRibbonConfiguration {
    private static final Logger log = LoggerFactory.getLogger(DefaultRibbonConfiguration.class);

    private static final String LOCAL_HOST = getIP();

    @Bean
    @ConditionalOnProperty(name = "zuul.ratelimit.enabled", havingValue = "false", matchIfMissing = true)
    public IRule ribbonRule() {
        return StringUtils.isNotBlank(LOCAL_HOST) ? new LocalServerBalancerRule() : new ZoneAvoidanceRule();
    }

    public static class LocalServerBalancerRule extends ZoneAvoidanceRule {


        @Override
        public Server choose(Object o) {
            List<Server> serverList = getLoadBalancer().getAllServers();
            Server chosen = null;
            for (Server server : serverList) {
                if (server instanceof DiscoveryEnabledServer && ((DiscoveryEnabledServer) server).getInstanceInfo().getAppName().equalsIgnoreCase(String.valueOf(o))
                        && LOCAL_HOST.equals(server.getHost())) {
                    chosen = server;
                    break;
                }
            }
            if (chosen == null) {
                return super.choose(o);
            } else {
                return chosen;
            }
        }
    }

    public static String getIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
