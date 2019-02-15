package com.example.blds.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class HzUser {
    @Id
    private Long id;

    private Long userId;

    private String name;

    private Integer userState;

    private BigDecimal userScore;

    private String userLevel;

    private Long departmentId;

    private String department;

    private Long hospitalId;

    private String hospitalName;

    private String sex;

    private String phone;

    private String photo;

    private String position;

    private Integer totalConsulation;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    private Integer isDelete;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @JSONField(serialize=false)
    private Date deleteTime;

    private String description;

}