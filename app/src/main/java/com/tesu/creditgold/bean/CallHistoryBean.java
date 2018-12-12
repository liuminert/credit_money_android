package com.tesu.creditgold.bean;

/**
 * Created by Administrator on 2017/3/13 0013.
 */
public class CallHistoryBean {
    private String name;
    private String dial_mode;
    private String mobile_phone;
    private int call_time;
    private String call_when;

    @Override
    public String toString() {
        return "CallHistoryBean{" +
                "name='" + name + '\'' +
                ", dial_mode='" + dial_mode + '\'' +
                ", mobile_phone='" + mobile_phone + '\'' +
                ", call_time=" + call_time +
                ", call_when='" + call_when + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDial_mode() {
        return dial_mode;
    }

    public void setDial_mode(String dial_mode) {
        this.dial_mode = dial_mode;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public int getCall_time() {
        return call_time;
    }

    public void setCall_time(int call_time) {
        this.call_time = call_time;
    }

    public String getCall_when() {
        return call_when;
    }

    public void setCall_when(String call_when) {
        this.call_when = call_when;
    }
}
