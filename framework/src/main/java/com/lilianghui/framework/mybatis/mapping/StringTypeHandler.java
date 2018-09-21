package com.lilianghui.framework.mybatis.mapping;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.text.ParseException;

public class StringTypeHandler implements TypeHandler<String> {

    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if (StringUtils.isBlank(parameter)) {
            parameter = null;
        }
        if (jdbcType == JdbcType.DATE) {
            if (parameter == null) {
                ps.setDate(i, null);
            } else {
                try {
                    ps.setDate(i, new Date(DateUtils.parseDate(parameter, "").getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            ps.setString(i, parameter);
        }
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        return getValue(rs.getString(columnName));
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        return getValue(rs.getString(columnIndex));
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getValue(cs.getString(columnIndex));
    }

    private String getValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value;
    }

}
