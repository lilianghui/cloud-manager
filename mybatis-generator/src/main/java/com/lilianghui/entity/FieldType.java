package com.lilianghui.entity;

import java.awt.Container;
import java.lang.reflect.Field;

public class FieldType {
	private String name;
	private String value;
	private String type = "JTextField";
	private String field;
	private String[] data;

	public FieldType(String name, Config config, String type, String field) {
		try {
			this.name = name;
			this.type = type;
			this.field = field;
			Field field2 = config.getClass().getDeclaredField(field);
			field2.setAccessible(true);
			this.value = (String) field2.get(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FieldType(String name, Config config, String field) {
		this(name, config, "JTextField", field);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}

	public Container getContainer() {
		return null;
	}

}
