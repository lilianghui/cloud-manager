package com.lilianghui.shiro.spring.starter;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilianghui.shiro.spring.starter.config.*;
import com.lilianghui.shiro.spring.starter.core.AbstractChainDefinitionSectionMetaSource;
import com.lilianghui.shiro.spring.starter.core.RetryLimitHashedCredentialsMatcher;
import com.lilianghui.shiro.spring.starter.event.ApplicationRefreshListener;
import com.lilianghui.shiro.spring.starter.interceptor.*;
import com.lilianghui.shiro.spring.starter.utils.BeanFactoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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

    @Bean
    public ApplicationRefreshListener applicationRefreshListener() {
        return new ApplicationRefreshListener();
    }


    //缓存管理
    @Bean
    @Order(1)
//    @ConditionalOnProperty(prefix = ShiroProperties.PREFIX, value = "enableRedisCache", havingValue = "true")
//    @ConditionalOnBean(RedisTemplate.class)
    @ConditionalOnMissingBean
    public CacheManager cacheRedisManager(@Autowired @Qualifier("redisTemplateObject") RedisTemplate redisTemplateObject) {
        SpringShiroRedisCacheManager springShiroRedisCacheManager = new SpringShiroRedisCacheManager(redisTemplateObject, shiroProperties.getRedisCache());
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
    public DefaultWebSessionManager sessionManager(@Autowired SessionDAO sessionDAO) {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionIdCookieEnabled(true);
        defaultWebSessionManager.setSessionIdCookie(sessionIdCookie());
        defaultWebSessionManager.setGlobalSessionTimeout(shiroProperties.getGlobalSessionTimeout());
        defaultWebSessionManager.setDeleteInvalidSessions(true);
        defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);
        defaultWebSessionManager.setSessionValidationScheduler(sessionValidationScheduler(defaultWebSessionManager));
        defaultWebSessionManager.setSessionDAO(sessionDAO);
        defaultWebSessionManager.setSessionFactory(new RedisSessionFactory());
        return defaultWebSessionManager;
    }


    @Bean
    public RedisTemplate<Object, Object> redisTemplateObject(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new ObjectSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        template.afterPropertiesSet();
        return template;
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
    public DefaultWebSecurityManager securityManager(@Autowired CacheManager cacheManager, @Autowired SessionDAO sessionDAO) throws Exception {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(cacheManager);
        securityManager.setRememberMeManager(rememberMeManager());
        securityManager.setSessionManager(sessionManager(sessionDAO));
        List<Realm> realms = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(shiroProperties.getRealms())) {
            for (ShiroProperties.RealmProperties realmProperties : shiroProperties.getRealms()) {
                Realm instance = BeanFactoryUtils.registerBeanDefinition(applicationContext, realmProperties.getRealmClass());
                if (instance instanceof AuthenticatingRealm) {
                    AuthenticatingRealm authenticatingRealm = ((AuthenticatingRealm) instance);
                    authenticatingRealm.setAuthenticationCacheName(realmProperties.getAuthenticationCacheName());
                    authenticatingRealm.setAuthenticationCachingEnabled(realmProperties.isAuthenticationCachingEnabled());
                    authenticatingRealm.setAuthenticationTokenClass(realmProperties.getAuthenticationTokenClass());
                    CredentialsMatcher credentialsMatcher = resolveCredentialsMatcher(realmProperties.getCredentialsMatcher());
//                    if (realmProperties.getCredentialsMatcherClass() != null) {
//                        credentialsMatcher = BeanFactoryUtils.registerBeanDefinitionExist(applicationContext, realmProperties.getCredentialsMatcherClass());
//                    }
                    if (credentialsMatcher != null) {
                        authenticatingRealm.setCredentialsMatcher(credentialsMatcher);
                    } else {
                        authenticatingRealm.setCredentialsMatcher(retryLimitHashedCredentialsMatcher(cacheManager));
                    }
                }
                realms.add(instance);
            }
        }
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(shiroProperties.getAuthenticationStrategyMode().instance());
        modularRealmAuthenticator.setRealms(realms);
        securityManager.setAuthenticator(modularRealmAuthenticator);
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    private CredentialsMatcher resolveCredentialsMatcher(ShiroProperties.RetryLimitHashedCredentialsMatcherProperties matcherProperties) {
        if (matcherProperties.getCredentialsMatcherClass() == null) {
            return null;
        }
        CredentialsMatcher credentialsMatcher = BeanFactoryUtils.registerBeanDefinitionExist(applicationContext, matcherProperties.getCredentialsMatcherClass());
        if (credentialsMatcher instanceof HashedCredentialsMatcher) {
            HashedCredentialsMatcher hashedCredentialsMatcher=(HashedCredentialsMatcher)credentialsMatcher;
            hashedCredentialsMatcher.setHashAlgorithmName(matcherProperties.getHashAlgorithm());
            hashedCredentialsMatcher.setHashIterations(matcherProperties.getHashIterations());
            hashedCredentialsMatcher.setStoredCredentialsHexEncoded(matcherProperties.isStoredCredentialsHexEncoded());
        }
        return credentialsMatcher;
    }

    @Bean
    public RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher(@Autowired CacheManager cacheManager) {
        RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher();
        ShiroProperties.RetryLimitHashedCredentialsMatcherProperties credentialsMatcher = shiroProperties.getCredentialsMatcher();
        retryLimitHashedCredentialsMatcher.setCacheName(credentialsMatcher.getCacheName());
        retryLimitHashedCredentialsMatcher.setTotal(credentialsMatcher.getTotal());
        retryLimitHashedCredentialsMatcher.setEnable(credentialsMatcher.isEnable());
        retryLimitHashedCredentialsMatcher.setHashAlgorithmName(credentialsMatcher.getHashAlgorithm());
        retryLimitHashedCredentialsMatcher.setStoredCredentialsHexEncoded(credentialsMatcher.isStoredCredentialsHexEncoded());
        retryLimitHashedCredentialsMatcher.setHashIterations(credentialsMatcher.getHashIterations());
        retryLimitHashedCredentialsMatcher.setCacheManager(cacheManager);
        return retryLimitHashedCredentialsMatcher;
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

//    @Bean
//    @ConditionalOnMissingBean(SessionDAO.class)
//    public SessionDAO sessionDAO() {
//        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
//        sessionDAO.setActiveSessionsCacheName(shiroProperties.getActiveSessionsCacheName());
//        sessionDAO.setSessionIdGenerator(sessionIdGenerator());
//        return sessionDAO;
//    }

    @Bean
    public SessionDAO sessionDAO(@Autowired @Qualifier("redisTemplateObject") RedisTemplate redisTemplateObject) {
        SpringShiroSessionDAO sessionDAO = new SpringShiroSessionDAO();
        sessionDAO.setRedisTemplate(redisTemplateObject);
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
    public ShiroFilterFactoryBean shiroFilter(@Autowired CacheManager cacheManager,
                                              @Autowired SessionManager sessionManager,
                                              @Autowired SessionDAO sessionDAO) throws Exception {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setLoginUrl(shiroProperties.getLoginUrl());
        shiroFilterFactoryBean.setSuccessUrl(shiroProperties.getSuccessUrl());
        shiroFilterFactoryBean.setUnauthorizedUrl(shiroProperties.getUnauthorizedUrl());
        shiroFilterFactoryBean.setSecurityManager(securityManager(cacheManager, sessionDAO));
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
        if (StringUtils.isNotBlank(shiroProperties.getKickout().getKickoutUrl())) {
            filters.put("kickout", new KickoutSessionControlFilter(sessionManager, cacheManager, shiroProperties.getKickout()));
        }
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
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Autowired CacheManager cacheManager, @Autowired SessionDAO sessionDAO) throws Exception {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager(cacheManager, sessionDAO));
        return authorizationAttributeSourceAdvisor;
    }

}
