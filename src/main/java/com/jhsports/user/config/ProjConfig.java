package com.jhsports.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by TQ on 2017/12/11.
 */
@Component
@ConfigurationProperties(prefix = "proj")
public class ProjConfig {
    private String callbackGuoAn;
    private String callbackCSL;
    private String salt;
    private int smsTime;

    public String getCallbackGuoAn() {
        return callbackGuoAn;
    }

    public void setCallbackGuoAn(String callbackGuoAn) {
        this.callbackGuoAn = callbackGuoAn;
    }

    public String getCallbackCSL() {
        return callbackCSL;
    }

    public void setCallbackCSL(String callbackCSL) {
        this.callbackCSL = callbackCSL;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getSmsTime() {
        return smsTime;
    }

    public void setSmsTime(int smsTime) {
        this.smsTime = smsTime;
    }
}
