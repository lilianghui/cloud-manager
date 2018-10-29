package com.lilianghui.spring.starter.mybatis;

import com.lilianghui.spring.starter.utils.BridgeHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.loader.ResultLoaderMap;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetWrapper;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

@Slf4j
public class DefaultResultSetHandlerEx extends DefaultResultSetHandler {

    private static Method applyAutomaticMappingsMethod = null;
    private static Method prependPrefixMethod = null;
    private static Method getPropertyMappingValueMethod = null;
    private static Object DEFERED = null;


    static {
        try {
            applyAutomaticMappingsMethod = DefaultResultSetHandler.class.getDeclaredMethod("applyAutomaticMappings"
                    , ResultSetWrapper.class, ResultMap.class, MetaObject.class, String.class);
            applyAutomaticMappingsMethod.setAccessible(true);

            prependPrefixMethod = DefaultResultSetHandler.class.getDeclaredMethod("prependPrefix", String.class, String.class);
            prependPrefixMethod.setAccessible(true);

            getPropertyMappingValueMethod = DefaultResultSetHandler.class.getDeclaredMethod("getPropertyMappingValue",
                    ResultSet.class, MetaObject.class, ResultMapping.class, ResultLoaderMap.class, String.class);
            getPropertyMappingValueMethod.setAccessible(true);

            Field DEFERED_FIELD = DefaultResultSetHandler.class.getDeclaredField("DEFERED");
            DEFERED_FIELD.setAccessible(true);
            DEFERED = DEFERED_FIELD.get(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Configuration configuration;

    public DefaultResultSetHandlerEx(Executor executor, MappedStatement mappedStatement, ParameterHandler parameterHandler, ResultHandler<?> resultHandler, BoundSql boundSql, RowBounds rowBounds) {
        super(executor, mappedStatement, parameterHandler, resultHandler, boundSql, rowBounds);
        this.configuration = mappedStatement.getConfiguration();
    }


    public boolean applyPropertyMappings(ResultSetWrapper rsw, ResultMap resultMap, MetaObject metaObject, ResultLoaderMap lazyLoader, String columnPrefix)
            throws SQLException {
        final List<String> mappedColumnNames = rsw.getMappedColumnNames(resultMap, columnPrefix);
        boolean foundValues = false;
        try {
            final List<ResultMapping> propertyMappings = resultMap.getPropertyResultMappings();
            for (ResultMapping propertyMapping : propertyMappings) {
                String column = (String) prependPrefixMethod.invoke(this, propertyMapping.getColumn(), columnPrefix);
                if (propertyMapping.getNestedResultMapId() != null) {
                    // the user added a column attribute to a nested result map, ignore it
                    column = null;
                }
                if (propertyMapping.isCompositeResult()
                        || (column != null && mappedColumnNames.contains(column.toUpperCase(Locale.ENGLISH)))
                        || propertyMapping.getResultSet() != null) {
                    Object value = getPropertyMappingValueMethod.invoke(this, rsw.getResultSet(), metaObject, propertyMapping, lazyLoader, columnPrefix);
                    // issue #541 make property optional


                    //final String property = propertyMapping.getProperty();
                    String property = BridgeHelper.findProperty(resultMap.getType(), column);
                    if (property == null) {
                        property = metaObject.findProperty(column, configuration.isMapUnderscoreToCamelCase());
                    }


                    if (property == null) {
                        continue;
                    } else if (value == DEFERED) {
                        foundValues = true;
                        continue;
                    }
                    if (value != null) {
                        foundValues = true;
                    }
                    if (value != null || (configuration.isCallSettersOnNulls() && !metaObject.getSetterType(property).isPrimitive())) {
                        // gcode issue #377, call setter on nulls (value is not 'found')
                        metaObject.setValue(property, value);
                    }
                }
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return foundValues;
    }

    public boolean applyAutomaticMappings(ResultSetWrapper rsw, ResultMap resultMap, MetaObject metaObject, String columnPrefix) throws SQLException {
        boolean foundValues = false;
        try {
            foundValues = (boolean) applyAutomaticMappingsMethod.invoke(this, rsw, resultMap, metaObject, columnPrefix);
            BridgeHelper.applyParamsMappings(rsw, resultMap, metaObject, columnPrefix);
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return foundValues;
    }

}
