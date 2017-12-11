package com.jhsports.user.entity.enums;

/**
 * Created by TQ on 2017/12/4.
 */
public enum AuthorizedTypeEnum {
    WeChat(1),
    QQ(2),
    WeiBo(3),
    Mobile(4),
    TokenLogin(5),
    TokenGetInfo(6),
    TokenValidate(7),
    AnonymousLogin(8);

    AuthorizedTypeEnum(int authorizedType) {
        this.authorizedType = authorizedType;
    }

    private int authorizedType;

    public int getAuthorizedType() {
        return authorizedType;
    }

    public void setAuthorizedType(int authorizedType) {
        this.authorizedType = authorizedType;
    }
}
