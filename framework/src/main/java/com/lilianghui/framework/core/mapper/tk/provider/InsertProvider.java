package com.lilianghui.framework.core.mapper.tk.provider;

import org.apache.ibatis.mapping.MappedStatement;

public interface InsertProvider {
	String insertList(MappedStatement ms);

	String mergeInto(MappedStatement ms);

	String mergeIntoForField(MappedStatement ms);

	String mergeIntoSelective(MappedStatement ms);

	String duplicateSelectiveByPrimaryKey(MappedStatement ms);
}
