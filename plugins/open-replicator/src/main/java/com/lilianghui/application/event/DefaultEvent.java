package com.lilianghui.application.event;

import com.lilianghui.spring.starter.core.RowEvent;
import com.lilianghui.spring.starter.entity.CDCEvent;
import org.springframework.stereotype.Component;

@Component
public class DefaultEvent implements RowEvent {
    @Override
    public void onEvents(CDCEvent event) {
        System.out.println(event);
    }
}
