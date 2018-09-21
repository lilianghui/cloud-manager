package com.lilianghui.framework.core.mapper.tk.provider;

import com.lilianghui.framework.core.mapper.tk.helper.ProviderHelper;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.ognl.Ognl;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.*;

import java.util.Set;

public class MySqlInsertProvider extends MapperTemplate implements InsertProvider {
	private ShareInsertProvider insertProvider;

	public MySqlInsertProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	public MySqlInsertProvider(Class<?> mapperClass, MapperHelper mapperHelper, ShareInsertProvider insertProvider) {
		super(mapperClass, mapperHelper);
		this.insertProvider = insertProvider;
	}

	@Override
	public String duplicateSelectiveByPrimaryKey(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
		// 开始拼sql
		StringBuilder sql = new StringBuilder();
		sql.append(insertProvider.getBind("fields", "@" + Ognl.class.getName() + "@fieldToString(_parameter)"));
		sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
		sql.append("(").append(insertProvider.getBindValue("fields")).append(")");
		sql.append(" VALUES ");
		sql.append("<foreach collection=\"records\" item=\"record\" separator=\",\" >");
		sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
		for (EntityColumn column : columnList) {
			sql.append("<if test=\"@" + Ognl.class.getName() + "@hasField(_parameter,'" + column.getProperty() + "')\">");
			sql.append(column.getColumnHolder("record") + ",");
			sql.append("</if>");
		}
		sql.append("</trim>");
		sql.append("</foreach>");
		sql.append("ON DUPLICATE KEY UPDATE");
		sql.append("<trim suffixOverrides=\",\">");
		for (EntityColumn column : columnList) {
			if (column.isId()) {
				continue;
			}
			sql.append("<if test=\"@" + Ognl.class.getName() + "@hasField(_parameter,'" + column.getProperty() + "')\">");
			sql.append(column.getColumn()).append("=VALUES(").append(column.getColumn()).append("),");
			sql.append("</if>");
		}
		sql.append("</trim>");
		return sql.toString();
	}

	@Override
	public String insertList(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		// 开始拼sql
		StringBuilder sql = new StringBuilder();
		EntityColumn PKColumn = EntityHelper.getPKColumns(entityClass).iterator().next();
		// 获取全部列
		sql.append("<choose><when test=\"null != list and list.size > 0\">");
		Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
		sql.append(ProviderHelper.getBindSql(this, columnList));
		sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
		sql.append(SqlHelper.insertColumns(entityClass, insertProvider.isIncrement(PKColumn), false, false));
		sql.append(" VALUES ");
		sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
		String prefix = "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">";
		sql.append(ProviderHelper.getColumnValuesSql(this, "record", prefix, columnList, insertProvider.isIncrement(PKColumn)));
		sql.append("</foreach>");
		if (insertProvider.isUuid(PKColumn)) {
			SelectKeyHelper.newSelectKeyMappedStatement(ms, PKColumn,entityClass ,isBEFORE(), getIDENTITY(PKColumn));
		}
		sql.append("</when><otherwise>SELECT 1 FROM DUAL</otherwise></choose>");
		return sql.toString();
	}

	@Override
	public String mergeInto(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		EntityColumn PKColumn = EntityHelper.getPKColumns(entityClass).iterator().next();
		// 开始拼sql
		StringBuilder sql = new StringBuilder();
		String tableName = SqlHelper.getDynamicTableName(entityClass, tableName(entityClass));
		Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
		sql.append(ProviderHelper.getBindSql(this, columnList, insertProvider.isIncrement(PKColumn)));
		sql.append("REPLACE  INTO ").append(tableName);
		sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
		String prefix = "<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">";
		sql.append(ProviderHelper.getColumnValuesSql(this, "", prefix, columnList, insertProvider.isIncrement(PKColumn)));
		return sql.toString();
	}

	@Override
	public String mergeIntoSelective(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		EntityColumn PKColumn = EntityHelper.getPKColumns(entityClass).iterator().next();
		// 开始拼sql
		StringBuilder sql = new StringBuilder();
		String tableName = SqlHelper.getDynamicTableName(entityClass, tableName(entityClass));
		Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
		sql.append(ProviderHelper.getBindSql(this, columnList));
		sql.append("REPLACE  INTO ").append(tableName);
		sql.append(ProviderHelper.getDynamicColumnSql(this, columnList, insertProvider.isIncrement(PKColumn)));
		sql.append(ProviderHelper.getDynamicColumnValuesSql(this, columnList, insertProvider.isIncrement(PKColumn)));
		return sql.toString();
	}

	@Override
	public String mergeIntoForField(MappedStatement ms) {
		return null;
	}

}
