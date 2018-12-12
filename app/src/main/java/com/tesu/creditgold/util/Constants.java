package com.tesu.creditgold.util;

import android.util.FloatMath;

/**
 * @作者: 许明达
 * @创建时间: 2015-3-22下午14:03:31
 * @版权: 特速版权所有
 * @描述: 全局常量
 */
public interface Constants {


    /**
     * 正式服环境
     */
//    String XINYONGSERVER_URL = "http://m.tesu100.com:8080/";
//    String FENGKONGSERVER_URL = "http://risk.tesu100.com:8080/";
//    int WOODSTORETYPE = 18;  //正式服木材分期storeType
//    int ShowArtcicle = 108;  //正式服商家承若书id
//    int PersonalInformationId = 105;  //正式服个人信息使用授权书id
//    int LoanAgreementId = 100;  //正式服借款协议id
//    int updateStrId  = 109;  //正式服更新id

    /**
     * 测试服环境
     */
    String XINYONGSERVER_URL = "http://47.104.156.153:8069/";
    String FENGKONGSERVER_URL = "http://47.104.156.153:8069/";
    int WOODSTORETYPE = 13;  //测试服服木材分期storeType
    int ShowArtcicle = 95; //测试服服商家承若书id
    int PersonalInformationId  = 93;  //测试服个人信息使用授权书id
    int LoanAgreementId  = 73;  //测试服借款协议id
    int updateStrId  = 98;  //测试服更新id


//    String XINYONGSERVER_URL = "http://192.168.0.52:8081/";
//    String FENGKONGSERVER_URL = "http://192.168.0.33:8080/";


    String BaseImageUrl = "http://img01.tesu100.com/";


    String app_id = "1300002";
    String app_secret = "asdasd321a2sd314";

    String fk_app_id = "1300006";
    String fk_app_secret = "asdasd321a2sd318";


    int XutilTimeOut = 120000;
    long XutilCache = 5000;
//
//    // 微信APP_ID 替换为你的应用从官方网站申请到的合法appId
//    public static final String APP_ID = "wxf66fff0d0fbdd642";
}
