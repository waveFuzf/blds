package com.example.blds.entity;

import lombok.Data;

import java.util.Date;
@Data
public class HzSlide {
    private Integer id;

    private String uuid;

    private Integer consultId;

    private Long hospitalId;

    private Integer processStatus;

    private Integer type;

    private String slideName;

    private String clientSlidePath;

    private String cosSlidePath;

    private String slideSize;

    private String factoryUuid;

    private String tagUrl;

    private String tagClientPath;

    private String tagNo;

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public Integer getConsultId() {
        return consultId;
    }

    public void setConsultId(Integer consultId) {
        this.consultId = consultId;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSlideName() {
        return slideName;
    }

    public void setSlideName(String slideName) {
        this.slideName = slideName == null ? null : slideName.trim();
    }

    public String getClientSlidePath() {
        return clientSlidePath;
    }

    public void setClientSlidePath(String clientSlidePath) {
        this.clientSlidePath = clientSlidePath == null ? null : clientSlidePath.trim();
    }

    public String getCosSlidePath() {
        return cosSlidePath;
    }

    public void setCosSlidePath(String cosSlidePath) {
        this.cosSlidePath = cosSlidePath == null ? null : cosSlidePath.trim();
    }

    public String getSlideSize() {
        return slideSize;
    }

    public void setSlideSize(String slideSize) {
        this.slideSize = slideSize == null ? null : slideSize.trim();
    }

    public String getFactoryUuid() {
        return factoryUuid;
    }

    public void setFactoryUuid(String factoryUuid) {
        this.factoryUuid = factoryUuid == null ? null : factoryUuid.trim();
    }

    public String getTagUrl() {
        return tagUrl;
    }

    public void setTagUrl(String tagUrl) {
        this.tagUrl = tagUrl == null ? null : tagUrl.trim();
    }

    public String getTagClientPath() {
        return tagClientPath;
    }

    public void setTagClientPath(String tagClientPath) {
        this.tagClientPath = tagClientPath == null ? null : tagClientPath.trim();
    }

    public String getTagNo() {
        return tagNo;
    }

    public void setTagNo(String tagNo) {
        this.tagNo = tagNo == null ? null : tagNo.trim();
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