package com.example.blds.entity;

import lombok.Data;

import java.util.Date;
@Data
public class HzConsultDoctor {
    private Integer id;

    private Integer consultId;

    private Long doctorId;

    private Integer doctorType;

    private String doctorName;

    private String doctorPosition;

    private String doctorPhoto;

    private Long doctorDepartmentId;

    private String doctorDepartment;

    private Long hospitalId;

    private String hospitalName;

    private String doctorPhone;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;

    private Date deleteTime;

    private Integer collection;

}