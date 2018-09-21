package com.lilianghui.framework.mybatis.mapping;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class StringArrayTypeHandler implements TypeHandler<String[]> {
	private static final String SEPARATE = ",";

	@Override
	public void setParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType) throws SQLException {
		String val = null;
		if (ArrayUtils.isNotEmpty(parameter)) {
			val = StringUtils.join(parameter, SEPARATE);
		}
		ps.setString(i, val);
	}

	@Override
	public String[] getResult(ResultSet rs, String columnName) throws SQLException {
		return getValue(rs.getString(columnName));
	}

	@Override
	public String[] getResult(ResultSet rs, int columnIndex) throws SQLException {
		return getValue(rs.getString(columnIndex));
	}

	@Override
	public String[] getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return getValue(cs.getString(columnIndex));
	}

	private String[] getValue(Object value) {
		if (value instanceof String) {
			String val = (String) value;
			return val.split(SEPARATE);
		}
		return null;
	}
}
