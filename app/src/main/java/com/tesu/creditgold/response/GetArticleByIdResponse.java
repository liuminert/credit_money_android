package com.tesu.creditgold.response;

/**
 * Created by Administrator on 2017/1/17 0017.
 */
public class GetArticleByIdResponse {
    private int code;
    private String msg;
    private Artical data;

    @Override
    public String toString() {
        return "GetArticleByIdResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Artical getData() {
        return data;
    }

    public void setData(Artical data) {
        this.data = data;
    }

    public class Artical{
        private int article_id;
        private String article_url;
        private String article_title;
        private String article_content;
        private String article_time;

        @Override
        public String toString() {
            return "Artical{" +
                    "article_id=" + article_id +
                    ", article_url='" + article_url + '\'' +
                    ", article_title='" + article_title + '\'' +
                    ", article_content='" + article_content + '\'' +
                    ", article_time='" + article_time + '\'' +
                    '}';
        }

        public int getArticle_id() {
            return article_id;
        }

        public void setArticle_id(int article_id) {
            this.article_id = article_id;
        }

        public String getArticle_url() {
            return article_url;
        }

        public void setArticle_url(String article_url) {
            this.article_url = article_url;
        }

        public String getArticle_title() {
            return article_title;
        }

        public void setArticle_title(String article_title) {
            this.article_title = article_title;
        }

        public String getArticle_content() {
            return article_content;
        }

        public void setArticle_content(String article_content) {
            this.article_content = article_content;
        }

        public String getArticle_time() {
            return article_time;
        }

        public void setArticle_time(String article_time) {
            this.article_time = article_time;
        }
    }
}
