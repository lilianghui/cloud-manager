package com.lilianghui.framework.core.mapper.tk.resolve;

import com.lilianghui.framework.core.mapper.tk.helper.TkHelper;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.EntityField;
import tk.mybatis.mapper.entity.EntityTable;

public class DefaultEntityResolve extends tk.mybatis.mapper.mapperhelper.resolve.DefaultEntityResolve {
    @Override
    protected void processField(EntityTable entityTable, EntityField field, Config config, Style style) {
        if (TkHelper.isBreak(field)) {
            return;
        }
        super.processField(entityTable, field, config, style);
    }
}
