package com.lilianghui.framework.core.mapper.tk.provider;

import com.lilianghui.framework.core.mapper.tk.helper.ProviderHelper;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class ShareUpdateProvider extends MapperTemplate {

	public ShareUpdateProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	public String updateForFieldByPrimaryKey(MappedStatement ms) {
		Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
		sql.append(ProviderHelper.updateSetColumns(entityClass, "record", false, false));
		sql.append(ProviderHelper.wherePKColumns(entityClass, "record"));
		return sql.toString();
	}

	public String updateForFieldByExample(MappedStatement ms) {
		Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
		sql.append(ProviderHelper.updateSetColumns(entityClass, "record", false, false));
		sql.append(SqlHelper.updateByExampleWhereClause());
		return sql.toString();
	}

}
