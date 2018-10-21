package com.lilianghui.framework.poi.excel;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static net.zhuisuyun.saas.stock.client.utils.poi2.Constant.DEFAULT_GROUP;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD})
public @interface CellAttribute {

    String title() default "";

    String defaultValue() default ""; //默认值

    String name() default "";//实体类属性名

    String ofName() default "";//实体类属性名

    int cellIndex() default -1;//列号

    String gorup() default DEFAULT_GROUP;

    String[] parameters() default {};//额外参数

    //import
    boolean allowRepeat() default true;// 允许重复数据 默认允许

    //#{sheetName} #{rowIndex} #{cellIndex} #{p1} #{p2} #{value}
    String repeatTemplate() default "";//重复异常模板

    boolean allowBlank() default true;// 允许为空 默认允许

    //#{sheetName} #{rowIndex} #{cellIndex} #{p1} #{p2} #{value}
    String blankTemplate() default "";//为空异常模板


    Class<? extends ReadConverter> readConverter()
            default DefaultConverter.class;//读数据转换器

    //export

    String comment() default "";     //批注

    Class<? extends WriteConverter> writeConverter()
            default DefaultConverter.class;//写数据转换器

    String dateFormat() default Constant.DATE_FORMAT;

}
