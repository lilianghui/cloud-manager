package com.lilianghui.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lilianghui.util.Ctrls;
import org.apache.commons.lang3.StringUtils;


public class Table {
	private String table; // 表
	private String tableComment; // 表描述
	private String iDaoName;
	private String iServiceName;
	private String serviceImplName;
	private Column pkColumn;
	private String actionName;
	private String entityName;
	private String alias;
	private String prevAlias;
	private TableRef ref;
	private List<Column> list = new ArrayList<Column>();

	private Set<TableRef> many = new HashSet<TableRef>();
	private Set<TableRef> one = new HashSet<TableRef>();

	public Table(Config config, boolean identity, String table, String tableComment, String iDaoName, String iServiceName, String serviceImplName, String actionName,
				 String pk, String entityName, String alias, List<Map<String, String>> list) {
		super();
		this.table = table;
		if(StringUtils.isNotBlank(tableComment)){
			this.tableComment = "//"+ Ctrls.replace(tableComment);
		}
		this.iDaoName = iDaoName;
		this.iServiceName = iServiceName;
		this.serviceImplName = serviceImplName;
		this.actionName = actionName;
		this.entityName = entityName;
		this.alias = alias;
		for (Map<String, String> map : list) {
			Column column=new Column(config,map,pk);
			this.list.add(column);
			if(column.isIdentity()){
				pkColumn=column;
			}
		}
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getiDaoName() {
		return iDaoName;
	}

	public void setiDaoName(String iDaoName) {
		this.iDaoName = iDaoName;
	}

	public String getiServiceName() {
		return iServiceName;
	}

	public void setiServiceName(String iServiceName) {
		this.iServiceName = iServiceName;
	}

	public String getServiceImplName() {
		return serviceImplName;
	}

	public void setServiceImplName(String serviceImplName) {
		this.serviceImplName = serviceImplName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}


	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public List<Column> getList() {
		return list;
	}

	public void setList(List<Column> list) {
		this.list = list;
	}

	public Set<TableRef> getMany() {
		return many == null ? new HashSet<TableRef>() : many;
	}

	public void setMany(Set<TableRef> many) {
		this.many = many;
	}

	public Set<TableRef> getOne() {
		return one == null ? new HashSet<TableRef>() : one;
	}

	public void setOne(Set<TableRef> one) {
		this.one = one;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public TableRef getRef() {
		return ref;
	}

	public void setRef(TableRef ref) {
		this.ref = ref;
	}

	public String getPrevAlias() {
		return prevAlias;
	}

	public void setPrevAlias(String prevAlias) {
		this.prevAlias = prevAlias;
	}

	public Column getPkColumn() {
		return pkColumn;
	}

	public void setPkColumn(Column pkColumn) {
		this.pkColumn = pkColumn;
	}

	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

}
