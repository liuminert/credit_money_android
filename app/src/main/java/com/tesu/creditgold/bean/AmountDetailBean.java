package com.tesu.creditgold.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Administrator on 2017/8/25 0025.
 */
public class AmountDetailBean implements Parcelable {
    private int transaction_id; //交易id
    private int user_id;    //用户id
    private int transaction_type;  //交易类型：1.授信金额，2.交易支出，3.还款金额,4.额度过期
    private double available_amount;  //可用额度
    private double income_amount;  //收入金额
    private double pay_amount;  //支出金额
    private double freeze_amount;  //冻结金额
    private double transaction_amount;  //交易金额
    private int freeze_mode;  //冻结模式 0：正常，1：冻结，2：解冻
    private String transaction_time;  //交易时间
    private String transaction_time_format; //交易时间格式化后的数据
    private String remark;
    private String order_sn; //交易单号

    protected AmountDetailBean(Parcel in) {
        transaction_id = in.readInt();
        user_id = in.readInt();
        transaction_type = in.readInt();
        available_amount = in.readDouble();
        income_amount = in.readDouble();
        pay_amount = in.readDouble();
        freeze_amount = in.readDouble();
        transaction_amount = in.readDouble();
        freeze_mode = in.readInt();
        transaction_time = in.readString();
        transaction_time_format = in.readString();
        remark = in.readString();
        order_sn = in.readString();
    }

    public static final Creator<AmountDetailBean> CREATOR = new Creator<AmountDetailBean>() {
        @Override
        public AmountDetailBean createFromParcel(Parcel in) {
            return new AmountDetailBean(in);
        }

        @Override
        public AmountDetailBean[] newArray(int size) {
            return new AmountDetailBean[size];
        }
    };

    @Override
    public String toString() {
        return "AmountDetailBean{" +
                "transaction_id=" + transaction_id +
                ", user_id=" + user_id +
                ", transaction_type=" + transaction_type +
                ", available_amount=" + available_amount +
                ", income_amount=" + income_amount +
                ", pay_amount=" + pay_amount +
                ", freeze_amount=" + freeze_amount +
                ", transaction_amount=" + transaction_amount +
                ", freeze_mode=" + freeze_mode +
                ", transaction_time='" + transaction_time + '\'' +
                ", transaction_time_format='" + transaction_time_format + '\'' +
                ", remark='" + remark + '\'' +
                ", order_sn='" + order_sn + '\'' +
                '}';
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(int transaction_type) {
        this.transaction_type = transaction_type;
    }

    public double getAvailable_amount() {
        return available_amount;
    }

    public void setAvailable_amount(double available_amount) {
        this.available_amount = available_amount;
    }

    public double getIncome_amount() {
        return income_amount;
    }

    public void setIncome_amount(double income_amount) {
        this.income_amount = income_amount;
    }

    public double getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(double pay_amount) {
        this.pay_amount = pay_amount;
    }

    public double getFreeze_amount() {
        return freeze_amount;
    }

    public void setFreeze_amount(double freeze_amount) {
        this.freeze_amount = freeze_amount;
    }

    public double getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(double transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public int getFreeze_mode() {
        return freeze_mode;
    }

    public void setFreeze_mode(int freeze_mode) {
        this.freeze_mode = freeze_mode;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
    }

    public String getTransaction_time_format() {
        return transaction_time_format;
    }

    public void setTransaction_time_format(String transaction_time_format) {
        this.transaction_time_format = transaction_time_format;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(transaction_id);
        dest.writeInt(user_id);
        dest.writeInt(transaction_type);
        dest.writeDouble(available_amount);
        dest.writeDouble(income_amount);
        dest.writeDouble(pay_amount);
        dest.writeDouble(freeze_amount);
        dest.writeDouble(transaction_amount);
        dest.writeInt(freeze_mode);
        dest.writeString(transaction_time);
        dest.writeString(transaction_time_format);
        dest.writeString(remark);
        dest.writeString(order_sn);
    }
}
