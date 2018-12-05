package com.lilianghui.shiro.spring.starter.interceptor;

import com.lilianghui.shiro.spring.starter.core.ShiroHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Slf4j
public class AuthcFilter extends FormAuthenticationFilter {

    @Override
    protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        try {
            if (WebApplicationContextUtils.getWebApplicationContext(request.getServletContext()).getBean(ShiroHandler.class).authcFailedHandle(request, response)) {
                super.saveRequestAndRedirectToLogin(request, response);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
