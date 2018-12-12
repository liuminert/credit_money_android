package com.tesu.creditgold.response;

import com.tesu.creditgold.bean.QuotaOrderBean;

/**
 * Created by Administrator on 2017/6/13 0013.
 */
public class GetQuotaOrderInfoResponse {
    private int code;
    private String msg;
    private QuotaOrderBean return_param;

    @Override
    public String toString() {
        return "GetQuotaOrderInfoResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", return_param=" + return_param +
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

    public QuotaOrderBean getReturn_param() {
        return return_param;
    }

    public void setReturn_param(QuotaOrderBean return_param) {
        this.return_param = return_param;
    }
}
