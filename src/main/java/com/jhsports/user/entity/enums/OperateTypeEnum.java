package com.jhsports.user.entity.enums;

/**
 * Created by TQ on 2017/12/5.
 */
public enum OperateTypeEnum {
    ResetPassword(1), // 找回密码
    ModifyPassword(2), // 修改密码
    ResetMobileNum(3), // 换绑手机号
    BindUser(4);

    OperateTypeEnum(int operateType) {
        this.operateType = operateType;
    }

    private int operateType;

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }
}
