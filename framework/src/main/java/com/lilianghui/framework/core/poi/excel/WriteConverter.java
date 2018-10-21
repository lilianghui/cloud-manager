package com.lilianghui.framework.core.poi.excel;

/**
 * Created by Administrator on 2018/4/25 0025.
 */
public interface WriteConverter {
    Object write(CellBeanDefinition cellBeanDefinition, Object bean, Object o);
}
