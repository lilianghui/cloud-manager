package com.lilianghui.spring.starter.core;

import com.google.code.or.binlog.BinlogEventV4;
import com.lilianghui.spring.starter.entity.CDCEvent;

public interface RowEvent {
    void onEvents(CDCEvent event);
}
