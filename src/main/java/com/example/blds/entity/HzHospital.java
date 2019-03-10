package com.example.blds.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.blds.CustomJsonDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import javax.persistence.Id;
import java.util.Date;
@Data
public class HzHospital {
    @Id
    private Integer id;

    private Integer unionId;

    private Integer hospitalId;

    private String name;

    private String address;

    private String tel;

    private String footLogo;

    private String appCode;

    private String gzhCode;

    private String fwhCode;

    private String fwcCode;

    private Integer isDelete;
    @JSONField(format = "YYYY-MM-dd HH:mm:ss")
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date updataTime;
    @JSONField(format = "YYYY-MM-dd HH:mm:ss")
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date createTime;


}