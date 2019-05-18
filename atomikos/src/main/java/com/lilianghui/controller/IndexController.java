package com.lilianghui.controller;

import com.atomikos.tcc.rest.ParticipantLink;
import com.atomikos.tcc.rest.Transaction;
import com.lilianghui.client.TccCoordinatorClient;
import com.lilianghui.entity.Item;
import com.lilianghui.entity.User;
import com.lilianghui.service.IndexService;
import com.lilianghui.spring.starter.controller.TccParticipantController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class IndexController {

    @Resource
    private IndexService indexService;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private TccCoordinatorClient tccCoordinatorClient;

    @RequestMapping("saveData")
    public String saveData() {
        Item item = new Item();
        long id = new Random().nextLong();
        item.setId(id);
        item.setIndate(new Date());
        item.setValue(new Random().nextInt());
        User user = new User();
        user.setId(id);
        user.setIndate(new Date());
        user.setName("name-" + id);
        indexService.saveData(user, item);
        return "success";
    }

    @RequestMapping("newOrderWithTcc")
    public void newOrderWithTcc(HttpServletRequest request) {
        String txId = UUID.randomUUID().toString();
        long expireTime = System.currentTimeMillis() + 30000;

        String ip = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        List<ParticipantLink> participantLinks = new ArrayList<>(2);

        String orderServiceUrl = String.format("%s/user/tcc/%s", ip, txId);
        participantLinks.add(new ParticipantLink(orderServiceUrl, expireTime));

        String inventoryServiceUrl = String.format("%s/item/tcc/%s", ip, txId);
        participantLinks.add(new ParticipantLink(inventoryServiceUrl, expireTime));

        Transaction transaction = new Transaction(participantLinks);
        try {

            //1. try participant order-service
            User user = new User();
            user.setName("aaa");
            user.setId(System.currentTimeMillis());
            user.setTxId(txId);
            user.setIndate(new Date());
            restTemplate.postForEntity(orderServiceUrl, user, String.class);

            //2. try participant inventory-service
            Item item = new Item();
            restTemplate.postForEntity(inventoryServiceUrl, item, String.class);

            //3. call coordinator to confirm
            tccCoordinatorClient.confirm(transaction);
        } catch (Exception e) {
            //4. call coordinator to cancel
            tccCoordinatorClient.cancel(transaction);
            String msg = e instanceof HttpStatusCodeException ? ((HttpStatusCodeException) e).getResponseBodyAsString() : e.getMessage();
            throw new RuntimeException(msg, e);
        }
    }
}
