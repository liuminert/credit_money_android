package com.tesu.creditgold.response;

import java.io.Serializable;
import java.util.Date;
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
public class GetUsrInfResponse {
    @Override
    public String toString() {
        return "GetUsrInfResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", return_param=" + return_param +
                '}';
    }

    /**
     * 服务器响应码
     */
    public String code;
    public String msg;
    /**描述*/
    public UsrInDate return_param;
    public class UsrInDate{
        /**会员id*/
        public String usrid;
        //手机号码
        public String mobile_phone;
        //真实姓名
        public String usrname;
        //身份证号码
        public String id_card;
        //借款总笔数
        public double borrow_cnt;
        //借款总金额
        public double borrow_amount;
        //近7天应还
        public String shouldreturn_amount_7;
        //最近应还一期金额
        public String shouldreturn_amount_1;
        //身份证正面照
        public String id_card_front_pic;
        //身份证反面照
        public String id_card_reverse_pic;
        //手持身份证照片
        public String with_id_card_pic;
        //订单列表
        public List<OrderBean> order_list;
        //借款人真实姓名
        public String borrower_usrname;
        //借款人身份证号码
        public String borrower_id_card;
        public String is_store;
        //头像照片
        public String head_portrait_pic;
        //是否进行了实名认证，0:表示未进行实名认证 1:表示已经进行实名认证
        public int is_real_name_check;

        //贷款总额度
        public double loan_limit;
        //已用额度
        public double loan_used;
        //可用额度
        public double loan_unused;
        //冻结额度
        public double freeze_loan_quota;
        public long limit_validity_time;

        public int check_flag;  //审核标志，0：初始状态，1：未审核，:2：初审不通过，3：初审通过，4：复审不通过，5：复审通过，7：终审不通过，8：终审通过
                                    //只有在，0或者2的时候可以提交审核
        public String lateLyDay; //还款日
        public int loan_isexpired;  //额度是否过期,0 没有过期，  1 过期

        public String nick_name;  //昵称

        public double total_credit_amount;  //累计授信额度
        public double returned_amount;  //已还额度
        public double unreturned_amount;  //待还额度

        @Override
        public String toString() {
            return "UsrInDate{" +
                    "usrid='" + usrid + '\'' +
                    ", mobile_phone='" + mobile_phone + '\'' +
                    ", usrname='" + usrname + '\'' +
                    ", id_card='" + id_card + '\'' +
                    ", borrow_cnt=" + borrow_cnt +
                    ", borrow_amount=" + borrow_amount +
                    ", shouldreturn_amount_7='" + shouldreturn_amount_7 + '\'' +
                    ", shouldreturn_amount_1='" + shouldreturn_amount_1 + '\'' +
                    ", id_card_front_pic='" + id_card_front_pic + '\'' +
                    ", id_card_reverse_pic='" + id_card_reverse_pic + '\'' +
                    ", with_id_card_pic='" + with_id_card_pic + '\'' +
                    ", order_list=" + order_list +
                    ", borrower_usrname='" + borrower_usrname + '\'' +
                    ", borrower_id_card='" + borrower_id_card + '\'' +
                    ", is_store='" + is_store + '\'' +
                    ", head_portrait_pic='" + head_portrait_pic + '\'' +
                    ", is_real_name_check=" + is_real_name_check +
                    ", loan_limit=" + loan_limit +
                    ", loan_used=" + loan_used +
                    ", loan_unused=" + loan_unused +
                    ", freeze_loan_quota=" + freeze_loan_quota +
                    ", limit_validity_time=" + limit_validity_time +
                    ", check_flag=" + check_flag +
                    ", lateLyDay='" + lateLyDay + '\'' +
                    ", loan_isexpired=" + loan_isexpired +
                    ", nick_name='" + nick_name + '\'' +
                    ", total_credit_amount=" + total_credit_amount +
                    ", returned_amount=" + returned_amount +
                    ", unreturned_amount=" + unreturned_amount +
                    '}';
        }
    }




    public class OrderBean implements Serializable{
        public String order_sn;//	订单号
        public long create_time;//	订单日期
        public int order_type	;//订单类型,0:旅游，1：家具,2：家装
        public String store_type_name;
        public String borrow_uid;//	借款人usrid
        public String mobile_phone;//	手机号码
        public String usrname;//	真实姓名
        public String id_card;//	身份证号码
        public double order_money;//	金额
        public String store_uid	;//商家usrid
        public String store_name;//	商家名称
        public int order_status;//	订单状态，0：资料提交中 1：待审核，2：待还款，3：还款中，4：还款完成
        public String order_status_ch;  //订单状态 中文
        public String wood_type_name;  //木材品名

        @Override
        public String toString() {
            return "OrderBean{" +
                    "order_sn='" + order_sn + '\'' +
                    ", create_time=" + create_time +
                    ", order_type=" + order_type +
                    ", store_type_name='" + store_type_name + '\'' +
                    ", borrow_uid='" + borrow_uid + '\'' +
                    ", mobile_phone='" + mobile_phone + '\'' +
                    ", usrname='" + usrname + '\'' +
                    ", id_card='" + id_card + '\'' +
                    ", order_money=" + order_money +
                    ", store_uid='" + store_uid + '\'' +
                    ", store_name='" + store_name + '\'' +
                    ", order_status=" + order_status +
                    ", order_status_ch='" + order_status_ch + '\'' +
                    ", wood_type_name='" + wood_type_name + '\'' +
                    '}';
        }
    }
}
