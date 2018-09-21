package com.lilianghui.framework.mybatis.plugin.helper;

import com.lilianghui.framework.mybatis.entity.Entity;
import com.lilianghui.framework.mybatis.plugin.sqlsource.AbstractSqlSource;
import com.lilianghui.framework.mybatis.plugin.sqlsource.PageDynamicSqlSource;
import com.lilianghui.framework.mybatis.plugin.sqlsource.PageRawSqlSource;
import com.lilianghui.framework.mybatis.utils.BridgeHelper;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;

import java.util.List;

public class PageProcessHelper {
    public static final PageProcessHelper instance = new PageProcessHelper();

    private PageProcessHelper() {
    }

    public Object listByPage(Invocation invocation, String defalutOrderBy) throws Exception {
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        Entity baseEntity = BridgeHelper.parseBaseEntity(invocation.getArgs()[1]);
        boolean singleQuery = baseEntity.isSingleQuery();
        SqlSource sqlSource = ms.getSqlSource();
        if (!baseEntity.isPagingQuery()) {
            if (sqlSource instanceof AbstractSqlSource) {
                MetaObject metaObject = SystemMetaObject.forObject(ms);
                metaObject.setValue("sqlSource", ((AbstractSqlSource) sqlSource).getSqlSource());
                Object object = invocation.proceed();
                metaObject.setValue("sqlSource", sqlSource);
                return object;
            } else {
                return invocation.proceed();
            }
        }
        if (baseEntity.isPageIndexQuery()) {
            baseEntity.setStartRowByPageIndex();
        }
        if (!(sqlSource instanceof AbstractSqlSource)) {
            synchronized (ms) {
                if (!(ms.getSqlSource() instanceof AbstractSqlSource)) {
                    AbstractSqlSource dynamicSqlSource = null;
                    if (sqlSource instanceof DynamicSqlSource) {
                        dynamicSqlSource = new PageDynamicSqlSource(sqlSource, defalutOrderBy, singleQuery, false);
                    } else if (sqlSource instanceof RawSqlSource) {
                        dynamicSqlSource = new PageRawSqlSource((SqlSource) SystemMetaObject.forObject(sqlSource).getValue("sqlSource"), defalutOrderBy, singleQuery, false);
                    } else if (sqlSource instanceof StaticSqlSource) {
                        dynamicSqlSource = new PageRawSqlSource(sqlSource, defalutOrderBy, singleQuery, false);
                    } else {
                        throw new RuntimeException("没有为" + ms.getSqlSource().getClass() + "添加分页!");
                    }
                    MetaObject metaObject = SystemMetaObject.forObject(ms);
                    metaObject.setValue("sqlSource", dynamicSqlSource);
                }
            }
        }
        String key = ms.getId() + BridgeHelper.COUNT_SUFFIX;
        MappedStatement countMs = null;
        if (ms.getConfiguration().hasStatement(key)) {
            countMs = ms.getConfiguration().getMappedStatement(key);
        } else {
            synchronized (ms) {
                if (!ms.getConfiguration().hasStatement(key)) {
                    AbstractSqlSource dynamicSqlSource = null;
                    if (ms.getSqlSource() instanceof PageDynamicSqlSource) {
                        dynamicSqlSource = new PageDynamicSqlSource(ms.getSqlSource(), defalutOrderBy, singleQuery, true);
                    } else if (ms.getSqlSource() instanceof PageRawSqlSource) {
                        dynamicSqlSource = new PageRawSqlSource(ms.getSqlSource(), defalutOrderBy, singleQuery, true);
                    }
                    countMs = BridgeHelper.newMappedStatement(ms, key, dynamicSqlSource, Integer.class);
                    ms.getConfiguration().addMappedStatement(countMs);
                }
            }
        }
        invocation.getArgs()[0] = ms.getConfiguration().getMappedStatement(key);
        Object objectval = invocation.proceed();
        Integer totalCount = (Integer) ((List<?>) objectval).get(0);
        baseEntity.setTotalCount(totalCount);
        if (baseEntity.getPageIndex() > baseEntity.getTotalPage()) {
            baseEntity.setPageIndex(baseEntity.getTotalPage());
        }
        invocation.getArgs()[0] = ms;
        Object list = invocation.proceed();
        if (list instanceof List) {
            baseEntity.setCurrentPageRow(((List<?>) list).size());
        }
        return list;
    }
}
