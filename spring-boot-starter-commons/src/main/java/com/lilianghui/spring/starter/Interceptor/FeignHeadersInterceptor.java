package com.lilianghui.spring.starter.Interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class FeignHeadersInterceptor implements RequestInterceptor {


    private Set<String> ignoreHeaders = new HashSet<>();

    @Override
    public void apply(RequestTemplate requestTemplate) {
//        HttpServletRequest request = RequestContextHolder.getHttpServletRequest();
//        Optional.ofNullable(request).map(httpServletRequest -> httpServletRequest.getHeaderNames())
//                .map(Collections::list).filter(key -> !ignoreHeaders.contains(key))
//                .orElseGet(ArrayList::new)
//                .forEach(name -> requestTemplate.header(name, request.getHeader(name)));
    }

}
