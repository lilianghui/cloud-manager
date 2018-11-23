package com.lilianghui.shiro.spring.starter.core;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    @Resource
    private CacheManager cacheManager;

    private String cacheName;
    private int total = 5;
    private boolean enable = true;

    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        boolean matches = super.doCredentialsMatch(token, info);
        if (!enable) {
            return matches;
        }
        String account = (String) token.getPrincipal();
        AtomicInteger retryCount = getPasswordRetryCache().get(account);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
        }
        if (retryCount.incrementAndGet() > this.total) {
            throw new ExcessiveAttemptsException();
        }
        if (matches) {
            getPasswordRetryCache().remove(account);
        } else {
            getPasswordRetryCache().put(account, retryCount);
        }
        return matches;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public Cache<String, AtomicInteger> getPasswordRetryCache() {
        return cacheManager.getCache(cacheName);
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
