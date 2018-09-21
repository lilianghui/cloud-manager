package com.lilianghui.config;

import com.google.common.collect.Maps;
import com.lilianghui.framework.core.shiro.interceptor.AuthcFilter;
import com.lilianghui.framework.core.shiro.interceptor.LogoutFilter;
import com.lilianghui.framework.core.shiro.interceptor.PermissionsOrAuthorizationFilter;
import com.lilianghui.framework.core.shiro.interceptor.RolesOrAuthorizationFilter;
import com.lilianghui.framework.core.shiro.quartz.QuartzSessionValidationScheduler2;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@Configuration
public class ShiroConfig {

    @Resource
    private ShiroProperties shiroProperties;

    @Autowired(required = false)
    private RedisConnectionFactory redisConnectionFactory;

    //缓存管理
    @Bean
    public CacheManager cacheManager() {
        if (shiroProperties.isEnableRedisCache()) {
//            SpringShiroRedisCacheManager springShiroRedisCacheManager = new SpringShiroRedisCacheManager(RedisCacheManager.create(redisConnectionFactory));
//            return springShiroRedisCacheManager;
        }
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile(shiroProperties.getCacheManagerConfigFile());
        return ehCacheManager;
    }


    //会话管理器
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionIdCookieEnabled(true);
        defaultWebSessionManager.setSessionIdCookie(sessionIdCookie());
        defaultWebSessionManager.setGlobalSessionTimeout(shiroProperties.getGlobalSessionTimeout());
        defaultWebSessionManager.setDeleteInvalidSessions(true);
        defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);
//        defaultWebSessionManager.setSessionValidationScheduler(sessionValidationScheduler());
        defaultWebSessionManager.setSessionDAO(sessionDAO());
        return defaultWebSessionManager;
    }


    //session cookie策略
    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie sessionIdCookie = new SimpleCookie(shiroProperties.getCookieName());
        sessionIdCookie.setPath("/");
        sessionIdCookie.setMaxAge(-1);
        sessionIdCookie.setHttpOnly(true);
        return sessionIdCookie;
    }


    //rememberMeCookie cookie策略
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie sessionIdCookie = new SimpleCookie(shiroProperties.getRememberMeCookieName());
        sessionIdCookie.setPath("/");
        sessionIdCookie.setMaxAge(shiroProperties.getRememberMeMaxAge());
        sessionIdCookie.setHttpOnly(true);
        return sessionIdCookie;
    }

    //rememberMeCookie cookie策略
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCipherKey(Base64.decode(shiroProperties.getCipherKey()));
        return cookieRememberMeManager;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() throws Exception {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(cacheManager());
        securityManager.setRememberMeManager(rememberMeManager());
        securityManager.setSessionManager(sessionManager());
        List<Realm> realms = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(shiroProperties.getRealms())) {
            for (Class<?> ream : shiroProperties.getRealms()) {
                realms.add((Realm) ream.newInstance());
            }
        }
        securityManager.setRealms(realms);
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        securityManager.setAuthenticator(modularRealmAuthenticator);
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }


    @Bean
    public EnterpriseCacheSessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setActiveSessionsCacheName(shiroProperties.getActiveSessionsCacheName());
        sessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return sessionDAO;
    }


    @Bean
    public JavaUuidSessionIdGenerator sessionIdGenerator() {
        JavaUuidSessionIdGenerator sessionIdGenerator = new JavaUuidSessionIdGenerator();
        return sessionIdGenerator;
    }


    @Bean
    public QuartzSessionValidationScheduler2 sessionValidationScheduler() {
        QuartzSessionValidationScheduler2 sessionValidationScheduler = new QuartzSessionValidationScheduler2();
        sessionValidationScheduler.setSessionManager(sessionManager());
        sessionValidationScheduler.setSessionValidationInterval(shiroProperties.getSessionValidationInterval());
        return sessionValidationScheduler;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter() throws Exception {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setLoginUrl(shiroProperties.getLoginUrl());
        shiroFilterFactoryBean.setSuccessUrl(shiroProperties.getSuccessUrl());
        shiroFilterFactoryBean.setUnauthorizedUrl(shiroProperties.getUnauthorizedUrl());
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        Map<String, String> filterChainDefinitionMap = Maps.newLinkedHashMap();
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        Map<String, Filter> filters = Maps.newLinkedHashMap();
        filters.put("orRoles", new RolesOrAuthorizationFilter());
        filters.put("orPerms", new PermissionsOrAuthorizationFilter());
        filters.put("authc", new AuthcFilter());
        filters.put("logout", new LogoutFilter());
        shiroFilterFactoryBean.setFilters(filters);
        return shiroFilterFactoryBean;
    }


    /**
     * Shiro生命周期处理器
     *
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    /**
     * AuthorizationAttributeSourceAdvisor，shiro里实现的Advisor类，
     * 内部使用AopAllianceAnnotationsAuthorizingMethodInterceptor来拦截用以下注解的方法。
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() throws Exception {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    @ConfigurationProperties(prefix = ShiroProperties.PREFIX)
    @Data
    @Builder
    @Component
    static class ShiroProperties {
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

    }
}
