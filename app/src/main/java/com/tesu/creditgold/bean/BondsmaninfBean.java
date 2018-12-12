package com.tesu.creditgold.bean;

/**
 * Created by Administrator on 2016/12/5 0005.
 */
public class BondsmaninfBean {
    private int relation;
    private String rel_usrname;
    private String rel_mobile_phone;

    @Override
    public String toString() {
        return "BondsmaninfBean{" +
                "relation=" + relation +
                ", rel_usrname='" + rel_usrname + '\'' +
                ", rel_mobile_phone='" + rel_mobile_phone + '\'' +
                '}';
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public String getRel_usrname() {
        return rel_usrname;
    }

    public void setRel_usrname(String rel_usrname) {
        this.rel_usrname = rel_usrname;
    }

    public String getRel_mobile_phone() {
        return rel_mobile_phone;
    }

    public void setRel_mobile_phone(String rel_mobile_phone) {
        this.rel_mobile_phone = rel_mobile_phone;
    }
}
