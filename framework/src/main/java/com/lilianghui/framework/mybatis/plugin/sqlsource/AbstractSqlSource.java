package com.lilianghui.framework.mybatis.plugin.sqlsource;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lilianghui.framework.mybatis.entity.OrderBy;
import com.lilianghui.framework.mybatis.entity.Entity;
import com.lilianghui.framework.mybatis.plugin.helper.PageSqlHelper;
import com.lilianghui.framework.mybatis.utils.BridgeHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractSqlSource implements SqlSource {
    private static final CaseInsensitiveMap PATTERN_MAP = new CaseInsensitiveMap();
    private static final Map<String, String> MAP = Maps.newHashMap();
    private static final String ORDER_BY = " ORDER BY ";

    protected Configuration configuration;
    protected String databaseId;
    protected MetaObject metaObject;
    protected SqlSource sqlSource;
    protected String defalutOrderBy;

    static {
        MAP.put(PageSqlHelper.HEADER, "(#header\\{(.*?)header\\})");
        MAP.put(PageSqlHelper.ORDER_BY, "(#orderby\\{(.*?)\\})");
        MAP.put(PageSqlHelper.ORDER_BY_OVER, "(#overby\\{(.*?)\\})");
    }

    public AbstractSqlSource(SqlSource sqlSource, String defalutOrderBy) {
        this.defalutOrderBy = defalutOrderBy;
        this.sqlSource = sqlSource;
        this.metaObject = SystemMetaObject.forObject(sqlSource);
        this.configuration = (Configuration) metaObject.getValue("configuration");
        this.databaseId = BridgeHelper.getDialect(configuration.getDatabaseId());

    }

    public SqlSource getSqlSource() {
        return sqlSource;
    }

    private String replace(boolean flag, Entity baseEntity, String[] keys, String sql) {
        for (String key : keys) {
            Pattern pattern = (Pattern) PATTERN_MAP.get(key);
            if (pattern == null) {
                pattern = Pattern.compile(MAP.get(key), Pattern.CASE_INSENSITIVE);
                PATTERN_MAP.put(key, pattern);
            }
            String val = "";
            String replace = "";
            Matcher matcher = pattern.matcher(sql);
            if (matcher.find()) {
                replace = matcher.group(1);
                val = matcher.group(2);
            }
            val = converOrderBy(flag, baseEntity, key, val);
            sql = sql.replace(key, val);
            sql = sql.replace(replace, "");
        }
        return sql;

    }

    /**
     * @param flag
     * @param singleQuery
     * @param sql
     * @param baseEntity
     * @return
     */
    protected String replace(boolean flag, boolean singleQuery, String sql, Entity baseEntity) {
        sql = sql.replaceAll("[\\s]+", " ");
        sql = sql.replace("{startRow}", String.valueOf(baseEntity.getStartRow()));
        sql = sql.replace("{endRow}", String.valueOf(baseEntity.getEndRow()));
        sql = sql.replace("{pageSize}", String.valueOf(baseEntity.getPageSize()));
        String PKColumn = getPKColumn(baseEntity);
        sql = sql.replace("{PKColumn}", PKColumn);
        if (sql.indexOf("#overby") >= 0) {
            sql = replace(flag, baseEntity, new String[]{PageSqlHelper.HEADER, PageSqlHelper.ORDER_BY, PageSqlHelper.ORDER_BY_OVER}, sql);
        } else {
            sql = sql.replace(PageSqlHelper.ORDER_BY_OVER, converOrderByOver(baseEntity));
            sql = replace(flag, baseEntity, new String[]{PageSqlHelper.HEADER, PageSqlHelper.ORDER_BY}, sql);
        }
        return sql;
    }

    protected String converOrderByOver(Entity baseEntity) {
        String sql = "";
        boolean mysql = "mysql".equalsIgnoreCase(this.databaseId);
        if (!mysql || (mysql && !baseEntity.isSingleQuery())) {
            sql = ORDER_BY + getPKColumn(baseEntity);
        }
        return sql;
    }

    protected String converOrderBy(boolean flag, Entity baseEntity, String key, String sql) {
        if (key.equals(PageSqlHelper.ORDER_BY_OVER)) {
            sql = ORDER_BY + sql.replaceAll("(?i)order(\\s*)by", "")+ ","+getPKColumn(baseEntity);
        } else if (key.equals(PageSqlHelper.ORDER_BY) && !flag) {
            String sort = getSorting(baseEntity);
            boolean isBlank = StringUtils.isBlank(sql);
            if (isBlank) {
                sql = ORDER_BY + sort;
            } else {
                sort = StringUtils.isNotBlank(sort) ? sort + "," : "";
                sql = ORDER_BY + sort + sql.replaceAll("(?i)order(\\s*)by", "");
            }
        }
        if (ORDER_BY.equals(sql)) {
            sql = "";
        }
        return sql;
    }

    private String getSorting(Entity baseEntity) {
        StringBuffer sb = new StringBuffer();
        List<OrderBy> orderBys = baseEntity.getOrderBys();
        if (CollectionUtils.isNotEmpty(orderBys)) {
            //sb.append(" ORDER BY  ");
            List<String> list = Lists.newArrayList();
            for (OrderBy orderBy : orderBys) {
//                String column = BridgeHelper.getOrderColumn(baseEntity.getClass(), orderBy);
//                list.add(column + " " + orderBy.getSortDisplayText() + " ");
            }
            sb.append(StringUtils.join(list, ","));
        }
        return sb.toString();
    }

    private String getPKColumn(Entity baseEntity) {
        String PKColumn = baseEntity.getPkColumn();
        PKColumn = StringUtils.isBlank(PKColumn) ? "ID" : PKColumn;
        return PKColumn;
    }

}
