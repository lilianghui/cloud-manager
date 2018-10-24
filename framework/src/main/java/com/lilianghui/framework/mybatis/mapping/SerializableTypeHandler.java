package com.lilianghui.framework.mybatis.mapping;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

@MappedTypes({ Serializable.class })
public class SerializableTypeHandler implements TypeHandler<Serializable> {

	@Override
	public void setParameter(PreparedStatement ps, int i, Serializable parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, (String) parameter);
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
	public Serializable getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return getValue(cs.getString(columnIndex));
	}

	private String getValue(String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		return value;
	}

}
