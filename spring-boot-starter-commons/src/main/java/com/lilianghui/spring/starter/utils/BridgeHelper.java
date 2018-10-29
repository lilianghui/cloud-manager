package com.lilianghui.spring.starter.utils;

import com.lilianghui.spring.starter.annotation.Property;
import com.lilianghui.spring.starter.annotation.RelationMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.resultset.ResultSetWrapper;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;
import org.springframework.util.ClassUtils;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@Slf4j
public class BridgeHelper {
    private static final boolean tkMybatisPresent = ClassUtils.isPresent("tk.mybatis.mapper.entity.EntityColumn", BridgeHelper.class.getClassLoader());
    public static final String COUNT_SUFFIX = "_Count";
    private static final String PARAMS = "params_";
    private static final String PROPERTY = "params";
    private static final Map<Class<?>, Map<String, String>> TABLE_COLUMN_REF = new HashMap<>();

    public static String getResultMapId(Class<?> entityClass) {
        return entityClass.getName() + "-BaseResultMap";
    }

    public static MappedStatement newMappedStatement(MappedStatement ms) {
        return newMappedStatement(ms, ms.getId(), ms.getSqlSource(), null);
    }

    public static MappedStatement newMappedStatement(MappedStatement ms, String id, SqlSource sqlSource, Class<?> returnType) {
        Configuration configuration = ms.getConfiguration();
        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id, sqlSource, ms.getSqlCommandType());
        statementBuilder.resource(ms.getResource());
        statementBuilder.fetchSize(ms.getFetchSize());
        statementBuilder.statementType(ms.getStatementType());
        statementBuilder.keyGenerator(ms.getKeyGenerator());
        statementBuilder.keyProperty(StringUtils.join(ms.getKeyProperties(), ","));
        statementBuilder.keyColumn(StringUtils.join(ms.getKeyColumns(), ","));
        statementBuilder.databaseId(ms.getDatabaseId());
        statementBuilder.lang(configuration.getDefaultScriptingLanuageInstance());
        statementBuilder.resultOrdered(ms.isResultOrdered());
        statementBuilder.resulSets(StringUtils.join(ms.getResulSets(), ","));
        statementBuilder.timeout(configuration.getDefaultStatementTimeout());

        statementBuilder.parameterMap(ms.getParameterMap());

        if (returnType != null) {
            List<ResultMap> resultMaps = new ArrayList<ResultMap>();
            ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(ms.getConfiguration(), id, returnType, new ArrayList<ResultMapping>(), null);
            resultMaps.add(inlineResultMapBuilder.build());
            statementBuilder.resultMaps(resultMaps);
        } else {
            statementBuilder.resultMaps(ms.getResultMaps());
        }
        statementBuilder.resultSetType(ms.getResultSetType());

        statementBuilder.flushCacheRequired(ms.isFlushCacheRequired());
        statementBuilder.useCache(ms.isUseCache());
        statementBuilder.cache(ms.getCache());

        MappedStatement statement = statementBuilder.build();
        return statement;
    }

    public static ResultMapping getResultMapping(Configuration configuration, Field field) {
        if (field.isAnnotationPresent(Transient.class) || Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
            return null;
        }
        String property = field.getName();
        String column = null;
        String nestedResultMapId = null;
        String columnPrefix = null;
        Class<?> type = field.getType();
        JdbcType jdbcType = null;
        Class<? extends TypeHandler<?>> typeHandler = null;
        if (field.isAnnotationPresent(RelationMapping.class)) {
            RelationMapping relationMapping = field.getAnnotation(RelationMapping.class);
            columnPrefix = relationMapping.columnPrefix();
            if (Collection.class.isAssignableFrom(field.getType()) && !Class.class.equals(relationMapping.ofType())) {
                nestedResultMapId = getResultMapId(relationMapping.ofType());
            } else if (StringUtils.isBlank(nestedResultMapId)) {
                nestedResultMapId = getResultMapId(field.getType());
            }
            if (StringUtils.isBlank(nestedResultMapId)) {
                throw new IllegalArgumentException("RelationMapping结果集映射不能为空");
            }
        } else if (field.isAnnotationPresent(Property.class)) {
            Property p = field.getAnnotation(Property.class);
            column = p.column();
            jdbcType = p.jdbcType();
            typeHandler = p.typeHandler();
        } else if (field.isAnnotationPresent(Column.class)) {
            Column p = field.getAnnotation(Column.class);
            column = p.name();
            if (tkMybatisPresent) {
                ColumnType columnType = field.getAnnotation(ColumnType.class);
                if (columnType != null) {
                    jdbcType = columnType.jdbcType();
                    typeHandler = columnType.typeHandler();
                }
            }
        }
        column = StringUtils.isBlank(column) ? field.getName() : column;
        ResultMapping.Builder builder = new ResultMapping.Builder(configuration, property, column, type);
        if (jdbcType != null && JdbcType.UNDEFINED != jdbcType) {
            builder.jdbcType(jdbcType);
        }
        if (typeHandler != null && !UnknownTypeHandler.class.equals(typeHandler)) {
            try {
                builder.typeHandler(typeHandler.newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.isNotBlank(nestedResultMapId)) {
            builder.nestedResultMapId(nestedResultMapId);
        }
        if (StringUtils.isNotBlank(columnPrefix)) {
            builder.columnPrefix(columnPrefix);
        }
        if (field.isAnnotationPresent(Id.class)) {
            List<ResultFlag> flags = new ArrayList<>();
            flags.add(ResultFlag.ID);
            builder.flags(flags);
        }
        return builder.build();
    }

    public static void applyParamsMappings(ResultSetWrapper rsw, ResultMap resultMap, MetaObject metaObject, String columnPrefix) {
        try {
            for (String column : rsw.getUnmappedColumnNames(resultMap, columnPrefix)) {
                String prefix = ((columnPrefix == null ? "" : columnPrefix) + PARAMS).toLowerCase();
                if (column.toLowerCase().startsWith(prefix)) {
                    Object object = metaObject.getValue(PROPERTY);
                    if (object != null && object instanceof Map) {
                        String name = column.substring(prefix.length());
                        ((Map<String, Object>) metaObject.getValue(PROPERTY)).put(name, rsw.getResultSet().getObject(column));
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static String findProperty(Class<?> type, String propertyName) {
        try {
            if (tkMybatisPresent) {
                Map<String, String> tableColumn = TABLE_COLUMN_REF.get(type);
                if (tableColumn == null) {
                    TABLE_COLUMN_REF.put(type, new HashMap<String, String>());
                    tableColumn = TABLE_COLUMN_REF.get(type);
                    for (EntityColumn column : EntityHelper.getEntityTable(type).getEntityClassColumns()) {
                        tableColumn.put(column.getColumn(), column.getProperty());
                    }
                }
                return tableColumn.get(propertyName);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static boolean isSqlServer(String databaseId) {
        if ("Microsoft SQL Server".equals(databaseId)) {
            return true;
        }
        return false;
    }

    public static String getDialect(String databaseId) {
        if (isSqlServer(databaseId)) {
            return "sqlserver";
        }
        return databaseId;
    }
}
