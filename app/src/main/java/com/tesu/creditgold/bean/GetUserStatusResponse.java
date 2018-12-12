package com.tesu.creditgold.bean;

/**
 * Created by Administrator on 2016/12/5 0005.
 */
public class GetUserStatusResponse {
    private int code;
    private String msg;
    private String sina_whithhold_url;

    @Override
    public String toString() {
        return "GetUserStatusResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", sina_whithhold_url='" + sina_whithhold_url + '\'' +
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

    public String getSina_whithhold_url() {
        return sina_whithhold_url;
    }

    public void setSina_whithhold_url(String sina_whithhold_url) {
        this.sina_whithhold_url = sina_whithhold_url;
    }
}
