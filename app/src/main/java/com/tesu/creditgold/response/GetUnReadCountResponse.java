package com.tesu.creditgold.response;

/**
 * Created by Administrator on 2017/8/8 0008.
 */
public class GetUnReadCountResponse {
    private int code;
    private String msg;
    private int data;

    @Override
    public String toString() {
        return "GetUnReadCountResponse{" +
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

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
