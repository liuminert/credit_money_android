package com.tesu.creditgold.bean;

/**
 * Created by Administrator on 2016/12/28 0028.
 */
public class GetStoreDescStoreIdResponse {
    private int code;
    private String msg;
    private ScanResultBean data;

    @Override
    public String toString() {
        return "GetStoreDescStoreIdResponse{" +
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

    public ScanResultBean getData() {
        return data;
    }

    public void setData(ScanResultBean data) {
        this.data = data;
    }
}
