package com.lilianghui.spring.starter.Interceptor;

import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class RequestContextHolder {

    public static String getHeader(String name) {
        return Optional.ofNullable(getHttpServletRequest()).map(httpServletRequest -> httpServletRequest.getHeader(name)).orElse(null);
    }

    public static HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

}
