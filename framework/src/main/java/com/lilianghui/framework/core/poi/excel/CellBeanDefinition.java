package com.lilianghui.framework.core.poi.excel;


public class CellBeanDefinition {
    private String name;//属性名
    private String title;//标题
    private String comment;//批注
    private String dateFormat;//日期格式化
    private String[] parameters;//额外参数
    private String defaultValue;//默认值
    private String ofName;// 实体类属性名
    private boolean allowRepeat = true;// 允许重复数据 默认允许
    private boolean allowBlank = true;// 允许为空 默认允许
    private Class<?> javaType;//java类型
    private Class<? extends WriteConverter> write;
    private Class<? extends ReadConverter> read;
    private int cellIndex;//列索引
    private int index;//第几个属性

    private String blankTemplate;//为空异常模板
    private String repeatTemplate;//重复异常模板

    private CellValidEvent validEvent;

    public static CellBeanDefinition build() {
        return new CellBeanDefinition();
    }

    public CellBeanDefinition name(String name) {
        this.name = name;
        return this;
    }

    public CellBeanDefinition title(String title) {
        this.title = title;
        return this;
    }

    public CellBeanDefinition cellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
        return this;
    }


    public CellBeanDefinition blankTemplate(String blankTemplate) {
        this.blankTemplate = blankTemplate;
        return this;
    }


    public CellBeanDefinition repeatTemplate(String repeatTemplate) {
        this.repeatTemplate = repeatTemplate;
        return this;
    }

    public CellBeanDefinition allowRepeat(boolean allowRepeat) {
        this.allowRepeat = allowRepeat;
        return this;
    }


    public CellBeanDefinition allowBlank(boolean allowBlank) {
        this.allowBlank = allowBlank;
        return this;
    }

    public CellBeanDefinition defaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }


    public CellBeanDefinition ofName(String ofName) {
        this.ofName = ofName;
        return this;
    }

    public CellBeanDefinition validEvent(CellValidEvent validEvent) {
        this.validEvent = validEvent;
        return this;
    }


    public CellBeanDefinition write(Class<? extends WriteConverter> write) {
        this.write = write;
        return this;
    }


    public CellBeanDefinition validEvent(Class<? extends ReadConverter> read) {
        this.read = read;
        return this;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAllowRepeat() {
        return allowRepeat;
    }

    public void setAllowRepeat(boolean allowRepeat) {
        this.allowRepeat = allowRepeat;
    }

    public boolean isAllowBlank() {
        return allowBlank;
    }

    public void setAllowBlank(boolean allowBlank) {
        this.allowBlank = allowBlank;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    public String getOfName() {
        return ofName;
    }

    public void setOfName(String ofName) {
        this.ofName = ofName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Class<? extends WriteConverter> getWrite() {
        return write;
    }

    public void setWrite(Class<? extends WriteConverter> write) {
        this.write = write;
    }

    public Class<? extends ReadConverter> getRead() {
        return read;
    }

    public void setRead(Class<? extends ReadConverter> read) {
        this.read = read;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    public String getBlankTemplate() {
        return blankTemplate;
    }

    public void setBlankTemplate(String blankTemplate) {
        this.blankTemplate = blankTemplate;
    }

    public String getRepeatTemplate() {
        return repeatTemplate;
    }

    public void setRepeatTemplate(String repeatTemplate) {
        this.repeatTemplate = repeatTemplate;
    }

    public CellValidEvent getValidEvent() {
        return validEvent;
    }

    public void setValidEvent(CellValidEvent validEvent) {
        this.validEvent = validEvent;
    }
}
