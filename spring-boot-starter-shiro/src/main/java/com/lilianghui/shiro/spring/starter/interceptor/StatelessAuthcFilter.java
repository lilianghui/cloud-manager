package com.lilianghui.shiro.spring.starter.interceptor;

import com.lilianghui.shiro.spring.starter.core.StatelessAuthenticationToken;
import com.lilianghui.shiro.spring.starter.helper.Helper;
import com.lilianghui.shiro.spring.starter.helper.TokenManager;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.CacheManagerAware;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Slf4j
public class StatelessAuthcFilter extends AuthenticatingFilter implements CacheManagerAware {
    public static final String AUTHORIZATION_HEADER = "x-auth-token";
    public static final String X_AUTH_TOKEN_CACHE_NAME = "xAuthTokenCache";

    private Cache<String, Object> cache;
    private boolean shouldRefresh = false;

    /**
     * 这里重写了父类的方法，使用我们自己定义的Token类，提交给shiro。这个方法返回null的话会直接抛出异常，进入isAccessAllowed（）的异常处理逻辑。
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String authorization = servletRequest.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isBlank(authorization)) {
            return null;
        }
        Claims claims = Helper.getTokenManager().parseJWT(authorization);
        return new StatelessAuthenticationToken(authorization, claims);
    }

    /**
     * 父类会在请求进入拦截器后调用该方法，返回true则继续，返回false则会调用onAccessDenied()。这里在不通过时，还调用了isPermissive()方法，我们后面解释。
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            if (this.isLoginRequest(request, response) || super.isPermissive(mappedValue)) {
                return true;
            }
            HttpServletRequest servletRequest = (HttpServletRequest) request;
            String authorization = servletRequest.getHeader(AUTHORIZATION_HEADER);
            if (StringUtils.isBlank(authorization) || cache.get(authorization) == null ||
                    TokenManager.JwtTokenStatus.JWT_SUCCESS != Helper.getTokenManager().validateJWT(authorization).getStatus()) {
                return false;
            }
            return executeLogin(request, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 如果这个Filter在之前isAccessAllowed（）方法中返回false,则会进入这个方法。我们这里直接返回错误的response
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=UTF-8");
//        httpResponse.setStatus(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
        httpResponse.setStatus(203);
        httpResponse.setContentType("application/json");
        httpResponse.getOutputStream().write("{\"success\":\"false\"}".getBytes());
        return false;
    }

    /**
     * 如果Shiro Login认证成功，会进入该方法，等同于用户名密码登录成功，我们这里还判断了是否要刷新Token
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        String newToken = null;
        if (token instanceof StatelessAuthenticationToken) {
            StatelessAuthenticationToken jwtToken = (StatelessAuthenticationToken) token;
            newToken = jwtToken.getToken();
//            UserDto user = (UserDto) subject.getPrincipal();
//            boolean shouldRefresh = shouldTokenRefresh(jwtToken.getClaims().getIssuedAt(),1);
            if (shouldRefresh) {
//                newToken = userService.generateJwtToken(user.getUsername());
            }
        }
        if (StringUtils.isNotBlank(newToken)) {
            httpResponse.setHeader(AUTHORIZATION_HEADER, newToken);
            //            cache.put(newToken, 1);
        }
        return true;
    }

    /**
     * 如果调用shiro的login认证失败，会回调这个方法，这里我们什么都不做，因为逻辑放到了onAccessDenied（）中。
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.error("Validate token fail, token:{}, error:{}", token.toString(), e.getMessage());
        return false;
    }

    @Override
    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache(X_AUTH_TOKEN_CACHE_NAME);
    }

    protected boolean shouldTokenRefresh(Date issueAt, long tokenRefreshInterval) {
        LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(tokenRefreshInterval).isAfter(issueTime);
    }
}
