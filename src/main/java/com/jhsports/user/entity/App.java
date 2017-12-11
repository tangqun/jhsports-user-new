package com.jhsports.user.entity;

/**
 * Created by TQ on 2017/11/22.
 */
public class App {
    private int id;
    private String name;
    private String ipSet;
    private boolean isWork;
    private String logo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpSet() {
        return ipSet;
    }

    public void setIpSet(String ipSet) {
        this.ipSet = ipSet;
    }

    public boolean isWork() {
        return isWork;
    }

    public void setWork(boolean work) {
        isWork = work;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
