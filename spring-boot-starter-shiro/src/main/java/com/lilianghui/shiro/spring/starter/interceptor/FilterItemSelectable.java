package com.lilianghui.shiro.spring.starter.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.lang.reflect.Method;

@Slf4j
public class FilterItemSelectable extends AuthorizationFilter {
    private ShiroFilterFactoryBean shiroFilterFactoryBean;
    private static Method method = null;

    static {
        try {
            method = AccessControlFilter.class.getDeclaredMethod("isAccessAllowed", ServletRequest.class, ServletResponse.class, Object.class);
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public FilterItemSelectable(ShiroFilterFactoryBean shiroFilterFactoryBean) {
        this.shiroFilterFactoryBean = shiroFilterFactoryBean;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            if (mappedValue instanceof String[] && ArrayUtils.isNotEmpty((String[]) mappedValue)) {
                AbstractShiroFilter abstractShiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
                PathMatchingFilterChainResolver pathMatchingFilterChainResolver = (PathMatchingFilterChainResolver) abstractShiroFilter.getFilterChainResolver();
                for (String name : (String[]) mappedValue) {
                    try {
                        Filter filter = ((DefaultFilterChainManager) pathMatchingFilterChainResolver.getFilterChainManager()).getFilter(name);
                        if (filter instanceof AccessControlFilter) {
                            boolean value = (boolean) method.invoke(filter, request, response, mappedValue);
                            if (value) {
                                return true;
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}
