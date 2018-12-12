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
public class FindFrostmoneyListResponse {

    /**
     * 服务器响应码
     */
    public int code;
    public String msg;
    public List<FrostmoneyBean> dataList;
    public class FrostmoneyBean{
        //  记录ld
        public String id;
        //平台统一usrid
        public String usrid;
        //商家id
        public String store_id;
        //订单号
        public String order_sn;
        //借款人usrid
        public String borrow_uid;
        //  借款人手机号码
        public String borrower_mobile_phone;
        //真实姓名
        public String usrname;
        //交易金额
        public double deal_money;
        //入账金额
        public double entry_money;
        //入账时间
        public String add_time;
        //  0冻结中1已冻结
        public int is_defrost;
    }
}
