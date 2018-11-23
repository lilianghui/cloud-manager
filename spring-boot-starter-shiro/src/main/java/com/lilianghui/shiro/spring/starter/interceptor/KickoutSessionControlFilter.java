package com.lilianghui.shiro.spring.starter.interceptor;

import com.lilianghui.shiro.spring.starter.config.ShiroProperties;
import com.lilianghui.shiro.spring.starter.config.ShiroUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.ReflectionUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Deque;
import java.util.LinkedList;

@Slf4j
public class KickoutSessionControlFilter extends AccessControlFilter {
    private final static String ATTRIBUTE_NAME = "kickout";

    private ShiroProperties.KickoutProperties kickoutProperties;
    private SessionManager sessionManager;
    private Cache<String, LinkedList<Serializable>> cache;

    public KickoutSessionControlFilter(SessionManager sessionManager, CacheManager cacheManager, ShiroProperties.KickoutProperties kickoutProperties) {
        this.sessionManager = sessionManager;
        this.kickoutProperties = kickoutProperties;
        this.cache = cacheManager.getCache(kickoutProperties.getCacheName());
    }

    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            // 如果没有登录，直接进行之后的流程
            return true;
        }

        Session session = subject.getSession();
        Object principal = subject.getPrincipal();
        String account = null;
        if (principal instanceof ShiroUser) {
            account = ((ShiroUser) principal).getIdentity();
        } else if (principal instanceof String) {
            account = (String) principal;
        } else {
            Field field = ReflectionUtils.findField(principal.getClass(), "id");
            account = String.valueOf(ReflectionUtils.getField(field, principal));
        }
        Serializable sessionId = session.getId();

        // TODO 同步控制
        LinkedList<Serializable> deque = cache.get(account);
        System.out.println(deque);
        if (deque == null) {
            deque = new LinkedList<>();
        }

        // 如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if (!deque.contains(sessionId) && session.getAttribute(ATTRIBUTE_NAME) == null) {
            deque.push(sessionId);
        }

        // 如果队列里的sessionId数超出最大会话数，开始踢人
        while (deque.size() > kickoutProperties.getMaxSession()) {
            Serializable kickoutSessionId = null;
            if (kickoutProperties.isKickoutAfter()) { // 如果踢出后者
                kickoutSessionId = deque.removeFirst();
            } else { // 否则踢出前者
                kickoutSessionId = deque.removeLast();
            }
            try {
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if (kickoutSession != null) {
                    // 设置会话的kickout属性表示踢出了
                    kickoutSession.setAttribute(ATTRIBUTE_NAME, true);
                }
            } catch (Exception e) {// ignore exception
                log.error(e.getMessage(), e);
            }
        }
        cache.put(account, deque);

        // 如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute(ATTRIBUTE_NAME) != null) {
            // 会话被踢出了
            try {
                subject.logout();
            } catch (Exception e) { // ignore
                log.error(e.getMessage(), e);
            }
            saveRequest(request);
            WebUtils.issueRedirect(request, response, kickoutProperties.getKickoutUrl());
            return false;
        }

        return true;
    }

}
