package com.tesu.creditgold.bean;

/**
 * Created by Administrator on 2016/12/5 0005.
 */
public class UsrOrderPicBean {
    private String pic_desc;
    private String pic_addr;
    private int pic_type;

    @Override
    public String toString() {
        return "UsrOrderPicBean{" +
                "pic_desc='" + pic_desc + '\'' +
                ", pic_addr='" + pic_addr + '\'' +
                ", pic_type=" + pic_type +
                '}';
    }

    public String getPic_desc() {
        return pic_desc;
    }

    public void setPic_desc(String pic_desc) {
        this.pic_desc = pic_desc;
    }

    public String getPic_addr() {
        return pic_addr;
    }

    public void setPic_addr(String pic_addr) {
        this.pic_addr = pic_addr;
    }

    public int getPic_type() {
        return pic_type;
    }

    public void setPic_type(int pic_type) {
        this.pic_type = pic_type;
    }
}
