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
public class FindStoreDescByUsridResponse implements Serializable{

    /**
     * 服务器响应码
     */
    public int code;
    public String msg;
    /**
     * 商户余额decimal(11,2)
     */
    public double store_account;


    public BankCard bank_card_info;
    public class BankCard implements Serializable{
        //账户银行所属人名称
        public String bank_username;
        //	提现账户所属银行名称
        public String bank_name;
        //	提现账户银行卡号
        public String bank_card_number	;
        public String branch_name;
    }

    public StoreIfo store_info;
    public class StoreIfo implements Serializable{
        //入账冻结资金
        public double in_freeze_account;
        public double freeze_account;  //冻结金额
        //店铺id
        public String store_id;
        //店铺门脸照片图片文件名
        public String store_pic;
        //店铺名称
        public String store_name;
        //	店铺类型(1.家具 2.装修 3.旅游 100.其它)4.9.4接口
        public int store_type;
        public String store_type_name;
        //店铺所属省id
        public String province_id;
        //店铺所属市id
        public String city_id	;
        //店铺所属区id
        public String area_id;
        //详细地址
        public String address;
        //联系人姓名
        public String contract_name	;
        //电话
        public String contract_telephone;
        //店铺营业执照图片文件名
        public String licence_pic;
        //店铺描述
        public String description;
        //行政区域信息(如 广东省 深圳市 福田区)
        public String area_info;
        // 店铺所属人的uid，相当于store_uid
        public String usr_id;
        //  是否贴息(0.不贴息  1.贴息) 默认为0
        public int is_tiexi;
        //   是否已经设置提现密码，0否，1是
        public int is_set_withdraw_pwd;
        // 身份证正面照(相对路径)
        public String id_front_pic;
        //   身份证反面照(相对路径)
        public String id_back_pic;
        //    经营场所照片(相对路径)
        public String businesssite_pic;
    }
    //店铺二维码图片路径
    public String qr_pic;
    public List<StoreRate> store_rate_list;
    public class  StoreRate implements Serializable{
        public String id;
        public int period;//	期数
        public String user_charge_rate;//	年化，链金所手续费，如12.5%此处应写12.5
        public String interest_rate;//	年化, 分期利息，如年化利率为 7.5%, 此处应填 7.5
        public String store_charge_rate;//	百分比，商家抽水比例，如10.5%抽水从,此处填10.5
    }

    @Override
    public String toString() {
        return "FindStoreDescByUsridResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", store_account='" + store_account + '\'' +
                ", bank_card_info=" + bank_card_info +
                ", store_info=" + store_info +
                ", qr_pic='" + qr_pic + '\'' +
                ", store_rate_list=" + store_rate_list +
                '}';
    }
}
