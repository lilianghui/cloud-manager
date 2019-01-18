package com.lilianghui.application.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class TableInfo {

    private String databaseName;
    private String tableName;
    private String fullName;

}