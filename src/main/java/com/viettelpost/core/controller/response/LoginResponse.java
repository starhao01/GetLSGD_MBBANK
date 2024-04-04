package com.viettelpost.core.controller.response;

import java.io.Serializable;

public class LoginResponse implements Serializable {
    String[] userId;
    String[] staffCode;
    String[] deptId;
    String[] fullName;
    String[] phoneNumber;
    String[] email;
    String jti;

    public String[] getUserId() {
        return userId;
    }

    public String get1UserId() {
        if (userId != null && userId.length > 0) {
            return userId[0];
        }
        return "-1";
    }

    public void setUserId(String[] userId) {
        this.userId = userId;
    }

    public String[] getStaffCode() {
        return staffCode;
    }

    public String get1StaffCode() {
        if (staffCode != null && staffCode.length > 0) {
            return staffCode[0];
        }
        return "";
    }

    public void setStaffCode(String[] staffCode) {
        this.staffCode = staffCode;
    }

    public String[] getDeptId() {
        return deptId;
    }

    public String get1DeptId() {
        if (deptId != null && deptId.length > 0) {
            return deptId[0];
        }
        return "-1";
    }

    public void setDeptId(String[] deptId) {
        this.deptId = deptId;
    }

    public String[] getFullName() {
        return fullName;
    }

    public String get1FullName() {
        if (fullName != null && fullName.length > 0) {
            return fullName[0];
        }
        return "";
    }

    public void setFullName(String[] fullName) {
        this.fullName = fullName;
    }

    public String[] getPhoneNumber() {
        return phoneNumber;
    }

    public String get1PhoneNumber() {
        if (phoneNumber != null && phoneNumber.length > 0) {
            return phoneNumber[0];
        }
        return "";
    }


    public void setPhoneNumber(String[] phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String[] getEmail() {
        return email;
    }

    public String get1Email() {
        if (email != null && email.length > 0) {
            return email[0];
        }
        return "";
    }

    public void setEmail(String[] email) {
        this.email = email;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }
}
