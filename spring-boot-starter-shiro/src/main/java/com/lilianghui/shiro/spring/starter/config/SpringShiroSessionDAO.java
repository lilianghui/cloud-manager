package com.lilianghui.shiro.spring.starter.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SpringShiroSessionDAO extends AbstractSessionDAO {
    @Setter
    private RedisTemplate redisTemplate;
    @Setter
    private String activeSessionsCacheName;

    public SpringShiroSessionDAO() {
    }

    public SpringShiroSessionDAO(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        this.saveSession(session);
        log.info("保存session-{}", session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            log.error("session id is null");
            return null;
        }
        Session session = (Session) this.redisTemplate.opsForValue().get(getKey(sessionId));
        log.info("读取session-{}", session);
        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        log.info("更新session-{}", session);
        this.saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            log.error("session or session id is null");
            return;
        }
        log.info("删除session-{}", session);
        this.redisTemplate.delete(getKey(session.getId()));
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> values = new HashSet<>();
        keys().forEach(key -> {
            Object value = this.redisTemplate.opsForValue().get(key);
            values.add((Session) value);
        });
        return values;
    }

    public Set keys() {
        return this.redisTemplate.keys(this.activeSessionsCacheName);
    }

    public void saveSession(Session session) {
        if (session == null || session.getId() == null) {
            log.error("session or session id is null");
            return;
        }
        this.redisTemplate.opsForValue().set(getKey(session.getId()), session, session.getTimeout(), TimeUnit.MILLISECONDS);
    }

    private String getKey(Object key) {
        return new StringBuffer(StringUtils.defaultString(this.activeSessionsCacheName))
                .append(":").append(key).toString();
    }

}
