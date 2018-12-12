package com.tesu.creditgold.response;

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
public class GetAdCarouselFigureResponse {
    /**
     * 服务器响应码
     */
    public String code;
    /**描述*/
    public String msg;
    /**会员id*/
    public List<AdCarouselFigureBean> dataList;

    @Override
    public String toString() {
        return "GetAdCarouselFigureResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", dataList=" + dataList +
                '}';
    }

    public class AdCarouselFigureBean{
        public String id;
        public String img;
        public String name;

        @Override
        public String toString() {
            return "AdCarouselFigureBean{" +
                    "id='" + id + '\'' +
                    ", img='" + img + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

}
