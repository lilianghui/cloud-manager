package com.lilianghui.shiro.spring.starter.config;

import com.lilianghui.shiro.spring.starter.core.AbstractChainDefinitionSectionMetaSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.pam.AllSuccessfulStrategy;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.realm.Realm;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.lilianghui.shiro.spring.starter.config.ShiroProperties.AuthenticationStrategyMode.AT_LEAST_ONE_SUCCESSFUL_STRATEGY;

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
    private AuthenticationStrategyMode authenticationStrategyMode = AT_LEAST_ONE_SUCCESSFUL_STRATEGY;
    private String cipherKey = "4AvVhmFLUs0KTA3Kprsdag==";
    private String rememberMeCookieName;
    private Class<? extends Realm>[] realms;
    private int rememberMeMaxAge = 2592000;//30天
    private LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
    private Class<? extends AbstractChainDefinitionSectionMetaSource> filterChainDefinitionClass = AbstractChainDefinitionSectionMetaSource.DefaultChainDefinitionSectionMetaSource.class;

    private Map<String, Class<? extends Filter>> filters = new HashMap<>();
    private Map<String, RedisCacheProperties> redisCache = new HashMap<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RedisCacheProperties {
        public static final RedisCacheProperties DEFALUT_CONFIG = new RedisCacheProperties(10, TimeUnit.MINUTES);

        private long timeout;
        private TimeUnit timeUnit = TimeUnit.SECONDS;
    }

    public static enum AuthenticationStrategyMode {

        ALL_SUCCESSFUL_STRATEGY {
            @Override
            public AuthenticationStrategy instance() {
                return new AllSuccessfulStrategy();
            }
        }, AT_LEAST_ONE_SUCCESSFUL_STRATEGY {
            @Override
            public AuthenticationStrategy instance() {
                return new AtLeastOneSuccessfulStrategy();
            }
        }, FIRST_SUCCESSFUL_STRATEGY {
            @Override
            public AuthenticationStrategy instance() {
                return new FirstSuccessfulStrategy();
            }
        };

        public abstract AuthenticationStrategy instance();

    }
}