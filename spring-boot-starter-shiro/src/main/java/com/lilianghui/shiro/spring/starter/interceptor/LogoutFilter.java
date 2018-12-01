package com.lilianghui.shiro.spring.starter.interceptor;

import com.lilianghui.shiro.spring.starter.core.LogoutHandle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.CacheManagerAware;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import static com.lilianghui.shiro.spring.starter.interceptor.StatelessAuthcFilter.AUTHORIZATION_HEADER;
import static com.lilianghui.shiro.spring.starter.interceptor.StatelessAuthcFilter.X_AUTH_TOKEN_CACHE_NAME;

@Slf4j
public class LogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter implements CacheManagerAware {

    private Cache<String, Object> cache;

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        try {
            if (!WebApplicationContextUtils.getWebApplicationContext(request.getServletContext()).getBean(LogoutHandle.class).process(request, response)) {
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        // 在这里执行退出系统前需要清空的数据　　　　
        Subject subject = getSubject(request, response);
        String redirectUrl = getRedirectUrl(request, response, subject);
        try {
            subject.logout();
            ((HttpServletRequest) request).getSession().invalidate();
        } catch (SessionException e) {
            log.error(e.getMessage(), e);
        }

        String authorization = ((HttpServletRequest) request).getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isNotBlank(authorization) && cache != null) {
            cache.remove(authorization);
        }

        issueRedirect(request, response, redirectUrl);
        // 返回false表示不执行后续的过滤器，直接返回跳转到登录页面
        return false;

    }

    @Override
    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache(X_AUTH_TOKEN_CACHE_NAME);
    }

}
