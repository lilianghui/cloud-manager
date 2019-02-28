package com.lilianghui.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Table(name = "item")
public class Item  implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "value")
    private Integer value;
    @Column(name = "indate")
    private Date indate ;
}
