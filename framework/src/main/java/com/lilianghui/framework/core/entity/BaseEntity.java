package com.lilianghui.framework.core.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lilianghui.framework.mybatis.entity.Entity;
import com.lilianghui.framework.mybatis.entity.OrderBy;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

public class BaseEntity implements Entity, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8757717879090225150L;

    @JsonIgnore
    @Transient
    private transient int pageSize = 20;// 页记录数

    @JsonIgnore
    @Transient
    private transient int pageIndex = 1;// 当前页数

    @JsonIgnore
    @Transient
    private transient int totalCount = 0;// 总记录数

    @JsonIgnore
    @Transient
    private transient int totalPage = 0;// 总页数

    @JsonIgnore
    @Transient
    private transient int startRow = 0;// 开始查询的记录数

    @JsonIgnore
    @Transient
    private transient int currentPageRow = 0;// 当前页的记录数


    @Transient
    private transient Map<String, Object> params = new HashMap<String, Object>();

    @JsonIgnore
    @Transient
    private transient List<OrderBy> orderBys = new ArrayList<>();

    @JsonIgnore
    @Transient
    private transient boolean singleQuery = false;// 单表limit分页

    @JsonIgnore
    @Transient
    private transient boolean useCCJSqlParse = false;//使用CCJSqlParse处理分页信息

    @JsonIgnore
    @Transient
    private transient boolean pagingQuery = true;// 是否采用分页查询

    @JsonIgnore
    @Transient
    private transient boolean pageIndexQuery = false;// 是否输入页码查询

    @JsonIgnore
    @Transient
    private transient boolean useDefalutOrderBy = true;// 是否启用默认排序字段

    @JsonIgnore
    @Transient
    private transient String pkColumn;// 关联分页主键

    @JsonIgnore
    @Transient
    private transient Serializable[] primaryKeys;// 主键数组

    @Transient
    private int rownumber;//数据库查询序号  伪列

    @Transient
    private String currentMenuId;//当前菜单Id

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
        pageIndexQuery = true;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        if (totalCount <= 0) {
            this.totalPage = 1;
        } else {
            this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
        }
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getStartRow() {
        if (pageIndexQuery) {
            return (pageIndex - 1) * pageSize;
        }
        return this.startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    @JsonIgnore
    public int getEndRow() {
        return getStartRow() + pageSize;
    }

    public void setEndRow(int endRow) {
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void putParams(String name, Object value) {
        this.params.put(name, value);
    }

    public <T> T getParams(String name) {
        return (T) this.params.get(name);
    }

    public List<OrderBy> getOrderBys() {
        return orderBys;
    }

    public void setOrderBys(List<OrderBy> orderBys) {
        this.orderBys = orderBys;
    }

    public void putOrderBy(String field) {
        putOrderBy(field, OrderBy.OrderBySort.ASC);
    }

    public void putOrderBy(String field, OrderBy.OrderBySort sort) {
        if (this.orderBys == null) {
            this.orderBys = new ArrayList<>();
        }
        orderBys.add(new OrderBy(field, sort));
    }

    public void setStartRowByPageIndex() {
        this.startRow = (pageIndex - 1) * pageSize;
    }

    public boolean isSingleQuery() {
        return singleQuery;
    }

    public void setSingleQuery(boolean singleQuery) {
        this.singleQuery = singleQuery;
    }

    public int getCurrentPageRow() {
        return currentPageRow;
    }

    public void setCurrentPageRow(int currentPageRow) {
        this.currentPageRow = currentPageRow;
    }

    public boolean isPagingQuery() {
        return pagingQuery;
    }

    public void setPagingQuery(boolean pagingQuery) {
        this.pagingQuery = pagingQuery;
    }

    public boolean isPageIndexQuery() {
        return pageIndexQuery;
    }

    public String getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(String pkColumn) {
        this.pkColumn = pkColumn;
    }

    public Serializable[] getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(Serializable[] primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public boolean isUseDefalutOrderBy() {
        return useDefalutOrderBy;
    }

    public void setUseDefalutOrderBy(boolean useDefalutOrderBy) {
        this.useDefalutOrderBy = useDefalutOrderBy;
    }

    public int getRownumber() {
        return rownumber;
    }

    public void setRownumber(int rownumber) {
        this.rownumber = rownumber;
    }

    public boolean isUseCCJSqlParse() {
        return useCCJSqlParse;
    }

    public void setUseCCJSqlParse(boolean useCCJSqlParse) {
        this.useCCJSqlParse = useCCJSqlParse;
    }


    public String getCurrentMenuId() {
        return currentMenuId;
    }

    public void setCurrentMenuId(String currentMenuId) {
        this.currentMenuId = currentMenuId;
    }

}