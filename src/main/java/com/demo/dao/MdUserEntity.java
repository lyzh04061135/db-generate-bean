package com.demo.dao;

import com.demo.annotation.FieldAnnotation;
import com.demo.annotation.TableAnnotation;

@TableAnnotation("md_user_generate")
public class MdUserEntity {
    @FieldAnnotation(unique = true, maxLength = 32)
    private java.lang.String userId;
    private java.lang.String loginName;
    private java.lang.String password;
    private java.lang.String realName;
    private java.lang.String sex;
    private java.lang.String phone;
    private java.lang.String email;
    private java.lang.String status;
    private java.sql.Timestamp createTime;
    private java.sql.Timestamp updateTime;
    private java.lang.String creator;
    private java.lang.String updater;
    private java.lang.String deleteFlag;

    public MdUserEntity() {
    }

    public MdUserEntity(java.lang.String userId, java.lang.String loginName, java.lang.String password, java.lang.String realName, java.lang.String sex, java.lang.String phone, java.lang.String email, java.lang.String status, java.sql.Timestamp createTime, java.sql.Timestamp updateTime, java.lang.String creator, java.lang.String updater, java.lang.String deleteFlag) {
        this.userId = userId;
        this.loginName = loginName;
        this.password = password;
        this.realName = realName;
        this.sex = sex;
        this.phone = phone;
        this.email = email;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.creator = creator;
        this.updater = updater;
        this.deleteFlag = deleteFlag;
    }

    public java.lang.String getUserId() {
        return userId;
    }

    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }

    public java.lang.String getLoginName() {
        return loginName;
    }

    public void setLoginName(java.lang.String loginName) {
        this.loginName = loginName;
    }

    public java.lang.String getPassword() {
        return password;
    }

    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    public java.lang.String getRealName() {
        return realName;
    }

    public void setRealName(java.lang.String realName) {
        this.realName = realName;
    }

    public java.lang.String getSex() {
        return sex;
    }

    public void setSex(java.lang.String sex) {
        this.sex = sex;
    }

    public java.lang.String getPhone() {
        return phone;
    }

    public void setPhone(java.lang.String phone) {
        this.phone = phone;
    }

    public java.lang.String getEmail() {
        return email;
    }

    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    public java.lang.String getStatus() {
        return status;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    public java.sql.Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.sql.Timestamp createTime) {
        this.createTime = createTime;
    }

    public java.sql.Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(java.sql.Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public java.lang.String getCreator() {
        return creator;
    }

    public void setCreator(java.lang.String creator) {
        this.creator = creator;
    }

    public java.lang.String getUpdater() {
        return updater;
    }

    public void setUpdater(java.lang.String updater) {
        this.updater = updater;
    }

    public java.lang.String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(java.lang.String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"userId\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(userId);
        stringBuilder.append("\",");
        stringBuilder.append("{");
        stringBuilder.append("\"loginName\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(loginName);
        stringBuilder.append("\",");
        stringBuilder.append("{");
        stringBuilder.append("\"password\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(password);
        stringBuilder.append("\",");
        stringBuilder.append("{");
        stringBuilder.append("\"realName\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(realName);
        stringBuilder.append("\",");
        stringBuilder.append("{");
        stringBuilder.append("\"sex\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(sex);
        stringBuilder.append("\",");
        stringBuilder.append("{");
        stringBuilder.append("\"phone\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(phone);
        stringBuilder.append("\",");
        stringBuilder.append("{");
        stringBuilder.append("\"email\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(email);
        stringBuilder.append("\",");
        stringBuilder.append("{");
        stringBuilder.append("\"status\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(status);
        stringBuilder.append("\",");
        stringBuilder.append("{");
        stringBuilder.append("\"createTime\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(createTime);
        stringBuilder.append("\",");
        stringBuilder.append("{");
        stringBuilder.append("\"updateTime\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(updateTime);
        stringBuilder.append("\",");
        stringBuilder.append("{");
        stringBuilder.append("\"creator\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(creator);
        stringBuilder.append("\",");
        stringBuilder.append("{");
        stringBuilder.append("\"updater\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(updater);
        stringBuilder.append("\",");
        stringBuilder.append("{");
        stringBuilder.append("\"deleteFlag\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(deleteFlag);
        stringBuilder.append("\"");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

}
