package com.lilianghui.controller;

import com.lilianghui.entity.Item;
import com.lilianghui.service.IndexService;
import com.lilianghui.spring.starter.controller.TccParticipantController;
import com.lilianghui.spring.starter.entity.AtomikosTccState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("item")
public class ItemController extends TccParticipantController<Item> {
    @Resource
    private IndexService indexService;

    @Override
    public String getParticipantName() {
        return "item";
    }

    @Override
    public ResponseEntity executeTry(String txId, Item body) {
        body.setTxId(txId);
        body.setExpireTime(DateUtils.addMilliseconds(new Date(), 30));
        body.setState(AtomikosTccState.ORDERED.name());
        indexService.saveItem(body);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity executeCancel(String txId) {
        Item item = new Item();
        item.setTxId(txId);
        item.setState(AtomikosTccState.CANCELED.name());
        indexService.updateItem(item);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Override
    public ResponseEntity executeConfirm(String txId) {
        Item item = new Item();
        item.setTxId(txId);
        item.setState(AtomikosTccState.CONFIRMED.name());
        indexService.updateItem(item);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
