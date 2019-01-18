package com.lilianghui.application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BinlogInfo {
    private String binlogName;
    private Long fileSize;
}
