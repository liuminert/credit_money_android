package com.tesu.creditgold.bean;

/**
 * Created by Administrator on 2017/1/13 0013.
 */
public class SetCreditInfResponse {
    private int code;
    private String msg;
    private Result return_param;

    @Override
    public String toString() {
        return "SetCreditInfResponse{" +
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

    public Result getReturn_param() {
        return return_param;
    }

    public void setReturn_param(Result return_param) {
        this.return_param = return_param;
    }

    public class Result{
        private long usr_order_id;

        @Override
        public String toString() {
            return "Result{" +
                    "usr_order_id=" + usr_order_id +
                    '}';
        }

        public long getUsr_order_id() {
            return usr_order_id;
        }

        public void setUsr_order_id(long usr_order_id) {
            this.usr_order_id = usr_order_id;
        }
    }
}
