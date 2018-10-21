package com.lilianghui.framework.poi.excel;


/**
 * Created by Administrator on 2018/4/25 0025.
 */
public class DefaultConverter implements ReadConverter,WriteConverter {
    @Override
    public Object read(CellBeanDefinition cellBeanDefinition,Object o) {
        return o;
    }

    @Override
    public Object write(CellBeanDefinition cellBeanDefinition, Object bean,Object o) {
        return o;
    }
}
