package com.lilianghui.framework.mybatis.mapping;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class IntegerArrayTypeHandler implements TypeHandler<Integer[]> {
	private static final String SEPARATE = ",";

	@Override
	public void setParameter(PreparedStatement ps, int i, Integer[] parameter, JdbcType jdbcType) throws SQLException {
		String val = null;
		if (ArrayUtils.isNotEmpty(parameter)) {
			val = StringUtils.join(parameter, SEPARATE);
		}
		ps.setString(i, val);
	}

	@Override
	public Integer[] getResult(ResultSet rs, String columnName) throws SQLException {
		return getValue(rs.getString(columnName));
	}

	@Override
	public Integer[] getResult(ResultSet rs, int columnIndex) throws SQLException {
		return getValue(rs.getString(columnIndex));
	}

	@Override
	public Integer[] getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return getValue(cs.getString(columnIndex));
	}

	private Integer[] getValue(Object value) {
		if (value instanceof String) {
			String val = (String) value;
			String[] splits = val.split(SEPARATE);
			Integer[] values = new Integer[splits.length];
			for (int i = 0; i < values.length; i++) {
				values[i] = Integer.valueOf(splits[i]);
			}
			return values;
		}
		return null;
	}
}
