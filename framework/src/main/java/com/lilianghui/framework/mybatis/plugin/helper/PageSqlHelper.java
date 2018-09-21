package com.lilianghui.framework.mybatis.plugin.helper;

import org.apache.commons.collections.map.CaseInsensitiveMap;

import java.util.Map;

public class PageSqlHelper {
    public static final String HEADER = "{header}";
    public static final String ORDER_BY = "{orderby}";
    public static final String ORDER_BY_OVER = "{orderbyover}";

    private static final Map<String, DialectType> LEFT_JION_MAP = new CaseInsensitiveMap();
    private static final Map<String, DialectType> SINGLE_QUERY_MAP = new CaseInsensitiveMap();

    public static String getSql(String countextSql, String databaseId, boolean singleQuery) {
        if (singleQuery) {
            return SINGLE_QUERY_MAP.get(databaseId).getPrefix() + countextSql + SINGLE_QUERY_MAP.get(databaseId).getSuffix();
        }
        return LEFT_JION_MAP.get(databaseId).getPrefix() + countextSql + LEFT_JION_MAP.get(databaseId).getSuffix();
    }

    public static String getCountSql(String countextSql, String databaseId, boolean singleQuery) {
        if (singleQuery) {
            return SINGLE_QUERY_MAP.get(databaseId).getCountPrefix() + countextSql + SINGLE_QUERY_MAP.get(databaseId).getCountSuffix();
        }
        return LEFT_JION_MAP.get(databaseId).getCountPrefix() + countextSql + LEFT_JION_MAP.get(databaseId).getCountSuffix();
    }

    // 多表查询拼接
    static {
        DialectType mysql = new DialectType();
        mysql.setCountPrefix("SELECT COUNT(DISTINCT  {PKColumn}) FROM(");
        mysql.setCountSuffix(") T");
        mysql.setPrefix("SELECT * FROM ( SELECT CASE WHEN @id != T.{PKColumn} THEN @dense_rank := @dense_rank + 1 ELSE @dense_rank := @dense_rank END AS rownum, @id := T.{PKColumn} AS ke, T.* FROM ( SELECT * FROM (");
        mysql.setSuffix(") T, (SELECT @dense_rank := 0) d, (SELECT @id := '') k " + ORDER_BY + ") T ) t WHERE ROWNUM > {startRow} AND ROWNUM <= {endRow}");
        LEFT_JION_MAP.put("mysql", mysql);

        DialectType oracle = new DialectType();
        oracle.setCountPrefix("SELECT COUNT(DISTINCT NUMROW) FROM(SELECT DENSE_RANK() OVER(" + ORDER_BY_OVER + ") NUMROW FROM (");
        oracle.setCountSuffix(") T) T");
        oracle.setPrefix("SELECT * FROM(SELECT DENSE_RANK() OVER(" + ORDER_BY_OVER + ") NUMROW,T.* FROM (");
        oracle.setSuffix(") T ) T WHERE T.NUMROW >{startRow} AND T.NUMROW <= {endRow} "+ORDER_BY);
        LEFT_JION_MAP.put("oracle", oracle);

        DialectType sqlserver = new DialectType();
        sqlserver.setCountPrefix(HEADER + "SELECT COUNT(DISTINCT NUMROW) FROM(SELECT DENSE_RANK() OVER(" + ORDER_BY_OVER + ") NUMROW FROM (");
        sqlserver.setCountSuffix(") T) T");
        sqlserver.setPrefix(HEADER + "SELECT * FROM(SELECT DENSE_RANK() OVER(" + ORDER_BY_OVER + ") NUMROW,T.* FROM (");
        sqlserver.setSuffix(") T ) T WHERE T.NUMROW >{startRow} AND T.NUMROW <= {endRow} "+ORDER_BY);
        LEFT_JION_MAP.put("sqlserver", sqlserver);
    }

    // 单表查询拼接
    static {
        DialectType mysql = new DialectType();
        mysql.setCountPrefix("SELECT COUNT(*) FROM( ");
        mysql.setCountSuffix(" ) T");
        mysql.setPrefix("");
        mysql.setSuffix(ORDER_BY + " LIMIT {startRow}, {pageSize}");
        SINGLE_QUERY_MAP.put("mysql", mysql);

        DialectType oracle = new DialectType();
        oracle.setCountPrefix("SELECT COUNT(DISTINCT NUMROW) FROM(SELECT DENSE_RANK() OVER(" + ORDER_BY_OVER + ") NUMROW  FROM (");
        oracle.setCountSuffix(") T) T");
        oracle.setPrefix("SELECT * FROM(SELECT DENSE_RANK() OVER(" + ORDER_BY_OVER + ") NUMROW, T.* FROM (");
        oracle.setSuffix(") T ) T WHERE T.NUMROW >{startRow} AND T.NUMROW <= {endRow} "+ORDER_BY);
        SINGLE_QUERY_MAP.put("oracle", oracle);

        DialectType sqlserver = new DialectType();
        sqlserver.setCountPrefix(HEADER + "SELECT COUNT(DISTINCT NUMROW) FROM(SELECT DENSE_RANK() OVER(" + ORDER_BY_OVER + ") NUMROW FROM (");
        sqlserver.setCountSuffix(") T) T");
        sqlserver.setPrefix(HEADER + "SELECT * FROM(SELECT DENSE_RANK() OVER(" + ORDER_BY_OVER + ") NUMROW,T.* FROM (");
        sqlserver.setSuffix(") T ) T WHERE T.NUMROW >{startRow} AND T.NUMROW <= {endRow} "+ORDER_BY);
        SINGLE_QUERY_MAP.put("sqlserver", sqlserver);
    }

    public static class DialectType {
        public DialectType() {
        }

        private String prefix;
        private String suffix;
        private String countPrefix;
        private String countSuffix;

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        public String getCountPrefix() {
            return countPrefix;
        }

        public void setCountPrefix(String countPrefix) {
            this.countPrefix = countPrefix;
        }

        public String getCountSuffix() {
            return countSuffix;
        }

        public void setCountSuffix(String countSuffix) {
            this.countSuffix = countSuffix;
        }
    }
}
