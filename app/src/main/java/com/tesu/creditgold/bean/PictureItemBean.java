package com.tesu.creditgold.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/2 0002.
 */
public class PictureItemBean implements Serializable{
    private String picturePath;
    private String pictureUrl;
    private String picWholeUrl;

    @Override
    public String toString() {
        return "PictureItemBean{" +
                "picturePath='" + picturePath + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", picWholeUrl='" + picWholeUrl + '\'' +
                '}';
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPicWholeUrl() {
        return picWholeUrl;
    }

    public void setPicWholeUrl(String picWholeUrl) {
        this.picWholeUrl = picWholeUrl;
    }
}
