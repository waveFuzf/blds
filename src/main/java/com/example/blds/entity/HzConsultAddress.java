package com.example.blds.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.blds.CustomJsonDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
@Data
public class HzConsultAddress {
    @Id
    private Integer id;

    @Transient
    private String addressId;

    private Integer type;

    private Integer consultId;

    private String mailProvince;

    private String mailCity;

    private String mailAddress;

    private String mailName;

    private String mailPhone;

    private String mailCompany;

    private String mailNumber;

    private String mailPdf;

    private String returnProvince;

    private String returnCity;

    private String returnAddress;

    private String returnName;

    private String returnPhone;

    private String returnPdf;

    private String returnCompany;

    private String returnNumber;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;

    private Date deleteTime;


}