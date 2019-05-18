package com.lilianghui.controller;

import com.lilianghui.entity.User;
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
@RequestMapping("user")
public class UserController extends TccParticipantController<User> {
    @Resource
    private IndexService indexService;

    @Override
    public String getParticipantName() {
        return "user";
    }

    @Override
    public ResponseEntity executeTry(String txId, User body) {
        body.setTxId(txId);
        body.setExpireTime(DateUtils.addMilliseconds(new Date(), 30));
        body.setState(AtomikosTccState.ORDERED.name());
        indexService.saveUser(body);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity executeCancel(String txId) {
        User user = new User();
        user.setTxId(txId);
        user.setState(AtomikosTccState.CANCELED.name());
        indexService.updateUser(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Override
    public ResponseEntity executeConfirm(String txId) {
        User user = new User();
        user.setTxId(txId);
        user.setState(AtomikosTccState.CONFIRMED.name());
        indexService.updateUser(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
