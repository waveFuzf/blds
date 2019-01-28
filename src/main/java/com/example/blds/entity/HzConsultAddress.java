package com.example.blds.entity;

import lombok.Data;

import java.util.Date;
@Data
public class HzConsultAddress {
    private Integer id;

    private Integer type;

    private Integer consultId;

    private String mailProvince;

    private String mailCity;

    private String mailAddress;

    private String mailName;

    private String mailPhone;

    private String mailRemark;

    private String mailCompany;

    private String mailNumber;

    private String returnProvince;

    private String returnCity;

    private String returnAddress;

    private String returnName;

    private String returnPhone;

    private String returnRemark;

    private String returnCompany;

    private String returnNumber;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getConsultId() {
        return consultId;
    }

    public void setConsultId(Integer consultId) {
        this.consultId = consultId;
    }

    public String getMailProvince() {
        return mailProvince;
    }

    public void setMailProvince(String mailProvince) {
        this.mailProvince = mailProvince == null ? null : mailProvince.trim();
    }

    public String getMailCity() {
        return mailCity;
    }

    public void setMailCity(String mailCity) {
        this.mailCity = mailCity == null ? null : mailCity.trim();
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress == null ? null : mailAddress.trim();
    }

    public String getMailName() {
        return mailName;
    }

    public void setMailName(String mailName) {
        this.mailName = mailName == null ? null : mailName.trim();
    }

    public String getMailPhone() {
        return mailPhone;
    }

    public void setMailPhone(String mailPhone) {
        this.mailPhone = mailPhone == null ? null : mailPhone.trim();
    }

    public String getMailRemark() {
        return mailRemark;
    }

    public void setMailRemark(String mailRemark) {
        this.mailRemark = mailRemark == null ? null : mailRemark.trim();
    }

    public String getMailCompany() {
        return mailCompany;
    }

    public void setMailCompany(String mailCompany) {
        this.mailCompany = mailCompany == null ? null : mailCompany.trim();
    }

    public String getMailNumber() {
        return mailNumber;
    }

    public void setMailNumber(String mailNumber) {
        this.mailNumber = mailNumber == null ? null : mailNumber.trim();
    }

    public String getReturnProvince() {
        return returnProvince;
    }

    public void setReturnProvince(String returnProvince) {
        this.returnProvince = returnProvince == null ? null : returnProvince.trim();
    }

    public String getReturnCity() {
        return returnCity;
    }

    public void setReturnCity(String returnCity) {
        this.returnCity = returnCity == null ? null : returnCity.trim();
    }

    public String getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress == null ? null : returnAddress.trim();
    }

    public String getReturnName() {
        return returnName;
    }

    public void setReturnName(String returnName) {
        this.returnName = returnName == null ? null : returnName.trim();
    }

    public String getReturnPhone() {
        return returnPhone;
    }

    public void setReturnPhone(String returnPhone) {
        this.returnPhone = returnPhone == null ? null : returnPhone.trim();
    }

    public String getReturnRemark() {
        return returnRemark;
    }

    public void setReturnRemark(String returnRemark) {
        this.returnRemark = returnRemark == null ? null : returnRemark.trim();
    }

    public String getReturnCompany() {
        return returnCompany;
    }

    public void setReturnCompany(String returnCompany) {
        this.returnCompany = returnCompany == null ? null : returnCompany.trim();
    }

    public String getReturnNumber() {
        return returnNumber;
    }

    public void setReturnNumber(String returnNumber) {
        this.returnNumber = returnNumber == null ? null : returnNumber.trim();
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