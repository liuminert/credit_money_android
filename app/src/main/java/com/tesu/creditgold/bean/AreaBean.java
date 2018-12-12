package com.tesu.creditgold.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/5 0005.
 */
public class AreaBean implements Serializable{
    private int area_id;
    private String area_name;

    @Override
    public String toString() {
        return "AreaBean{" +
                "area_id=" + area_id +
                ", area_name='" + area_name + '\'' +
                '}';
    }

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AreaBean areaBean = (AreaBean) o;

        if (area_id != areaBean.area_id) return false;
        return !(area_name != null ? !area_name.equals(areaBean.area_name) : areaBean.area_name != null);

    }

    @Override
    public int hashCode() {
        int result = area_id;
        result = 31 * result + (area_name != null ? area_name.hashCode() : 0);
        return result;
    }
}
