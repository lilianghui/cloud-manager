package com.lilianghui.framework.mybatis.entity;

import java.util.List;

public interface Entity {

    boolean isSingleQuery();

    boolean isPagingQuery();

    boolean isPageIndexQuery();

    void setStartRowByPageIndex();

    void setTotalCount(int totalCount);

    int getTotalPage();

    int getPageIndex();

    void setPageIndex(int totalPage);

    void setCurrentPageRow(int size);

    int getStartRow();

    int getEndRow();

    int getPageSize();

    String getPkColumn();

    List<OrderBy> getOrderBys();

    boolean isUseCCJSqlParse();
}
