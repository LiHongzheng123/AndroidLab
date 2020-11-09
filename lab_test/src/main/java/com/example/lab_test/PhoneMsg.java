package com.example.lab_test;

import cn.bmob.v3.BmobObject;

/**
 * Created by lihongzheng on 2020/10/28.
 * 存储手机存入数据库的基本信息
 */

public class PhoneMsg extends BmobObject {
    private String name;//应用名称
    private String start_time;//开始时间
    private String end_time;//结束时间
    private long all_use_time;//使用总时间

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public long getAll_use_time() {
        return all_use_time;
    }

    public void setAll_use_time(long all_use_time) {
        this.all_use_time = all_use_time;
    }

    public PhoneMsg(String name, String  start_time, String end_time, long all_use_time) {
        this.name = name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.all_use_time = all_use_time;
    }

    @Override
    public String toString() {
        return "PhoneMsg{" +
                "name='" + name + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", all_use_time=" + all_use_time +
                '}';
    }
}