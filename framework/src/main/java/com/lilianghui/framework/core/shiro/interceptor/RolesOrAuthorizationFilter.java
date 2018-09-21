package com.lilianghui.framework.core.shiro.interceptor;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

public class RolesOrAuthorizationFilter extends AuthorizationFilter {
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		Subject subject = getSubject(request, response);
		String[] rolesArray = (String[]) mappedValue;

		if (rolesArray == null || rolesArray.length == 0) {
			return true;
		}
		for (String aRolesArray : rolesArray) {
			if (subject.hasRole(aRolesArray)) {
				return true;
			}
		}
		return false;
	}
}