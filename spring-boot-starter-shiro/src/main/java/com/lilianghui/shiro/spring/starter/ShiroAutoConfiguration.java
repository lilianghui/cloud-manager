package com.lilianghui.shiro.spring.starter;


import com.lilianghui.shiro.spring.starter.config.QuartzSessionValidationScheduler2;
import com.lilianghui.shiro.spring.starter.config.ShiroProperties;
import com.lilianghui.shiro.spring.starter.config.SpringShiroRedisCacheManager;
import com.lilianghui.shiro.spring.starter.core.AbstractChainDefinitionSectionMetaSource;
import com.lilianghui.shiro.spring.starter.interceptor.AuthcFilter;
import com.lilianghui.shiro.spring.starter.interceptor.LogoutFilter;
import com.lilianghui.shiro.spring.starter.interceptor.PermissionsOrAuthorizationFilter;
import com.lilianghui.shiro.spring.starter.interceptor.RolesOrAuthorizationFilter;
import com.lilianghui.shiro.spring.starter.utils.BeanFactoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.pam.*;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.servlet.*;
import java.util.*;

@Order
@Slf4j
@Configuration
@EnableConfigurationProperties(ShiroProperties.class)
@ConditionalOnClass(DefaultWebSecurityManager.class)
@ConditionalOnProperty(prefix = ShiroProperties.PREFIX, value = {"loginUrl", "successUrl"})
public class ShiroAutoConfiguration {

    @Resource
    private ShiroProperties shiroProperties;
    @Resource
    private ApplicationContext applicationContext;


    public ShiroAutoConfiguration() {
    }

    //缓存管理
    @Bean
    @Order(1)
//    @ConditionalOnProperty(prefix = ShiroProperties.PREFIX, value = "enableRedisCache", havingValue = "true")
//    @ConditionalOnBean(RedisTemplate.class)
    @ConditionalOnMissingBean
    public CacheManager cacheRedisManager(@Autowired RedisTemplate redisTemplate) {
        SpringShiroRedisCacheManager springShiroRedisCacheManager = new SpringShiroRedisCacheManager(redisTemplate, shiroProperties.getRedisCache());
        return springShiroRedisCacheManager;
    }


//    //缓存管理
//    @Bean
//    @ConditionalOnMissingBean
//    @Order(1)
////    @ConditionalOnProperty(prefix = ShiroProperties.PREFIX, value = "enableRedisCache", havingValue = "false")
//    public CacheManager cacheManager() {
//        EhCacheManager ehCacheManager = new EhCacheManager();
//        ehCacheManager.setCacheManagerConfigFile(shiroProperties.getCacheManagerConfigFile());
//        return ehCacheManager;
//    }


    //会话管理器
    @Bean
    @ConditionalOnMissingBean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionIdCookieEnabled(true);
        defaultWebSessionManager.setSessionIdCookie(sessionIdCookie());
        defaultWebSessionManager.setGlobalSessionTimeout(shiroProperties.getGlobalSessionTimeout());
        defaultWebSessionManager.setDeleteInvalidSessions(true);
        defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);
        defaultWebSessionManager.setSessionValidationScheduler(sessionValidationScheduler(defaultWebSessionManager));
        defaultWebSessionManager.setSessionDAO(sessionDAO());
        return defaultWebSessionManager;
    }


    //session cookie策略
    @Bean
    @ConditionalOnMissingBean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie sessionIdCookie = new SimpleCookie(shiroProperties.getCookieName());
        sessionIdCookie.setPath("/");
        sessionIdCookie.setMaxAge(-1);
        sessionIdCookie.setHttpOnly(true);
        return sessionIdCookie;
    }


    //rememberMeCookie cookie策略
    @Bean
    @ConditionalOnMissingBean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie sessionIdCookie = new SimpleCookie(shiroProperties.getRememberMeCookieName());
        sessionIdCookie.setPath("/");
        sessionIdCookie.setMaxAge(shiroProperties.getRememberMeMaxAge());
        sessionIdCookie.setHttpOnly(true);
        return sessionIdCookie;
    }

    //rememberMeCookie cookie策略
    @Bean
    @ConditionalOnMissingBean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCipherKey(Base64.decode(shiroProperties.getCipherKey()));
        return cookieRememberMeManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultWebSecurityManager securityManager(@Autowired CacheManager cacheManager) throws Exception {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(cacheManager);
        securityManager.setRememberMeManager(rememberMeManager());
        securityManager.setSessionManager(sessionManager());
        List<Realm> realms = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(shiroProperties.getRealms())) {
            for (Class<? extends Realm> ream : shiroProperties.getRealms()) {
                realms.add(BeanFactoryUtils.registerBeanDefinition(applicationContext, ream));
            }
        }
