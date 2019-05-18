package com.lilianghui.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@Table(name = "item")
public class Item {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "value")
    private Integer value;
    @Column(name = "indate")
    private Date indate ;

    @Column(name = "txId")
    private String txId;

    @Column(name = "expireTime")
    private Date expireTime;

    @Column(name = "state")
    private String state;

}
