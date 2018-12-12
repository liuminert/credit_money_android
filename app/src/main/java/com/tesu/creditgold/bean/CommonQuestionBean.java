package com.tesu.creditgold.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/8/8 0008.
 */
public class CommonQuestionBean implements Parcelable {
    private int id;
    private String question;
    private String answer;

    @Override
    public String toString() {
        return "CommonQuestionBean{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.question);
        dest.writeString(this.answer);
    }

    public CommonQuestionBean() {
    }

    protected CommonQuestionBean(Parcel in) {
        this.id = in.readInt();
        this.question = in.readString();
        this.answer = in.readString();
    }

    public static final Parcelable.Creator<CommonQuestionBean> CREATOR = new Parcelable.Creator<CommonQuestionBean>() {
        @Override
        public CommonQuestionBean createFromParcel(Parcel source) {
            return new CommonQuestionBean(source);
        }

        @Override
        public CommonQuestionBean[] newArray(int size) {
            return new CommonQuestionBean[size];
        }
    };
}
