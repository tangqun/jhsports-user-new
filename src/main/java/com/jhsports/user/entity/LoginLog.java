package com.jhsports.user.entity;

import java.util.Date;

/**
 * Created by TQ on 2017/12/4.
 */
public class LoginLog {
    private int id;
    private int appId;
    private String unionUserId;
    private int loginTypeId;
    private Date loginTime;
    private String loginIP;
    private int systemTypeId;
    private String equipmentNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getUnionUserId() {
        return unionUserId;
    }

    public void setUnionUserId(String unionUserId) {
        this.unionUserId = unionUserId;
    }

    public int getLoginTypeId() {
        return loginTypeId;
    }

    public void setLoginTypeId(int loginTypeId) {
        this.loginTypeId = loginTypeId;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
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
}
