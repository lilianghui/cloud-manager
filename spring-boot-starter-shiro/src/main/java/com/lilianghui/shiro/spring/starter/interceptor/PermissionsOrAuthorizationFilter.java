package com.lilianghui.shiro.spring.starter.interceptor;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PermissionsOrAuthorizationFilter extends AuthorizationFilter {
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		Subject subject = getSubject(request, response);
		String[] permsArray = (String[]) mappedValue;

		if (permsArray == null || permsArray.length == 0) {
			return true;
		}
		for (String perm : permsArray) {
			if (subject.isPermitted(perm)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		Subject subject = getSubject(request, response);

//		if (subject.getPrincipal() == null) {
//			if (com.lilianghui.framework.core.utils.WebUtils.isAjax(httpRequest)) {
//				com.lilianghui.framework.core.utils.WebUtils.writeJson(httpResponse, JsonUtils.writeValue(MessageHandler.error("当前用户未登录,请登录")));
//			} else {
//				saveRequestAndRedirectToLogin(request, response);
//			}
//		} else {
//			if (com.lilianghui.framework.core.utils.WebUtils.isAjax(httpRequest)) {
//				com.lilianghui.framework.core.utils.WebUtils.writeJson(httpResponse, JsonUtils.writeValue(MessageHandler.error("无权限")));
//			} else {
//				String unauthorizedUrl = getUnauthorizedUrl();
//				if (StringUtils.isNotBlank(unauthorizedUrl)) {
//					WebUtils.issueRedirect(request, response, unauthorizedUrl);
//				} else {
//					WebUtils.toHttp(response).sendError(401);
//				}
//			}
//		}
		return false;
	}
}