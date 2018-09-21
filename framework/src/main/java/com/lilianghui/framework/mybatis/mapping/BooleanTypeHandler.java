package com.lilianghui.framework.mybatis.mapping;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BooleanTypeHandler implements TypeHandler<Boolean> {

	@Override
	public Boolean getResult(ResultSet resultset, String s) throws SQLException {
		return getValue(resultset.getString(s));
	}

	@Override
	public Boolean getResult(ResultSet resultset, int i) throws SQLException {
		return getValue(resultset.getString(i));
	}

	@Override
	public Boolean getResult(CallableStatement callablestatement, int i) throws SQLException {
		return getValue(callablestatement.getString(i));
	}

	@Override
	public void setParameter(PreparedStatement ps, int index, Boolean parameter, JdbcType jdbcType) throws SQLException {
		if (parameter == null) {
			ps.setString(index, null);
		} else {
			ps.setString(index, Boolean.TRUE.equals(parameter) ? "Y" : "N");
		}
	}

	private Boolean getValue(String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		return value.equalsIgnoreCase("1") || value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("YES");
	}
}
