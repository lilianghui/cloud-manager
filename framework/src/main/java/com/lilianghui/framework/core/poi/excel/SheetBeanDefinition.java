package com.lilianghui.framework.core.poi.excel;

import java.util.LinkedHashMap;
import java.util.Map;

public class SheetBeanDefinition {
    private Class<?> clazz;
    private Map<String, Map<String, CellBeanDefinition>> property = new LinkedHashMap<>();//group -  field - CellBeanDefinition

    public Map<String, CellBeanDefinition> getGroup(String groupName) {
        return property.get(groupName);
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void setProperty(Map<String, Map<String, CellBeanDefinition>> property) {
        this.property = property;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Map<String, Map<String, CellBeanDefinition>> getProperty() {
        return property;
    }

}
