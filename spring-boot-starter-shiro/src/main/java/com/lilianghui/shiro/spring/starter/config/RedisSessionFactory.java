package com.lilianghui.shiro.spring.starter.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.web.session.mgt.DefaultWebSessionContext;

import javax.servlet.http.HttpServletRequest;

public class RedisSessionFactory implements SessionFactory {

    @Override
    public Session createSession(SessionContext initData) {
        HttpServletRequest request = (HttpServletRequest) initData.get(DefaultWebSessionContext.class.getName() + ".SERVLET_REQUEST");
//        if (initData != null) {
//            String host = initData.getHost();
//            if (host != null) {
//                return new RedisSimpleSession(host);
//            }
//        }
        return new RedisSimpleSession(getIpAddress(request));
    }

    public static String getIpAddress(HttpServletRequest request) {
        String localIP = "127.0.0.1";
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
