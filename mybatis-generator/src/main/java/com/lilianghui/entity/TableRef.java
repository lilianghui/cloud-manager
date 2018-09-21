package com.lilianghui.entity;

public class TableRef {
	private String manyTableName;
	private String manyColumnName;
	private String manyConstraintType;
	private String oneTableName;
	private String oneColumnName;
	private String oneConstraintType;

	public String getManyTableName() {
		return manyTableName;
	}

	public void setManyTableName(String manyTableName) {
		this.manyTableName = manyTableName;
	}

	public String getManyColumnName() {
		return manyColumnName;
	}

	public void setManyColumnName(String manyColumnName) {
		this.manyColumnName = manyColumnName;
	}

	public String getManyConstraintType() {
		return manyConstraintType;
	}

	public void setManyConstraintType(String manyConstraintType) {
		this.manyConstraintType = manyConstraintType;
	}

	public String getOneTableName() {
		return oneTableName;
	}

	public void setOneTableName(String oneTableName) {
		this.oneTableName = oneTableName;
	}

	public String getOneColumnName() {
		return oneColumnName;
	}

	public void setOneColumnName(String oneColumnName) {
		this.oneColumnName = oneColumnName;
	}

	public String getOneConstraintType() {
		return oneConstraintType;
	}

	public void setOneConstraintType(String oneConstraintType) {
		this.oneConstraintType = oneConstraintType;
	}

}
