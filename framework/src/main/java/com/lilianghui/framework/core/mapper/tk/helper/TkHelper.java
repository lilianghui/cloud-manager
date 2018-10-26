package com.lilianghui.framework.core.mapper.tk.helper;

import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.entity.EntityField;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class TkHelper {
    private static final List<Class<? extends Annotation>> ANNOTATIONS = new ArrayList<Class<? extends Annotation>>();
    private static final String LIST_PARAMETER = "list";

    static {
        ANNOTATIONS.add(Id.class);
        ANNOTATIONS.add(Column.class);
        ANNOTATIONS.add(ColumnType.class);
        ANNOTATIONS.add(SequenceGenerator.class);
        ANNOTATIONS.add(GeneratedValue.class);
        ANNOTATIONS.add(OrderBy.class);
        ANNOTATIONS.add(KeySql.class);
//		ANNOTATIONS.add(ColumnType.class);
//		ANNOTATIONS.add(RelationMapping.class);
//		ANNOTATIONS.add(Property.class);
    }

    public static boolean isBreak(EntityField field) {
        for (Class<? extends Annotation> clazz : ANNOTATIONS) {
            if (null != field.getAnnotation(clazz)) {
                return false;
            }
        }
        return true;
    }

/*	public static boolean parseField(EntityTable table, EntityField field) {
		EntityTable entityTable = (EntityTable) table;
		if (field.isAnnotationPresent(RelationMapping.class) || field.isAnnotationPresent(Property.class)) {
			entityTable.getResultColumns().add(getField(field));
			return true;
		}
		return false;
	}

	private static Field getField(EntityField field) {
		try {
			Field f = field.getClass().getDeclaredField("field");
			f.setAccessible(true);
			return (Field) f.get(field);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}

	public static ResultMap buildResultMap(Configuration configuration, EntityTable table) {
		EntityTable entityTable = (EntityTable) table;
		if (CollectionUtils.isEmpty(entityTable.getEntityClassColumns()) && CollectionUtils.isEmpty(entityTable.getResultColumns())) {
			return null;
		}
		List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
		buildResultMapping(table, resultMappings, configuration, entityTable.getEntityClassColumns());
		buildRelationMapping(resultMappings, configuration, entityTable.getResultColumns());
		ResultMap.Builder builder = new ResultMap.Builder(configuration, BridgeHelper.getResultMapId(entityTable.getEntityClass()), entityTable.getEntityClass(), resultMappings, true);
		return builder.build();
	}

	private static void buildResultMapping(EntityTable table, List<ResultMapping> resultMappings, Configuration configuration, Set<EntityColumn> entityClassColumns) {
		if (CollectionUtils.isNotEmpty(entityClassColumns)) {
			for (EntityColumn entityColumn : entityClassColumns) {
				String column = entityColumn.getColumn();
				//去掉可能存在的分隔符
				Matcher matcher = EntityTable.DELIMITER.matcher(column);
				if (matcher.find()) {
					column = matcher.group(1);
				}
				ResultMapping.Builder builder = new ResultMapping.Builder(configuration, entityColumn.getProperty(), column, entityColumn.getJavaType());
				if (entityColumn.getJdbcType() != null) {
					builder.jdbcType(entityColumn.getJdbcType());
				}
				if (entityColumn.getTypeHandler() != null) {
					try {
						builder.typeHandler(table.getInstance(entityColumn.getJavaType(), entityColumn.getTypeHandler()));
					} catch (Exception e) {
						throw new MapperException(e);
					}
				}
				List<ResultFlag> flags = new ArrayList<ResultFlag>();
				if (entityColumn.isId()) {
					flags.add(ResultFlag.ID);
				}
				builder.flags(flags);
				resultMappings.add(builder.build());
			}
		}
	}

	private static void buildRelationMapping(List<ResultMapping> resultMappings, Configuration configuration, Set<Field> relationColumns) {
		if (CollectionUtils.isNotEmpty(relationColumns)) {
			for (Field field : relationColumns) {
				resultMappings.add(BridgeHelper.getResultMapping(configuration, field));
			}
		}
	}

	public static void bindType(EntityColumn column, EntityField field) {
		EntityColumn entityColumn = (EntityColumn) column;
		if (field.isAnnotationPresent(ValueStyle.class)) {
			ValueStyle valueStyle = field.getAnnotation(ValueStyle.class);
			entityColumn.setType(valueStyle.type());
			entityColumn.setBindOgnl(valueStyle.value());
		}
		if (field.isAnnotationPresent(ColumnStyle.class)) {
			ColumnStyle columnStyle = field.getAnnotation(ColumnStyle.class);
			entityColumn.setPreName(columnStyle.preName());
			entityColumn.setPostName(columnStyle.postName());
		}
	}

	public static void setIds(SelectKeyGenerator keyGenerator, MappedStatement keyStatement, Executor executor, Configuration configuration, Object parameter, String[] keyProperties) throws Exception {
		List<Object> parameters = new ArrayList<>();
		boolean array = validate(parameter);
		if (array) {
			parameters = (List<Object>) ((StrictMap) parameter).get(LIST_PARAMETER);
		} else {
			parameters.add(parameter);
			StrictMap strictMap = new StrictMap();
			strictMap.put(LIST_PARAMETER, Arrays.asList(parameter));
			parameter = strictMap;
		}
		if(parameters.size()<1){
			return;
		}
		((StrictMap) parameter).put("size", parameters.size());
		List<Object> values = executor.query(keyStatement, parameter, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER);
		if (values.size() == 0) {
			throw new ExecutorException("SelectKey returned no data.");
		} else {
			for (int i = 0; i < parameters.size(); i++) {
				MetaObject metaParam = configuration.newMetaObject(parameters.get(i));
				MetaObject metaResult = configuration.newMetaObject(values.get(i));
				if (keyProperties.length == 1) {
					if (metaResult.hasGetter(keyProperties[0])) {
						keyGenerator.setValue(metaParam, keyProperties[0], metaResult.getValue(keyProperties[0]));
					} else {
						// no getter for the property - maybe just a single value object
						// so try that
						keyGenerator.setValue(metaParam, keyProperties[0], values.get(i));
					}
				} else {
					keyGenerator.handleMultipleProperties(keyProperties, metaParam, metaResult);
				}
			}
		}

	}

	private static boolean validate(Object parameter) {
		if ((parameter instanceof StrictMap) && ((StrictMap) parameter).get(LIST_PARAMETER) instanceof Collection<?>) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public static void appendBindSql(Class<?> entityClass, StringBuilder sql) {
		//获取全部列
		Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
		for (EntityColumn column : columnList) {
			if (ProviderHelper.allowUpdate(column)) {
				sql.append(SqlHelper.getBindValue(column, column.getBindOgnl()));
			}
		}
	}

	public static void appendSql(String entityName, boolean notEmpty, boolean notNull, EntityColumn column, StringBuilder sql) {
		if (ProviderHelper.allowUpdate(column)) {
			String columnHolder = column.getColumn() + " = " + column.getColumnHolder(null, "_bind", null) + ",";
			sql.append(SqlHelper.getIfNotNull(entityName, column, column.getColumnEqualsHolder(entityName) + ",", notEmpty));
			sql.append(SqlHelper.getIfIsNull(entityName, column, columnHolder, notEmpty));
		} else {
			if (notNull) {
				sql.append(SqlHelper.getIfNotNull(entityName, column, column.getColumnEqualsHolder(entityName) + ",", notEmpty));
			} else {
				sql.append(column.getColumnEqualsHolder(entityName) + ",");
			}
		}
	}

	public static SqlSource newSelectKeySqlSource(MappedStatement ms, EntityColumn column, Class<?> entityClass, String IDENTITY, XMLLanguageDriver languageDriver) {
		SqlSource sqlSource = null;
		Boolean xml = null;
		String databaseId = ms.getConfiguration().getDatabaseId();
		if (column.getJavaType().equals(String.class) && IdentityDialect.MYSQL.getIdentityRetrievalStatement().equalsIgnoreCase(IDENTITY)) {
			if ("mysql".equalsIgnoreCase(databaseId)) {
				IDENTITY = "<script><foreach collection=\"list\" separator=\"UNION ALL\">SELECT REPLACE(UUID(),'-','')</foreach></script>";
				xml = Boolean.TRUE;
			} else if ("oracle".equalsIgnoreCase(databaseId)) {
				IDENTITY = "select sys_guid() from dual connect by level <= ${size}";
				xml = Boolean.TRUE;
			} else if (BridgeHelper.isSqlServer(databaseId)) {
				IDENTITY = "<script><foreach collection=\"list\" separator=\"UNION ALL\">SELECT REPLACE(NEWID(),'-','')</foreach></script>";
				xml = Boolean.TRUE;
			}
		}
		if (xml == null) {
			try {
				DocumentHelper.parseText(IDENTITY);
			} catch (Exception e) {
				xml = false;
			}
		}
		if (xml) {
			sqlSource = languageDriver.createSqlSource(ms.getConfiguration(), IDENTITY, null);
		} else {
			sqlSource = new RawSqlSource(ms.getConfiguration(), IDENTITY, entityClass);
		}
		return sqlSource;
	}

	public static String convertByDataBaseId(Config config, Configuration configuration,String name){
		if(!config.isEnableQuotes()){
			return name;
		}
		String databaseId=BridgeHelper.getDialect(configuration.getDatabaseId());
		String open="";
		String close="";
		String[] array = config.getQuotesMap().get(databaseId);
		if(ArrayUtils.isNotEmpty(array)&&array.length==2){
			open=array[0];
			close=array[1];
		}
		return String.format("%s%s%s",open,name,close);
	}*/

    public static boolean isSqlServer(String databaseId) {
        if ("Microsoft SQL Server".equals(databaseId)) {
            return true;
        }
        return false;
    }
}
