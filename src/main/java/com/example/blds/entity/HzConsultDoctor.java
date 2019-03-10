package com.example.blds.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.blds.CustomJsonDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import javax.persistence.Id;
import java.util.Date;
@Data
public class HzConsultDoctor {
    @Id
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

    public void autoAssignment(HzUser expertDoc) {
        this.doctorId=expertDoc.getUserId();
        this.doctorName=expertDoc.getName();
        this.doctorDepartment=expertDoc.getDepartment();
        this.doctorDepartmentId=expertDoc.getDepartmentId();
        this.hospitalId=expertDoc.getHospitalId();
        this.hospitalName=expertDoc.getHospitalName();
        this.doctorPosition=expertDoc.getPosition();
        this.doctorPhone=expertDoc.getPhone();
        this.doctorPhoto=expertDoc.getPhoto();
    }
}