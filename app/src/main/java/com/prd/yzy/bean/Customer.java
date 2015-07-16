package com.prd.yzy.bean;

/**
 *这是员工信息的bean类
 *
 * Created by 李富 on 2015/7/16.
 */
public class Customer {

    private String cid;
    private String cname;
    private String cphone;
    private String sex;
    private String driverOperId;
    public String getCid() {
        return cid;
    }
    public void setCid(String cid) {
        this.cid = cid;
    }
    public String getCname() {
        return cname;
    }
    public void setCname(String cname) {
        this.cname = cname;
    }
    public String getCphone() {
        return cphone;
    }
    public void setCphone(String cphone) {
        this.cphone = cphone;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getDriverOperId() {
        return driverOperId;
    }
    public void setDriverOperId(String driverOperId) {
        this.driverOperId = driverOperId;
    }
    @Override
    public String toString() {
        return "Customer [cid=" + cid + ", cname=" + cname + ", cphone="
                + cphone + ", sex=" + sex + ", driverOperId=" + driverOperId
                + "]";
    }

}
