package com.lilianghui.shiro.spring.starter.config;

import com.lilianghui.shiro.spring.starter.core.AbstractChainDefinitionSectionMetaSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.pam.AllSuccessfulStrategy;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.realm.Realm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import javax.servlet.Filter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.lilianghui.shiro.spring.starter.config.ShiroProperties.AuthenticationStrategyMode.AT_LEAST_ONE_SUCCESSFUL_STRATEGY;

@Data
@RefreshScope
@ConfigurationProperties(prefix = ShiroProperties.PREFIX)
public class ShiroProperties {
    public static final String PREFIX = "spring.shiro";

    private String loginUrl;
    private String successUrl;
    private String unauthorizedUrl;
    private String cacheManagerConfigFile;
    private long globalSessionTimeout = 7200000;
    private long sessionValidationInterval = 1800000;
    private String activeSessionsCacheName;
    private String cookieName = "shiroCookie";
    private String cipherKey = "4AvVhmFLUs0KTA3Kprsdag==";
    private String rememberMeCookieName = "rememberMeCookie";
    private int rememberMeMaxAge = 2592000;//30天
    private List<RealmProperties> realms = new ArrayList<>();
    private AuthenticationStrategyMode authenticationStrategyMode = AT_LEAST_ONE_SUCCESSFUL_STRATEGY;
    private RetryLimitHashedCredentialsMatcherProperties credentialsMatcher = new RetryLimitHashedCredentialsMatcherProperties();
    private LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
    private Class<? extends AbstractChainDefinitionSectionMetaSource> filterChainDefinitionClass = AbstractChainDefinitionSectionMetaSource.DefaultChainDefinitionSectionMetaSource.class;

    private Map<String, Class<? extends Filter>> filters = new HashMap<>();
    private Map<String, String> redisCache = new HashMap<>();
    private KickoutProperties kickout = new KickoutProperties();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RealmProperties {
        private Class<? extends Realm> realmClass;
        private boolean useDefaultMatcher = false;
        private boolean authenticationCachingEnabled = true;
        private String authenticationCacheName = "authenticationCache";
        private Class<? extends AuthenticationToken> authenticationTokenClass = UsernamePasswordToken.class;
        private RetryLimitHashedCredentialsMatcherProperties credentialsMatcher = new RetryLimitHashedCredentialsMatcherProperties();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KickoutProperties {
        private String kickoutUrl; // 踢出后到的地址
        private String cacheName = "kickoutCache"; //缓存名称
        private boolean kickoutAfter = false; // 踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
        private int maxSession = 1; // 同一个帐号最大会话数 默认1
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RedisCacheProperties {
//        public static final RedisCacheProperties DEFALUT_CONFIG = new RedisCacheProperties(10, TimeUnit.MINUTES);

        private long timeout;
        private TimeUnit timeUnit = TimeUnit.SECONDS;

        public static RedisCacheProperties resolveTimeout(String value) {
            // y/Y
            //  M
            // d/D
            // h/H
            // m
            // s/S
            // mi/MI
            RedisCacheProperties redisCacheProperties = new RedisCacheProperties();
            String lowerVal = value.toLowerCase();
            if (lowerVal.endsWith("y")) {//年
                redisCacheProperties.setTimeUnit(TimeUnit.DAYS);
                redisCacheProperties.setTimeout(Long.valueOf(value.substring(0, value.length() - 1)) * 365);
            } else if (value.endsWith("M")) {//月
                redisCacheProperties.setTimeUnit(TimeUnit.DAYS);
                redisCacheProperties.setTimeout(Long.valueOf(value.substring(0, value.length() - 1)) * 30);
            } else if (lowerVal.endsWith("d")) {//日
                redisCacheProperties.setTimeUnit(TimeUnit.DAYS);
                redisCacheProperties.setTimeout(Long.valueOf(value.substring(0, value.length() - 1)));
            } else if (lowerVal.endsWith("h")) {//时
                redisCacheProperties.setTimeUnit(TimeUnit.HOURS);
                redisCacheProperties.setTimeout(Long.valueOf(value.substring(0, value.length() - 1)));
            } else if (value.endsWith("m")) {//分
                redisCacheProperties.setTimeUnit(TimeUnit.MINUTES);
                redisCacheProperties.setTimeout(Long.valueOf(value.substring(0, value.length() - 1)));
            } else if (lowerVal.endsWith("s")) {//秒
                redisCacheProperties.setTimeUnit(TimeUnit.SECONDS);
                redisCacheProperties.setTimeout(Long.valueOf(value.substring(0, value.length() - 1)));
            } else if (lowerVal.endsWith("mi")) {//毫秒
                redisCacheProperties.setTimeUnit(TimeUnit.MILLISECONDS);
                redisCacheProperties.setTimeout(Long.valueOf(value.substring(0, value.length() - 2)));
            } else {
                throw new RuntimeException("can not resolve the time express: " + value);
            }
            return redisCacheProperties;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RetryLimitHashedCredentialsMatcherProperties {
        private String cacheName = "retryLimitCache";
        private int total = 5;
        private boolean enable = true;
        private String hashAlgorithm = "MD5";
        private int hashIterations = 1;
        private boolean hashSalted = false;
        private boolean storedCredentialsHexEncoded = true;
        private Class<? extends CredentialsMatcher> credentialsMatcherClass = null;
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