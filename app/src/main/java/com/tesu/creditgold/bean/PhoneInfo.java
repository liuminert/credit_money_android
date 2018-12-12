package com.tesu.creditgold.bean;

/**
 * Created by Administrator on 2017/1/13 0013.
 */
public class PhoneInfo {
    private String name;
    private String number;
    public PhoneInfo(String name, String number) {
        this.name = name;
        this.number = number;
    }
    public String getName() {
        return name;
    }
    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "PhoneInfo{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
