package com.prd.yzy.bean;

/**
 * Created by 李富 on 2015/9/1.
 */
public class Chart {

    private String date;
    private String count;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Chart{" +
                "date='" + date + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
