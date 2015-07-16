package com.prd.testlistview.bean;

import java.util.List;

/**
 * Created by 李富 on 2015/7/15.
 */
public class TestBean {

    private String name;
    private List<String> list;
    private int index;
    private int offSet;

    public TestBean(String name, List<String> list, int index, int offSet) {
        this.name = name;
        this.list = list;
        this.index = index;
        this.offSet = offSet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getOffSet() {
        return offSet;
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;


    }

    @Override
    public String toString() {
        return "TestBean{" +
                "name='" + name + '\'' +
                ", list=" + list +
                ", index=" + index +
                ", offSet=" + offSet +
                '}';
    }

}
