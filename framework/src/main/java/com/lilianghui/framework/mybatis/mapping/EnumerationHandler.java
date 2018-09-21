package com.lilianghui.framework.mybatis.mapping;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumerationHandler<E extends Enum<?>> extends BaseTypeHandler<E> {
	private Class<? extends Enum<?>> type;
	private E[] enums;

	public EnumerationHandler(Class<? extends Enum<?>> type) {
		this.type = type;
		if (this.type == null) {
			throw new IllegalArgumentException("Type argument cannot be null");
		}
		this.enums = (E[]) type.getEnumConstants();
		if (this.enums == null) {
			throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
		}
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		Object value = parameter.name();
		ps.setObject(i, value);
	}

	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return getValue(rs.getObject(columnName));
	}

	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return getValue(rs.getObject(columnIndex));
	}

	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return getValue(cs.getObject(columnIndex));
	}

	private E getValue(Object status) {
		for (E em : enums) {
			if (em.name().equals(status)) {
				return em;
			}
		}
		return null;
	}
}
