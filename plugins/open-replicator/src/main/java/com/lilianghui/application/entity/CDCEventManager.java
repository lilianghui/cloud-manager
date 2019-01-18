package com.lilianghui.application.entity;

import java.util.concurrent.ConcurrentLinkedDeque;

import com.lilianghui.application.entity.CDCEvent;

public class CDCEventManager {
    public static final ConcurrentLinkedDeque<CDCEvent> queue = new ConcurrentLinkedDeque<>();
}