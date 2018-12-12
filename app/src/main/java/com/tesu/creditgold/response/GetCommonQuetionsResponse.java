package com.tesu.creditgold.response;

import com.tesu.creditgold.bean.CommonQuestionBean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/8 0008.
 */
public class GetCommonQuetionsResponse {
    private int code;
    private String msg;
    private List<CommonQuestionBean> data;

    @Override
    public String toString() {
        return "GetCommonQuetionsResponse{" +
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

    public List<CommonQuestionBean> getData() {
        return data;
    }

    public void setData(List<CommonQuestionBean> data) {
        this.data = data;
    }
}
