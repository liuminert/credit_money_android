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
public class GetUsrOrderListResponse {
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
        /**总页数*/
        public int  page_total;
        /**总条数*/
        public int  record_total;
        //订单列表
        public List<OrderBean> order_list;

    }




    public class OrderBean implements Serializable{
        public String usr_order_id;
        public String order_sn;//	订单号
        public long create_time;//	订单日期
        public int order_type	;//订单类型,0:旅游，1：家具,2：家装
        public String store_type_name;
        public String borrow_uid;//	借款人usrid
        public String mobile_phone;//	手机号码
        public String usrname;//	真实姓名
        public String id_card;//	身份证号码
        public int order_money;//	金额
        public String store_uid	;//商家usrid
        public String store_name;//	商家名称
        public int order_status;//	订单状态，0：资料提交中 1：待审核，2：待还款，3：还款中，4：还款完成,5：初审不通过,6：复审不通过
        public int submit_step;//订单提交进度：0：创建订单，包括商家、借款人、借款金额、利息等相关信息，1：提交身份认证，2：活体检测通过，3：提交信息认证，4：提交分期资料认证，并生成合同，5：提交手机通讯录、短信等私人资料，提交私人资料后认为该订单提交完成
        public int scene_id;
        public String borrowed_contract;
        public int amortization_cnt;
        public String amortization_money;
        //0：不显示 ，1 ：显示
        public int is_show_flag;
        public double freeze_loan_quota;  //冻结金额
        public int order_from; //订单来源 0：买家下单，1：卖家下单

        @Override
        public String toString() {
            return "OrderBean{" +
                    "usr_order_id='" + usr_order_id + '\'' +
                    ", order_sn='" + order_sn + '\'' +
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
                    ", submit_step=" + submit_step +
                    ", scene_id=" + scene_id +
                    ", borrowed_contract='" + borrowed_contract + '\'' +
                    ", amortization_cnt=" + amortization_cnt +
                    ", amortization_money='" + amortization_money + '\'' +
                    ", is_show_flag=" + is_show_flag +
                    ", freeze_loan_quota=" + freeze_loan_quota +
                    ", order_from=" + order_from +
                    '}';
        }
    }
}
