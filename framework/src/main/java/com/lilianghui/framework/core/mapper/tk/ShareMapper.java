package com.lilianghui.framework.core.mapper.tk;

import com.lilianghui.framework.core.mapper.tk.provider.PrimaryKeysProvider;
import org.apache.ibatis.annotations.InsertProvider;

import java.io.Serializable;
import java.util.List;

public interface ShareMapper<T> extends ShareInsertMapper<T>, ShareUpdateMapper<T> {

    @InsertProvider(type = PrimaryKeysProvider.class, method = "dynamicSQL")
    int deleteByPrimaryKeys(Serializable[] keys);

    @InsertProvider(type = PrimaryKeysProvider.class, method = "dynamicSQL")
    List<T> selectByPrimaryKeys(Serializable[] keys);

}
