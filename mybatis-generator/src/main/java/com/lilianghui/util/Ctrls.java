package com.lilianghui.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Ctrls {
	/**
	 * 
	 * @param tableName
	 * @param prefix删除前缀
	 * @param status
	 *            true驼峰首字母小写 false首字母大写
	 * @return
	 */
	public static String getBeanName(String tableName, String prefix, Boolean status) {
		if (StringUtils.isNotBlank(prefix) && tableName.toUpperCase().indexOf(prefix.toUpperCase()) >= 0) {
			tableName = tableName.substring(tableName.lastIndexOf(prefix) + prefix.length());
		}
		String[] name = tableName.split("_");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < name.length; i++) {
			if (name[i] != null && !"".equalsIgnoreCase(name[i])) {
				StringBuffer buffer = new StringBuffer(name[i].toLowerCase());
				buffer.setCharAt(0, Character.toUpperCase(buffer.charAt(0)));
				sb.append(buffer);
			}
		}
		if (status != null) {
			if (status) {
				sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
			} else {
				sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
			}
		}
		return sb.toString();
	}

	/**
	 * 驼峰命名 userName
	 * 
	 * @param columnName
	 * @return
	 */
	public static String getFieldName(String columnName, String jdbcColumnPrefix) {
		return getBeanName(columnName, jdbcColumnPrefix, true);
	}

	public static String toLowerCase(String value) {
		StringBuffer buffer = new StringBuffer(value);
		buffer.setCharAt(0, Character.toLowerCase(buffer.charAt(0)));
		return buffer.toString();
	}

	public static String toUpperCase(String value) {
		StringBuffer buffer = new StringBuffer(value);
		buffer.setCharAt(0, Character.toUpperCase(buffer.charAt(0)));
		return buffer.toString();
	}

	public static String getJavaType(String dataType, String dataLength) {
		if (dataType.indexOf("char") >= 0) {
			if (StringUtils.isNotBlank(dataLength) && Integer.parseInt(dataLength) == 1) {
				return "Boolean";
			} else {
				return "String";
			}
		} else if (dataType.indexOf("varchar") >= 0 || dataType.indexOf("text") >= 0) {
			return "String";
		} else if (dataType.indexOf("number") >= 0 || dataType.indexOf("double") >= 0 || dataType.indexOf("numeric") >= 0) {
			return "Double";
		} else if (dataType.indexOf("int") >= 0) {
			return "Integer";
		} else if (dataType.indexOf("date") >= 0 || dataType.indexOf("time") >= 0) {
			return "Date";
		} else if (dataType.indexOf("tinyint") >= 0) {
			return "Boolean";
		} else if (dataType.indexOf("float") >= 0) {
			return "Float";
		} else if (dataType.indexOf("varbinary") >= 0) {
			return "byte[]";
		}
		return null;

	}

	public static String getJdbcType(String dataType) {
		if (dataType.indexOf("varchar") >= 0 || dataType.indexOf("text") >= 0 || dataType.indexOf("char") >= 0) {
			return "VARCHAR";
		} else if (dataType.indexOf("number") >= 0) {
			return "NUMERIC";
		} else if (dataType.indexOf("integer") >= 0 || dataType.indexOf("int") >= 0) {
			return "INTEGER";
		} else if (dataType.indexOf("date") >= 0 || dataType.indexOf("time") >= 0) {
			return "DATE";
		} else if (dataType.indexOf("double") >= 0 || dataType.indexOf("numeric") >= 0) {
			return "DOUBLE";
		} else if (dataType.indexOf("float") >= 0) {
			return "FLOAT";
		} else if (dataType.indexOf("varbinary") >= 0) {
			return "BLOB";
		}
		return null;
	}

	public static String list2String(List<String> list, String op) {
		StringBuffer buffer = new StringBuffer();
		for (String str : list) {
			buffer.append(str).append(op);
		}
		if (buffer.length() > 0) {
			buffer = buffer.deleteCharAt(buffer.length() - 1);
		}
		return buffer.toString();

	}

	public static String replace(String source) {
		return source.replaceAll("\\s", " ");
	}

	public static void main(String[] args) {
		String tableName = "M_TR_USER";
		System.out.println(tableName.substring(tableName.lastIndexOf("M_") + "M_".length()));
	}
}
