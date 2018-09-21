package com.lilianghui.entity;

import com.lilianghui.framework.core.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_market_manage_customer")
public class User extends BaseEntity {
    @Id
    private String id;
    @Column
    private String customer;
    @Column(name = "TEL_PHONE")
    private String telPhone;
    private String currentAddress;
    private String nativeAddress;
}
