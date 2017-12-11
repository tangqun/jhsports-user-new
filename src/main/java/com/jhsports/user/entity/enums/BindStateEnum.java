package com.jhsports.user.entity.enums;

/**
 * Created by TQ on 2017/12/4.
 */
public enum BindStateEnum {
    NotBind(1),
    HasBeenBound(2);

    BindStateEnum(int bindState) {
        this.bindState = bindState;
    }

    private int bindState;

    public int getBindState() {
        return bindState;
    }

    public void setBindState(int bindState) {
        this.bindState = bindState;
    }
}
