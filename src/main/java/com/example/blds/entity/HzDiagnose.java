package com.example.blds.entity;

import lombok.Data;

import java.util.Date;
@Data
public class HzDiagnose {
    private Integer id;

    private Integer consultId;

    private Long doctorId;

    private String mirrorView;

    private String diagnose;

    private String immuneTag;

    private String immuneRemark;

    private String immuno;

    private String pdf;

    private String remark;

    private Integer slideEstimate;

    private Integer diagnosisEstimate;

    private Date diagnoseTime;

    private Integer materialNum;

    private Integer isCandle;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;

    private Date deleteTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getConsultId() {
        return consultId;
    }

    public void setConsultId(Integer consultId) {
        this.consultId = consultId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getImmuno() {
        return immuno;
    }

    public void setImmuno(String immuno) {
        this.immuno = immuno == null ? null : immuno.trim();
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf == null ? null : pdf.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getSlideEstimate() {
        return slideEstimate;
    }

    public void setSlideEstimate(Integer slideEstimate) {
        this.slideEstimate = slideEstimate;
    }

    public Integer getDiagnosisEstimate() {
        return diagnosisEstimate;
    }

    public void setDiagnosisEstimate(Integer diagnosisEstimate) {
        this.diagnosisEstimate = diagnosisEstimate;
    }

    public Date getDiagnoseTime() {
        return diagnoseTime;
    }

    public void setDiagnoseTime(Date diagnoseTime) {
        this.diagnoseTime = diagnoseTime;
    }

    public Integer getMaterialNum() {
        return materialNum;
    }

    public void setMaterialNum(Integer materialNum) {
        this.materialNum = materialNum;
    }

    public Integer getIsCandle() {
        return isCandle;
    }

    public void setIsCandle(Integer isCandle) {
        this.isCandle = isCandle;
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

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }
}