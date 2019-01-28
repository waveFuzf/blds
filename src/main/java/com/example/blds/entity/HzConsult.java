package com.example.blds.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
public class HzConsult {
    @Id
    private Integer id;

    private Integer slideType;

    private Integer supplementSlideType;

    private Integer consultStatus;

    private String consultNo;

    private Integer verifyStatus;

    private String verifyReason;

    private Integer isCancel;

    private Integer payType;

    private String payUrl;

    private String payOrderNo;

    private Long payOrderCreateTime;

    private Integer drawBackStatus;

    private String caseTypeName;

    private String subspecialityName;

    private String parts;

    private Integer unionId;

    private Integer price;

    private Integer supplementPrice;

    private String reasonCancel;

    private String reason;

    private Date supplementCommitTime;

    private Date supplementReportTime;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;

    private Date deleteTime;

    private Integer preferentialPrice;
    @Transient
    private String consult_id;

    @Transient
    private HzConsultAddress hzConsultAddress;

    @Transient
    private List<HzConsultDoctor> doctors;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSlideType() {
        return slideType;
    }

    public void setSlideType(Integer slideType) {
        this.slideType = slideType;
    }

    public Integer getSupplementSlideType() {
        return supplementSlideType;
    }

    public void setSupplementSlideType(Integer supplementSlideType) {
        this.supplementSlideType = supplementSlideType;
    }

    public Integer getConsultStatus() {
        return consultStatus;
    }

    public void setConsultStatus(Integer consultStatus) {
        this.consultStatus = consultStatus;
    }

    public String getConsultNo() {
        return consultNo;
    }

    public void setConsultNo(String consultNo) {
        this.consultNo = consultNo == null ? null : consultNo.trim();
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getVerifyReason() {
        return verifyReason;
    }

    public void setVerifyReason(String verifyReason) {
        this.verifyReason = verifyReason == null ? null : verifyReason.trim();
    }

    public Integer getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(Integer isCancel) {
        this.isCancel = isCancel;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl == null ? null : payUrl.trim();
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo == null ? null : payOrderNo.trim();
    }

    public Long getPayOrderCreateTime() {
        return payOrderCreateTime;
    }

    public void setPayOrderCreateTime(Long payOrderCreateTime) {
        this.payOrderCreateTime = payOrderCreateTime;
    }

    public Integer getDrawBackStatus() {
        return drawBackStatus;
    }

    public void setDrawBackStatus(Integer drawBackStatus) {
        this.drawBackStatus = drawBackStatus;
    }

    public String getCaseTypeName() {
        return caseTypeName;
    }

    public void setCaseTypeName(String caseTypeName) {
        this.caseTypeName = caseTypeName == null ? null : caseTypeName.trim();
    }

    public String getSubspecialityName() {
        return subspecialityName;
    }

    public void setSubspecialityName(String subspecialityName) {
        this.subspecialityName = subspecialityName == null ? null : subspecialityName.trim();
    }

    public String getParts() {
        return parts;
    }

    public void setParts(String parts) {
        this.parts = parts == null ? null : parts.trim();
    }

    public Integer getUnionId() {
        return unionId;
    }

    public void setUnionId(Integer unionId) {
        this.unionId = unionId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSupplementPrice() {
        return supplementPrice;
    }

    public void setSupplementPrice(Integer supplementPrice) {
        this.supplementPrice = supplementPrice;
    }

    public String getReasonCancel() {
        return reasonCancel;
    }

    public void setReasonCancel(String reasonCancel) {
        this.reasonCancel = reasonCancel == null ? null : reasonCancel.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Date getSupplementCommitTime() {
        return supplementCommitTime;
    }

    public void setSupplementCommitTime(Date supplementCommitTime) {
        this.supplementCommitTime = supplementCommitTime;
    }

    public Date getSupplementReportTime() {
        return supplementReportTime;
    }

    public void setSupplementReportTime(Date supplementReportTime) {
        this.supplementReportTime = supplementReportTime;
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