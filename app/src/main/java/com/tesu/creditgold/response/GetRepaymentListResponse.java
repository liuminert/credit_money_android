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
public class GetRepaymentListResponse {
    /**
     * 服务器响应码
     */
    public String code;
    /**描述*/
    public String msg;
    /**验证码id*/
    public Data return_param;

    @Override
    public String toString() {
        return "GetRepaymentListResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", return_param=" + return_param +
                '}';
    }

    public class Data{
        @Override
        public String toString() {
            return "Data{" +
                    "record_total=" + record_total +
                    ", repayment_list=" + repayment_list +
                    ", interest_total='" + interest_total + '\'' +
                    '}';
        }

        public int record_total;//	总条数
        public List<RepaymentBean> repayment_list;//	还款详情列表
        public String interest_total;
    }
    public class RepaymentBean implements Serializable{
        @Override
        public String toString() {
            return "RepaymentBean{" +
                    "cur_period=" + cur_period +
                    ", repay_money='" + repay_money + '\'' +
                    ", repay_time='" + repay_time + '\'' +
                    ", repay_status=" + repay_status +
                    ", expiredMoney='" + expiredMoney + '\'' +
                    ", isShowing=" + isShowing +
                    ", yxt_repayment_id='" + yxt_repayment_id + '\'' +
                    ", bank_id='" + bank_id + '\'' +
                    ", order_allot=" + order_allot +
                    '}';
        }

        public int cur_period;//	期数
        public String repay_money;//	还款金额
        public String repay_time;//	还款时间
        public int repay_status	;//还款状态,不为0则已还款，为0则未还款
        public String expiredMoney;//罚息
        public boolean isShowing;  //是否显示详情
        public String yxt_repayment_id; //易行通还款ID
        public String bank_id;  //银行卡尾号
        public int order_allot;  //2 可以立即还款
    }
}
