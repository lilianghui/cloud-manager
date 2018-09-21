package com.lilianghui.framework.core.mapper.tk.provider;

import com.lilianghui.framework.core.mapper.tk.helper.Ognl;
import com.lilianghui.framework.core.mapper.tk.helper.ProviderHelper;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.*;

import java.util.Set;

public class SqlServerInsertProvider extends MapperTemplate implements InsertProvider {
	private ShareInsertProvider insertProvider;

	public SqlServerInsertProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	public SqlServerInsertProvider(Class<?> mapperClass, MapperHelper mapperHelper, ShareInsertProvider insertProvider) {
		super(mapperClass, mapperHelper);
		this.insertProvider = insertProvider;
	}

	@Override
	public String insertList(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		// 开始拼sql
		StringBuilder sql = new StringBuilder();
		// 获取全部列
		sql.append("<choose><when test=\"null != list and list.size > 0\">");
		Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
		sql.append(ProviderHelper.getBindSql(this, columnList, false));
		sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
		sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
		sql.append(" VALUES ");
		sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
		String prefix = "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">";
		sql.append(ProviderHelper.getColumnValuesSql(this, "record", prefix, columnList, false));
		sql.append("</foreach>");
		EntityColumn PKColumn = EntityHelper.getPKColumns(entityClass).iterator().next();
		if (insertProvider.isUuid(PKColumn)) {
			SelectKeyHelper.newSelectKeyMappedStatement(ms, PKColumn,entityClass ,isBEFORE(), getIDENTITY(PKColumn));
		}
		sql.append("</when><otherwise>SELECT 1 </otherwise></choose>");
		return sql.toString();
	}

	@Override
	public String mergeInto(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		// 开始拼sql
		StringBuilder sql = new StringBuilder();
		Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
		sql.append(ProviderHelper.getBindSql(this, columnList));
		sql.append(insertProvider.getBind("using", "@" + Ognl.class.getName() + "@mergeUsing(_parameter,\"\")"));
		String tableName = SqlHelper.getDynamicTableName(entityClass, tableName(entityClass));
		sql.append("MERGE INTO ").append(tableName).append(" T USING ").append(insertProvider.getBindValue("using"));
		sql.append(" WHEN NOT MATCHED THEN INSERT");
		sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
		String prefix = "<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">";
		sql.append(ProviderHelper.getColumnValuesSql(this, "record", prefix, columnList, false));
		sql.append("WHEN MATCHED THEN UPDATE ");
		sql.append(SqlHelper.updateSetColumns(entityClass, "record", false, false));
		sql.append(";");
		return sql.toString();
	}

	@Override
	public String mergeIntoSelective(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		// 开始拼sql
		StringBuilder sql = new StringBuilder();
		Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
		sql.append(ProviderHelper.getBindSql(this, columnList));
		String tableName = SqlHelper.getDynamicTableName(entityClass, tableName(entityClass));
		sql.append(insertProvider.getBind("using", "@" + Ognl.class.getName() + "@mergeUsing(_parameter,\"\")"));
		sql.append("MERGE INTO ").append(tableName).append(" T ");
		sql.append("USING ").append(insertProvider.getBindValue("using"));
		sql.append(" WHEN NOT MATCHED THEN INSERT");
		sql.append(ProviderHelper.getDynamicColumnSql(this, columnList, false, "record"));
		sql.append(ProviderHelper.getDynamicColumnValuesSql(this, columnList, false, "record"));
		sql.append("WHEN MATCHED THEN UPDATE ");
		sql.append(SqlHelper.updateSetColumns(entityClass, "record", true, isNotEmpty()));
		sql.append(";");
		return sql.toString();
	}

	@Override
	public String mergeIntoForField(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		// 开始拼sql
		StringBuilder sql = new StringBuilder();
		Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
		sql.append(ProviderHelper.getBindSql(this, columnList));
		String tableName = SqlHelper.getDynamicTableName(entityClass, tableName(entityClass));
		sql.append(insertProvider.getBind("using", "@" + Ognl.class.getName() + "@mergeUsing(_parameter,\"\")"));
		sql.append("MERGE INTO ").append(tableName).append(" T ");
		sql.append("USING ").append(insertProvider.getBindValue("using"));
		sql.append(" WHEN NOT MATCHED THEN INSERT");
		sql.append(ProviderHelper.getDynamicColumnSql(this, columnList, false, "record"));
		sql.append(ProviderHelper.getDynamicColumnValuesSql(this, columnList, false, "record"));
		sql.append("WHEN MATCHED THEN UPDATE ");
		sql.append(ProviderHelper.updateSetColumns(entityClass, "record", false, isNotEmpty()));
		sql.append(";");
		return sql.toString();
	}


	@Override
	public String duplicateSelectiveByPrimaryKey(MappedStatement ms) {
		return null;
	}

}
