package com.tesu.creditgold.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/5 0005.
 */
public class GetAreaListResponse {
    private int code;
    private String resultText;
    private ArrayList<AreaBean> dataList;

    @Override
    public String toString() {
        return "GetAreaListResponse{" +
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

    public ArrayList<AreaBean> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<AreaBean> dataList) {
        this.dataList = dataList;
    }
}
