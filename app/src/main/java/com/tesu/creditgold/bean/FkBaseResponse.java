package com.tesu.creditgold.bean;


/**
 * Created by Administrator on 2016/12/3 0003.
 */
public class FkBaseResponse {
    private int code;
    private String msg;
    private Object return_param;

    @Override
    public String toString() {
        return "BaseResponse{" +
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

    public Object getReturn_param() {
        return return_param;
    }

    public void setReturn_param(Object return_param) {
        this.return_param = return_param;
    }
}
