package com.lilianghui.config.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;


public class AuthHeaderFilter extends ZuulFilter {

    private static Logger logger = LoggerFactory.getLogger(AuthHeaderFilter.class);

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        if (SecurityUtils.getSubject().getPrincipal() == null) {
            requestContext.addZuulRequestHeader("X-SHIRO-USER-ID", StringUtils.EMPTY);
        } else {
            String userId = (String) SecurityUtils.getSubject().getPrincipal();
            requestContext.addZuulRequestHeader("X-SHIRO-USER-ID", userId);
        }
        return null;
    }

}
