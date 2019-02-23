package com.lilianghui.spring.starter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BinlogInfo {
    private String binlogName;
    private Long fileSize;
    private long position;

    public BinlogInfo(String binlogName, Long fileSize) {
        this.binlogName = binlogName;
        this.fileSize = fileSize;
    }
}
