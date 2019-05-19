package com.lilianghui.spring.starter.gray.predicate;

import com.lilianghui.spring.starter.Interceptor.RequestContextHolder;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;

public class MetadataAwarePredicate extends DiscoveryEnabledPredicate {
    public final static String GRAY_HEADER_NAME = "host-mark";
    public final static String GRAY_HEADER_RUNNUNG = "running-host";
    public final static String GRAY_HEADER_GRAY = "gray-host";

    public MetadataAwarePredicate() {
    }

    protected boolean apply(DiscoveryEnabledServer server) {
        String value = Optional.ofNullable((String) RequestContextHolder.getHttpServletRequest().getAttribute(MetadataAwarePredicate.class.getSimpleName()))
                .orElse(RequestContextHolder.getHeader(GRAY_HEADER_NAME));
        Map<String, String> metadata = server.getInstanceInfo().getMetadata();
        return Optional.ofNullable(metadata.get(GRAY_HEADER_NAME))
                .filter(StringUtils::isNotBlank).orElse(GRAY_HEADER_RUNNUNG)
                .equals(Optional.ofNullable(value)
                        .filter(StringUtils::isNotBlank).orElse(GRAY_HEADER_RUNNUNG));
    }
}
