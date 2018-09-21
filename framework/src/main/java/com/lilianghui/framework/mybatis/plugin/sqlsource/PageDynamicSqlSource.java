package com.lilianghui.framework.mybatis.plugin.sqlsource;

import com.lilianghui.framework.mybatis.entity.Entity;
import com.lilianghui.framework.mybatis.plugin.helper.PageSqlHelper;
import com.lilianghui.framework.mybatis.plugin.helper.SQLParseHelper;
import com.lilianghui.framework.mybatis.utils.BridgeHelper;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.SqlNode;

import java.util.Map;

public class PageDynamicSqlSource extends AbstractSqlSource {
	private SqlNode rootSqlNode;
	private boolean flag = false;
	private boolean singleQuery = false;

	public PageDynamicSqlSource(SqlSource sqlSource,String defalutOrderBy,boolean singleQuery, boolean flag) {
		super(sqlSource,defalutOrderBy);
		this.rootSqlNode = (SqlNode) metaObject.getValue("rootSqlNode");
		this.flag = flag;
		this.singleQuery = singleQuery;
	}

	@Override
	public BoundSql getBoundSql(Object parameterObject) {
		Entity baseEntity =  BridgeHelper.parseBaseEntity(parameterObject);
		DynamicContext context = new DynamicContext(configuration, parameterObject);
		rootSqlNode.apply(context);
		String countextSql = context.getSql();
		SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
		Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
		String sql = null;
		if(baseEntity.isUseCCJSqlParse()){
			sql = SQLParseHelper.parse(countextSql, databaseId, singleQuery,baseEntity);
		}else{
			if (flag) {
				sql = PageSqlHelper.getCountSql(countextSql, databaseId,singleQuery);
			} else {
				sql = PageSqlHelper.getSql(countextSql, databaseId,singleQuery);
			}
		}
		sql = replace(flag,singleQuery,sql, baseEntity);
		SqlSource sqlSource = sqlSourceParser.parse(sql, parameterType, context.getBindings());
		BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
		for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
			boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
		}

		return boundSql;
	}
}
