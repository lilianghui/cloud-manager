package com.lilianghui.shiro.spring.starter.config;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

public class SpringShiroRedisCacheManager implements CacheManager {
    private RedisTemplate redisTemplate;
    private Map<String, ShiroProperties.RedisCacheProperties> redisCache;

    public SpringShiroRedisCacheManager(RedisTemplate redisTemplate, Map<String, ShiroProperties.RedisCacheProperties> redisCache) {
        this.redisTemplate = redisTemplate;
        this.redisCache = redisCache;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return new SpringShiroCache(name, redisTemplate, redisCache.get(name) == null ? ShiroProperties.RedisCacheProperties.DEFALUT_CONFIG : redisCache.get(name));
    }

    static class SpringShiroCache implements Cache {
        private String name;
        private RedisTemplate redisTemplate;
        private ShiroProperties.RedisCacheProperties redisCacheProperties;

        public SpringShiroCache(String name, RedisTemplate redisTemplate, ShiroProperties.RedisCacheProperties redisCacheProperties) {
            this.name = name;
            this.redisTemplate = redisTemplate;
            this.redisCacheProperties = redisCacheProperties;
        }

        public Object get(Object key) throws CacheException {
            Object value = this.redisTemplate.opsForValue().get(getKey(key));
            return value instanceof SimpleValueWrapper ? ((SimpleValueWrapper) value).get() : value;
        }

        public Object put(Object key, Object value) throws CacheException {
            this.redisTemplate.opsForValue().set(getKey(key), value, redisCacheProperties.getTimeout(), redisCacheProperties.getTimeUnit());
            return value;
        }

        public Object remove(Object key) throws CacheException {
            Object k = getKey(key);
            Object val = get(k);
            this.redisTemplate.delete(k);
            return val;
        }

        public void clear() throws CacheException {
            this.redisTemplate.delete(keys());
        }

        public int size() {
            return keys().size();
        }

        public Set keys() {
            return this.redisTemplate.keys(this.name);
        }

        public Collection values() {
            List values = new ArrayList<>();
            keys().forEach(key -> {
                Object value = this.redisTemplate.opsForValue().get(key);
                values.add(value);
            });
            return values;
        }

        private byte[] getKey(Object key) {
            return redisTemplate.getKeySerializer().serialize(new StringBuffer(this.name).append("::").append(key).toString());
        }

    }
}
