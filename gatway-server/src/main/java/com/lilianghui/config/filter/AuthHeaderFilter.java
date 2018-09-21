package com.lilianghui.config.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AuthHeaderFilter extends ZuulFilter {

    private static Logger logger = LoggerFactory.getLogger(AuthHeaderFilter.class);

    @Override
    public String filterType() {
        return "pre";
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
//        if (SecurityUtils.getSubject().getPrincipal() == null) {
//            requestContext.addZuulRequestHeader("X-SHIRO-USER-ID", StringUtils.EMPTY);
//            requestContext.addZuulRequestHeader("X-SHIRO-USER-NAME", StringUtils.EMPTY);
//            requestContext.addZuulRequestHeader("X-SHIRO-ENTP-ID", StringUtils.EMPTY);
//            requestContext.addZuulRequestHeader("X-SHIRO-SYSTEM-ID", StringUtils.EMPTY);
//        } else {
//            ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
//            requestContext.addZuulRequestHeader("X-SHIRO-USER-ID", shiroUser.getId());
//            requestContext.addZuulRequestHeader("X-SHIRO-USER-NAME", shiroUser.getLoginName());
//            Map<String, String> userDetailMap = (HashMap) shiroUser.getUserDetail();
//            requestContext.addZuulRequestHeader("X-SHIRO-ENTP-ID", userDetailMap.get("entpId"));
//            requestContext.addZuulRequestHeader("X-SHIRO-SYSTEM-ID", "zsy_".concat(userDetailMap.get("systemId")).concat("_system"));
//        }
        return null;
    }

}
