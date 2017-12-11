package com.jhsports.user.entity.enums;

/**
 * Created by TQ on 2017/12/4.
 */
public enum StateEnum {
    Normal(1),
    Frozen(2);

    StateEnum(int state) {
        this.state = state;
    }

    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
