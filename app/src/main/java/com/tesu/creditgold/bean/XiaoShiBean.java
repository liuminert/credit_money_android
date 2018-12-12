package com.tesu.creditgold.bean;

/**
 * Created by Administrator on 2016/12/7 0007.
 */
public class XiaoShiBean {
    private int flag;
    private int usrinf_id;
    private String xiao_shi_password;
    private String xiao_shi_account;

    @Override
    public String toString() {
        return "XiaoShiBean{" +
                "flag=" + flag +
                ", usrinf_id='" + usrinf_id + '\'' +
                ", xiao_shi_password='" + xiao_shi_password + '\'' +
                ", xiao_shi_account='" + xiao_shi_account + '\'' +
                '}';
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getXiao_shi_password() {
        return xiao_shi_password;
    }

    public void setXiao_shi_password(String xiao_shi_password) {
        this.xiao_shi_password = xiao_shi_password;
    }

    public String getXiao_shi_account() {
        return xiao_shi_account;
    }

    public void setXiao_shi_account(String xiao_shi_account) {
        this.xiao_shi_account = xiao_shi_account;
    }

    public int getUsrinf_id() {
        return usrinf_id;
    }

    public void setUsrinf_id(int usrinf_id) {
        this.usrinf_id = usrinf_id;
    }
}
