package com.lilianghui.shiro.spring.starter.core;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by Administrator on 2018/4/9 0009.
 */
public interface ShiroHandler {
    boolean logoutHandle(ServletRequest request, ServletResponse response);

    boolean authcFailedHandle(ServletRequest request, ServletResponse response);
}
