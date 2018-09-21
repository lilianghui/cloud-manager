package com.lilianghui.framework.core.mapper.tk.provider;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;

public class NullInsertProvider extends MapperTemplate implements InsertProvider {


    public NullInsertProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public NullInsertProvider(Class<?> mapperClass, MapperHelper mapperHelper, ShareInsertProvider insertProvider) {
        super(mapperClass, mapperHelper);
    }

    @Override
    public String duplicateSelectiveByPrimaryKey(MappedStatement ms) {
        return null;

    }

    @Override
    public String insertList(MappedStatement ms) {
        return null;
    }

    @Override
    public String mergeInto(MappedStatement ms) {
        return null;

    }

    @Override
    public String mergeIntoSelective(MappedStatement ms) {
        return null;

    }

    @Override
    public String mergeIntoForField(MappedStatement ms) {
        return null;
    }

}
