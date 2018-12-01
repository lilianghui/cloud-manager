package com.lilianghui.shiro.spring.starter.core;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by Administrator on 2018/4/9 0009.
 */
public interface LogoutHandle {
    boolean process(ServletRequest request, ServletResponse response);
}
