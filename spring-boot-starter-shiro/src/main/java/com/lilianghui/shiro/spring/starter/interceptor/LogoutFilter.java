package com.lilianghui.shiro.spring.starter.interceptor;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class LogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter {

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		try {
//			StaticApplication.getBean(LogoutHandle.class).process(request,response);
		}catch (Exception e){
		}
		// 在这里执行退出系统前需要清空的数据　　　　
		Subject subject = getSubject(request, response);
		String redirectUrl = getRedirectUrl(request, response, subject);
		try {
//			((HttpServletRequest)request).getSession().removeAttribute(Constant.SESSION);
			((HttpServletRequest)request).getSession().invalidate();
			subject.logout();
		} catch (SessionException ise) {
			ise.printStackTrace();
		}
		issueRedirect(request, response, redirectUrl);

		// 返回false表示不执行后续的过滤器，直接返回跳转到登录页面
		return false;

	}
}
