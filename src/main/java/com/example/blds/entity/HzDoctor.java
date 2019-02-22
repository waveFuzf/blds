package com.example.blds.entity;

import java.util.Date;

public class HzDoctor {
    private Integer id;

    private String doctorName;

    private String doctorPosition;

    private String doctorPhoto;

    private Integer doctorDepartmentId;

    private String doctorDepartment;

    private Integer hosiptalId;

    private String hospitalName;

    private String doctorPhone;

    private Date createTime;

    private Date updateTime;

    private String isDelete;

    private Date deleteTime;

    private String goodAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName == null ? null : doctorName.trim();
    }

    public String getDoctorPosition() {
        return doctorPosition;
    }

    public void setDoctorPosition(String doctorPosition) {
        this.doctorPosition = doctorPosition == null ? null : doctorPosition.trim();
    }

    public String getDoctorPhoto() {
        return doctorPhoto;
    }

    public void setDoctorPhoto(String doctorPhoto) {
        this.doctorPhoto = doctorPhoto == null ? null : doctorPhoto.trim();
    }

    public Integer getDoctorDepartmentId() {
        return doctorDepartmentId;
    }

    public void setDoctorDepartmentId(Integer doctorDepartmentId) {
        this.doctorDepartmentId = doctorDepartmentId;
    }

    public String getDoctorDepartment() {
        return doctorDepartment;
    }

    public void setDoctorDepartment(String doctorDepartment) {
        this.doctorDepartment = doctorDepartment == null ? null : doctorDepartment.trim();
    }

    public Integer getHosiptalId() {
        return hosiptalId;
    }

    public void setHosiptalId(Integer hosiptalId) {
        this.hosiptalId = hosiptalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName == null ? null : hospitalName.trim();
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone == null ? null : doctorPhone.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete == null ? null : isDelete.trim();
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getGoodAt() {
        return goodAt;
    }

    public void setGoodAt(String goodAt) {
        this.goodAt = goodAt == null ? null : goodAt.trim();
    }
}