package com.jhsports.user.entity;

import java.util.Date;

/**
 * Created by TQ on 2017/12/6.
 */
public class UpdateLog {
    private int id;
    private String oldUnionUserId;
    private String unionUserId;
    private int state;
    private Date createTime;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOldUnionUserId() {
        return oldUnionUserId;
    }

    public void setOldUnionUserId(String oldUnionUserId) {
        this.oldUnionUserId = oldUnionUserId;
    }

    public String getUnionUserId() {
        return unionUserId;
    }

    public void setUnionUserId(String unionUserId) {
        this.unionUserId = unionUserId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
