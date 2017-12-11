package com.jhsports.user.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jhsports.user.entity.enums.CodeEnum;

/**
 * Created by TQ on 2017/11/15.
 */
public class RESTful {
    private int code;
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String desc;

    // 项目特殊字段
//    @JsonProperty("unionuserid")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String unionUserId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;
//    @JsonProperty("authorizedtypeid")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer authorizedTypeId;
//    @JsonProperty("mobilenum")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mobileNum;
//    @JsonProperty("smscode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String smsCode;

    public static RESTful Success(CodeEnum codeEnum) {
        return Success(codeEnum.getCode(), codeEnum.getMsg());
    }

    public static RESTful Success(int code, String msg) {
        return new RESTful(code, msg);
    }

    public static RESTful Fail(CodeEnum codeEnum) {
        return Success(codeEnum.getCode(), codeEnum.getMsg());
    }

    public static RESTful Fail(int code, String msg) {
        return new RESTful(code, msg);
    }

    public RESTful(int code, String msg, Object data, String desc)
    {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.desc = desc;
    }

    public RESTful(CodeEnum codeEnum) {
        this(codeEnum.getCode(), codeEnum.getMsg());
    }

    // 构造函数重载
    public RESTful(int code, String msg) {
        this(code, msg, null, null);
    }

    public RESTful(int code, String msg, Object data) {
        this(code, msg, data, "");
    }

    public RESTful(int code, String msg, String desc) {
        this(code, msg, null, desc);
    }

    public int getCode() { return code; }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUnionUserId() {
        return unionUserId;
    }

    public void setUnionUserId(String unionUserId) {
        this.unionUserId = unionUserId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getAuthorizedTypeId() {
        return authorizedTypeId;
    }

    public void setAuthorizedTypeId(Integer authorizedTypeId) {
        this.authorizedTypeId = authorizedTypeId;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
