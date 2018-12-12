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
public class GetArticleListResponse {

    /**
     * 服务器响应码
     */
    public int code;
    public String msg;
    /**描述*/
    public List<ArticleBean> dataList;
    public class ArticleBean{
        public int  article_id	;
        public String  article_url	;//跳转链接
        public String article_title;//	标题
        public String article_content;//	内容
        public String article_time;//	发布时间
    }




}
