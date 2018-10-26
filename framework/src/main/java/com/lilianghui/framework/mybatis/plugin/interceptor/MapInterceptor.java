package com.lilianghui.framework.mybatis.plugin.interceptor;

import com.lilianghui.framework.mybatis.entity.MapParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Slf4j
@Intercepts({@Signature(method = "handleResultSets", type = ResultSetHandler.class, args = {Statement.class})})
public class MapInterceptor implements Interceptor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.ibatis.plugin.Interceptor#intercept(org.apache.ibatis.plugin.
	 * Invocation)
	 */
	public Object intercept(Invocation invocation) throws Throwable {
		try {
			// 通过invocation获取代理的目标对象
			Object target = invocation.getTarget();
			// 暂时ResultSetHandler只有FastResultSetHandler这一种实现
			if (target instanceof DefaultResultSetHandler) {
				DefaultResultSetHandler resultSetHandler = (DefaultResultSetHandler) target;
				Field field = ReflectionUtils.findField(DefaultResultSetHandler.class, "parameterHandler");
				field.setAccessible(true);
				ParameterHandler parameterHandler = (ParameterHandler) field.get(resultSetHandler);
				// 利用反射获取到FastResultSetHandler的ParameterHandler属性，从而获取到ParameterObject；
				Object parameterObj = parameterHandler.getParameterObject();
				// 判断ParameterObj是否是我们定义的MapParam，如果是则进行自己的处理逻辑
				if (parameterObj instanceof MapParam) {// 拦截到了
					MapParam mapParam = (MapParam) parameterObj;
					// 获取到当前的Statement
					Statement stmt = (Statement) invocation.getArgs()[0];
					// 通过Statement获取到当前的结果集，对其进行处理，并返回对应的处理结果
					return handleResultSet(stmt.getResultSet(), mapParam);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		// 如果没有进行拦截处理，则执行默认逻辑
		return invocation.proceed();
	}

	/**
	 * 处理结果集
	 * 
	 * @param resultSet
	 * @param mapParam
	 * @return
	 */
	private Object handleResultSet(ResultSet resultSet, MapParam mapParam) {
		try {
			if (resultSet != null) {
				Map<Object, Collection> mmm = new HashMap<Object, Collection>();
				List<Object> resultList = new ArrayList<Object>();
				ResultSetMetaData meata = resultSet.getMetaData();
				int count = meata.getColumnCount();
				while (resultSet.next()) {
					if (Boolean.FALSE.equals(mapParam.get(MapParam.ENABLED))) {
						resultList.add(processRow(resultSet, meata, count));
					} else {
						mmm = processRow(resultSet, mapParam, mmm);
					}
				}
				if (Boolean.TRUE.equals(mapParam.get(MapParam.ENABLED))) {
					resultList.add(mmm);
				}
				return resultList;
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		} finally {
			closeResultSet(resultSet);
		}
		return null;
	}

	private Map<Object, Object> processRow(ResultSet resultSet, ResultSetMetaData meata, int count) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		for (int index = 1; index <= count; index++) {
			String columnLabel = meata.getColumnLabel(index);
			map.put(columnLabel, resultSet.getObject(columnLabel));
		}
		return map;
	}

	private Map<Object, Collection> processRow(ResultSet resultSet, MapParam mapParam, Map<Object, Collection> mmm) throws Exception {
		// 拿到Key对应的字段
		String keyField = (String) mapParam.get(MapParam.KEY_FIELD);
		// 拿到Value对应的字段
		String valueField = (String) mapParam.get(MapParam.VALUE_FIELD);
		Object key = resultSet.getObject(keyField);
		Object value = resultSet.getObject(valueField);
		// map.put(key, value);
		if (mmm.get(key) == null) {
			mmm.put(key, new HashSet<>());
		}
		mmm.get(key).add(value);
		return mmm;
	}

	/**
	 * 关闭ResultSet
	 * 
	 * @param resultSet
	 *            需要关闭的ResultSet
	 */
	private void closeResultSet(ResultSet resultSet) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		} catch (SQLException e) {

		}
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
