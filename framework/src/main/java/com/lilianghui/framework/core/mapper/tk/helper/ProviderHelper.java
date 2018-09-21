package com.lilianghui.framework.core.mapper.tk.helper;


import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.util.StringUtil;

import java.util.Set;

public class ProviderHelper {

	public static String getBindSql(MapperTemplate context, Set<EntityColumn> columnList) {
		return getBindSql(context, columnList, true);
	}

	public static String getBindSql(MapperTemplate context, Set<EntityColumn> columnList, boolean skipUuid) {
		StringBuilder sql = new StringBuilder();
		for (EntityColumn column : columnList) {
			if (!column.isInsertable()) {
				continue;
			}
//			if (skipUuid && column.isUuid()) {
//				sql.append(SqlHelper.getBindValue(column, context.getUUID()));
//			}
//			if (ProviderHelper.allowInsert(column)) {
//				sql.append(SqlHelper.getBindValue(column, column.getBindOgnl()));
//			}
		}
		return sql.toString();
	}

	public static String getDynamicColumnSql(MapperTemplate context, Set<EntityColumn> columnList, boolean skipId) {
		return getDynamicColumnSql(context, columnList, skipId, null);
	}
	
	public static String getDynamicColumnSql(MapperTemplate context, Set<EntityColumn> columnList, boolean skipId,String entityName) {
		StringBuilder sql = new StringBuilder();
		sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
		for (EntityColumn column : columnList) {
			if (!column.isInsertable() || (skipId && column.isId())) {
				continue;
			}
			if (ProviderHelper.allowInsert(column)) {
				sql.append(column.getColumn() + ",");
			} else {
				sql.append(SqlHelper.getIfNotNull(entityName,column, column.getColumn() + ",", context.isNotEmpty()));
			}
		}
		sql.append("</trim>");
		return sql.toString();
	}

	public static String getDynamicColumnValuesSql(MapperTemplate context, Set<EntityColumn> columnList, boolean skipId) {
		return getDynamicColumnValuesSql(context, columnList, skipId, null);
	}
	
	public static String getDynamicColumnValuesSql(MapperTemplate context, Set<EntityColumn> columnList, boolean skipId,String entityName) {
		StringBuilder sql = new StringBuilder();
		sql.append("<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">");
		for (EntityColumn column : columnList) {
			if (!column.isInsertable() || (skipId && column.isId())) {
				continue;
			}
			if (ProviderHelper.allowInsert(column)) {
				sql.append(SqlHelper.getIfIsNull(entityName,column, column.getColumnHolder(null, "_bind", ","), context.isNotEmpty()));
			}
			sql.append(SqlHelper.getIfNotNull(entityName,column, column.getColumnHolder(entityName, null, ","), context.isNotEmpty()));
		}
		sql.append("</trim>");
		return sql.toString();
	}

	public static String getColumnValuesSql(MapperTemplate context, String entityName, String prefix, Set<EntityColumn> columnList, boolean skipId) {
		if (StringUtil.isEmpty(entityName)) {
			entityName = "";
		}
		StringBuilder sql = new StringBuilder();
		boolean isNotEmpty = context.isNotEmpty();
		sql.append(prefix);
		for (EntityColumn column : columnList) {
			if (!column.isInsertable() || (skipId && column.isId())) {
				continue;
			}
			// 优先使用传入的属性值,当原属性property!=null时，用原属性
			// //自增的情况下,如果默认有值,就会备份到property_cache中,所以这里需要先判断备份的值是否存在
			// if (!column.isIdentity()) {
			// 其他情况值仍然存在原property中
			sql.append(SqlHelper.getIfNotNull(entityName, column, column.getColumnHolder(entityName, null, ","), isNotEmpty));
			// }
			// 当属性为null时，如果存在主键策略，会自动获取值，如果不存在，则使用null
			// 序列的情况
			/*if (StringUtil.isNotEmpty(column.getSequenceName())) {
				sql.append(SqlHelper.getIfIsNull(entityName, column, context.getSeqNextVal(column) + " ,", false));
			} else if (column.isUuid()) {
				sql.append(SqlHelper.getIfIsNull(entityName, column, column.getColumnHolder(null, "_bind", ","), isNotEmpty));
			} else */if (ProviderHelper.allowInsert(column)) {
				sql.append(SqlHelper.getIfIsNull(entityName, column, column.getColumnHolder(null, "_bind", ","), isNotEmpty));
			} else {
				// 当null的时候，如果不指定jdbcType，oracle可能会报异常，指定VARCHAR不影响其他
				sql.append(SqlHelper.getIfIsNull(entityName, column, column.getColumnHolder(entityName, null, ","), isNotEmpty));
			}
		}
		sql.append("</trim>");
		return sql.toString();
	}

	/**
	 * update set列
	 *
	 * @param entityClass
	 * @param entityName
	 *            实体映射名
	 * @param notNull
	 *            是否判断!=null
	 * @param notEmpty
	 *            是否判断String类型!=''
	 * @return
	 */
	public static String updateSetColumns(Class<?> entityClass, String entityName, boolean notNull, boolean notEmpty) {
		StringBuilder sql = new StringBuilder();
		// 获取全部列
		Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
		for (EntityColumn column : columnList) {
			if (ProviderHelper.allowUpdate(column)) {
//				sql.append(SqlHelper.getBindValue(column, column.getBindOgnl()));
			}
		}
		sql.append("<set>");
		// 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
		for (EntityColumn column : columnList) {
			if (!column.isId()) {
//				if (ProviderHelper.allowUpdate(column)) {
//					String columnHolder = column.getColumn() + " = " + column.getColumnHolder(null, "_bind", null) + ",";
//					sql.append(SqlHelper.getIfNotNull(entityName, column, column.getColumnEqualsHolder(entityName) + ",", notEmpty));
//					sql.append(SqlHelper.getIfIsNull(entityName, column, columnHolder, notEmpty));
//				}
				sql.append("<if test=\"@" + Ognl.class.getName() + "@hasField(_parameter,'" + column.getProperty() + "')\">");
				if (notNull) {
					sql.append(SqlHelper.getIfNotNull(entityName, column, column.getColumnEqualsHolder(entityName) + ",", notEmpty));
				} else {
					sql.append(column.getColumnEqualsHolder(entityName) + ",");
				}
				sql.append("</if>");
			}
		}
		sql.append("</set>");
		return sql.toString();
	}

	public static String wherePKColumns(Class<?> entityClass, String entityName) {
		StringBuilder sql = new StringBuilder();
		sql.append("<where>");
		// 获取全部列
		Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
		// 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
		for (EntityColumn column : columnList) {
			sql.append(" AND " + column.getColumnEqualsHolder(entityName));
		}
		sql.append("</where>");
		return sql.toString();
	}

	public static boolean allowInsert(EntityColumn column) {
//		return column.isInsertable() && (column.getType() == ValueType.ALL || column.getType() == ValueType.INSERT);
		return false;
	}

	public static boolean allowUpdate(EntityColumn column) {
//		return column.isUpdatable() && (column.getType() == ValueType.ALL || column.getType() == ValueType.UPDATE);
		return false;
	}
}
