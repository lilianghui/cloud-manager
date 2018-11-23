package com.lilianghui.shiro.spring.starter.interceptor;

import com.lilianghui.shiro.spring.starter.utils.WebUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthcFilter extends FormAuthenticationFilter {

    @Override
    protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        if (WebUtils.isAjax((HttpServletRequest) request)) {
//
        }
//        String str = "<script>top.window.location.href = \"" + WebUtils.getWebsite((HttpServletRequest) request) + getLoginUrl() + "\"</script>";
//        WebUtils.writeJson((HttpServletResponse) response, str);
        ((HttpServletResponse)response).sendRedirect("/");
    }
}
