package com.lilianghui.framework.core.service;

import com.github.pagehelper.Page;
import com.lilianghui.framework.core.entity.BaseEntity;
import com.lilianghui.framework.core.mapper.Mapper;

import java.util.List;

public interface BaseMapperService<T extends BaseEntity> extends Mapper<T> {

	public void saveOrUpdate(T record) throws Exception;

	public void saveOrUpdate(List<T> records) throws Exception;

	public Page<T> selectByRowBounds(T record);

	public <T> T getMapper();
}