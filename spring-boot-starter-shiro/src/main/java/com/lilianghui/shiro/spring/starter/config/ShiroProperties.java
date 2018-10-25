package com.lilianghui.shiro.spring.starter.config;

import com.lilianghui.shiro.spring.starter.core.AbstractChainDefinitionSectionMetaSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;

@ConfigurationProperties(prefix = ShiroProperties.PREFIX)
@Data
public class ShiroProperties {
    public static final String PREFIX = "spring.shiro";

    private String loginUrl;
    private String successUrl;
    private String unauthorizedUrl;
    private String cacheName;
    private String hashAlgorithmName;
    private String hashIterations;
    private String total;
    private String cacheManagerConfigFile;
    private long globalSessionTimeout;
    private long sessionValidationInterval;
    private String activeSessionsCacheName;
    private String cookieName;
    private String cipherKey = "4AvVhmFLUs0KTA3Kprsdag==";
    private String rememberMeCookieName;
    private Class[] realms;
    private int rememberMeMaxAge = 2592000;//30天
    private boolean enableRedisCache = false;
    private LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
    private Class<? extends AbstractChainDefinitionSectionMetaSource> filterChainDefinitionClass = AbstractChainDefinitionSectionMetaSource.DefaultChainDefinitionSectionMetaSource.class;

}