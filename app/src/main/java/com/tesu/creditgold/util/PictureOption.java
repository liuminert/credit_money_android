package com.tesu.creditgold.util;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tesu.creditgold.R;

//设置在加载网络图片前的显示
public class PictureOption {
    /**
     * 设置常用的设置项
     *
     * @return
     */
    public static DisplayImageOptions getSimpleOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.loadding) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(null)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(null)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .build();//构建完成
        return options;
    }

    /**
     * 设置圆角的设置项
     *
     * @return
     */
    public static DisplayImageOptions getSimpleRoundOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.loadding) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(null)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(null)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .displayer(new RoundedBitmapDisplayer(10))//是否设置为圆角，弧度为多少
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .build();//构建完成
        return options;
    }

    /**
     * 设置常用的设置项
     *
     * @return
     */
    public static DisplayImageOptions getRectangleOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.loadding) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.loadding)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.img_load)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .build();//构建完成
        return options;
    }

    /**
     * 首页轮播图
     *
     * @return
     */
    public static DisplayImageOptions getBannerOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.banner_default) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.banner_default)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.banner_default)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .build();//构建完成
        return options;
    }
    /**
     * 首页商铺类型图
     *
     * @return
     */
    public static DisplayImageOptions getFirstOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.loading1) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.loading1)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.loading1)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .build();//构建完成
        return options;
    }

    /**
     * 设置常用的设置项
     *
     * @return
     */
    public static DisplayImageOptions getBgOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pic_communucation_bg) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.pic_communucation_bg)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.pic_communucation_bg)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .build();//构建完成
        return options;
    }

    //    /**
//     * 设置常用的设置项
//     * @return
//     */
//    public static DisplayImageOptions getHeadImageOptions() {
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//        .showImageOnLoading(R.drawable.headimage_default) //设置图片在下载期间显示的图片
//        .showImageForEmptyUri(R.drawable.headimage_default)//设置图片Uri为空或是错误的时候显示的图片
//        .showImageOnFail(R.drawable.headimage_default)  //设置图片加载/解码过程中错误时候显示的图片
//        .cacheInMemory(true)//设置下载的图片是否缓存在内存中
//        .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
//        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
//        .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
//        .build();//构建完成
//        return options;
//    }
//
//    /**
//     * 设置常用的设置项
//     * @return
//     */
//    public static DisplayImageOptions getBgOptions() {
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//        .showImageOnLoading(R.drawable.bg_myself) //设置图片在下载期间显示的图片
//        .showImageForEmptyUri(R.drawable.bg_myself)//设置图片Uri为空或是错误的时候显示的图片
//        .showImageOnFail(R.drawable.bg_myself)  //设置图片加载/解码过程中错误时候显示的图片
//        .cacheInMemory(true)//设置下载的图片是否缓存在内存中
//        .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
////        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
////        .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
//        .build();//构建完成
//        return options;
//    }
    public static DisplayImageOptions getOptions() {
        DisplayImageOptions options = options = new DisplayImageOptions.Builder().cacheInMemory(true) // default不缓存至内存
                .cacheOnDisk(true) // default 不缓存至手机SDCard
                .build();
        return options;
    }
}
