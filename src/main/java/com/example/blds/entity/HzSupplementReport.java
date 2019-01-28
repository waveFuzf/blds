package com.example.blds.entity;

import lombok.Data;

import java.util.Date;
@Data
public class HzSupplementReport {
    private Integer id;

    private Integer consultId;

    private String censorate;

    private Integer materialNum;

    private Integer isCandle;

    private String initialJudgement;

    private String remarkDoctor;

    private String ultimateJudgement;

    private String supplementaryOpinion;

    private Date commitTime;

    private Date reportTime;

    private String reportPath;

    private String sendReportPath;

    private Integer isDelete;

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

    public String getCensorate() {
        return censorate;
    }

    public void setCensorate(String censorate) {
        this.censorate = censorate == null ? null : censorate.trim();
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

    public String getInitialJudgement() {
        return initialJudgement;
    }

    public void setInitialJudgement(String initialJudgement) {
        this.initialJudgement = initialJudgement == null ? null : initialJudgement.trim();
    }

    public String getRemarkDoctor() {
        return remarkDoctor;
    }

    public void setRemarkDoctor(String remarkDoctor) {
        this.remarkDoctor = remarkDoctor == null ? null : remarkDoctor.trim();
    }

    public String getUltimateJudgement() {
        return ultimateJudgement;
    }

    public void setUltimateJudgement(String ultimateJudgement) {
        this.ultimateJudgement = ultimateJudgement == null ? null : ultimateJudgement.trim();
    }

    public String getSupplementaryOpinion() {
        return supplementaryOpinion;
    }

    public void setSupplementaryOpinion(String supplementaryOpinion) {
        this.supplementaryOpinion = supplementaryOpinion == null ? null : supplementaryOpinion.trim();
    }

    public Date getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath == null ? null : reportPath.trim();
    }

    public String getSendReportPath() {
        return sendReportPath;
    }

    public void setSendReportPath(String sendReportPath) {
        this.sendReportPath = sendReportPath == null ? null : sendReportPath.trim();
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}