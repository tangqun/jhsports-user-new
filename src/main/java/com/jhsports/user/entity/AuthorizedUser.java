package com.jhsports.user.entity;

import java.util.Date;

/**
 * Created by TQ on 2017/12/5.
 */
public class AuthorizedUser {
    private String id;
    private String openId;
    private String unionId;
    private int appId;
    private int authorizedTypeId;
    private Date createTime;
    private String createIP;
    private int systemTypeId;
    private String equipmentNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

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
}
