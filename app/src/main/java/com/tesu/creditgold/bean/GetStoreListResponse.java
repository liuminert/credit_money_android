package com.tesu.creditgold.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/6 0006.
 */
public class GetStoreListResponse {
    private int code;
    private String msg;
    private List<ShopBean> dataList;

    @Override
    public String toString() {
        return "getStoreListResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", dataList=" + dataList +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ShopBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<ShopBean> dataList) {
        this.dataList = dataList;
    }
}
