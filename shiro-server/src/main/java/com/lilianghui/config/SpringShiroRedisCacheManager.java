package com.lilianghui.config;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.Collection;
import java.util.Set;

public class SpringShiroRedisCacheManager implements CacheManager {
    private RedisCacheManager redisCacheManager;

    public SpringShiroRedisCacheManager(RedisCacheManager redisCacheManager) {
        this.redisCacheManager = redisCacheManager;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        org.springframework.cache.Cache cache = redisCacheManager.getCache(s);
        return new SpringShiroCache(cache);
    }

    static class SpringShiroCache implements Cache {
        private org.springframework.cache.Cache springCache;

        SpringShiroCache(org.springframework.cache.Cache springCache) {
            this.springCache = springCache;
        }

        public Object get(Object key) throws CacheException {
            Object value = this.springCache.get(key);
            return value instanceof SimpleValueWrapper ? ((SimpleValueWrapper) value).get() : value;
        }

        public Object put(Object key, Object value) throws CacheException {
            this.springCache.put(key, value);
            return value;
        }

        public Object remove(Object key) throws CacheException {
            this.springCache.evict(key);
            return null;
        }

        public void clear() throws CacheException {
            this.springCache.clear();
        }

        public int size() {
            throw new UnsupportedOperationException("invoke spring cache abstract size method not supported");
        }

        public Set keys() {
            throw new UnsupportedOperationException("invoke spring cache abstract keys method not supported");
        }

        public Collection values() {
            throw new UnsupportedOperationException("invoke spring cache abstract values method not supported");
        }
    }
}
