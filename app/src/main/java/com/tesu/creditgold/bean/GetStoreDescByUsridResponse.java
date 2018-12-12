package com.tesu.creditgold.bean;

/**
 * Created by Administrator on 2017/1/19 0019.
 */
public class GetStoreDescByUsridResponse {
    private int code;
    private String msg;
    private ShopBean store_info;

    @Override
    public String toString() {
        return "GetStoreDescByUsridResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", store_info=" + store_info +
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

    public ShopBean getStore_info() {
        return store_info;
    }

    public void setStore_info(ShopBean store_info) {
        this.store_info = store_info;
    }
}
