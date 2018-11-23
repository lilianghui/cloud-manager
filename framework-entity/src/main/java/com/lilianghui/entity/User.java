package com.lilianghui.entity;

import com.lilianghui.framework.core.entity.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode
@Table(name = "t_market_manage_customer")
public class User extends BaseEntity {
    @Id
    private String id;

    @Column
    private String customer;

    @Column(name = "TEL_PHONE")
    private String telPhone;

    @Column(name = "CERTIFICATE_CODE")
    private String certificateCode;

    private String currentAddress;

    private String nativeAddress;
}
