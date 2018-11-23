package com.lilianghui.entity;

import com.lilianghui.framework.core.entity.BaseEntity;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode
@Table(name = "t_nimble_entp")

@Document(indexName = "t_nimble_entp", type = "simple", shards = 1, replicas = 1, refreshInterval = "-1")
/**
 * 默认情况下添加@Document注解会对实体中的所有属性建立索引
 */
public class Contract extends BaseEntity {

    @Id
    @org.springframework.data.annotation.Id
    private String id;

    @Column(name = "PARENT_ENTP_ID")
    @org.springframework.data.annotation.Transient
    private String parentEntpId;

    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    @Column(name = "ENTP_NO")
    private String entpNo;

    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    @Column(name = "ENTP_NAME")
    private String entpName;

    @Column(name = "ENTP_NATURE")
    @org.springframework.data.annotation.Transient
    private String entpNature;

    @Column(name = "ENTP_TYPE")
    @org.springframework.data.annotation.Transient
    private String entpType;

    @Column(name = "SUPPLIER_NATURE")
    @org.springframework.data.annotation.Transient
    private String supplierNature;

    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    @Column(name = "REG_ADDRESS")
    private String regAddress;

    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    @Column(name = "CONTACT_PERSON")
    private String contactPerson;

    @Column(name = "CONTACT_DUTIES")
    @org.springframework.data.annotation.Transient
    private String contactDuties;

    @Column(name = "CONTACT_DETAIL")
    @org.springframework.data.annotation.Transient
    private String contactDetail;

    @Column(name = "OTHER_ID")
    @org.springframework.data.annotation.Transient
    private String otherId;

    @Column(name = "CONTACT_FAX")
    @org.springframework.data.annotation.Transient
    private String contactFax;

    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    @Column(name = "COUNTRY_ID")
    private String countryId;

    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    @Column(name = "PROVINCE_ID")
    private String provinceId;

    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    @Column(name = "CITY_ID")
    private String cityId;

    @Column(name = "AREA_ID")
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String areaId;

    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    @Column(name = "ENTP_ADDRESS")
    private String entpAddress;

    @Field(type = FieldType.Double)
    @Column(name = "FIELD_LATITUDE")
    private BigDecimal fieldLatitude;

    @Field(type = FieldType.Double)
    @Column(name = "FIELD_LONGITUDE")
    private BigDecimal fieldLongitude;

    @Column(name = "ENTP_POSTCODE")
    @org.springframework.data.annotation.Transient
    private String entpPostcode;

    @Column(name = "BIZ_REG_NUMBER")
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String bizRegNumber;

    @Column(name = "LEGAL_REPRESENTATIVE")
    @org.springframework.data.annotation.Transient
    private String legalRepresentative;

    @Column(name = "ESTABLISHMENT_DATE")
    @org.springframework.data.annotation.Transient
    private Date establishmentDate;

    @Column(name = "REGISTERED_CAPITAL")
    @org.springframework.data.annotation.Transient
    private BigDecimal registeredCapital;

    @Column(name = "ENTP_WEBSITE")
    @org.springframework.data.annotation.Transient
    private String entpWebsite;

    @Column(name = "ENTP_INTRO")
    @org.springframework.data.annotation.Transient
    private String entpIntro;

    @Column(name = "ENTP_WECHAT")
    @org.springframework.data.annotation.Transient
    private String entpWechat;

    @Column(name = "MARKET_ID")
    @org.springframework.data.annotation.Transient
    private String marketId;

    @Column(name = "ENTP_SHORT_NAME")
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String entpShortName;

    @Column(name = "END_DATE")
    @org.springframework.data.annotation.Transient
    private Date endDate;

    @Column(name = "FILE_INFO_ID")
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String fileInfoId;

    @Column(name = "BAK_DATE")
    @org.springframework.data.annotation.Transient
    private Date bakDate;

    @Column(name = "TRACE_FLAG")
    @org.springframework.data.annotation.Transient
    private String traceFlag;

    @Column(name = "ENTP_TEMPLATE")
    @org.springframework.data.annotation.Transient
    private String entpTemplate;

    @Column(name = "ENTP_QR_CODE")
    @org.springframework.data.annotation.Transient
    private String entpQrCode;

    @Column(name = "STALL_NUM")
    @org.springframework.data.annotation.Transient
    private String stallNum;

    @Column(name = "PRODUCT_FILE_INFO")
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String productFileInfo;

    @Column(name = "EMAIL")
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String email;

    @Field(type = FieldType.Date)
    @Column(name = "SYS_REG_TMSP")
    private Date sysRegTmsp;

    @Field(type = FieldType.Date)
    @Column(name = "SYS_UPD_TMSP")
    private Date sysUpdTmsp;

    @Field(type = FieldType.Integer)
    @Column(name = "DEL_FLG")
    private Date delFlg;
}
