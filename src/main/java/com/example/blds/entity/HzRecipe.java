package com.example.blds.entity;

import lombok.Data;

import java.util.Date;
@Data
public class HzRecipe {
    private Integer id;

    private Integer consuiltId;

    private String name;

    private String amount;

    private String amountUnit;

    private String dosage;

    private String dosageUnit;

    private String times;

    private String timesState;

    private String day;

    private String method;

    private String price;

    private Integer isDelete;

    private Date updateTime;

    private Date createTime;

    private String prescriptiontype;

    private String decoction;

    private String decoctionMethod;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getConsuiltId() {
        return consuiltId;
    }

    public void setConsuiltId(Integer consuiltId) {
        this.consuiltId = consuiltId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount == null ? null : amount.trim();
    }

    public String getAmountUnit() {
        return amountUnit;
    }

    public void setAmountUnit(String amountUnit) {
        this.amountUnit = amountUnit == null ? null : amountUnit.trim();
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage == null ? null : dosage.trim();
    }

    public String getDosageUnit() {
        return dosageUnit;
    }

    public void setDosageUnit(String dosageUnit) {
        this.dosageUnit = dosageUnit == null ? null : dosageUnit.trim();
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times == null ? null : times.trim();
    }

    public String getTimesState() {
        return timesState;
    }

    public void setTimesState(String timesState) {
        this.timesState = timesState == null ? null : timesState.trim();
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day == null ? null : day.trim();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPrescriptiontype() {
        return prescriptiontype;
    }

    public void setPrescriptiontype(String prescriptiontype) {
        this.prescriptiontype = prescriptiontype == null ? null : prescriptiontype.trim();
    }

    public String getDecoction() {
        return decoction;
    }

    public void setDecoction(String decoction) {
        this.decoction = decoction == null ? null : decoction.trim();
    }

    public String getDecoctionMethod() {
        return decoctionMethod;
    }

    public void setDecoctionMethod(String decoctionMethod) {
        this.decoctionMethod = decoctionMethod == null ? null : decoctionMethod.trim();
    }
}