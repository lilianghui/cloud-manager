package com.lilianghui.framework.poi.excel;

import com.google.common.collect.*;
import net.zhuisuyun.saas.stock.client.vo.InstockQuickTraceVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Import {
    private static final Logger logger = LoggerFactory.getLogger(Import.class);
    private static final Map<Class, Class<? extends ReadConverterFactory>> CLASS_READ_CONVERTER_FACTORY_MAP = Maps.newConcurrentMap();
    public static final int BLANK_TYPE = 1;
    public static final int REPEAT_TYPE = 2;

    static {
        register(BigDecimal.class, ReadConverterFactory.BigDecimalConverter.class);
        register(Enum.class, ReadConverterFactory.EnumConverter.class);
    }

    public static void main(String[] args) throws Exception {
//        SheetBean<Map> sheetBean = new SheetBean<>();
//        sheetBean.setSheetIndex(1);
//        sheetBean.setSkipSubTitle(true);
//        sheetBean.setDataIndex(2);
//        sheetBean.setClazz(Map.class);
//        sheetBean.setSkipNoMapping(true);
//        sheetBean.getField().put(1, CellBeanDefinition.build().name("entpName"));
//        sheetBean.getField().put(2, CellBeanDefinition.build().name("phone"));
//        sheetBean.getField().put(3, CellBeanDefinition.build().name("outDate").allowBlank(false).blankTemplate("第#{rowIndex}行第#{cellIndex}列出库时间不能为空"));
//        sheetBean.getField().put(4, CellBeanDefinition.build().name("carNo"));
//        sheetBean.getField().put(5, CellBeanDefinition.build().name("people"));
//        sheetBean.getField().put(6, CellBeanDefinition.build().name("employee").allowBlank(false).blankTemplate("第#{rowIndex}行第#{cellIndex}列登记人不能为空"));
//        sheetBean.getField().put(7, CellBeanDefinition.build().name("productNo").allowBlank(false).blankTemplate("第#{rowIndex}行第#{cellIndex}列产品SKU不能为空"));
//        sheetBean.getField().put(8, CellBeanDefinition.build().name("productName"));
//        sheetBean.getField().put(9, CellBeanDefinition.build().name("quantity"));
//        sheetBean.getField().put(10, CellBeanDefinition.build().name("price"));
//        Import.imports(new FileInputStream("C:\\360安全浏览器下载\\outstocktemp.xlsx"), sheetBean);
//        sheetBean.getData().forEach(map -> {
//            System.out.println(map);
//        });
//        System.err.println(sheetBean.getError());
        SheetBean<InstockQuickTraceVO> sheetBean = new SheetBean();
        sheetBean.setSheetIndex(1);
        sheetBean.setSkipSubTitle(true);
        sheetBean.setDataIndex(2);
        sheetBean.setClazz(InstockQuickTraceVO.class);
        Import.imports(new FileInputStream("C:\\Users\\zsy\\Desktop\\quickTraceNmInStock.xlsx"), sheetBean);

        List<InstockQuickTraceVO> excelRowList = sheetBean.getData();
        System.out.println(excelRowList);
    }

    public static void imports(InputStream inputStream, SheetBean... sheetBeans) throws Exception {
        try {
            imports(createWorkbook(new BufferedInputStream(inputStream)), sheetBeans);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public static void imports(Workbook workBook, SheetBean... sheetBeans) throws Exception {
        try {
            for (SheetBean sheetBean : sheetBeans) {
                Sheet sheet = null;
                if (StringUtils.isNotBlank(sheetBean.getSheetName())) {
                    sheet = workBook.getSheet(sheetBean.getSheetName());
                } else {
                    sheet = workBook.getSheetAt(sheetBean.getSheetIndex() - 1);
                }
                Assert.isTrue(sheet != null, "未找到工作簿[" + sheetBean.getSheetName() + "]");
                if (Map.class.isAssignableFrom(sheetBean.getClazz())) {
                    List<List<DisplayText>> displayTexts = Lists.newArrayList();
                    sheetBean.setData(importMap(sheet, sheetBean, displayTexts));
                    validate(sheet, sheetBean, displayTexts);
                } else {
                    SheetBeanCache.initSheetBean(sheetBean.getClazz());
                    Multimap<Integer, CellBeanDefinition> multimap = null;
                    if (sheetBean.getField() != null && !sheetBean.getField().isEmpty()) {
                        multimap = getSubByField(sheet, sheetBean);
                    } else if (sheetBean.isSkipSubTitle()) {
                        multimap = getSkipSubTitle(sheetBean);
                    } else {
                        multimap = getSubTitle(sheet, sheetBean);
                    }
                    List<List<DisplayText>> displayTexts = getDisplayText(sheetBean, sheet, multimap);
                    validate(sheet, sheetBean, multimap, displayTexts);
                    List list = getData(sheetBean.getClazz(), displayTexts);
                    sheetBean.setData(list);
                }
            }
        } finally {
            if (workBook != null) {
                workBook.close();
            }
        }
    }


    public static void register(Class<?> clazz, Class<? extends ReadConverterFactory> clazzConverter) {
        CLASS_READ_CONVERTER_FACTORY_MAP.put(clazz, clazzConverter);
    }

    private static List<Map<Serializable, Object>> importMap(Sheet sheet, SheetBean sheetBean, List<List<DisplayText>> displayTexts) throws Exception {
        List<Map<Serializable, Object>> result = Lists.newArrayList();
        for (int y = sheetBean.getDataIndex() - 1; y <= sheet.getLastRowNum(); y++) {
            List<DisplayText> displayTextList = Lists.newArrayList();
            Row row = sheet.getRow(y);
            if (row == null) {
                continue;
            }
            Map<Serializable, Object> map = new LinkedHashMap<>();
            int empty = 0;
            for (int i = 0; i < row.getLastCellNum(); i++) {
                Cell cell = row.getCell(i);
                if (cell == null) {
                    continue;
                }
                Collection<CellBeanDefinition> fields = sheetBean.getField().get(i + 1);
                if (CollectionUtils.isEmpty(fields)) {
                    if (sheetBean.isSkipNoMapping()) {
                        continue;
                    }
                    map.put(i, cell == null ? null : getCell(cell).getValue());
                } else {
                    DisplayText displayText = new DisplayText();
                    displayText.setRowIndex(y);
                    displayText.setCellIndex(i);
                    Map<CellBeanDefinition, Object> propertyValue = Maps.newHashMap();
                    fields.forEach(o -> {
                        Object value = null;
                        if (cell != null) {
                            value = getCell(cell).getValue();
                        }
                        String defaultValue = o.getDefaultValue();
                        map.put(o.getName(), value == null ? (StringUtils.isNotBlank(defaultValue) ? defaultValue : null) : value);
                        propertyValue.put(o, value);
                    });
                    displayText.setPropertyValue(propertyValue);
                    displayTextList.add(displayText);
                }
            }
            for (Map.Entry<Serializable, Object> m : map.entrySet()) {
                if (m.getValue() == null || StringUtils.isBlank(m.getValue().toString()) || "null".equalsIgnoreCase(m.getValue().toString())) {
                    empty++;
                } else {
                    break;
                }
            }
            if (empty == map.size()) {
                logger.info("工作簿[{}]第[{}]行为空白行，已忽略", sheet.getSheetName(), (y + 1));
                continue;
            }
            result.add(map);
            displayTexts.add(displayTextList);
        }
        return result;
    }

    private static Workbook createWorkbook(InputStream inputStream) throws Exception {
        Workbook workBook = null;//WorkbookFactory.create(inputStream);
        if (POIFSFileSystem.hasPOIFSHeader(inputStream)) {
            workBook = new HSSFWorkbook(inputStream);
        } else if (POIXMLDocument.hasOOXMLHeader(inputStream)) {
            OPCPackage opcPackage = OPCPackage.open(inputStream);
            workBook = new XSSFWorkbook(opcPackage);
        }
        return workBook;
    }

    private static List<List<DisplayText>> getDisplayText(SheetBean sheetBean, Sheet sheet, Multimap<Integer, CellBeanDefinition> multimap) throws Exception {
        int dataIndex = sheetBean.getDataIndex() > 0 ? sheetBean.getDataIndex() - 1 : getSubTitleIndex(sheetBean) + 1;
        int rows = sheet.getLastRowNum();
        List<List<DisplayText>> validateDisplayTexts = new ArrayList<>();
        Set<String> keys = Sets.newHashSet();
        multimap.asMap().values().forEach(cellBeanDefinitions -> {
            cellBeanDefinitions.forEach(cellBeanDefinition -> {
                keys.add(cellBeanDefinition.getName());
            });
        });
        for (int y = dataIndex; y <= rows; y++) {
            Row row = sheet.getRow(y);
            if (row == null) {
                continue;
            }
            List<DisplayText> displayTexts = getRow(sheetBean, row, multimap, Lists.newArrayList(keys));
            int empty = 0;
            for (DisplayText displayText : displayTexts) {
                if (displayText.isDefaultFlag() || displayText.getValue() == null || StringUtils.isBlank(displayText.getStringValue()) || "null".equalsIgnoreCase(displayText.getStringValue())) {
                    empty++;
                } else {
                    break;
                }
            }
            if (empty == displayTexts.size()) {
                logger.info("工作簿[{}]第[{}]行为空白行，已忽略", sheet.getSheetName(), (y + 1));
                continue;
            }
            validateDisplayTexts.add(displayTexts);
        }
        return validateDisplayTexts;
    }

    private static <T> List<T> getData(Class<T> clazz, List<List<DisplayText>> displayTexts) throws Exception {
        List<T> result = new ArrayList<>();
        for (List<DisplayText> list : displayTexts) {
            T t = clazz.newInstance();
            for (DisplayText displayText : list) {
                Map<CellBeanDefinition, Object> propertyValue = displayText.getPropertyValue();
                if (MapUtils.isNotEmpty(propertyValue)) {
                    for (Map.Entry<CellBeanDefinition, Object> bean : propertyValue.entrySet()) {
                        CellBeanDefinition cellBeanDefinition = bean.getKey();
                        String name = cellBeanDefinition.getName();
                        String ofName = cellBeanDefinition.getOfName();
                        if (StringUtils.isNotBlank(ofName)) {
                            name += "." + ofName;
                        }
                        Class<?> javaType = cellBeanDefinition.getJavaType();
                        Class<? extends ReadConverterFactory> converter = CLASS_READ_CONVERTER_FACTORY_MAP.get(cellBeanDefinition.getJavaType());
                        if (javaType.isEnum()) {
                            if (converter == null) {
                                converter = CLASS_READ_CONVERTER_FACTORY_MAP.get(Enum.class);
                            }
                        }
                        Object value = bean.getValue();
                        if (converter != null) {
                            value = converter.newInstance().returnValue(cellBeanDefinition, displayText);
                        }
                        PlatformUtils.setPropertyValue(t, name, value);

                    }
                }
            }
            result.add(t);
        }
        return result;
    }

    private static void validate(Sheet sheet, SheetBean sheetBean, List<List<DisplayText>> displayTexts) {
        for (List<DisplayText> list : displayTexts) {
            Multimap<String, String> map = ArrayListMultimap.create();
            for (DisplayText displayText : list) {
                displayText.getPropertyValue().forEach((cellBeanDefinition, value) -> {
                    validateField(sheet, sheetBean, cellBeanDefinition, map, displayText, value);
                });
                if (StringUtils.isNotBlank(displayText.getError())) {
                    sheetBean.errorText(sheet, sheetBean, displayText, displayText.getError());
                }
            }
        }
    }

    private static void validateField(Sheet sheet, SheetBean sheetBean, CellBeanDefinition cellBeanDefinition, Multimap<String, String> map, DisplayText displayText, Object val) {
        val = val == null ? displayText.getValue() : val;
        if (!cellBeanDefinition.isAllowBlank()) {
            if (val == null || StringUtils.isBlank(val.toString()) || "null".equalsIgnoreCase(val.toString())) {
                if (StringUtils.isNotBlank(cellBeanDefinition.getBlankTemplate())) {
                    sheetBean.getError().add(parseError(cellBeanDefinition.getBlankTemplate(), sheet, sheetBean, cellBeanDefinition, displayText));
                } else {
                    sheetBean.error(sheet, sheetBean, cellBeanDefinition, displayText, BLANK_TYPE);
                }
            }
        }
        if (!cellBeanDefinition.isAllowRepeat()) {
            String value = val == null ? "" : val.toString();
            if (map.get(cellBeanDefinition.getName()).contains(value)) {
                if (StringUtils.isNotBlank(cellBeanDefinition.getRepeatTemplate())) {
                    sheetBean.getError().add(parseError(cellBeanDefinition.getRepeatTemplate(), sheet, sheetBean, cellBeanDefinition, displayText));
                } else {
                    sheetBean.error(sheet, sheetBean, cellBeanDefinition, displayText, REPEAT_TYPE);
                }
            }
            map.put(cellBeanDefinition.getName(), value);
        }

        if(cellBeanDefinition.getValidEvent()!=null){
            Set<String> msg = cellBeanDefinition.getValidEvent().valid(sheet, sheetBean, cellBeanDefinition, displayText, val);
            if(CollectionUtils.isNotEmpty(msg)){
                sheetBean.getError().addAll(msg);
            }
        }
    }

    private static void validate(Sheet sheet, SheetBean sheetBean, Multimap<Integer, CellBeanDefinition> multimap, List<List<DisplayText>> displayTexts) {
        Multimap<String, String> map = ArrayListMultimap.create();
        for (List<DisplayText> list : displayTexts) {
            for (DisplayText displayText : list) {
                int cellIndex = displayText.getCellIndex() + 1;
                if (CollectionUtils.isNotEmpty(multimap.get(cellIndex))) {
                    for (CellBeanDefinition cellBeanDefinition : multimap.get(cellIndex)) {
                        validateField(sheet, sheetBean, cellBeanDefinition, map, displayText, null);
                    }
                }
                if (StringUtils.isNotBlank(displayText.getError())) {
                    sheetBean.errorText(sheet, sheetBean, displayText, displayText.getError());
                }
            }
        }
    }

    private static String parseError(String template, Sheet sheet, SheetBean sheetBean, CellBeanDefinition
            cellBeanDefinition, DisplayText displayText) {
        //#{sheetName} #{rowIndex} #{cellIndex} #{p1} #{p2} #{value}
        template = template.replace("#{sheetName}", sheet.getSheetName());
        template = template.replace("#{rowIndex}", String.valueOf(displayText.getRowIndex() + 1));
        template = template.replace("#{cellIndex}", String.valueOf(displayText.getCellIndex() + 1));
        template = template.replace("#{value}", displayText.getValue() == null ? "" : displayText.getValue().toString());
        if (ArrayUtils.isNotEmpty(cellBeanDefinition.getParameters())) {
            for (int i = 0; i < cellBeanDefinition.getParameters().length; i++) {
                template = template.replace("#{p" + (i + 1) + "}", cellBeanDefinition.getParameters()[i]);
            }
        }
        return template;
    }

    private static <T> Multimap<Integer, CellBeanDefinition> getSkipSubTitle(SheetBean<T> sheetBean) {
        Multimap<Integer, CellBeanDefinition> multimap = LinkedHashMultimap.create();
        Map<String, CellBeanDefinition> cellBeanDefinitionMap = SheetBeanCache.initSheetBean(sheetBean.getClazz()).getGroup(sheetBean.getGorup());
        cellBeanDefinitionMap.forEach((field, cellBeanDefinitions) -> {
            multimap.put(cellBeanDefinitions.getCellIndex(), cellBeanDefinitions);
        });
        return multimap;
    }

    private static Multimap<Integer, CellBeanDefinition> getSubByField(Sheet sheet, SheetBean sheetBean) {
        Multimap<Integer, CellBeanDefinition> multimap = LinkedHashMultimap.create();
        Map<String, Class<?>> cellBeanDefinitionMap = SheetBeanCache.initCellBeanDefinition(sheetBean.getClazz(), sheetBean.getField().values());
        Map<Integer, Collection<CellBeanDefinition>> fields = sheetBean.getField().asMap();
        fields.forEach((key, list) -> {
            list.forEach(value -> {
                value.setJavaType(cellBeanDefinitionMap.get(value.getName()));
                multimap.put((Integer) key, value);
            });

        });
        return multimap;
    }

    private static Multimap<Integer, CellBeanDefinition> getSubTitle(Sheet sheet, SheetBean sheetBean) throws Exception {
        Row row = sheet.getRow(getSubTitleIndex(sheetBean));
        Multimap<String, CellBeanDefinition> cellBeanNameDefinitionMap = LinkedHashMultimap.create();
        Multimap<Integer, CellBeanDefinition> multimap = LinkedHashMultimap.create();
        SheetBeanCache.initSheetBean(sheetBean.getClazz()).getGroup(sheetBean.getGorup()).forEach((key, cellBeanDefinition) -> {
            if (StringUtils.isNotBlank(cellBeanDefinition.getTitle())) {
                cellBeanNameDefinitionMap.put(cellBeanDefinition.getTitle(), cellBeanDefinition);
            } else {
                multimap.put(cellBeanDefinition.getCellIndex(), cellBeanDefinition);
            }
        });
        for (DisplayText displayText : getRow(sheetBean, row, null, null)) {
            Collection<CellBeanDefinition> list = cellBeanNameDefinitionMap.get(displayText.getStringValue());
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(cellBeanDefinition -> {
                    multimap.put(displayText.getCellIndex() + 1, cellBeanDefinition);
                });
            }
        }
        return multimap;
    }

    private static int getSubTitleIndex(SheetBean sheetBean) {
        int subTitleIndex = sheetBean.getSubTitleIndex();
        if (sheetBean.isShowTitle()) {
            subTitleIndex = sheetBean.getTitleIndex() > subTitleIndex ? sheetBean.getTitleIndex() + 1 : subTitleIndex;
        }
        return subTitleIndex - 1;
    }

    private static void parseValue(CellBeanDefinition cellBeanDefinition, DisplayText displayText, Map<CellBeanDefinition, Object> propertyValue) throws Exception {
        if (cellBeanDefinition.getRead() != null && !cellBeanDefinition.getRead().equals(DefaultConverter.class)) {
            Object val = cellBeanDefinition.getRead().newInstance().read(cellBeanDefinition, displayText.getValue());
            propertyValue.put(cellBeanDefinition, val);
        } else {
            if (Date.class.isAssignableFrom(cellBeanDefinition.getJavaType())) {
                propertyValue.put(cellBeanDefinition, displayText.getDateValue());
            } else {
                propertyValue.put(cellBeanDefinition, displayText.getValue());
            }
        }
    }

    private static List<DisplayText> getRow(SheetBean sheetBean, Row row, Multimap<Integer, CellBeanDefinition> multimap, List<String> keys) throws Exception {
        List<DisplayText> displayTexts = new ArrayList<>();
        List<String> columns = null;
        if(CollectionUtils.isNotEmpty(keys)){
            columns = Lists.newArrayList(keys);
        }else{
            columns = Lists.newArrayList();
        }
        int cells = row.getLastCellNum();// 获得列数
        for (int i = 0; i < cells; i++) {
            Cell cell = row.getCell(i);
            DisplayText displayText = null;
            if (cell == null) {
                displayText = new DisplayText();
            } else {
                displayText = getCell(cell);
            }
            displayText.setRowIndex(row.getRowNum());
            displayText.setCellIndex(i);
            if (multimap != null && CollectionUtils.isNotEmpty(multimap.get(i + 1))) {
                Map<CellBeanDefinition, Object> propertyValue = new HashMap<>();
                for (CellBeanDefinition cellBeanDefinition : multimap.get(i + 1)) {
                    parseValue(cellBeanDefinition, displayText, propertyValue);
                    if (displayText.getValue() != null && StringUtils.isNotBlank(displayText.getValue().toString())) {
                        columns.remove(cellBeanDefinition.getName());
                    }
                }
                displayText.setPropertyValue(propertyValue);
            }
            displayTexts.add(displayText);
        }
        if (multimap != null) {
            SheetBeanDefinition sheetBeanDefinition = SheetBeanCache.initSheetBean(sheetBean.getClazz());
            Map<String, CellBeanDefinition> defaultValueMapping = sheetBeanDefinition.getDefaultValueMapping(sheetBean.getGorup());
            if (defaultValueMapping == null) {
                defaultValueMapping = sheetBeanDefinition.getDefaultValueMapping(sheetBean);
            }
            if (MapUtils.isNotEmpty(defaultValueMapping)) {
                for (Map.Entry<String, CellBeanDefinition> cellBeanDefinitionEntry : defaultValueMapping.entrySet()) {
                    if (columns.contains(cellBeanDefinitionEntry.getKey())) {
                        DisplayText displayText = new DisplayText();
                        displayText.setRowIndex(row.getRowNum());
                        displayText.setCellIndex(cellBeanDefinitionEntry.getValue().getCellIndex() - 1);
                        displayText.setValue(cellBeanDefinitionEntry.getValue().getDefaultValue());
                        Map<CellBeanDefinition, Object> propertyValue = new HashMap<>();
                        parseValue(cellBeanDefinitionEntry.getValue(), displayText, propertyValue);
                        displayText.setPropertyValue(propertyValue);
                        displayText.setDefaultFlag(true);
                        displayTexts.add(displayText);
                    }
                }
            }
        }
        return displayTexts;
    }


    private static DisplayText getCell(Cell cell) {
        DisplayText displayText = new DisplayText();
        try {
            Comment cellComment = cell.getCellComment();
            if (cellComment != null) {
                RichTextString text = cellComment.getString();
                displayText.setComments(text.getString());
            }
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    String result = null;
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                        SimpleDateFormat sdf = null;
                        if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                            sdf = new SimpleDateFormat("HH:mm");
                        } else {// 日期
                            sdf = new SimpleDateFormat(Constant.DATE_TIME_FORMAT);
                        }
                        Date date = cell.getDateCellValue();
                        result = sdf.format(date);
                    } else if (cell.getCellStyle().getDataFormat() == 58) {
                        // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_TIME_FORMAT);
                        double value = cell.getNumericCellValue();
                        Date date = DateUtil.getJavaDate(value);
                        result = sdf.format(date);
                    } else {
                        // 返回数值类型的值
                        Object inputValue = null;// 单元格值
                        Long longVal = Math.round(cell.getNumericCellValue());
                        Double doubleVal = cell.getNumericCellValue();
                        if (Double.parseDouble(longVal + ".0") == doubleVal) {   //判断是否含有小数位.0
                            inputValue = longVal;
                        } else {
                            inputValue = doubleVal;
                        }
                        DecimalFormat df = new DecimalFormat("#.####");    //格式化为四位小数，按自己需求选择；
                        result = String.valueOf(df.format(inputValue));      //返回String类型

//                        double value = cell.getNumericCellValue();
                       /* CellStyle style = cell.getCellStyle();
                        DecimalFormat decimalFormat = new DecimalFormat();
                        String temp = style.getDataFormatString();
                        // 单元格设置成常规
                        if (temp.equals("General")) {
                            decimalFormat.applyPattern("#");
                        }
                        result = decimalFormat.format(value);*/
//                        result=String.valueOf(inputValue);
                    }
                    displayText.setValue(result);
                    break;
                case Cell.CELL_TYPE_STRING:
                    displayText.setValue(cell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    displayText.setValue(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    Object strCell = null;
                    try {
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            Date date = cell.getDateCellValue();
                            strCell = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                            break;
                        } else {
                            strCell = cell.getNumericCellValue();
                        }
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        strCell = cell.getStringCellValue();

                    }
                    displayText.setValue(strCell);
                    break;
                default:
                    displayText.setValue(cell.getStringCellValue());
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("单元格第[%s]行第[%s]列数据读取错误:错误原因:[%s]", cell.getRowIndex() + 1, cell.getColumnIndex() + 1, e.getMessage()), e);
        }
        if (displayText.getValue() != null && String.class.equals(displayText.getValue().getClass())) {
            displayText.setValue(displayText.getValue().toString().trim());
        }
        return displayText;
    }

}
