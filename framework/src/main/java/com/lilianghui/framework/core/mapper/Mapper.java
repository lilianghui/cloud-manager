package com.lilianghui.framework.core.mapper;

import com.lilianghui.framework.core.mapper.tk.ShareMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface Mapper<T> extends tk.mybatis.mapper.common.Mapper<T>, ShareMapper<T> {

}
