package com.tesu.creditgold.response;

import java.io.Serializable;
import java.util.List;

/**
 * @作者: 许明达
 * @创建时间: 2016-3-23下午15:43:20
 * @版权: 特速版权所有
 * @描述: 封装服务器返回列表的参数
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class GetStoreTypeResponse {

    /**
     * 服务器响应码
     */
    public int code;
    public String msg;
    /**描述*/
    public List<StoreType> dataList;
    public class StoreType{
        public int store_type_id	;//门店类型id
        public String  alt	;//没图片时显示提示
        public String name;//	店铺类型名称
        public String store_type;//	店铺类型(1.家具 2.装修 3.旅游 100.其它)
        public String create_time;//	创建时间
        public String pic	;//门脸图片url

        @Override
        public String toString() {
            return "StoreType{" +
                    "store_type_id=" + store_type_id +
                    ", alt='" + alt + '\'' +
                    ", name='" + name + '\'' +
                    ", store_type='" + store_type + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", pic='" + pic + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetStoreTypeResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", dataList=" + dataList +
                '}';
    }
}
