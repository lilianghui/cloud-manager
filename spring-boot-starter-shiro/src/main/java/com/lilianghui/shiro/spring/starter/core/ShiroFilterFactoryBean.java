package com.lilianghui.shiro.spring.starter.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

public class ShiroFilterFactoryBean extends org.apache.shiro.spring.web.ShiroFilterFactoryBean {
	private String resourcePatterns;

	@Override
	protected AbstractShiroFilter createInstance() throws Exception {
		AbstractShiroFilter instance = super.createInstance();
		Set<String> patternSet = new HashSet<>();
		if (StringUtils.isNotBlank(resourcePatterns)) {
			for (String str : resourcePatterns.split(",")) {
				if (StringUtils.isNotBlank(str)) {
					patternSet.add(str.trim());
				}
			}

		}
		return new SpringShiroFilter(instance.getSecurityManager(), instance.getFilterChainResolver(), patternSet);
	}

	private static final class SpringShiroFilter extends AbstractShiroFilter {
		private Set<String> patternSet;
		private AntPathMatcher matcher;

		protected SpringShiroFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver, Set<String> patternSet) {
			super();
			if (webSecurityManager == null) {
				throw new IllegalArgumentException("WebSecurityManager property cannot be null.");
			}
			setSecurityManager(webSecurityManager);
			if (resolver != null) {
				setFilterChainResolver(resolver);
			}
			if (patternSet != null) {
				this.patternSet = patternSet;
				matcher = new AntPathMatcher();
			}
		}

		@Override
		protected void updateSessionLastAccessTime(ServletRequest request, ServletResponse response) {
			if (!isHttpSessions() && !isResource(request))
				super.updateSessionLastAccessTime(request, response);
		}

		private boolean isResource(ServletRequest servletRequest) {
			if (patternSet == null || patternSet.isEmpty()) {
				return false;
			}
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			int contextPathLen = request.getContextPath().length();
			String requestURI = request.getRequestURI();
			final String requestPath = requestURI.substring(contextPathLen);
			for (String str : patternSet) {
				if (matcher.match(str, requestPath)) {
					return true;
				}
			}
			return false;
		}
	}

	public void setResourcesPatterns(String patterns) {
		this.resourcePatterns = patterns;
	}
}