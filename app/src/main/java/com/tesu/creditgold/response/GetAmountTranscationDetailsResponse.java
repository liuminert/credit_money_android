package com.tesu.creditgold.response;

import com.tesu.creditgold.bean.AmountDetailBean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/25 0025.
 */
public class GetAmountTranscationDetailsResponse {
    private int code;
    private String msg;
    private List<AmountBean> resultText;

    @Override
    public String toString() {
        return "GetAmountTranscationDetailsResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", resultText=" + resultText +
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

    public List<AmountBean> getResultText() {
        return resultText;
    }

    public void setResultText(List<AmountBean> resultText) {
        this.resultText = resultText;
    }

    public class AmountBean{
        private List<AmountDetailBean> data;
        private double total_income_amount;  //总授信金额
        private double total_pay_amount;   //总支出金额
        private String month_key;  //时间年月
        private double total_return_amount;  //总还款金额

        @Override
        public String toString() {
            return "AmountBean{" +
                    "data=" + data +
                    ", total_income_amount=" + total_income_amount +
                    ", total_pay_amount=" + total_pay_amount +
                    ", month_key='" + month_key + '\'' +
                    ", total_return_amount=" + total_return_amount +
                    '}';
        }

        public List<AmountDetailBean> getData() {
            return data;
        }

        public void setData(List<AmountDetailBean> data) {
            this.data = data;
        }

        public double getTotal_income_amount() {
            return total_income_amount;
        }

        public void setTotal_income_amount(double total_income_amount) {
            this.total_income_amount = total_income_amount;
        }

        public double getTotal_pay_amount() {
            return total_pay_amount;
        }

        public void setTotal_pay_amount(double total_pay_amount) {
            this.total_pay_amount = total_pay_amount;
        }

        public String getMonth_key() {
            return month_key;
        }

        public void setMonth_key(String month_key) {
            this.month_key = month_key;
        }

        public double getTotal_return_amount() {
            return total_return_amount;
        }

        public void setTotal_return_amount(double total_return_amount) {
            this.total_return_amount = total_return_amount;
        }
    }
}
