package com.tesu.creditgold.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/5 0005.
 */
public class InterestsBean implements Serializable{
    private int period;   //期数
    private String amount;  //总金额
    private String interest_total; //总利息
    private String factorage_total;  //总手续费
    private String principal_interest; //每期本金+利息（等本降息的情况只有本金）
    private String factorage;  //每期手续费
    private float borrow_interest_rate;  //年化率（12.8%写入12.8）
    private float store_charge_rate;  //商家服务费费率（12.8%写入12.8）
    private float user_charge_rate;  //链金所手续费费率（12.8%写入12.8）

    @Override
    public String toString() {
        return "InterestsBean{" +
                "period=" + period +
                ", amount='" + amount + '\'' +
                ", interest_total='" + interest_total + '\'' +
                ", factorage_total='" + factorage_total + '\'' +
                ", principal_interest='" + principal_interest + '\'' +
                ", factorage='" + factorage + '\'' +
                ", borrow_interest_rate=" + borrow_interest_rate +
                ", store_charge_rate=" + store_charge_rate +
                ", user_charge_rate=" + user_charge_rate +
                '}';
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInterest_total() {
        return interest_total;
    }

    public void setInterest_total(String interest_total) {
        this.interest_total = interest_total;
    }

    public String getFactorage_total() {
        return factorage_total;
    }

    public void setFactorage_total(String factorage_total) {
        this.factorage_total = factorage_total;
    }

    public String getPrincipal_interest() {
        return principal_interest;
    }

    public void setPrincipal_interest(String principal_interest) {
        this.principal_interest = principal_interest;
    }

    public String getFactorage() {
        return factorage;
    }

    public void setFactorage(String factorage) {
        this.factorage = factorage;
    }

    public float getBorrow_interest_rate() {
        return borrow_interest_rate;
    }

    public void setBorrow_interest_rate(float borrow_interest_rate) {
        this.borrow_interest_rate = borrow_interest_rate;
    }

    public float getStore_charge_rate() {
        return store_charge_rate;
    }

    public void setStore_charge_rate(float store_charge_rate) {
        this.store_charge_rate = store_charge_rate;
    }

    public float getUser_charge_rate() {
        return user_charge_rate;
    }

    public void setUser_charge_rate(float user_charge_rate) {
        this.user_charge_rate = user_charge_rate;
    }
}
