package test;

import com.lilianghui.framework.core.poi.excel.CellBeanDefinition;
import com.lilianghui.framework.core.poi.excel.Import;
import com.lilianghui.framework.core.poi.excel.SheetBean;
import entity.InstockQuickTraceVO;
import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Category(ImportTest.class)
public class ImportTest {
    private static InputStream fileInputStream;

    @BeforeClass
    public static void init() throws Exception {
        fileInputStream = ImportTest.class.getResourceAsStream("/quickTraceNmInStock.xlsx");
    }

    @Test
    public void importEntityByTitle() throws Exception {
        SheetBean<InstockQuickTraceVO> sheetBean = new SheetBean();
        sheetBean.setSheetIndex(1);
        sheetBean.setSkipSubTitle(false);
        sheetBean.setSubTitleIndex(1);
        sheetBean.setDataIndex(2);
        sheetBean.setClazz(InstockQuickTraceVO.class);
        Import.imports(fileInputStream, sheetBean);
        List<?> excelRowList = sheetBean.getData();
        excelRowList.forEach(System.out::println);
        if (sheetBean.hasError()) {
            throw new AssertionError(StringUtils.join(sheetBean.getError(), ","));
        }
    }

    @Test
    public void importEntity() throws Exception {
        SheetBean<InstockQuickTraceVO> sheetBean = new SheetBean();
        sheetBean.setSheetIndex(1);
        sheetBean.setSkipSubTitle(true);
        sheetBean.setDataIndex(2);
        sheetBean.setClazz(InstockQuickTraceVO.class);
        Import.imports(fileInputStream, sheetBean);
        List<?> excelRowList = sheetBean.getData();
        excelRowList.forEach(System.out::println);
        if (sheetBean.hasError()) {
            throw new AssertionError(StringUtils.join(sheetBean.getError(), ","));
        }
    }


    @Test
    public void importMap() throws Exception {
        SheetBean<Map> sheetBean = new SheetBean<>();
        sheetBean.setSheetIndex(1);
        sheetBean.setSkipSubTitle(true);
        sheetBean.setDataIndex(2);
        sheetBean.setClazz(Map.class);
        sheetBean.setSkipNoMapping(true);
        sheetBean.getField().put(1, CellBeanDefinition.build().name("supplier_entp_id").allowBlank(false).blankTemplate("第#{rowIndex}行第#{cellIndex}列供应方不能为空"));
        sheetBean.getField().put(2, CellBeanDefinition.build().name("entp_id").allowBlank(false).blankTemplate("第#{rowIndex}行第#{cellIndex}列经营户不能为空"));
        sheetBean.getField().put(3, CellBeanDefinition.build().name("instock_date"));
        sheetBean.getField().put(4, CellBeanDefinition.build().name("employee_id").allowBlank(false).blankTemplate("第#{rowIndex}行第#{cellIndex}列操作员不能为空"));
        sheetBean.getField().put(5, CellBeanDefinition.build().name("plate_number"));
        sheetBean.getField().put(6, CellBeanDefinition.build().name("deliveryman"));
        sheetBean.getField().put(7, CellBeanDefinition.build().name("product_sku").allowBlank(false).blankTemplate("第#{rowIndex}行第#{cellIndex}列商品编号不能为空"));
        sheetBean.getField().put(8, CellBeanDefinition.build().name("product_name"));
        sheetBean.getField().put(9, CellBeanDefinition.build().name("other_id"));
        sheetBean.getField().put(10, CellBeanDefinition.build().name("animal_cert_code"));
        sheetBean.getField().put(11, CellBeanDefinition.build().name("quantity").defaultValue("0"));
        sheetBean.getField().put(12, CellBeanDefinition.build().name("price").defaultValue("0"));


        Import.imports(fileInputStream, sheetBean);
        List<?> excelRowList = sheetBean.getData();
        excelRowList.forEach(System.out::println);
        if (sheetBean.hasError()) {
            throw new AssertionError(StringUtils.join(sheetBean.getError(), ","));
        }
    }

    @AfterClass
    public static void destroy() throws Exception {
        if (fileInputStream != null) {
            fileInputStream.close();
        }
    }

}
