package com.tesu.creditgold.response;

import java.util.List;

/**
 * Created by Administrator on 2017/3/28 0028.
 */
public class GetSupportBankListResponse {
    private int code;
    private String resultText;
    private List<SupportBankBean>  dataList;

    @Override
    public String toString() {
        return "GetSupportBankListResponse{" +
                "code=" + code +
                ", resultText='" + resultText + '\'' +
                ", dataList=" + dataList +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    public List<SupportBankBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<SupportBankBean> dataList) {
        this.dataList = dataList;
    }

    public class SupportBankBean{
        private int id;
        private String bname;
        private String unit;
        private double dailyLimit;
        private double eachLimit;

        @Override
        public String toString() {
            return "SupportBankBean{" +
                    "id=" + id +
                    ", bname='" + bname + '\'' +
                    ", unit='" + unit + '\'' +
                    ", dailyLimit=" + dailyLimit +
                    ", eachLimit=" + eachLimit +
                    '}';
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBname() {
            return bname;
        }

        public void setBname(String bname) {
            this.bname = bname;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public double getDailyLimit() {
            return dailyLimit;
        }

        public void setDailyLimit(double dailyLimit) {
            this.dailyLimit = dailyLimit;
        }

        public double getEachLimit() {
            return eachLimit;
        }

        public void setEachLimit(double eachLimit) {
            this.eachLimit = eachLimit;
        }
    }
}
