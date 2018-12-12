package com.tesu.creditgold.bean;

/**
 * Created by Administrator on 2016/12/6 0006.
 */
public class ShopBean {
    private int store_id;
    private String store_pic;
    private String store_name;
    private int province_id;
    private int city_id;
    private int area_id;
    private String address;
    private String contract_telephone;
    private String contract_name;
    private String licence_pic;   //店铺营业执照图片文件名
    private String description;   //店铺描述
    private int store_type;
    private String area_info;
    private int usr_id;
    private int is_tiexi;
    private int gather_model_id;
    private String store_grade;

    @Override
    public String toString() {
        return "ShopBean{" +
                "store_id=" + store_id +
                ", store_pic='" + store_pic + '\'' +
                ", store_name='" + store_name + '\'' +
                ", province_id=" + province_id +
                ", city_id=" + city_id +
                ", area_id=" + area_id +
                ", address='" + address + '\'' +
                ", contract_telephone='" + contract_telephone + '\'' +
                ", contract_name='" + contract_name + '\'' +
                ", licence_pic='" + licence_pic + '\'' +
                ", description='" + description + '\'' +
                ", store_type=" + store_type +
                ", area_info='" + area_info + '\'' +
                ", usr_id=" + usr_id +
                ", is_tiexi=" + is_tiexi +
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

    public String getStore_pic() {
        return store_pic;
    }

    public void setStore_pic(String store_pic) {
        this.store_pic = store_pic;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContract_telephone() {
        return contract_telephone;
    }

    public void setContract_telephone(String contract_telephone) {
        this.contract_telephone = contract_telephone;
    }

    public String getContract_name() {
        return contract_name;
    }

    public void setContract_name(String contract_name) {
        this.contract_name = contract_name;
    }

    public String getLicence_pic() {
        return licence_pic;
    }

    public void setLicence_pic(String licence_pic) {
        this.licence_pic = licence_pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStore_type() {
        return store_type;
    }

    public void setStore_type(int store_type) {
        this.store_type = store_type;
    }

    public String getArea_info() {
        return area_info;
    }

    public void setArea_info(String area_info) {
        this.area_info = area_info;
    }

    public int getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(int usr_id) {
        this.usr_id = usr_id;
    }

    public int getIs_tiexi() {
        return is_tiexi;
    }

    public void setIs_tiexi(int is_tiexi) {
        this.is_tiexi = is_tiexi;
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
