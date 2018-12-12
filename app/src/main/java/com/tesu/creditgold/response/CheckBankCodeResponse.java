package com.tesu.creditgold.response;

/**
 * Created by Administrator on 2017/4/10 0010.
 */
public class CheckBankCodeResponse {
    private int code;
    private String msg;
    private CodeBean return_param;

    @Override
    public String toString() {
        return "CheckBankCodeResponse{" +
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

    public CodeBean getReturn_param() {
        return return_param;
    }

    public void setReturn_param(CodeBean return_param) {
        this.return_param = return_param;
    }

    public class CodeBean{
        private long auth_code_id;

        @Override
        public String toString() {
            return "CodeBean{" +
                    "auth_code_id=" + auth_code_id +
                    '}';
        }

        public long getAuth_code_id() {
            return auth_code_id;
        }

        public void setAuth_code_id(long auth_code_id) {
            this.auth_code_id = auth_code_id;
        }
    }
}
