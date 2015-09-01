package com.prd.yzy.bean;

/**
 * 作者：李富 on 2015/9/1.
 * 邮箱：lifuzz@163.com
 */
public class Work {

    private String name;
    private String bjName;
    private String count;
    private String pass;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBjName() {
        return bjName;
    }

    public void setBjName(String bjName) {
        this.bjName = bjName;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "Work{" +
                "name='" + name + '\'' +
                ", bjName='" + bjName + '\'' +
                ", count='" + count + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
