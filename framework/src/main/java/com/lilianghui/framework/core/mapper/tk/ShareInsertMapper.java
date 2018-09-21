package com.lilianghui.framework.core.mapper.tk;

import com.lilianghui.framework.core.mapper.tk.provider.ShareInsertProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShareInsertMapper<T> {

	@InsertProvider(type = ShareInsertProvider.class, method = "dynamicSQL")
	int insertList(List<T> recordList);

	@InsertProvider(type = ShareInsertProvider.class, method = "dynamicSQL")
	int mergeInto(@Param("record") T record, @Param("fields") String... fields);

	@InsertProvider(type = ShareInsertProvider.class, method = "dynamicSQL")
	int mergeIntoForField(@Param("record") T record, @Param("updateFields") String[] updateFields, @Param("fields") String... fields);

	@InsertProvider(type = ShareInsertProvider.class, method = "dynamicSQL")
	int mergeIntoSelective(@Param("record") T record, @Param("fields") String... fields);

	@InsertProvider(type = ShareInsertProvider.class, method = "dynamicSQL")
	int duplicateSelectiveByPrimaryKey(@Param("records") List<T> recordList, @Param("fields") String... fields);
}
