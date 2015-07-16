package com.prd.yzy.bean;

/**
 * Created by 李富 on 2015/7/9.
 */
public class Car {

    //车辆id
    private String vid;
    //车牌号码
    private String name;
    //车辆状态
    private String desc;
    //所属公司
    private String companyName;
    //当前时间
    private String driverName;
    //车辆速度
    private String speed;
    //纬度
    private String lat;
    //经度
    private String lon;
    //本月运行里程
    private String miles;
    //本月运行时间
    private String times;
    //上月运行里程
    private String beforeMiles;
    //上月运行时间
    private String beforeTimes;
    //地址
    private String Address;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getBeforeTimes() {
        return beforeTimes;
    }

    public void setBeforeTimes(String beforeTimes) {
        this.beforeTimes = beforeTimes;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getMiles() {
        return miles;
    }

    public void setMiles(String miles) {
        this.miles = miles;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getBeforeMiles() {
        return beforeMiles;
    }

    public void setBeforeMiles(String beforeMiles) {
        this.beforeMiles = beforeMiles;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    @Override
    public String toString() {
        return "Car{" +
                "vid='" + vid + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", companyName='" + companyName + '\'' +
                ", driverName='" + driverName + '\'' +
                ", speed='" + speed + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", miles='" + miles + '\'' +
                ", times='" + times + '\'' +
                ", beforeMiles='" + beforeMiles + '\'' +
                ", beforeTimes='" + beforeTimes + '\'' +
                ", Address='" + Address + '\'' +
                '}';
    }
}
