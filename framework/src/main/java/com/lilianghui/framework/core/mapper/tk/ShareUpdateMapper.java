package com.lilianghui.framework.core.mapper.tk;

import com.lilianghui.framework.core.mapper.tk.provider.ShareUpdateProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.entity.Example;


public interface ShareUpdateMapper<T> {

	@UpdateProvider(type = ShareUpdateProvider.class, method = "dynamicSQL")
	int updateForFieldByPrimaryKey(@Param("record") T record, @Param("fields") String... fields);

	@UpdateProvider(type = ShareUpdateProvider.class, method = "dynamicSQL")
	int updateForFieldByExample(@Param("record") T record, @Param("example") Example example, @Param("fields") String... fields);
}
