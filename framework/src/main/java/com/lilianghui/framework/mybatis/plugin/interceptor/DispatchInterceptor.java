package com.lilianghui.framework.mybatis.plugin.interceptor;

import com.lilianghui.framework.mybatis.plugin.helper.PageProcessHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@Slf4j
@Intercepts(@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
		RowBounds.class, ResultHandler.class }))
public class DispatchInterceptor implements Interceptor {
	private String defalutOrderBy = null;
	private Set<String> methodSuffix = new HashSet<>();

	public DispatchInterceptor(Set<String> methodSuffix, String defalutOrderBy) {
		this.methodSuffix=methodSuffix;
		this.defalutOrderBy=defalutOrderBy;
	}

	public Object intercept(Invocation invocation) throws Throwable {
		try {
			// 通过invocation获取代理的目标对象
			MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
			String msId = ms.getId();
			String method = msId.substring(msId.lastIndexOf(".") + 1);
			boolean flag=false;
			for(String s:methodSuffix){
				if (method.endsWith(s)) {
					flag=true;
					break;
				}
			}
			if (flag) {
				return PageProcessHelper.class.getMethod("listByPage", Invocation.class,String.class)
						.invoke(PageProcessHelper.instance, invocation,defalutOrderBy);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		// 如果没有进行拦截处理，则执行默认逻辑
		return invocation.proceed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
	 */
	public Object plugin(Object obj) {
		return Plugin.wrap(obj, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
	 */
	public void setProperties(Properties props) {

	}

}
