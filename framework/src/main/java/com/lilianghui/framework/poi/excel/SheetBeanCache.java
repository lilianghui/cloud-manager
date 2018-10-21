package com.lilianghui.framework.poi.excel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.zhuisuyun.saas.stock.client.utils.poi.DefaultConverter;
import net.zhuisuyun.saas.stock.client.utils.poi.PlatformUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Sets;

import java.lang.reflect.Field;
import java.util.*;

public class SheetBeanCache {
    private static final Map<Class<?>, SheetBeanDefinition> SHEET_BEAN_DEFINITION_LINKED_HASH_MAP = new LinkedHashMap<>();

    public static SheetBeanDefinition initSheetBean(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        SheetBeanDefinition sheetBeanDefinition = SHEET_BEAN_DEFINITION_LINKED_HASH_MAP.get(clazz);
        if (sheetBeanDefinition == null) {
            synchronized (clazz) {
                if (SHEET_BEAN_DEFINITION_LINKED_HASH_MAP.get(clazz) == null) {
                    sheetBeanDefinition = new SheetBeanDefinition();
                    sheetBeanDefinition.setClazz(clazz);
                    Map<String, Map<String, CellBeanDefinition>> property = new LinkedHashMap<>();
                    Map<String, Map<String, CellBeanDefinition>> defaultValueMapping = new LinkedHashMap<>();//group -  field - defaultValue
                    int index = 0;
                    for (Field field : PlatformUtils.getDeclaredFields(clazz, false)) {
                        CellAttributes cellAttributes = field.getAnnotation(CellAttributes.class);
                        CellAttribute cellAttribute = field.getAnnotation(CellAttribute.class);
                        List<CellAttribute> attributes = Lists.newArrayList();
                        if (cellAttributes != null) {
                            Collections.addAll(attributes, cellAttributes.value());
                        }
                        if (cellAttribute != null) {
                            attributes.add(cellAttribute);
                        }
                        int index_ = index++;
                        attributes.forEach(cellAttribute1 -> {
                            parse(cellAttribute1, field, property, defaultValueMapping, index_);
                        });
                    }
                    /*Collections.sort(cellBeans, new Comparator<net.zhuisuyun.saas.stock.client.utils.poi.SheetBean.CellBean>() {
                        @Override
                        public int compare(net.zhuisuyun.saas.stock.client.utils.poi.SheetBean.CellBean o1, net.zhuisuyun.saas.stock.client.utils.poi.SheetBean.CellBean o2) {
                            int cr = 0;
                            //按order升序排列
                            int a = o1.getOrder() - o2.getOrder();
                            if (a != 0) {
                                cr = (a > 0) ? 3 : -1;
                            } else {
                                //按index升序排列
                                a = o1.getIndex() - o2.getIndex();
                                if (a != 0) {
                                    cr = (a > 0) ? 2 : -2;
                                }
                            }
                            return cr;
                        }
                    });*/
                    sheetBeanDefinition.setProperty(property);
                    sheetBeanDefinition.setDefaultValueMapping(defaultValueMapping);
                    SHEET_BEAN_DEFINITION_LINKED_HASH_MAP.put(clazz, sheetBeanDefinition);
                } else {
                    sheetBeanDefinition = SHEET_BEAN_DEFINITION_LINKED_HASH_MAP.get(clazz);
                }
            }
        }
        return sheetBeanDefinition;
    }

    private static void parse(CellAttribute cellAttribute, Field field, Map<String, Map<String, CellBeanDefinition>> property, Map<String, Map<String, CellBeanDefinition>> defaultValueMapping, int index) {
        CellBeanDefinition cellBeanDefinition = new CellBeanDefinition();
        cellBeanDefinition.setName(field.getName());
        cellBeanDefinition.setTitle(cellAttribute.title());
        cellBeanDefinition.setDateFormat(cellAttribute.dateFormat());
        cellBeanDefinition.setParameters(cellAttribute.parameters());
        cellBeanDefinition.setAllowBlank(cellAttribute.allowBlank());
        cellBeanDefinition.setAllowRepeat(cellAttribute.allowRepeat());
        cellBeanDefinition.setJavaType(field.getType());
        cellBeanDefinition.setCellIndex(cellAttribute.cellIndex());
        cellBeanDefinition.setComment(cellAttribute.comment());
        cellBeanDefinition.setOfName(cellAttribute.ofName());
        cellBeanDefinition.setDefaultValue(cellAttribute.defaultValue());
        cellBeanDefinition.setBlankTemplate(cellAttribute.blankTemplate());
        cellBeanDefinition.setRepeatTemplate(cellAttribute.repeatTemplate());
        if (!cellAttribute.readConverter().equals(DefaultConverter.class)) {
            cellBeanDefinition.setRead(cellAttribute.readConverter());
        }
        if (!cellAttribute.writeConverter().equals(DefaultConverter.class)) {
            cellBeanDefinition.setWrite(cellAttribute.writeConverter());
        }
        cellBeanDefinition.setIndex(index);
        Map<String, CellBeanDefinition> group = property.get(cellAttribute.gorup());
        if (group == null) {
            property.put(cellAttribute.gorup(), Maps.newLinkedHashMap());
            group = property.get(cellAttribute.gorup());
        }
        group.put(field.getName(), cellBeanDefinition);

        if (StringUtils.isNotBlank(cellBeanDefinition.getDefaultValue())) {
            Map<String, CellBeanDefinition> defaultValue = defaultValueMapping.get(cellAttribute.gorup());
            if (defaultValue == null) {
                defaultValueMapping.put(cellAttribute.gorup(), Maps.newLinkedHashMap());
                defaultValue = defaultValueMapping.get(cellAttribute.gorup());
            }
            defaultValue.put(field.getName(), cellBeanDefinition);
        }
    }

    public static Map<String, Class<?>> initCellBeanDefinition(Class clazz, Collection<CellBeanDefinition> cellBeanDefinitions) {
        Map<String, Class<?>> cellBeanDefinitionMap = Maps.newHashMap();
        cellBeanDefinitions.forEach(cellBeanDefinition -> {
            Field field = PlatformUtils.getField(clazz, cellBeanDefinition.getName());
            cellBeanDefinitionMap.put(field.getName(), field.getType());
        });
        return cellBeanDefinitionMap;
    }
}
