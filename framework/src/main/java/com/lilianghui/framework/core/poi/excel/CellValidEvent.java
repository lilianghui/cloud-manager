package com.lilianghui.framework.core.poi.excel;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.Set;

public interface CellValidEvent {

    Set<String> valid(Sheet sheet, SheetBean sheetBean, CellBeanDefinition cellBeanDefinition, DisplayText displayText, Object val);
    
}
