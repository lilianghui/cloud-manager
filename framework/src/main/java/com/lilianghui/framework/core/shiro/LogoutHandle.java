package com.lilianghui.framework.core.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by Administrator on 2018/4/9 0009.
 */
public interface LogoutHandle {
    void process(ServletRequest request, ServletResponse response);
}
