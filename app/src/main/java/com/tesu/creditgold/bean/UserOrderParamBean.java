package com.tesu.creditgold.bean;

/**
 * Created by Administrator on 2016/12/29 0029.
 */
public class UserOrderParamBean {
    private String order_type;
    private String house_type;
    private String fixed_assets;
    private String store_uid;
    private String store_name;
    private String borrow_uid;
    private String store_contract;
    private String store_tel;
    private String borrow_name;
    private String borrow_money;
    private String borrow_interest;
    private String each_amount;
    private String borrow_interest_rate;
    private String store_charge_rate;
    private String user_charge_rate;
    private String borrow_duration;
    private String fee;
    private String borrow_info;
    private String interest_type;
    private String borrow_use;
    private String usr_order_pic_list;

    @Override
    public String toString() {
        return "UserOrderParamBean{" +
                "order_type='" + order_type + '\'' +
                ", house_type='" + house_type + '\'' +
                ", fixed_assets='" + fixed_assets + '\'' +
                ", store_uid='" + store_uid + '\'' +
                ", store_name='" + store_name + '\'' +
                ", borrow_uid='" + borrow_uid + '\'' +
                ", store_contract='" + store_contract + '\'' +
                ", store_tel='" + store_tel + '\'' +
                ", borrow_name='" + borrow_name + '\'' +
                ", borrow_money='" + borrow_money + '\'' +
                ", borrow_interest='" + borrow_interest + '\'' +
                ", each_amount='" + each_amount + '\'' +
                ", borrow_interest_rate='" + borrow_interest_rate + '\'' +
                ", store_charge_rate='" + store_charge_rate + '\'' +
                ", user_charge_rate='" + user_charge_rate + '\'' +
                ", borrow_duration='" + borrow_duration + '\'' +
                ", fee='" + fee + '\'' +
                ", borrow_info='" + borrow_info + '\'' +
                ", interest_type='" + interest_type + '\'' +
                ", borrow_use='" + borrow_use + '\'' +
                ", usr_order_pic_list='" + usr_order_pic_list + '\'' +
                '}';
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }

    public String getFixed_assets() {
        return fixed_assets;
    }

    public void setFixed_assets(String fixed_assets) {
        this.fixed_assets = fixed_assets;
    }

    public String getStore_uid() {
        return store_uid;
    }

    public void setStore_uid(String store_uid) {
        this.store_uid = store_uid;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getBorrow_uid() {
        return borrow_uid;
    }

    public void setBorrow_uid(String borrow_uid) {
        this.borrow_uid = borrow_uid;
    }

    public String getStore_contract() {
        return store_contract;
    }

    public void setStore_contract(String store_contract) {
        this.store_contract = store_contract;
    }

    public String getStore_tel() {
        return store_tel;
    }

    public void setStore_tel(String store_tel) {
        this.store_tel = store_tel;
    }

    public String getBorrow_name() {
        return borrow_name;
    }

    public void setBorrow_name(String borrow_name) {
        this.borrow_name = borrow_name;
    }

    public String getBorrow_money() {
        return borrow_money;
    }

    public void setBorrow_money(String borrow_money) {
        this.borrow_money = borrow_money;
    }

    public String getBorrow_interest() {
        return borrow_interest;
    }

    public void setBorrow_interest(String borrow_interest) {
        this.borrow_interest = borrow_interest;
    }

    public String getEach_amount() {
        return each_amount;
    }

    public void setEach_amount(String each_amount) {
        this.each_amount = each_amount;
    }

    public String getBorrow_interest_rate() {
        return borrow_interest_rate;
    }

    public void setBorrow_interest_rate(String borrow_interest_rate) {
        this.borrow_interest_rate = borrow_interest_rate;
    }

    public String getStore_charge_rate() {
        return store_charge_rate;
    }

    public void setStore_charge_rate(String store_charge_rate) {
        this.store_charge_rate = store_charge_rate;
    }

    public String getUser_charge_rate() {
        return user_charge_rate;
    }

    public void setUser_charge_rate(String user_charge_rate) {
        this.user_charge_rate = user_charge_rate;
    }

    public String getBorrow_duration() {
        return borrow_duration;
    }

    public void setBorrow_duration(String borrow_duration) {
        this.borrow_duration = borrow_duration;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getBorrow_info() {
        return borrow_info;
    }

    public void setBorrow_info(String borrow_info) {
        this.borrow_info = borrow_info;
    }

    public String getInterest_type() {
        return interest_type;
    }

    public void setInterest_type(String interest_type) {
        this.interest_type = interest_type;
    }

    public String getBorrow_use() {
        return borrow_use;
    }

    public void setBorrow_use(String borrow_use) {
        this.borrow_use = borrow_use;
    }

    public String getUsr_order_pic_list() {
        return usr_order_pic_list;
    }

    public void setUsr_order_pic_list(String usr_order_pic_list) {
        this.usr_order_pic_list = usr_order_pic_list;
    }
}
