package com.lilianghui.framework.mybatis.entity;

import java.util.HashMap;

public class MapParam extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	public static final String KEY_FIELD = "mapKeyField";
	public static final String VALUE_FIELD = "mapValueField";
	public static final String ENABLED = "Enabled";
	
	public MapParam() {
		setEnabled(false);
	}

	/** 
	 * 指定keyField和valueField 
	 * @param keyField Map中key对应的字段 
	 * @param valueField Map中value对应的字段 
	 */
	public MapParam(String keyField, String valueField) {
		this.put(KEY_FIELD, keyField);
		this.put(VALUE_FIELD, valueField);
	}

	public void setEnabled(boolean enabled) {
		this.put(ENABLED, enabled);
	}
}