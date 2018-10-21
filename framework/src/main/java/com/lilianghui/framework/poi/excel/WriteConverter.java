package com.lilianghui.framework.poi.excel;

import net.zhuisuyun.saas.stock.client.utils.poi.SheetBean;

/**
 * Created by Administrator on 2018/4/25 0025.
 */
public interface WriteConverter {
    Object write(CellBeanDefinition cellBeanDefinition, Object bean, Object o);
}
