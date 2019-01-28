package com.example.blds;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
public class LoginUser implements Serializable {

        private Long doctorId;
        private Long unitId;
        private String unitName;
        private String unitOrgCode;
        private Long sectionId;
        private String sectionName;
        private int commonSectionNo;
        private String commonSectionName;
        private int unionId;
        private int joinType;
        private int multiDoctors;
        private String addressCode;
        private Long userId;
        private String loginname;
        private String userName;
        private int userState;
        private String token;
        private Date loginTime;
        private String phone;
        private String photoPath;
        private String ipAddress;
        private String sex;
        private String loginPlatId;
        private String imUniqueId;

        public Long getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(Long doctorId) {
            this.doctorId = doctorId;
        }

        public String getCommonSectionName() { return commonSectionName; }

        public void setCommonSectionName(String commonSectionName) { this.commonSectionName = commonSectionName; }

        public Long getUnitId() {
            return unitId;
        }

        public void setUnitId(Long unitId) {
            this.unitId = unitId;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getUnitOrgCode() {
            return unitOrgCode;
        }

        public void setUnitOrgCode(String unitOrgCode) {
            this.unitOrgCode = unitOrgCode;
        }

        public Long getSectionId() {
            return sectionId;
        }

        public void setSectionId(Long sectionId) {
            this.sectionId = sectionId;
        }

        public String getSectionName() {
            return sectionName;
        }

        public void setSectionName(String sectionName) {
            this.sectionName = sectionName;
        }

        public int getCommonSectionNo() {
            return commonSectionNo;
        }

        public void setCommonSectionNo(int commonSectionNo) {
            this.commonSectionNo = commonSectionNo;
        }

        public int getUnionId() {
            return unionId;
        }

        public void setUnionId(int unionId) {
            this.unionId = unionId;
        }

        public int getJoinType() {
            return joinType;
        }

        public void setJoinType(int joinType) {
            this.joinType = joinType;
        }

        public int getMultiDoctors() {
            return multiDoctors;
        }

        public void setMultiDoctors(int multiDoctors) {
            this.multiDoctors = multiDoctors;
        }

        public String getAddressCode() {
            return addressCode;
        }

        public void setAddressCode(String addressCode) {
            this.addressCode = addressCode;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getLoginname() {
            return loginname;
        }

        public void setLoginname(String loginname) {
            this.loginname = loginname;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getUserState() {
            return userState;
        }

        public void setUserState(int userState) {
            this.userState = userState;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Date getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(Date loginTime) {
            this.loginTime = loginTime;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhotoPath() {
            return photoPath;
        }

        public void setPhotoPath(String photoPath) {
            this.photoPath = photoPath;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getLoginPlatId() {
            return loginPlatId;
        }

        public void setLoginPlatId(String loginPlatId) {
            this.loginPlatId = loginPlatId;
        }

        public String getImUniqueId() {
            return imUniqueId;
        }

        public void setImUniqueId(String imUniqueId) {
            this.imUniqueId = imUniqueId;
        }
}
