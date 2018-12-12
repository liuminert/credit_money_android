package com.tesu.creditgold.response;

import java.util.List;

/**
 * Created by Administrator on 2017/3/27 0027.
 */
public class GetUsrBankListResponse {
    private int code;
    private String msg;
    private ReturnParam return_param;

    @Override
    public String toString() {
        return "GetUsrBankListResponse{" +
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

    public ReturnParam getReturn_param() {
        return return_param;
    }

    public void setReturn_param(ReturnParam return_param) {
        this.return_param = return_param;
    }

    public class ReturnParam{
        private String id_card;
        private String usrname;
        private List<BankBean> bank_list;

        @Override
        public String toString() {
            return "ReturnParam{" +
                    "id_card='" + id_card + '\'' +
                    ", usrname='" + usrname + '\'' +
                    ", bank_list=" + bank_list +
                    '}';
        }

        public String getId_card() {
            return id_card;
        }

        public void setId_card(String id_card) {
            this.id_card = id_card;
        }

        public String getUsrname() {
            return usrname;
        }

        public void setUsrname(String usrname) {
            this.usrname = usrname;
        }

        public List<BankBean> getBank_list() {
            return bank_list;
        }

        public void setBank_list(List<BankBean> bank_list) {
            this.bank_list = bank_list;
        }
    }
    public class BankBean {
        private String bank_name;
        private String bank_no;
        private int is_modify;

        @Override
        public String toString() {
            return "BankBean{" +
                    "bank_name='" + bank_name + '\'' +
                    ", bank_no='" + bank_no + '\'' +
                    ", is_modify=" + is_modify +
                    '}';
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getBank_no() {
            return bank_no;
        }

        public void setBank_no(String bank_no) {
            this.bank_no = bank_no;
        }

        public int getIs_modify() {
            return is_modify;
        }

        public void setIs_modify(int is_modify) {
            this.is_modify = is_modify;
        }
    }
}
