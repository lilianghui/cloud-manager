package com.lilianghui.framework.mybatis.entity;

public class OrderBy {
	private static final String ASC = "ASC";
	private static final String DESC = "DESC";
	private String field;
	private OrderBySort sort= OrderBySort.ASC;

	public OrderBy() {
	}
	
	public OrderBy(String field) {
		this(field, OrderBySort.ASC);
	}

	public OrderBy(String field, OrderBySort sort) {
		super();
		this.field = field;
		this.sort = sort;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public OrderBySort getSort() {
		return sort;
	}

	public String getSortDisplayText() {
		return OrderBySort.ASC == this.sort ? ASC : DESC;
	}

	public void setSort(OrderBySort sort) {
		this.sort = sort;
	}

	public static enum OrderBySort {
		ASC, DESC;
	}

}
