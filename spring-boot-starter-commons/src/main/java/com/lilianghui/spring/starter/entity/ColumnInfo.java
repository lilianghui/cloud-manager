package com.lilianghui.spring.starter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColumnInfo {
    private String name;
    private String type;
}