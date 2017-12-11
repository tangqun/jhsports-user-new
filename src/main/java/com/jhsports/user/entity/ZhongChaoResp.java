package com.jhsports.user.entity;

/**
 * Created by TQ on 2017/12/6.
 */
public class ZhongChaoResp {
    private int oper_code;
    private String message;
    private int data;
    private String desc;

    public int getOper_code() {
        return oper_code;
    }

    public void setOper_code(int oper_code) {
        this.oper_code = oper_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
