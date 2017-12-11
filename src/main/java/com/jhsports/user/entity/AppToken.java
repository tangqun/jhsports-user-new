package com.jhsports.user.entity;

import java.util.Date;

/**
 * Created by TQ on 2017/11/28.
 */
public class AppToken {
    private int appId;
    private int authorizedTypeId;
    private String unionUserId;
    private String tokenValue;
    private Date createTime;
    private String createIP;
    private int systemTypeId;
    private String equipmentNum;
    private Date updateTime;
    private boolean isWork;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getAuthorizedTypeId() {
        return authorizedTypeId;
    }

    public void setAuthorizedTypeId(int authorizedTypeId) {
        this.authorizedTypeId = authorizedTypeId;
    }

    public String getUnionUserId() {
        return unionUserId;
    }

    public void setUnionUserId(String unionUserId) {
        this.unionUserId = unionUserId;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateIP() {
        return createIP;
    }

    public void setCreateIP(String createIP) {
        this.createIP = createIP;
    }

    public int getSystemTypeId() {
        return systemTypeId;
    }

    public void setSystemTypeId(int systemTypeId) {
        this.systemTypeId = systemTypeId;
    }

    public String getEquipmentNum() {
        return equipmentNum;
    }

    public void setEquipmentNum(String equipmentNum) {
        this.equipmentNum = equipmentNum;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean getIsWork() {
        return isWork;
    }

    public void setIsWork(boolean work) {
        isWork = work;
    }
}
