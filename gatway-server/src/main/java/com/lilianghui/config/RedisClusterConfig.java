package com.lilianghui.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilianghui.entity.User;
import com.lilianghui.framework.core.protobuf.ProtocbufRedisSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置文件 - Redis Cluster + Spring Cache
 * Created by xiepengcheng on 2017/9/13.
 */
//@Configuration
//@EnableCaching
public class RedisClusterConfig extends CachingConfigurerSupport {


    /**
     * 初始化 RedisTemplate
     * Spring 使用 StringRedisTemplate 封装了 RedisTemplate 对象来进行对redis的各种操作，它支持所有的 redis 原生的 api。
     *
     * @param clusterNodes
     * @param timeout
     * @param redirects
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Bean(name = "redisTemplate")
    public RedisTemplate redisTemplate(@Value("${spring.redis.cluster.nodes}") String clusterNodes,
                                       @Value("${spring.redis.cluster.timeout}") Long timeout,
                                       @Value("${spring.redis.cluster.max-redirects}") int redirects) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory(getClusterConfiguration(clusterNodes, timeout, redirects)));
        setSerializer(template);
        return template;


    }

    @Bean(name = "protocbufRedisTemplate")
    public RedisTemplate protocbufRedisTemplate(@Value("${spring.redis.cluster.nodes}") String clusterNodes,
                                                @Value("${spring.redis.cluster.timeout}") Long timeout,
                                                @Value("${spring.redis.cluster.max-redirects}") int redirects) {

        RedisTemplate template = new RedisTemplate();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new ProtocbufRedisSerializer());
        template.setConnectionFactory(connectionFactory(getClusterConfiguration(clusterNodes, timeout, redirects)));
        template.afterPropertiesSet();
        return template;
    }

    /**
     * Redis Cluster参数配置
     *
     * @param clusterNodes
     * @param timeout
     * @param redirects
     * @return
     */
    public RedisClusterConfiguration getClusterConfiguration(String clusterNodes, Long timeout, int redirects) {
        Map<String, Object> source = new HashMap<String, Object>();
        source.put("spring.redis.cluster.nodes", clusterNodes);
        source.put("spring.redis.cluster.timeout", timeout);
        source.put("spring.redis.cluster.max-redirects", redirects);
        return new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
    }


    /**
     * 连接池设置
     *
     * @param configuration
     * @return
     */
    private RedisConnectionFactory connectionFactory(RedisClusterConfiguration configuration) {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(configuration);
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
//        return new LettuceConnectionFactory(configuration);
    }

    /**
     * 序列化工具
     * 使用 Spring 提供的序列化工具替换 Java 原生的序列化工具，这样 ReportBean 不需要实现 Serializable 接口
     *
     * @param template
     */
    private void setSerializer(StringRedisTemplate template) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
    }

    /**
     * 管理缓存
     *
     * @param redisTemplate
     * @return
     */
//    @Bean
//    public CacheManager cacheManager(RedisTemplate redisTemplate) {
//        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
//        // Number of seconds before expiration. Defaults to unlimited (0)
//        cacheManager.setDefaultExpiration(600); //设置key-value超时时间，时间单位是秒。
//        return cacheManager;
//    }


    /**
     * 生产key的策略
     *
     * @return
     */
//    @Bean
//    public KeyGenerator wiselyKeyGenerator() {
//        return new KeyGenerator() {
//            @Override
//            public Object generate(Object target, Method method, Object... params) {
//                StringBuilder sb = new StringBuilder();
//                sb.append(target.getClass().getName());
//                sb.append(method.getName());
//                for (Object obj : params) {
//                    sb.append(obj.toString());
//                }
//                return sb.toString();
//            }
//        };
//    }
}