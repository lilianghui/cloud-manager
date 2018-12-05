package com.lilianghui.shiro.spring.starter;

import com.lilianghui.shiro.spring.starter.config.ShiroProperties;
import com.lilianghui.shiro.spring.starter.core.AbstractChainDefinitionSectionMetaSource;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class ShiroTemplate {

    @Setter
    private ShiroFilterFactoryBean shiroFilterFactoryBean;
    @Setter
    private ShiroProperties shiroProperties;
    @Setter
    private AbstractChainDefinitionSectionMetaSource abstractChainDefinitionSectionMetaSource;

    public void refreshAll() {
        refresh(null, true);
    }

    public void refresh(String account) {
        refresh(account, false);
    }

    private void refresh(String account, boolean all) {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        Subject subject = SecurityUtils.getSubject();
        Iterator<Realm> it = rsm.getRealms().iterator();
        while (it.hasNext()) {
            Realm realm = (Realm) it.next();
            if (realm instanceof AuthorizingRealm) {
                AuthorizingRealm authorizingRealm = (AuthorizingRealm) realm;
                if (all) {
                    authorizingRealm.getAuthorizationCache().clear();
                } else {
                    SimplePrincipalCollection principals = new SimplePrincipalCollection(account, authorizingRealm.getName());
                    subject.runAs(principals);
                    authorizingRealm.getAuthorizationCache().remove(subject.getPrincipals());
                    subject.releaseRunAs();
                }
            }
        }
    }

    public String hashProvidedCredentials(String credentials, String salt) {
        ShiroProperties.RetryLimitHashedCredentialsMatcherProperties credentialsMatcher = shiroProperties.getCredentialsMatcher();
        return new SimpleHash(credentialsMatcher.getHashAlgorithm(), credentials, salt, credentialsMatcher.getHashIterations()).toString();
    }

    /**
     * 重新加载权限
     */
    public synchronized void refreshChainDefinitions() {

        try {
            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
            }

            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

            // 清空老的权限控制
            manager.getFilterChains().clear();

            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            LinkedHashMap<String, String> filterChainDefinitionMap = abstractChainDefinitionSectionMetaSource.loadAllAuth(shiroProperties.getFilterChainDefinitionMap());
            shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
            // 重新构建生成
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim().replace(" ", "");
                manager.createChain(url, chainDefinition);
            }

            log.info("{}--更新权限成功", filterChainDefinitionMap.toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
