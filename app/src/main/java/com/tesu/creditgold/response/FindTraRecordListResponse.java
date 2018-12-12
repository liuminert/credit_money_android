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
public class FindTraRecordListResponse {
    /**
     * 服务器响应码
     */
    public int code;
    /**描述*/
    public String msg;
    /**会员id*/
    public List<TraRecord> tra_record_list;
    public class TraRecord implements Serializable{
        public String id;
        public int type	;//类型(1.交易收入 2.转款支出 100.其它)
        public boolean is_income;//	是否是收入(相对于支出)
        public String amount;//	本次流水金额
        public String time	;//交易时间
        public String remark;//	类型为1：用户姓名+手机号，类型为2：提现银行卡号
        public String order_sn	;//交易单号
        public String trade_no;//交易单号

        @Override
        public String toString() {
            return "TraRecord{" +
                    "id='" + id + '\'' +
                    ", type=" + type +
                    ", is_income=" + is_income +
                    ", amount='" + amount + '\'' +
                    ", time='" + time + '\'' +
                    ", remark='" + remark + '\'' +
                    ", order_sn='" + order_sn + '\'' +
                    ", trade_no='" + trade_no + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FindTraRecordListResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", tra_record_list=" + tra_record_list +
                '}';
    }
}
