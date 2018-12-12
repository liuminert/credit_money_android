package com.tesu.creditgold.bean;

/**
 * Created by Administrator on 2016/12/6 0006.
 */
public class GetStoreInfoResponse {
    private int code;
    private String msg;
    private ShopBean data;

    @Override
    public String toString() {
        return "GetStroreInfoResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
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

    public ShopBean getData() {
        return data;
    }

    public void setData(ShopBean data) {
        this.data = data;
    }
}
