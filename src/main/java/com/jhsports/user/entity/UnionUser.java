package com.jhsports.user.entity;

import java.util.Date;

/**
 * Created by TQ on 2017/11/22.
 */
public class UnionUser {
    private String id;
    private int appId;
    private String mobileNum;
    private String password;
    private String salt;
    private boolean isOld;
    private int authorizedTypeId;
    private String openId;
    private String authorizedUserId;
    private String unionId;
    private int bindState;
    private int state;
    private Date createTime;
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean getIsOld() { return isOld; }

    public void setIsOld(boolean isOld) { this.isOld = isOld; }

    public int getAuthorizedTypeId() {
        return authorizedTypeId;
    }

    public void setAuthorizedTypeId(int authorizedTypeId) {
        this.authorizedTypeId = authorizedTypeId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAuthorizedUserId() {
        return authorizedUserId;
    }

    public void setAuthorizedUserId(String authorizedUserId) {
        this.authorizedUserId = authorizedUserId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public int getBindState() {
        return bindState;
    }

    public void setBindState(int bindState) {
        this.bindState = bindState;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
}
