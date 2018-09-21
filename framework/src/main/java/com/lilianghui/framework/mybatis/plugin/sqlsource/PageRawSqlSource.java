package com.lilianghui.framework.mybatis.plugin.sqlsource;

import com.lilianghui.framework.mybatis.entity.Entity;
import com.lilianghui.framework.mybatis.plugin.helper.PageSqlHelper;
import com.lilianghui.framework.mybatis.plugin.helper.SQLParseHelper;
import com.lilianghui.framework.mybatis.utils.BridgeHelper;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;

public class PageRawSqlSource extends AbstractSqlSource {
	private String sql;
	private List<ParameterMapping> parameterMappings;
	private boolean flag;
	private boolean singleQuery = false;

	public PageRawSqlSource(SqlSource sqlSource,String defalutOrderBy,boolean singleQuery, boolean flag) {
		super(sqlSource,defalutOrderBy);
		this.sql = (String) metaObject.getValue("sql");
		this.parameterMappings = (List<ParameterMapping>) metaObject.getValue("parameterMappings");
		this.flag = flag;
		this.singleQuery = singleQuery;
	}

	@Override
	public BoundSql getBoundSql(Object parameterObject) {
		Entity baseEntity =  BridgeHelper.parseBaseEntity(parameterObject);
		String sql = null;
		if(baseEntity.isUseCCJSqlParse()){
			sql =SQLParseHelper.parse(this.sql, databaseId, singleQuery,baseEntity);
		}else{
			if (flag) {
				sql = PageSqlHelper.getCountSql(this.sql, databaseId, singleQuery);
			} else {
				sql = PageSqlHelper.getSql(this.sql, databaseId, singleQuery);
			}
			sql = replace(flag,singleQuery, sql, baseEntity);
		}
		return new BoundSql(configuration, sql, parameterMappings, parameterObject);
	}
}
