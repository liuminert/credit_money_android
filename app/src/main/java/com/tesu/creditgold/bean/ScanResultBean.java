package com.tesu.creditgold.bean;

/**
 * Created by Administrator on 2016/12/7 0007.
 */
public class ScanResultBean {
    private int store_id;
    private String type;
    private int usr_id;
    private String store_name;
    private String store_contract;
    private String store_tel;
    private int is_tiexi;
    private int store_status;
    private int store_type_id;
    private int gather_model_id;
    private String store_grade;

    @Override
    public String toString() {
        return "ScanResultBean{" +
                "store_id=" + store_id +
                ", type='" + type + '\'' +
                ", usr_id=" + usr_id +
                ", store_name='" + store_name + '\'' +
                ", store_contract='" + store_contract + '\'' +
                ", store_tel='" + store_tel + '\'' +
                ", is_tiexi=" + is_tiexi +
                ", store_status=" + store_status +
                ", store_type_id=" + store_type_id +
                ", gather_model_id=" + gather_model_id +
                ", store_grade='" + store_grade + '\'' +
                '}';
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(int usr_id) {
        this.usr_id = usr_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
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

    public int getIs_tiexi() {
        return is_tiexi;
    }

    public void setIs_tiexi(int is_tiexi) {
        this.is_tiexi = is_tiexi;
    }

    public int getStore_status() {
        return store_status;
    }

    public void setStore_status(int store_status) {
        this.store_status = store_status;
    }

    public int getStore_type_id() {
        return store_type_id;
    }

    public void setStore_type_id(int store_type_id) {
        this.store_type_id = store_type_id;
    }

    public int getGather_model_id() {
        return gather_model_id;
    }

    public void setGather_model_id(int gather_model_id) {
        this.gather_model_id = gather_model_id;
    }

    public String getStore_grade() {
        return store_grade;
    }

    public void setStore_grade(String store_grade) {
        this.store_grade = store_grade;
    }
}
