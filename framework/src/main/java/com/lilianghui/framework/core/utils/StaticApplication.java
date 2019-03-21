package com.lilianghui.framework.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.Locale;

@Component
public class StaticApplication implements ApplicationContextAware, ServletContextAware, InitializingBean {
    private static ApplicationContext applicationContext;
    public static String ctx = "ctx";
    private static String contentPath = "/";
    public static ServletContext servletContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        StaticApplication.applicationContext = applicationContext;
    }

    public static <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return applicationContext.getBean(beanName, clazz);
    }

    public static String getMessage(String key, Object[] params, Locale locale, String defaultMessage) {
        return applicationContext.getMessage(key, params, defaultMessage, locale);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setServletContext(ServletContext servletContext) {
        servletContext.setAttribute(ctx, servletContext.getContextPath() + "/");
        StaticApplication.contentPath = (String) servletContext.getAttribute(ctx);
        StaticApplication.servletContext = servletContext;
    }

    public static String getCtx() {
        return StaticApplication.ctx;
    }

    public static void setCtx(String ctx) {
        StaticApplication.ctx = ctx;
    }

    public static String getContentPath() {
        return contentPath;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

}
