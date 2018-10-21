package com.lilianghui.framework.poi.excel;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public interface ReadConverterFactory<T> {
    T returnValue(CellBeanDefinition cellBeanDefinition, DisplayText displayText);

    class BigDecimalConverter implements ReadConverterFactory<BigDecimal> {

        @Override
        public BigDecimal returnValue(CellBeanDefinition cellBeanDefinition, DisplayText displayText) {
            if (displayText.getValue() != null && StringUtils.isNotBlank(displayText.getValue().toString())) {
                return new BigDecimal(String.valueOf(displayText.getValue()));
            }
            return null;
        }
    }

    class EnumConverter implements ReadConverterFactory<Enum> {

        @Override
        public Enum returnValue(CellBeanDefinition cellBeanDefinition, DisplayText displayText) {
            if (displayText.getValue() != null && StringUtils.isNotBlank(displayText.getValue().toString())) {
                Class<Enum> ca= (Class<Enum>) cellBeanDefinition.getJavaType();
                return Enum.valueOf(ca, displayText.getValue().toString());
            }
            return null;
        }
    }


}

