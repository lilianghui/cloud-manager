package com.lilianghui.entity;

public class DataBaseType {
	private int index;
	private String type;
	private String url;
	private String driver;

	public DataBaseType(int index, String type, String url, String driver) {
		super();
		this.index = index;
		this.type = type;
		this.url = url;
		this.driver = driver;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
