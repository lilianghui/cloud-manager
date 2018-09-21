package com.lilianghui.framework.core.mapper.tk.provider;

import com.lilianghui.framework.core.mapper.tk.helper.TkHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;

public class ShareInsertProvider extends MapperTemplate implements InsertProvider {
    private InsertProvider insertProvider;

    public ShareInsertProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    @Override
    public String insertList(MappedStatement ms) {
        return getInsertProvider(ms).insertList(ms);
    }

    @Override
    public String mergeInto(MappedStatement ms) {
        return getInsertProvider(ms).mergeInto(ms);
    }

    @Override
    public String mergeIntoSelective(MappedStatement ms) {
        return getInsertProvider(ms).mergeIntoSelective(ms);
    }

    @Override
    public String mergeIntoForField(MappedStatement ms) {
        return getInsertProvider(ms).mergeIntoForField(ms);
    }

    @Override
    public String duplicateSelectiveByPrimaryKey(MappedStatement ms) {
        return getInsertProvider(ms).duplicateSelectiveByPrimaryKey(ms);
    }

    private InsertProvider getInsertProvider(MappedStatement ms) {
        if (this.insertProvider == null) {
            String databaseId = ms.getConfiguration().getDatabaseId();
            if ("oracle".equalsIgnoreCase(databaseId)) {
                this.insertProvider = new OracleInsertProvider(mapperClass, mapperHelper, this);
            } else if ("mysql".equalsIgnoreCase(databaseId)) {
                this.insertProvider = new MySqlInsertProvider(mapperClass, mapperHelper, this);
            } else if (TkHelper.isSqlServer(databaseId)) {
                this.insertProvider = new SqlServerInsertProvider(mapperClass, mapperHelper, this);
            }
        }
        return this.insertProvider == null ? new NullInsertProvider(mapperClass, mapperHelper, this) : this.insertProvider;
    }

    public boolean isUuid(EntityColumn column) {
        if (column.isIdentity() && StringUtils.isBlank(column.getGenerator()) && String.class.equals(column.getJavaType())) {
            return true;
        }
        return false;
    }

    public boolean isIncrement(EntityColumn column) {
        if ("JDBC".equalsIgnoreCase(column.getGenerator())) {
            return true;
        }
        return false;
    }

    public String getBind(String name, String value) {
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"");
        sql.append(name).append("_bind\" ");
        sql.append("value='").append(value).append("'/>");
        return sql.toString();
    }

    public String getBindValue(String name) {
        StringBuilder sql = new StringBuilder();
        sql.append("${");
        sql.append(name).append("_bind");
        sql.append("}");
        return sql.toString();
    }
}
