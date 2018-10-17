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
@Table(name = "t_market_manage_contract")
public class Contract extends BaseEntity {

    @Id
    private String id;

    @Column(name = "CONTRACT_CODE")
    private String contractCode;

}
