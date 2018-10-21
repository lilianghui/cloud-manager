package com.lilianghui.framework.poi.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.Map;

public class DisplayText {
    private int rowIndex;
    private int cellIndex;
    private Map<CellBeanDefinition, Object> propertyValue;
    private Object value;
    private String comments;
    private String error;
    private boolean defaultFlag;

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Map<CellBeanDefinition, Object> getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Map<CellBeanDefinition, Object> propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStringValue() {
        Object object = getValue();
        if (object instanceof String) {
            return (String) object;
        } else if (object instanceof Date) {
            return DateFormatUtils.format((Date) object, Constant.DATE_FORMAT_ARRAY[0]);
        }
        return String.valueOf(object);
    }

    public Boolean getBooleanValue() {
        Object object = getValue();
        if (object instanceof Boolean) {
            return (Boolean) object;
        }
        return "1".equalsIgnoreCase(getStringValue()) || "Y".equalsIgnoreCase(getStringValue());
    }

    public Date getDateValue() {
        try {
            Object object = getValue();
            if(object==null){
                return null;
            }
            if (object instanceof Date) {
                return (Date) object;
            }
            String value = getStringValue();
            if (StringUtils.isNotBlank(value)) {
                return DateUtils.parseDate(value, Constant.DATE_FORMAT_ARRAY);
            }
            return null;
        } catch (Exception e) {
            this.error = String.format("日期转换错误,请参照以下日期格式:%s", StringUtils.join(Constant.DATE_FORMAT_ARRAY, ","));
            e.printStackTrace();
            return null;
        }
    }

    public Integer getIntValue() {
        Object object = getValue();
        if (object instanceof Integer) {
            return (Integer) object;
        }
        return Integer.valueOf(getStringValue());
    }

    public Double getDoubleValue() {
        Object object = getValue();
        if (object instanceof Double) {
            return (Double) object;
        }
        return Double.valueOf(getStringValue());
    }

    public Float getFloatValue() {
        Object object = getValue();
        if (object instanceof Double) {
            return (Float) object;
        }
        return Float.valueOf(getStringValue());
    }

    public boolean isDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(boolean defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    @Override
    public String toString() {
        return getStringValue();
    }

}
