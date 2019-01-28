package com.example.blds.entity;

import lombok.Data;

import java.util.Date;
@Data
public class HzHospital {
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

    private Date updataTime;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUnionId() {
        return unionId;
    }

    public void setUnionId(Integer unionId) {
        this.unionId = unionId;
    }

    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public String getFootLogo() {
        return footLogo;
    }

    public void setFootLogo(String footLogo) {
        this.footLogo = footLogo == null ? null : footLogo.trim();
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode == null ? null : appCode.trim();
    }

    public String getGzhCode() {
        return gzhCode;
    }

    public void setGzhCode(String gzhCode) {
        this.gzhCode = gzhCode == null ? null : gzhCode.trim();
    }

    public String getFwhCode() {
        return fwhCode;
    }

    public void setFwhCode(String fwhCode) {
        this.fwhCode = fwhCode == null ? null : fwhCode.trim();
    }

    public String getFwcCode() {
        return fwcCode;
    }

    public void setFwcCode(String fwcCode) {
        this.fwcCode = fwcCode == null ? null : fwcCode.trim();
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getUpdataTime() {
        return updataTime;
    }

    public void setUpdataTime(Date updataTime) {
        this.updataTime = updataTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}