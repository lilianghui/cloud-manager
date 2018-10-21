package com.lilianghui.framework.poi.excel;


/**
 * Created by Administrator on 2018/4/25 0025.
 */
public interface ReadConverter {
    Object read(CellBeanDefinition cellBeanDefinition, Object o);
}