//        securityManager.setRealms(realms);
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(shiroProperties.getAuthenticationStrategyMode().instance());
        modularRealmAuthenticator.setRealms(realms);
        securityManager.setAuthenticator(modularRealmAuthenticator);
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }


    @Bean
    public ShiroTemplate shiroTemplate(@Autowired ShiroFilterFactoryBean shiroFilterFactoryBean,
                                       @Autowired AbstractChainDefinitionSectionMetaSource abstractChainDefinitionSectionMetaSource) {
        ShiroTemplate shiroTemplate = new ShiroTemplate();
        shiroTemplate.setShiroFilterFactoryBean(shiroFilterFactoryBean);
        shiroTemplate.setShiroProperties(shiroProperties);
        shiroTemplate.setAbstractChainDefinitionSectionMetaSource(abstractChainDefinitionSectionMetaSource);
        return shiroTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(SessionDAO.class)
    public SessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setActiveSessionsCacheName(shiroProperties.getActiveSessionsCacheName());
        sessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return sessionDAO;
    }


    @Bean
    @ConditionalOnMissingBean
    public JavaUuidSessionIdGenerator sessionIdGenerator() {
        JavaUuidSessionIdGenerator sessionIdGenerator = new JavaUuidSessionIdGenerator();
        return sessionIdGenerator;
    }


    public QuartzSessionValidationScheduler2 sessionValidationScheduler(ValidatingSessionManager sessionManager) {
        QuartzSessionValidationScheduler2 sessionValidationScheduler = new QuartzSessionValidationScheduler2();
        sessionValidationScheduler.setSessionManager(sessionManager);
        sessionValidationScheduler.setSessionValidationInterval(shiroProperties.getSessionValidationInterval());
        return sessionValidationScheduler;
    }

    @Bean
    @Order
    public ShiroFilterFactoryBean shiroFilter(@Autowired CacheManager cacheManager) throws Exception {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setLoginUrl(shiroProperties.getLoginUrl());
        shiroFilterFactoryBean.setSuccessUrl(shiroProperties.getSuccessUrl());
        shiroFilterFactoryBean.setUnauthorizedUrl(shiroProperties.getUnauthorizedUrl());
        shiroFilterFactoryBean.setSecurityManager(securityManager(cacheManager));
        AbstractChainDefinitionSectionMetaSource abstractChainDefinitionSectionMetaSource = BeanFactoryUtils.registerBeanDefinition(applicationContext, shiroProperties.getFilterChainDefinitionClass());
        LinkedHashMap<String, String> filterChainDefinitionMap = abstractChainDefinitionSectionMetaSource.loadAllAuth(shiroProperties.getFilterChainDefinitionMap());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        LinkedHashMap<String, Filter> filters = new LinkedHashMap<>();
        shiroProperties.getFilters().forEach((name, filter) -> {
            filters.put(name, BeanFactoryUtils.registerBeanDefinition(applicationContext, filter));
        });
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
//    @Bean
//    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
//        return new LifecycleBeanPostProcessor();
//    }
//
//    /**
//     * DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
//     */
//    @Bean
//    @ConditionalOnMissingBean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
//        defaultAAP.setProxyTargetClass(true);
//        return defaultAAP;
//    }
//

    /**
     * AuthorizationAttributeSourceAdvisor，shiro里实现的Advisor类，
     * 内部使用AopAllianceAnnotationsAuthorizingMethodInterceptor来拦截用以下注解的方法。
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Autowired CacheManager cacheManager) throws Exception {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager(cacheManager));
        return authorizationAttributeSourceAdvisor;
    }

}
