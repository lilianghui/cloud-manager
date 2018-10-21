package com.lilianghui.framework.poi.excel;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Sets;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class SheetBeanDefinition {
    private Class<?> clazz;
    private Map<String, Map<String, CellBeanDefinition>> property = new LinkedHashMap<>();//group -  field - CellBeanDefinition
    private Map<String, Map<String, CellBeanDefinition>> defaultValueMapping = new LinkedHashMap<>();//group -  field - defaultValue

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

    public Map<String, CellBeanDefinition> getDefaultValueMapping(SheetBean sheetBean) {
        Map<String, CellBeanDefinition> defaultValueMapping = new LinkedHashMap<>();
        Map<Integer, Collection<CellBeanDefinition>> fields = sheetBean.getField().asMap();
        fields.forEach((o, o2) -> {
            o2.stream().filter(cellBeanDefinition -> StringUtils.isNotBlank(cellBeanDefinition.getDefaultValue())).forEach(cellBeanDefinition -> {
                defaultValueMapping.put(cellBeanDefinition.getName(), cellBeanDefinition);
            });
        });
        return defaultValueMapping;
    }

    public Map<String, CellBeanDefinition> getDefaultValueMapping(String groupName) {
        return defaultValueMapping.get(groupName);
    }

    public void setDefaultValueMapping(Map<String, Map<String, CellBeanDefinition>> defaultValueMapping) {
        this.defaultValueMapping = defaultValueMapping;
    }
}
