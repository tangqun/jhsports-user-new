package com.jhsports.user.entity;

import java.util.Date;

/**
 * Created by TQ on 2017/12/5.
 */
public class OperateLog {
    private int id;
    private int appId;
    private String unionUserId;
    private int operateTypeId;
    private Date operateTime;
    private String operateIP;
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

    public int getOperateTypeId() {
        return operateTypeId;
    }

    public void setOperateTypeId(int operateTypeId) {
        this.operateTypeId = operateTypeId;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperateIP() {
        return operateIP;
    }

    public void setOperateIP(String operateIP) {
        this.operateIP = operateIP;
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
