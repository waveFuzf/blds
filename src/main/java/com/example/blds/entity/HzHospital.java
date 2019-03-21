package com.example.blds.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.blds.CustomJsonDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
@Data
public class HzHospital {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private Integer hospitalId;

    private String name;

    private String address;

    private String tel;

    private String footLogo;

    private HzUser admin;

    private Integer isDelete;
    @JSONField(format = "YYYY-MM-dd HH:mm:ss")
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date updataTime;
    @JSONField(format = "YYYY-MM-dd HH:mm:ss")
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date createTime;


}