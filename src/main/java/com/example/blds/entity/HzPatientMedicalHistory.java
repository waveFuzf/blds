package com.example.blds.entity;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;
@Data
public class HzPatientMedicalHistory {
    @Id
    private Integer id;

    private Integer patientId;

    private String pastMedicalHistory;

    private String pastMedicalHistoryExtra;

    private String obstetricalHistory;

    private String allergicHistory;

    private String allergicHistoryExtra;

    private String familyMedicalHistory;

    private String familyMedicalHistoryExtra;

    private String personalHabits;

    private String personalHabitsExtra;

    private Integer isDelete;

    private Date createTime;

    private Date updataTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPastMedicalHistory() {
        return pastMedicalHistory;
    }

    public void setPastMedicalHistory(String pastMedicalHistory) {
        this.pastMedicalHistory = pastMedicalHistory == null ? null : pastMedicalHistory.trim();
    }

    public String getPastMedicalHistoryExtra() {
        return pastMedicalHistoryExtra;
    }

    public void setPastMedicalHistoryExtra(String pastMedicalHistoryExtra) {
        this.pastMedicalHistoryExtra = pastMedicalHistoryExtra == null ? null : pastMedicalHistoryExtra.trim();
    }

    public String getObstetricalHistory() {
        return obstetricalHistory;
    }

    public void setObstetricalHistory(String obstetricalHistory) {
        this.obstetricalHistory = obstetricalHistory == null ? null : obstetricalHistory.trim();
    }

    public String getAllergicHistory() {
        return allergicHistory;
    }

    public void setAllergicHistory(String allergicHistory) {
        this.allergicHistory = allergicHistory == null ? null : allergicHistory.trim();
    }

    public String getAllergicHistoryExtra() {
        return allergicHistoryExtra;
    }

    public void setAllergicHistoryExtra(String allergicHistoryExtra) {
        this.allergicHistoryExtra = allergicHistoryExtra == null ? null : allergicHistoryExtra.trim();
    }

    public String getFamilyMedicalHistory() {
        return familyMedicalHistory;
    }

    public void setFamilyMedicalHistory(String familyMedicalHistory) {
        this.familyMedicalHistory = familyMedicalHistory == null ? null : familyMedicalHistory.trim();
    }

    public String getFamilyMedicalHistoryExtra() {
        return familyMedicalHistoryExtra;
    }

    public void setFamilyMedicalHistoryExtra(String familyMedicalHistoryExtra) {
        this.familyMedicalHistoryExtra = familyMedicalHistoryExtra == null ? null : familyMedicalHistoryExtra.trim();
    }

    public String getPersonalHabits() {
        return personalHabits;
    }

    public void setPersonalHabits(String personalHabits) {
        this.personalHabits = personalHabits == null ? null : personalHabits.trim();
    }

    public String getPersonalHabitsExtra() {
        return personalHabitsExtra;
    }

    public void setPersonalHabitsExtra(String personalHabitsExtra) {
        this.personalHabitsExtra = personalHabitsExtra == null ? null : personalHabitsExtra.trim();
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdataTime() {
        return updataTime;
    }

    public void setUpdataTime(Date updataTime) {
        this.updataTime = updataTime;
    }
}