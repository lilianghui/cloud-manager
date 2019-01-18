package com.lilianghui.application.entity;

import lombok.Data;

@Data
public class BinlogMasterStatus {
    private String binlogName;
    private long position;
}
