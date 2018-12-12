package com.tesu.creditgold.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/8/8 0008.
 */
public class MyInformationBean implements Parcelable {
    private int id;
    private String msg_title;   //消息标题
    private String send_msg;
    private String send_time;
    private int read_flag;    //浏览状态：0未读，1已读，2删除
    private String send_time_string;

    @Override
    public String toString() {
        return "MyInformationBean{" +
                "id=" + id +
                ", msg_title='" + msg_title + '\'' +
                ", send_msg='" + send_msg + '\'' +
                ", send_time='" + send_time + '\'' +
                ", read_flag=" + read_flag +
                ", send_time_string='" + send_time_string + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg_title() {
        return msg_title;
    }

    public void setMsg_title(String msg_title) {
        this.msg_title = msg_title;
    }

    public String getSend_msg() {
        return send_msg;
    }

    public void setSend_msg(String send_msg) {
        this.send_msg = send_msg;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public int getRead_flag() {
        return read_flag;
    }

    public void setRead_flag(int read_flag) {
        this.read_flag = read_flag;
    }

    public String getSend_time_string() {
        return send_time_string;
    }

    public void setSend_time_string(String send_time_string) {
        this.send_time_string = send_time_string;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.msg_title);
        dest.writeString(this.send_msg);
        dest.writeString(this.send_time);
        dest.writeInt(this.read_flag);
        dest.writeString(this.send_time_string);
    }

    public MyInformationBean() {
    }

    protected MyInformationBean(Parcel in) {
        this.id = in.readInt();
        this.msg_title = in.readString();
        this.send_msg = in.readString();
        this.send_time = in.readString();
        this.read_flag = in.readInt();
        this.send_time_string = in.readString();
    }

    public static final Parcelable.Creator<MyInformationBean> CREATOR = new Parcelable.Creator<MyInformationBean>() {
        @Override
        public MyInformationBean createFromParcel(Parcel source) {
            return new MyInformationBean(source);
        }

        @Override
        public MyInformationBean[] newArray(int size) {
            return new MyInformationBean[size];
        }
    };
}
