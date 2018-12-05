package com.lilianghui.shiro.spring.starter.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import static com.lilianghui.shiro.spring.starter.interceptor.StatelessAuthcFilter.AUTHORIZATION_HEADER;


@Slf4j
public class AllowOriginFilter extends PathMatchingFilter {

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        servletResponse.addHeader("Access-Control-Allow-Origin", "*");      //为安全起见，可配置允许访问的请求方地址。这里配置成*号，是允许所有访问。
        servletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");        //为安全起见，也可配置成只允许POST请求
        servletResponse.addHeader("Access-Control-Allow-Headers", "Content-Type," + AUTHORIZATION_HEADER);      //这里要注意，auth_token是我自定义的请求头当中带的token，在这里必须添加，否则你永远获取不到。
        servletResponse.addHeader("Access-Control-Max-Age", "1800");//30 min
        return super.onPreHandle(request, response, mappedValue);
    }
}
