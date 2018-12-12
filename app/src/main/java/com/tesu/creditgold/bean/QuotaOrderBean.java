package com.tesu.creditgold.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/10 0010.
 */
public class QuotaOrderBean {
    private String order_sn;
    private String store_contract;  //卖家姓名
    private String store_contract_id_card;  //卖家身份证号
    private String store_tel;  //卖家手机号
    private String com_name;  //单位名称
    private String wood_type_name;  //木材品名
    private String quantity;  //重量方数
    private double unit_price;  //单价
    private double order_money ;  //订单总价
    private String warehouse_address ;  //堆场/仓库地址
    private String warehouse_contract ;  //堆场联系人
    private String warehouse_contract_tel ;  //堆场联系人电话
    private List<UsrOrderPicBean> usr_order_pic_list;  //用户订单照片列表
    private int order_status;  //订单状态
    private ShopBean store_inf; //店铺信息
    private String usr_order_id;
    private String store_com_name;  //商家单位名称
    private int submit_step;
    private String borrow_uid;

    @Override
    public String toString() {
        return "QuotaOrderBean{" +
                "order_sn='" + order_sn + '\'' +
                ", store_contract='" + store_contract + '\'' +
                ", store_contract_id_card='" + store_contract_id_card + '\'' +
                ", store_tel='" + store_tel + '\'' +
                ", com_name='" + com_name + '\'' +
                ", wood_type_name='" + wood_type_name + '\'' +
                ", quantity='" + quantity + '\'' +
                ", unit_price=" + unit_price +
                ", order_money=" + order_money +
                ", warehouse_address='" + warehouse_address + '\'' +
                ", warehouse_contract='" + warehouse_contract + '\'' +
                ", warehouse_contract_tel='" + warehouse_contract_tel + '\'' +
                ", usr_order_pic_list=" + usr_order_pic_list +
                ", order_status=" + order_status +
                ", store_inf=" + store_inf +
                ", usr_order_id='" + usr_order_id + '\'' +
                ", store_com_name='" + store_com_name + '\'' +
                ", submit_step=" + submit_step +
                ", borrow_uid='" + borrow_uid + '\'' +
                '}';
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getStore_contract() {
        return store_contract;
    }

    public void setStore_contract(String store_contract) {
        this.store_contract = store_contract;
    }

    public String getStore_contract_id_card() {
        return store_contract_id_card;
    }

    public void setStore_contract_id_card(String store_contract_id_card) {
        this.store_contract_id_card = store_contract_id_card;
    }

    public String getStore_tel() {
        return store_tel;
    }

    public void setStore_tel(String store_tel) {
        this.store_tel = store_tel;
    }

    public String getCom_name() {
        return com_name;
    }

    public void setCom_name(String com_name) {
        this.com_name = com_name;
    }

    public String getWood_type_name() {
        return wood_type_name;
    }

    public void setWood_type_name(String wood_type_name) {
        this.wood_type_name = wood_type_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public double getOrder_money() {
        return order_money;
    }

    public void setOrder_money(double order_money) {
        this.order_money = order_money;
    }

    public String getWarehouse_address() {
        return warehouse_address;
    }

    public void setWarehouse_address(String warehouse_address) {
        this.warehouse_address = warehouse_address;
    }

    public String getWarehouse_contract() {
        return warehouse_contract;
    }

    public void setWarehouse_contract(String warehouse_contract) {
        this.warehouse_contract = warehouse_contract;
    }

    public String getWarehouse_contract_tel() {
        return warehouse_contract_tel;
    }

    public void setWarehouse_contract_tel(String warehouse_contract_tel) {
        this.warehouse_contract_tel = warehouse_contract_tel;
    }

    public List<UsrOrderPicBean> getUsr_order_pic_list() {
        return usr_order_pic_list;
    }

    public void setUsr_order_pic_list(List<UsrOrderPicBean> usr_order_pic_list) {
        this.usr_order_pic_list = usr_order_pic_list;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public ShopBean getStore_inf() {
        return store_inf;
    }

    public void setStore_inf(ShopBean store_inf) {
        this.store_inf = store_inf;
    }

    public String getUsr_order_id() {
        return usr_order_id;
    }

    public void setUsr_order_id(String usr_order_id) {
        this.usr_order_id = usr_order_id;
    }

    public String getStore_com_name() {
        return store_com_name;
    }

    public void setStore_com_name(String store_com_name) {
        this.store_com_name = store_com_name;
    }

    public int getSubmit_step() {
        return submit_step;
    }

    public void setSubmit_step(int submit_step) {
        this.submit_step = submit_step;
    }

    public String getBorrow_uid() {
        return borrow_uid;
    }

    public void setBorrow_uid(String borrow_uid) {
        this.borrow_uid = borrow_uid;
    }
}
