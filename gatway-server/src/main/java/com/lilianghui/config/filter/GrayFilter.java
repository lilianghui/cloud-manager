package com.lilianghui.config.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import static com.lilianghui.spring.starter.gray.predicate.MetadataAwarePredicate.GRAY_HEADER_GRAY;
import static com.lilianghui.spring.starter.gray.predicate.MetadataAwarePredicate.GRAY_HEADER_NAME;
import static com.lilianghui.spring.starter.gray.predicate.MetadataAwarePredicate.GRAY_HEADER_RUNNUNG;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.*;

public class GrayFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return !ctx.containsKey(FORWARD_TO_KEY) && !ctx.containsKey(SERVICE_ID_KEY);
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        String mark = requestContext.getRequest().getHeader(GRAY_HEADER_NAME);
        if ("enable".equals(mark) || GRAY_HEADER_GRAY.equals(mark)) {
            requestContext.addZuulRequestHeader(GRAY_HEADER_NAME, GRAY_HEADER_GRAY);
        } else {
            requestContext.addZuulRequestHeader(GRAY_HEADER_NAME, GRAY_HEADER_RUNNUNG);
        }
        return null;
    }
}
