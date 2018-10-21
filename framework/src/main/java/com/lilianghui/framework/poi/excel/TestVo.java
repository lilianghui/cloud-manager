package com.lilianghui.framework.poi.excel;

import java.math.BigDecimal;
import java.util.Date;

public class TestVo {

//    @CellAttribute(cellIndex = 6, gorup = "aaa",defaultValue = "2018-10-10",title = "供应方（*匹配系统内已有供应商）")
    private String supperlir;

//    @CellAttribute(cellIndex = 3, gorup = "aaa",defaultValue = "2018-10-10")
    private String date;

//    @CellAttributes({@CellAttribute(cellIndex = 2), @CellAttribute(cellIndex = 4, gorup = "aaa")})
    private String dianhao;


//    @CellAttribute(cellIndex = 3)
    private String depId;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDianhao() {
        return dianhao;
    }

    public void setDianhao(String dianhao) {
        this.dianhao = dianhao;
    }

    public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

    public String getSupperlir() {
        return supperlir;
    }

    public void setSupperlir(String supperlir) {
        this.supperlir = supperlir;
    }

    @Override
    public String toString() {
        return "TestVo{" +
                "supperlir='" + supperlir + '\'' +
                ", date='" + date + '\'' +
                ", dianhao='" + dianhao + '\'' +
                ", depId=" + depId +
                '}';
    }
}
