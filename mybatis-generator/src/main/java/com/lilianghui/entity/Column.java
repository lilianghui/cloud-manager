package com.lilianghui.entity;

import java.util.Map;

import com.lilianghui.Generator;
import com.lilianghui.util.Ctrls;
import org.apache.commons.lang3.StringUtils;


public class Column {

	private boolean identity;
	private String javaType;
	private String jdbcType;
	private String comments;
	private String columnName;
	private String propertyName;
	private String dataDefault;
	private String annotated;

	public Column(Config config, Map<String, String> map, String pk) {
		String comments = map.get("comments");
		comments = StringUtils.isBlank(comments) ? "" : " //" + Ctrls.replace(comments);
		String defaultValue = map.get("data_default");
		String tmp = defaultValue;
		String javaType = Ctrls.getJavaType(map.get("data_type"), map.get("data_length"));
		String columnName = map.get("column_name");
		String column_name = config.isColumnUpperCase() ? columnName.toUpperCase() : columnName;
		String property_name = Ctrls.getFieldName(columnName, config.getJdbcColumnPrefix());
		if ("true".equalsIgnoreCase(map.get("skip"))) {
			property_name = columnName;
			column_name = columnName;
		}
		String jdbcType = Ctrls.getJdbcType(map.get("data_type"));
		if (StringUtils.isNotBlank(defaultValue)) {
			comments += StringUtils.isBlank(comments) ? ("// 默认值:" + defaultValue) : ("  默认值:" + defaultValue);
			if ("String".equalsIgnoreCase(javaType)) {
				defaultValue = "=\"" + defaultValue + "\"";
			} else if ("Integer".equalsIgnoreCase(javaType)) {
				defaultValue = "=" + defaultValue;
			} else if ("Date".equalsIgnoreCase(javaType) && !isTime(property_name, javaType, "createdate", "createtime") && !isTime(property_name, javaType, "updatedate", "updatetime")) {
				defaultValue = "=new Date()";
			} else {
				defaultValue = "";
			}
		}
		this.columnName = column_name;
		this.javaType = javaType;
		this.comments = comments;
		this.propertyName = property_name;
		this.jdbcType = jdbcType;
		this.dataDefault = defaultValue;
		StringBuffer buffer = new StringBuffer();
		if (this.propertyName.equalsIgnoreCase(pk)) {
			this.identity = true;
		}

		if (isTime(propertyName, javaType, "createdate", "createtime")) {
			//			buffer.append("@Updatable\n\t");
			buffer.append("@ValueStyle(\"" + config.getOgnl() + "\")\n\t");
		} else if (isTime(propertyName, javaType, "updatedate", "updatetime")) {
			buffer.append("@ValueStyle(\"" + config.getOgnl() + "\")\n\t");
		} else if (StringUtils.isNotBlank(tmp)) {
//			String v = "Integer".equalsIgnoreCase(javaType) ? tmp : "\\\"" + tmp + "\\\"";
			String v = "Integer".equalsIgnoreCase(javaType) ? tmp : "'" + tmp + "'";
			if ("Boolean".equalsIgnoreCase(javaType)) {
				if ("Y".equalsIgnoreCase(tmp)) {
					v = "@java.lang.Boolean@TRUE";
				} else if ("N".equalsIgnoreCase(tmp)) {
					v = "@java.lang.Boolean@FALSE";
				}
			}
			buffer.append("@ValueStyle(value=\"" + v + "\",type=ValueType.INSERT)\n\t");
		} else if (map.get("nullable").equalsIgnoreCase("no") && !this.propertyName.equalsIgnoreCase(pk)) {
			//			buffer.append("@NotBlank(message=\""+map.get("comments")+"不能为空\")\n\t");
		}
		this.annotated = buffer.toString();
	}

	private boolean isTime(String propertyName, String javaType, String... types) {
		if ("date".equalsIgnoreCase(javaType)) {
			for (String type : types) {
				if (type.equalsIgnoreCase(propertyName)) {
					return true;
				}
			}
		}
		return false;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getDataDefault() {
		return this.dataDefault;
	}

	public void setDataDefault(String dataDefault) {
		this.dataDefault = dataDefault;
	}

	public boolean isIdentity() {
		return identity;
	}

	public void setIdentity(boolean identity) {
		this.identity = identity;
	}

	public String getAnnotated() {
		return annotated;
	}

	public void setAnnotated(String annotated) {
		this.annotated = annotated;
	}

	public String getUpdatable() {
		if (Generator.config.getExUpdateColumns().contains(columnName) || ((propertyName.equalsIgnoreCase("createdate") || propertyName.equalsIgnoreCase("createtime")) && "date".equalsIgnoreCase(javaType))) {
			return ",updatable=false";
		}
		return "";
	}

}
