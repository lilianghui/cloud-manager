package com.lilianghui.framework.core.poi.excel;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.Set;

public class SheetBean<T> {
    private Class<T> clazz;
    private String sheetName;
    private int sheetIndex = 1;
    private int titleIndex = 1;
    private int subTitleIndex = 2;
    private int dataIndex = subTitleIndex + 1;
    private boolean skipSubTitle = false;//跳过扫描列名  此时cellIndex不能为空
    private boolean skipNoMapping = false;//跳过没有映射的列名  map集合用
    private String gorup = Constant.DEFAULT_GROUP;//默认组名

    //data
    private List<T> data = Lists.newArrayList();
    private Set<String> error = Sets.newLinkedHashSet();

    //import map
    private Multimap<Integer, CellBeanDefinition> field = LinkedHashMultimap.create();

    //export
    public boolean showTitle = false;
    public boolean titleCenter = true;


    public void error(Sheet sheet, SheetBean sheetBean, CellBeanDefinition cellBeanDefinition, DisplayText displayText, int type) {
        if (type == Import.BLANK_TYPE) {
            error.add(String.format("工作簿[%s]在第[%s]行第[%s]列不能为空", sheet.getSheetName(), displayText.getRowIndex() + 1, displayText.getCellIndex() + 1));
        } else if (type == Import.REPEAT_TYPE) {
            error.add(String.format("工作簿[%s]在第[%s]行第[%s]列有重复值,重复值为[%s]", sheet.getSheetName(), displayText.getRowIndex() + 1, displayText.getCellIndex() + 1, displayText.getValue()));
        }
    }


    public void errorText(Sheet sheet, SheetBean sheetBean, DisplayText displayText, String errorText) {
        error.add(String.format("工作簿[%s]在第[%s]行第[%s]列错误:%s", sheet.getSheetName(), displayText.getRowIndex() + 1, displayText.getCellIndex() + 1, errorText));
    }

    public boolean hasError() {
        return CollectionUtils.isNotEmpty(error);
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public int getTitleIndex() {
        return titleIndex;
    }

    public void setTitleIndex(int titleIndex) {
        this.titleIndex = titleIndex;
    }

    public int getSubTitleIndex() {
        return subTitleIndex;
    }

    public void setSubTitleIndex(int subTitleIndex) {
        this.subTitleIndex = subTitleIndex;
    }

    public int getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(int dataIndex) {
        this.dataIndex = dataIndex;
    }

    public String getGorup() {
        return gorup;
    }

    public void setGorup(String gorup) {
        this.gorup = gorup;
    }

    public boolean isShowTitle() {
        return showTitle;
    }

    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    public boolean isTitleCenter() {
        return titleCenter;
    }

    public void setTitleCenter(boolean titleCenter) {
        this.titleCenter = titleCenter;
    }

    public boolean isSkipSubTitle() {
        return skipSubTitle;
    }

    public void setSkipSubTitle(boolean skipSubTitle) {
        this.skipSubTitle = skipSubTitle;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Set<String> getError() {
        return error;
    }

    public Multimap<Integer, CellBeanDefinition> getField() {
        return field;
    }

    public void setField(Multimap<Integer, CellBeanDefinition> field) {
        this.field = field;
    }

    public boolean isSkipNoMapping() {
        return skipNoMapping;
    }

    public void setSkipNoMapping(boolean skipNoMapping) {
        this.skipNoMapping = skipNoMapping;
    }

    public void putField(int cellIndex, String field) {
        CellBeanDefinition cellBeanDefinition = new CellBeanDefinition();
        cellBeanDefinition.setName(field);
        cellBeanDefinition.setAllowBlank(true);
        cellBeanDefinition.setAllowRepeat(true);
        this.field.put(cellIndex, cellBeanDefinition);
    }
}
