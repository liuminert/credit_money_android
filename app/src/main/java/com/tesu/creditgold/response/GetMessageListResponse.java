package com.tesu.creditgold.response;

import com.tesu.creditgold.bean.MyInformationBean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/8 0008.
 */
public class GetMessageListResponse {
    private int code;
    private String msg;
    private List<MyInformationBean> data;

    @Override
    public String toString() {
        return "GetMessageListResponse{" +
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

    public List<MyInformationBean> getData() {
        return data;
    }

    public void setData(List<MyInformationBean> data) {
        this.data = data;
    }
}
