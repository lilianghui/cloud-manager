package com.lilianghui.framework.mybatis.plugin.helper;

import com.lilianghui.framework.mybatis.entity.Entity;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.WithItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.LinkedList;

@Slf4j
public class SQLParseHelper {
    public static String parse(String sql, String databaseId, boolean flag, Object parameterObject) {

        try {
            Entity baseEntity = (Entity) parameterObject;
            String header = "";
            String orderby = " ORDER BY ID";
            Statement stmt = CCJSqlParserUtil.parse(sql);
            if (!(stmt instanceof Select)) {
                throw new RuntimeException("");
            }
            Select select = (Select) stmt;
            PlainSelect selectBody = (PlainSelect) select.getSelectBody();

            StringBuffer stringBuffer = new StringBuffer();
            if (CollectionUtils.isNotEmpty(select.getWithItemsList())) {
                LinkedList linkedList = new LinkedList();
                for (WithItem withItem : select.getWithItemsList()) {
                    linkedList.add(withItem.toString());
                }
                String withSql = StringUtils.join(linkedList, ",");
                stringBuffer.append("WITH ");
                stringBuffer.append(withSql);
                orderby = selectBody.orderByToString(selectBody.isOracleSiblings(), selectBody.getOrderByElements());
            }
            String body = toString(selectBody);
            if (flag) {
                stringBuffer.append(PageSqlHelper.getCountSql(body, databaseId, baseEntity.isSingleQuery()));
            } else {
                stringBuffer.append(PageSqlHelper.getSql(body, databaseId, baseEntity.isSingleQuery()));
            }
            sql = stringBuffer.toString();
            sql = sql.replace("{header}", header);
            sql = sql.replace("{orderby}", orderby);
            //System.out.println(sql);
            sql = sql.replace("{startRow}", String.valueOf(baseEntity.getStartRow()));
            sql = sql.replace("{endRow}", String.valueOf(baseEntity.getEndRow()));
            sql = sql.replace("{pageSize}", String.valueOf(baseEntity.getPageSize()));
            String PKColumn = baseEntity.getPkColumn();
            PKColumn = StringUtils.isBlank(PKColumn) ? "ID" : PKColumn;
            sql = sql.replace("{PKColumn}", PKColumn);
        } catch (JSQLParserException e) {
            //log.error(e.getMessage(),e);
        }

        return sql;
    }

    private static String toString(PlainSelect selectBody) {
        StringBuilder sql = new StringBuilder();
        if (selectBody.isUseBrackets()) {
            sql.append("(");
        }
        sql.append("SELECT ");

        if (selectBody.getOracleHint() != null) {
            sql.append(selectBody.getOracleHint()).append(" ");
        }

        if (selectBody.getSkip() != null) {
            sql.append(selectBody.getSkip()).append(" ");
        }

        if (selectBody.getFirst() != null) {
            sql.append(selectBody.getFirst()).append(" ");
        }

        if (selectBody.getDistinct() != null) {
            sql.append(selectBody.getDistinct()).append(" ");
        }
        if (selectBody.getTop() != null) {
            sql.append(selectBody.getTop()).append(" ");
        }
        sql.append(selectBody.getStringList(selectBody.getSelectItems()));

        if (selectBody.getIntoTables() != null) {
            sql.append(" INTO ");
            for (Iterator<Table> iter = selectBody.getIntoTables().iterator(); iter.hasNext(); ) {
                sql.append(iter.next().toString());
                if (iter.hasNext()) {
                    sql.append(", ");
                }
            }
        }

        if (selectBody.getFromItem() != null) {
            sql.append(" FROM ").append(selectBody.getFromItem());
            if (selectBody.getJoins() != null) {
                Iterator<Join> it = selectBody.getJoins().iterator();
                while (it.hasNext()) {
                    Join join = it.next();
                    if (join.isSimple()) {
                        sql.append(", ").append(join);
                    } else {
                        sql.append(" ").append(join);
                    }
                }
            }
            if (selectBody.getWhere() != null) {
                sql.append(" WHERE ").append(selectBody.getWhere());
            }
            if (selectBody.getOracleHierarchical() != null) {
                sql.append(selectBody.getOracleHierarchical().toString());
            }
            sql.append(selectBody.getFormatedList(selectBody.getGroupByColumnReferences(), "GROUP BY"));
            if (selectBody.getHaving() != null) {
                sql.append(" HAVING ").append(selectBody.getHaving());
            }
            //sql.append(selectBody.orderByToString(selectBody.isOracleSiblings(), selectBody.getOrderByElements()));
            if (selectBody.getLimit() != null) {
                sql.append(selectBody.getLimit());
            }
            if (selectBody.getOffset() != null) {
                sql.append(selectBody.getOffset());
            }
            if (selectBody.getFetch() != null) {
                sql.append(selectBody.getFetch());
            }
            if (selectBody.isForUpdate()) {
                sql.append(" FOR UPDATE");

                if (selectBody.getForUpdateTable() != null) {
                    sql.append(" OF ").append(selectBody.getForUpdateTable());
                }
            }
        } else {
            //without from
            if (selectBody.getWhere() != null) {
                sql.append(" WHERE ").append(selectBody.getWhere());
            }
        }
        if (selectBody.isUseBrackets()) {
            sql.append(")");
        }
        return sql.toString();
    }
}
