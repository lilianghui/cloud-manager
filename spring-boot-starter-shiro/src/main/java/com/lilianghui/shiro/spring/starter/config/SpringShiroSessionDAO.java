package com.lilianghui.shiro.spring.starter.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SpringShiroSessionDAO extends AbstractSessionDAO {
    private DefaultMemorySessionDAO memorySessionDAO = new DefaultMemorySessionDAO();
    @Setter
    private RedisTemplate redisTemplate;
    private String activeSessionsCacheName;
    private String key;
    @Setter
    private int timeToLiveSeconds = 10;

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
        cache(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            log.error("session id is null");
            return null;
        }
        Session session = cacheSession(sessionId);
        if (session == null || session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
            session = (Session) this.redisTemplate.opsForValue().get(getKey(sessionId));
            if (session != null && session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null) {
                if (session instanceof ValidatingSession) {
                    //检查session是否过期
                    ((ValidatingSession) session).validate();
                }
                if (session instanceof SimpleSession) {
                    // 重置Redis中Session的最后访问时间
                    ((SimpleSession) session).setLastAccessTime(new Date());
                }
                this.saveSession(session);
                cache(session);
                log.info("sessionId {} name {} 被读取并更新访问时间", sessionId, session.getClass().getName());
            }
        }
        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        //如果会话过期/停止 没必要再更新了
        try {
            if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
                return;
            }
        } catch (Exception e) {
            log.error("ValidatingSession error");
        }
        if (session instanceof RedisSimpleSession) {
            // 如果没有主要字段(除lastAccessTime以外其他字段)发生改变
            RedisSimpleSession redisSimpleSession = (RedisSimpleSession) session;
            if (!redisSimpleSession.isChanged()) {
                return;
            }
            redisSimpleSession.setChanged(false);
            log.info("更新session-{}", session);
            this.saveSession(session);
        }
        if (session instanceof ValidatingSession) {
            if (((ValidatingSession) session).isValid()) {
                //不更新ehcach中的session，使它在设定的时间内过期
                //cache(session, session.getId());
            } else {
                uncache(session);
            }
        } else {
            cache(session);
        }
    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            log.error("session or session id is null");
            return;
        }
        log.info("删除session-{}", session);
        uncache(session);
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
        return this.redisTemplate.keys(this.key);
    }

    public void saveSession(Session session) {
        if (session == null || session.getId() == null) {
            log.error("session or session id is null");
            return;
        }
        this.redisTemplate.opsForValue().set(getKey(session.getId()), session, session.getTimeout(), TimeUnit.MILLISECONDS);
    }

    public void setActiveSessionsCacheName(String activeSessionsCacheName) {
        this.activeSessionsCacheName = activeSessionsCacheName;
        this.key = activeSessionsCacheName + "*";
    }

    private String getKey(Object key) {
        return new StringBuffer(StringUtils.defaultString(this.activeSessionsCacheName))
                .append(":").append(key).toString();
    }

    private void cache(Session session) {
        try {
            if (session instanceof RedisSimpleSession) {
                Date date = new Date();
                ((RedisSimpleSession) session).setCreateTimestamp(date);
            }
            memorySessionDAO.create(session);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void uncache(Session session) {
        try {
            memorySessionDAO.delete(session);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private RedisSimpleSession cacheSession(Serializable sessionId) {
        RedisSimpleSession session = null;
        try {
            session = (RedisSimpleSession) memorySessionDAO.doReadSession(sessionId);
            if (session != null && DateUtils.addSeconds(session.getCreateTimestamp(), timeToLiveSeconds).before(new Date())) {
                return null;
            }
        } catch (UnknownSessionException e) {
            log.error(e.getMessage(), e);
        }
        return session;
    }
}
