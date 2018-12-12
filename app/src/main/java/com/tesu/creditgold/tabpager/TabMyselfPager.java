package com.tesu.creditgold.tabpager;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tesu.creditgold.R;
import com.tesu.creditgold.activity.AboutActivity;
import com.tesu.creditgold.activity.AccountManagementActivity;
import com.tesu.creditgold.activity.ApplicationActivity;
import com.tesu.creditgold.activity.ApplicationLimitActivity;
import com.tesu.creditgold.activity.CodeActivity;
import com.tesu.creditgold.activity.ContractActivity;
import com.tesu.creditgold.activity.CreateQuotaOrderActivity;
import com.tesu.creditgold.activity.DataManagementActivity;
import com.tesu.creditgold.activity.HelpCenterActivity;
import com.tesu.creditgold.activity.InstallmentRecordActivity;

import com.tesu.creditgold.activity.LoginActivity;
import com.tesu.creditgold.activity.MainActivity;
import com.tesu.creditgold.activity.MyAuthenticationActivity;
import com.tesu.creditgold.activity.MyInformationListActivity;
import com.tesu.creditgold.activity.MyMessageActivity;
import com.tesu.creditgold.activity.OrderManagementActivity;

import com.tesu.creditgold.activity.QuotaOrderListActivity;
import com.tesu.creditgold.activity.RepaymentAccountActivity;
import com.tesu.creditgold.activity.SetWithdrawalPasswordActivity;
import com.tesu.creditgold.activity.ShopSettingActivity;
import com.tesu.creditgold.activity.TransactionDetailsActivity;
import com.tesu.creditgold.activity.UseQuotaActivity;
import com.tesu.creditgold.activity.UserSettingActivity;
import com.tesu.creditgold.activity.WithdrawalActivity;
import com.tesu.creditgold.activity.XieYiWebActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.base.ViewTabBasePager;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.QuotaOrderBean;
import com.tesu.creditgold.fragment.ControlTabFragment;
import com.tesu.creditgold.protocol.FindStoreDescByUsridProtocol;
import com.tesu.creditgold.protocol.GetUsrInfProtocol;
import com.tesu.creditgold.request.FindStoreDescByUsridRequest;
import com.tesu.creditgold.request.GetUsrInfRequest;
import com.tesu.creditgold.response.FindStoreDescByUsridResponse;
import com.tesu.creditgold.response.GetArticleByIdResponse;
import com.tesu.creditgold.response.GetUnReadCountResponse;
import com.tesu.creditgold.response.GetUsrInfResponse;
import com.tesu.creditgold.support.PercentRelativeLayout;

import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;

import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.widget.CircleImageView;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * @作者: 许明达
 * @创建时间: 2016-4-27下午5:24:30
 * @版权: 特速版权所有
 * @描述: 详情页面
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class TabMyselfPager extends ViewTabBasePager implements View.OnClickListener {
    private View view;

    private String goods_desc;
    //css显示图片样式
    private RelativeLayout rl_installment;
    private RelativeLayout rl_authentication;
    private RelativeLayout rl_withdrawal;
    private PercentRelativeLayout pr_head;
    private PercentRelativeLayout pr_normal;
    private PercentRelativeLayout pr_shoper;
    private RelativeLayout rl_transaction_details;
    private RelativeLayout rl_shop;
    private RelativeLayout rl_fail;
    private RelativeLayout rl_order_management;
    private TextView tv_fail_msg;
    private Button btn_again_request;
    private TextView tv_normal_user;
    private TextView tv_shop_user;
    private Button btn_request;

    private String url;
    //接口请求菊花
    private Dialog loadingDialog;
    private AlertDialog alertDialog;
    private ControlTabFragment ctf;


    private TextView tv_user_tell;
    private TextView tv_debt;
    private boolean isLoaded = false;
    private GetUsrInfResponse getUsrInfResponse;
    private FindStoreDescByUsridResponse findStoreDescByUsridResponse;
    private RelativeLayout rl_commit_success;
    private RelativeLayout rl_disable;
    //店铺二维码
    private TextView tv_balance;
    private TextView tv_reimbursement;
    private String userId;
    private RelativeLayout rl_user_setting;
    private RelativeLayout rl_help_center;
    private TextView tv_freeze_money;
    private CircleImageView iv_myself_img;
    /**
     * 照相选择界面
     */
    private PopupWindow pWindow;
    private View root;
    private LayoutInflater mInflater;
    private String timepath;
    /**
     * 照片参数
     */
    private static final int PHOTO_GRAPH = 4;// 拍照
    private static final int PHOTO_ZOOM = 5; // 缩放
    private static final String IMAGE_UNSPECIFIED = "image/*";

    private TextView tv_tell;
    private TextView tv_tell_two;
    private TextView tv_telephone;
    //true代表普通用户，false代表商家
    private boolean is_normal = true;

    private AlertDialog articleDialog;

    private GetArticleByIdResponse getArticleByIdResponse;

    private boolean isClickShop;  //是否点击了商户中心

    private String token;

    private AlertDialog tellCallDialog;

    private TextView tv_total_balance;

    private DecimalFormat df = new DecimalFormat("###0.00");

    private ImageView iv_freeze;

    private TextView tv_available_credit;  //可用额度
    private TextView tv_use_credit;  //使用额度 / 申请额度
    private TextView tv_reimbursement_date;  //还款日
    private boolean isHaveCredit;  //是否有额度
    private double total_credit_amount;  //累计授信额度
    private double used_credit;  //已用额度
    private double loan_unused;  //可用额度
    private double freeze_loan_quota;  //冻结额度
    private long limit_validity_time;
    private int loan_isexpired;  ////额度是否过期,0 没有过期，  1 过期
    private double returned_amount; //已还额度
    private double unreturned_amount; //待还额度

    private RelativeLayout rl_initiating_order;
    private View line3;

    private List<GetUsrInfResponse.OrderBean> orderBeanList;

    private RelativeLayout rl_shop_setting;
    private RelativeLayout rl_use_credit;
    private TextView tv_use_credit_message;
    private PercentRelativeLayout rl_personal_message;

    private String head_portrait_pic;
    private String nick_name;
    private GetUnReadCountResponse getUnReadCountResponse;
    private PercentRelativeLayout rl_message_num;
    private PercentRelativeLayout rl_message;
    private TextView tv_message_num;
    private ImageView iv_new;
    private boolean has_initiating_ordered;
    private String tell;

    private boolean isQuota;  //是否申请或者使用额度;

    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    LogUtils.e("Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(mContext,
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    LogUtils.e("Unhandled msg - " + msg.what);
            }
        }
    };

    public TabMyselfPager(Context context) {
        super(context);
    }

    @Override
    protected View initView() {
        view = View.inflate(mContext,
                R.layout.myself_pager, null);
        ViewUtils.inject(this, view);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }
        root = mInflater.inflate(R.layout.alert_dialog, null);
        pWindow = new PopupWindow(root, ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.FILL_PARENT);
        iv_myself_img = (CircleImageView) view.findViewById(R.id.iv_myself_img);
        btn_again_request = (Button) view.findViewById(R.id.btn_again_request);
        tv_tell = (TextView) view.findViewById(R.id.tv_tell);
        tv_telephone = (TextView) view.findViewById(R.id.tv_telephone);
        tv_tell_two = (TextView) view.findViewById(R.id.tv_tell_two);
        tv_freeze_money = (TextView) view.findViewById(R.id.tv_freeze_money);
        tv_reimbursement = (TextView) view.findViewById(R.id.tv_reimbursement);
        tv_fail_msg = (TextView) view.findViewById(R.id.tv_fail_msg);
        tv_balance = (TextView) view.findViewById(R.id.tv_balance);
        btn_request = (Button) view.findViewById(R.id.btn_request);
        rl_order_management = (RelativeLayout) view.findViewById(R.id.rl_order_management);
        rl_help_center = (RelativeLayout) view.findViewById(R.id.rl_help_center);
        rl_fail = (RelativeLayout) view.findViewById(R.id.rl_fail);
        rl_disable = (RelativeLayout) view.findViewById(R.id.rl_disable);
        rl_commit_success = (RelativeLayout) view.findViewById(R.id.rl_commit_success);
        rl_installment = (RelativeLayout) view.findViewById(R.id.rl_installment);
        rl_authentication = (RelativeLayout) view.findViewById(R.id.rl_authentication);
        pr_head = (PercentRelativeLayout) view.findViewById(R.id.pr_head);
        pr_normal = (PercentRelativeLayout) view.findViewById(R.id.pr_normal);
        pr_shoper = (PercentRelativeLayout) view.findViewById(R.id.pr_shoper);
        rl_shop = (RelativeLayout) view.findViewById(R.id.rl_shop);
        rl_user_setting = (RelativeLayout) view.findViewById(R.id.rl_user_setting);
        rl_transaction_details = (RelativeLayout) view.findViewById(R.id.rl_transaction_details);
        rl_withdrawal = (RelativeLayout) view.findViewById(R.id.rl_withdrawal);
        tv_normal_user = (TextView) view.findViewById(R.id.tv_normal_user);
        tv_shop_user = (TextView) view.findViewById(R.id.tv_shop_user);
        tv_user_tell = (TextView) view.findViewById(R.id.tv_user_tell);
        tv_debt = (TextView) view.findViewById(R.id.tv_debt);
        tv_total_balance = (TextView) view.findViewById(R.id.tv_total_balance);
        iv_freeze = (ImageView) view.findViewById(R.id.iv_freeze);
        tv_available_credit = (TextView) view.findViewById(R.id.tv_available_credit);
        tv_use_credit = (TextView) view.findViewById(R.id.tv_use_credit);
        tv_reimbursement_date = (TextView) view.findViewById(R.id.tv_reimbursement_date);
        rl_initiating_order = (RelativeLayout) view.findViewById(R.id.rl_initiating_order);
        line3 = view.findViewById(R.id.line3);
        rl_shop_setting = (RelativeLayout) view.findViewById(R.id.rl_shop_setting);
        rl_use_credit = (RelativeLayout) view.findViewById(R.id.rl_use_credit);
        tv_use_credit_message = (TextView) view.findViewById(R.id.tv_use_credit_message);
        rl_personal_message = (PercentRelativeLayout) view.findViewById(R.id.rl_personal_message);
        rl_message_num = (PercentRelativeLayout) view.findViewById(R.id.rl_message_num);
        rl_message = (PercentRelativeLayout) view.findViewById(R.id.rl_message);
        tv_message_num = (TextView) view.findViewById(R.id.tv_message_num);
        iv_new = (ImageView) view.findViewById(R.id.iv_new);
        initData();
        return view;
    }

    @Override
    public void initData() {
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance()
                    .init(ImageLoaderConfiguration.createDefault(UIUtils
                            .getContext()));
        }
        if (ctf == null) {
            ctf = MainActivity.getCtf();
        }
        loadingDialog = DialogUtils.createLoadDialog(mContext, true);
        token = SharedPrefrenceUtils.getString(mContext, "token");

        tell = SharedPrefrenceUtils.getString(mContext, "mobile_phone");
        userId = SharedPrefrenceUtils.getString(mContext, "usrid");

        has_initiating_ordered = SharedPrefrenceUtils.getBoolean(mContext, userId + "has_initiating_ordered", false);
        if(has_initiating_ordered){
            iv_new.setVisibility(View.GONE);
        }else{
            iv_new.setVisibility(View.VISIBLE);
        }

        tv_tell.setOnClickListener(this);
        tv_telephone.setOnClickListener(this);
        tv_tell_two.setOnClickListener(this);
        rl_transaction_details.setOnClickListener(this);
        rl_withdrawal.setOnClickListener(this);
        rl_order_management.setOnClickListener(this);
        rl_installment.setOnClickListener(this);
        rl_help_center.setOnClickListener(this);
        rl_authentication.setOnClickListener(this);
        tv_shop_user.setOnClickListener(this);
        tv_normal_user.setOnClickListener(this);
        btn_request.setOnClickListener(this);
        btn_again_request.setOnClickListener(this);
        rl_user_setting.setOnClickListener(this);
        tv_freeze_money.setOnClickListener(this);

        iv_freeze.setOnClickListener(this);
        rl_use_credit.setOnClickListener(this);
        rl_initiating_order.setOnClickListener(this);
        rl_shop_setting.setOnClickListener(this);
        rl_personal_message.setOnClickListener(this);
        rl_message.setOnClickListener(this);

        root.findViewById(R.id.btn_Phone).setOnClickListener(itemsOnClick);
        root.findViewById(R.id.btn_TakePicture)
                .setOnClickListener(itemsOnClick);
        root.findViewById(R.id.bg_photo).getBackground().setAlpha(100);
        root.findViewById(R.id.btn_cancel).setOnClickListener(itemsOnClick);



    }


    /**
     * 查找未读的消息个数
     */
    public void getUnReadCount(String phone) {

        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/message/getUnReadCount.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("plateform", "android");
        params.put("alias", phone);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                LogUtils.e("查找未读的消息个数:" + json);
                getUnReadCountResponse = gson.fromJson(json, GetUnReadCountResponse.class);
                if (getUnReadCountResponse.getCode() == 0) {
                    int count = getUnReadCountResponse.getData();
                    if (count == 0) {
                        rl_message_num.setVisibility(View.GONE);
                    } else {
                        rl_message_num.setVisibility(View.VISIBLE);
                        tv_message_num.setText(String.valueOf(count));
                    }

                } else {
                    DialogUtils.showAlertDialog(mContext, getUnReadCountResponse.getMsg());
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);
            }
        });
    }

    /**
     * 获得当天年月日
     *
     * @return
     */
    @NonNull
    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            pWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_TakePicture: {
                    timepath = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    MainActivity.timepath = timepath;
                    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                        File file = new File(dir, timepath + ".jpg");
                        UIUtils.startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                                MediaStore.EXTRA_OUTPUT, Uri.fromFile(file)), PHOTO_GRAPH);
                    }
                    break;
                }
                case R.id.btn_Phone: {
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            IMAGE_UNSPECIFIED);
                    UIUtils.startActivityForResult(intent, PHOTO_ZOOM);
                    break;
                }
                case R.id.btn_cancel: {
                    pWindow.dismiss();
                    break;
                }
                default:
                    break;
            }

        }

    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_myself_img: {
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(pr_normal,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                break;
            }
            case R.id.rl_order_management: {
                Intent intent = new Intent(mContext, OrderManagementActivity.class);
                intent.putExtra("store_id", findStoreDescByUsridResponse.store_info.store_id);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_help_center: {
                Intent intent = new Intent(mContext, HelpCenterActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.tv_telephone: {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                //url:统一资源定位符
                //uri:统一资源标示符（更广）
                intent.setData(Uri.parse("tel:4006626985"));
                //开启系统拨号器
                UIUtils.startActivity(intent);
                break;
            }
            case R.id.tv_tell_two:
            case R.id.tv_tell: {
                tellCallDialog = DialogUtils.showTellCallDialog(mContext, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.tv_cancle:
                                tellCallDialog.dismiss();
                                break;
                            case R.id.tv_ensure:
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_CALL);
                                //url:统一资源定位符
                                //uri:统一资源标示符（更广）
                                intent.setData(Uri.parse("tel:4006626985"));
                                //开启系统拨号器
                                UIUtils.startActivity(intent);
                                break;
                        }
                    }
                });

                break;
            }
            case R.id.rl_user_setting: {
                Intent intent = new Intent(mContext, UserSettingActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
//            case R.id.tv_cancle:
//                alertDialog.dismiss();
//                break;
//            case R.id.tv_ensure: {
//                alertDialog.dismiss();
//                if (TextUtils.isEmpty(token)) {
//                    logout();
//                } else {
//                    logoutRequest();
//                }
//                break;
//            }

            case R.id.rl_transaction_details: {
                Intent intent = new Intent(mContext, TransactionDetailsActivity.class);
                intent.putExtra("store_id", findStoreDescByUsridResponse.store_info.store_id);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_withdrawal: {
                if (TextUtils.isEmpty(findStoreDescByUsridResponse.bank_card_info.bank_card_number) && TextUtils.isEmpty(findStoreDescByUsridResponse.bank_card_info.bank_name) && TextUtils.isEmpty(findStoreDescByUsridResponse.bank_card_info.bank_username)) {
                    Intent intent = new Intent(mContext, AccountManagementActivity.class);
                    intent.putExtra("is_set_withdraw_pwd", 0);
                    intent.putExtra("store_account", findStoreDescByUsridResponse.store_account);
                    intent.putExtra("store_id", findStoreDescByUsridResponse.store_info.store_id);
                    intent.putExtra("mobile_phone", SharedPrefrenceUtils.getString(mContext, "mobile_phone"));
                    UIUtils.startActivityForResultNextAnim(intent, 7);
                } else {
                    int is_set_withdraw_pwd = findStoreDescByUsridResponse.store_info.is_set_withdraw_pwd;
                    if(is_set_withdraw_pwd == 0){
                        Intent intent = new Intent(mContext, SetWithdrawalPasswordActivity.class);
                        intent.putExtra("type",1);
                        intent.putExtra("branch_name", findStoreDescByUsridResponse.bank_card_info.branch_name);
                        intent.putExtra("bank_username", findStoreDescByUsridResponse.bank_card_info.bank_username);
                        intent.putExtra("bank_name", findStoreDescByUsridResponse.bank_card_info.bank_name);
                        intent.putExtra("bank_card_number", findStoreDescByUsridResponse.bank_card_info.bank_card_number);
                        intent.putExtra("store_account", findStoreDescByUsridResponse.store_account);
                        intent.putExtra("store_id", findStoreDescByUsridResponse.store_info.store_id);
                        intent.putExtra("mobile_phone", SharedPrefrenceUtils.getString(mContext, "mobile_phone"));
                        intent.putExtra("is_set_withdraw_pwd", findStoreDescByUsridResponse.store_info.is_set_withdraw_pwd);
                        UIUtils.startActivityForResultNextAnim(intent, 7);
                    }else {
                        Intent intent = new Intent(mContext, WithdrawalActivity.class);
                        intent.putExtra("branch_name", findStoreDescByUsridResponse.bank_card_info.branch_name);
                        intent.putExtra("bank_username", findStoreDescByUsridResponse.bank_card_info.bank_username);
                        intent.putExtra("bank_name", findStoreDescByUsridResponse.bank_card_info.bank_name);
                        intent.putExtra("bank_card_number", findStoreDescByUsridResponse.bank_card_info.bank_card_number);
                        intent.putExtra("store_account", findStoreDescByUsridResponse.store_account);
                        intent.putExtra("store_id", findStoreDescByUsridResponse.store_info.store_id);
                        intent.putExtra("mobile_phone", SharedPrefrenceUtils.getString(mContext, "mobile_phone"));
                        intent.putExtra("is_set_withdraw_pwd", findStoreDescByUsridResponse.store_info.is_set_withdraw_pwd);
                        UIUtils.startActivityForResultNextAnim(intent, 7);
                    }

                }
                break;
            }
            case R.id.btn_again_request: {
                Intent intent = new Intent(mContext, ApplicationActivity.class);
                UIUtils.startActivityForResultNextAnim(intent, 7);
                break;
            }
            case R.id.btn_request: {
                Intent intent = new Intent(mContext, ApplicationActivity.class);
                UIUtils.startActivityForResultNextAnim(intent, 7);
                break;
            }
            case R.id.tv_normal_user: {
                userId = SharedPrefrenceUtils.getString(mContext, "usrid");
                is_normal = true;
                if (!TextUtils.isEmpty(userId)) {
                    runAsyncTask();
                }
                rl_disable.setVisibility(View.GONE);
                rl_fail.setVisibility(View.GONE);
                rl_commit_success.setVisibility(View.GONE);
                pr_head.setBackground(UIUtils.getResources().getDrawable(R.mipmap.head_status));
                tv_normal_user.setTextColor(UIUtils.getResources().getColor(R.color.tab_background));
                tv_shop_user.setTextColor(UIUtils.getResources().getColor(R.color.text_color_black));
                pr_normal.setVisibility(View.VISIBLE);
                rl_shop.setVisibility(View.GONE);
                pr_shoper.setVisibility(View.GONE);

                break;
            }
            case R.id.tv_shop_user: {
                userId = SharedPrefrenceUtils.getString(mContext, "usrid");
                is_normal = false;
                isClickShop = true;
                if(!TextUtils.isEmpty(userId)){
                    runFindShop();
                }
                break;
            }
            case R.id.rl_installment: {
                Intent intent = new Intent(mContext, InstallmentRecordActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_authentication: {
                Intent intent = new Intent(mContext, MyAuthenticationActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.btn_agree:
                if (articleDialog != null) {
                    articleDialog.dismiss();
                }
                break;
            case R.id.tv_article:
                if (articleDialog != null) {
                    articleDialog.dismiss();
                }
                Intent intent = new Intent(mContext, XieYiWebActivity.class);
                intent.putExtra("article_id", Constants.ShowArtcicle);
                UIUtils.startActivityNextAnim(intent);
                break;
            case R.id.rl_use_credit: //申请额度/使用额度
                userId = SharedPrefrenceUtils.getString(mContext, "usrid");
                is_normal = true;
                if (!TextUtils.isEmpty(userId)) {
                    isQuota = true;
                    runAsyncTask();
                }


                break;
            case R.id.rl_initiating_order: //发起订单
                getQuotaOrderList();
                break;
            case R.id.rl_shop_setting:
                Intent intent2 = new Intent(mContext, ShopSettingActivity.class);
                intent2.putExtra("findStoreDescByUsridResponse", findStoreDescByUsridResponse);
                UIUtils.startActivityNextAnim(intent2);
//                UIUtils.startActivityForResult(intent2,7);
                break;
            case R.id.rl_personal_message:
                Intent intent3 = new Intent(mContext, MyMessageActivity.class);
                intent3.putExtra("head_portrait_pic",head_portrait_pic);
                intent3.putExtra("nick_name", nick_name);
                UIUtils.startActivityNextAnim(intent3);
                break;
            case R.id.rl_message:  //我的消息
                if(is_normal){
                    Intent intent4 = new Intent(mContext, MyInformationListActivity.class);
                    UIUtils.startActivityNextAnim(intent4);
                }
                break;
        }
    }

    /**
     * 退出登录后设置
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void logout() {
        is_normal = true;
        rl_disable.setVisibility(View.GONE);
        rl_fail.setVisibility(View.GONE);
        rl_commit_success.setVisibility(View.GONE);
        pr_head.setBackground(UIUtils.getResources().getDrawable(R.mipmap.head_status));
        tv_normal_user.setTextColor(UIUtils.getResources().getColor(R.color.tab_background));
        tv_shop_user.setTextColor(UIUtils.getResources().getColor(R.color.text_color_black));
        pr_normal.setVisibility(View.VISIBLE);
        rl_shop.setVisibility(View.GONE);
        pr_shoper.setVisibility(View.GONE);

        SharedPrefrenceUtils.setBoolean(mContext,
                "isLogin", false);
        SharedPrefrenceUtils.setString(mContext, "usrid", "");
        SharedPrefrenceUtils.setString(mContext, "mobile_phone", "");
        SharedPrefrenceUtils.setString(mContext, "token", "");
        ctf.setChecked(2);
        ctf.mCurrentIndex = 2;
        ctf.switchCurrentPage();
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, "a_"));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    LogUtils.e(logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    LogUtils.e(logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    LogUtils.e(logs);
            }
//            UIUtils.showToastCenter(mContext,logs);
        }
    };

    /**
     * 显示商家承诺书
     */
    private void showArticle() {
//        String cDate = getCurrentDate();
//        String sDate = SharedPrefrenceUtils.getString(mContext, "show_article_date");
//        LogUtils.e("cDate:" + cDate);
//        if(!TextUtils.isEmpty(sDate)){
//            LogUtils.e("sDate:"+sDate);
//        }
//        if(TextUtils.isEmpty(sDate) || !cDate.equals(sDate)){
//            getArticleById(Constants.ShowArtcicle);
//        }

        if(!(articleDialog != null && articleDialog.isShowing())){
            articleDialog = DialogUtils.showArticleMessageDialog(mContext, null, TabMyselfPager.this);
        }

    }

    public void logoutRequest() {
        loadingDialog.show();
        GetUsrInfRequest getUsrInfRequest = new GetUsrInfRequest();
        url = Constants.FENGKONGSERVER_URL + "credit_money_background/user/applogout.do";
        getUsrInfRequest.map.put("token", token);
        LogUtils.e("token:" + token);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, getUsrInfRequest.map, new MyVolley.VolleyCallback() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                LogUtils.e("退出登录:" + json);
                FkBaseResponse fkBaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkBaseResponse.getCode() == 0) {
                    logout();

                } else {
                    DialogUtils.showAlertDialog(mContext, fkBaseResponse.getMsg());
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);
            }
        });
    }

    public void runAsyncTask() {
        loadingDialog.show();
        GetUsrInfProtocol getUsrInfProtocol = new GetUsrInfProtocol();
        GetUsrInfRequest getUsrInfRequest = new GetUsrInfRequest();
        url = Constants.FENGKONGSERVER_URL + getUsrInfProtocol.getApiFun();
        getUsrInfRequest.map.put("usrid", userId);
        LogUtils.e("userId:" + userId);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, getUsrInfRequest.map, new MyVolley.VolleyCallback() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                LogUtils.e("获取商户信息:" + json);
                getUsrInfResponse = gson.fromJson(json, GetUsrInfResponse.class);
                if (getUsrInfResponse.code.equals("0")) {
                    isLoaded = true;
                    nick_name = getUsrInfResponse.return_param.nick_name;
                    if(TextUtils.isEmpty(nick_name)){
                        String tell = SharedPrefrenceUtils.getString(mContext, "mobile_phone");
                        tell = tell.substring(0, 3) + "****" + tell.substring(7);
                        tv_user_tell.setText(tell);
                    }else {
                        SharedPrefrenceUtils.setString(mContext,"nick_name",nick_name);
                        tv_user_tell.setText(nick_name);
                    }

                    String debt = df.format(getUsrInfResponse.return_param.borrow_amount);
                    tv_debt.setText(debt);
                    if (!TextUtils.isEmpty(getUsrInfResponse.return_param.shouldreturn_amount_1)) {
                        tv_reimbursement.setText(getUsrInfResponse.return_param.shouldreturn_amount_1);
                    }
                    head_portrait_pic = getUsrInfResponse.return_param.head_portrait_pic;

                    if (!TextUtils.isEmpty(head_portrait_pic)) {
                        ImageLoader.getInstance().displayImage(head_portrait_pic, iv_myself_img, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {
                                LogUtils.e("错误空");
                                iv_myself_img.setImageResource(R.mipmap.img_avatar);
                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }
                        });
                    } else {
                        iv_myself_img.setImageResource(R.mipmap.img_avatar);
                    }

                    total_credit_amount = getUsrInfResponse.return_param.total_credit_amount;
                    returned_amount = getUsrInfResponse.return_param.returned_amount;
                    unreturned_amount = getUsrInfResponse.return_param.unreturned_amount;
                    used_credit = getUsrInfResponse.return_param.loan_used;
                    limit_validity_time = getUsrInfResponse.return_param.limit_validity_time;
                    loan_unused = getUsrInfResponse.return_param.loan_unused;
                    freeze_loan_quota = getUsrInfResponse.return_param.freeze_loan_quota;
                    loan_isexpired = getUsrInfResponse.return_param.loan_isexpired;
                    int check_flag = getUsrInfResponse.return_param.check_flag;
                    LogUtils.e("check_flag:" + getUsrInfResponse.return_param.check_flag);
                    String reimbursement_date = getUsrInfResponse.return_param.lateLyDay;
                    if (!TextUtils.isEmpty(reimbursement_date)) {
                        tv_reimbursement_date.setText("最近还款:" + reimbursement_date);
                        tv_reimbursement_date.setVisibility(View.VISIBLE);
                    } else {
                        tv_reimbursement_date.setVisibility(View.GONE);
                    }
                    tv_available_credit.setText(df.format(loan_unused));
                    if (check_flag == 0 || check_flag == 2 || check_flag == 4 || check_flag == 7) {
                        isHaveCredit = false;
                        tv_use_credit.setText("申请额度");
                        rl_use_credit.setEnabled(true);
                        tv_use_credit_message.setText("家具厂可以申请额度");
                    }  else if (check_flag == 8) {
                        isHaveCredit = true;
                        tv_use_credit.setText("使用额度");
                        rl_use_credit.setEnabled(true);
                        tv_use_credit_message.setText("家具厂可以使用额度");
                    } else {
                        isHaveCredit = false;
                        tv_use_credit.setText("申请额度");
                        rl_use_credit.setEnabled(false);
                        tv_use_credit_message.setText("正在审核中");
                    }


                    if(!TextUtils.isEmpty(tell)){
                        getUnReadCount(tell);
                    }
                    rl_message.setVisibility(View.VISIBLE);

                    if(isQuota){
                        Intent intent1 = null;
                        if (isHaveCredit || ((check_flag == 2 || check_flag == 4 || check_flag == 7) && limit_validity_time>0)) { //审核通过  || 审核不通过，但是以前有额度
                            intent1 = new Intent(mContext, UseQuotaActivity.class);//使用额度
                            intent1.putExtra("total_credit_amount", total_credit_amount);
                            intent1.putExtra("returned_amount", returned_amount);
                            intent1.putExtra("unreturned_amount", unreturned_amount);
                            intent1.putExtra("used_credit", used_credit);
                            intent1.putExtra("loan_unused", loan_unused);
                            intent1.putExtra("freeze_loan_quota", freeze_loan_quota);
                            intent1.putExtra("limit_validity_time", limit_validity_time);
                            intent1.putExtra("loan_isexpired", loan_isexpired);
                        } else {
                            intent1 = new Intent(mContext, ApplicationLimitActivity.class);  //申请额度
                        }
                        UIUtils.startActivityNextAnim(intent1);
                        isQuota = false;
                    }
                } else if (getUsrInfResponse.code.equals("2")) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                } else {
                    DialogUtils.showAlertDialog(mContext,
                            getUsrInfResponse.msg);

                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);
            }
        });
    }

    /**
     * 获取额度订单列表
     */
    public void getQuotaOrderList() {
        loadingDialog.show();
        GetUsrInfProtocol getUsrInfProtocol = new GetUsrInfProtocol();
        GetUsrInfRequest getUsrInfRequest = new GetUsrInfRequest();
        url = Constants.FENGKONGSERVER_URL + getUsrInfProtocol.getApiFun();
        getUsrInfRequest.map.put("usrid", userId);
        getUsrInfRequest.map.put("order_from", "1");
        LogUtils.e("userId:" + userId);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, getUsrInfRequest.map, new MyVolley.VolleyCallback() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                LogUtils.e("获取额度订单列表:" + json);
                getUsrInfResponse = gson.fromJson(json, GetUsrInfResponse.class);
                if (getUsrInfResponse.code.equals("0")) {
                    orderBeanList = getUsrInfResponse.return_param.order_list;

                    SharedPrefrenceUtils.setBoolean(mContext, userId+"has_initiating_ordered",true);
                    iv_new.setVisibility(View.GONE);

                    Intent intent = null;
                    if (orderBeanList != null && orderBeanList.size() > 0) {
                        intent = new Intent(mContext, QuotaOrderListActivity.class);   //订单列表
                        intent.putExtra("orderBeanListStr", new Gson().toJson(orderBeanList));
                    } else {
                        intent = new Intent(mContext, CreateQuotaOrderActivity.class); //创建订单
                    }
                    UIUtils.startActivityNextAnim(intent);


                } else if (getUsrInfResponse.code.equals("2")) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                } else {
                    DialogUtils.showAlertDialog(mContext,
                            getUsrInfResponse.msg);

                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void runFindShop() {
        pr_head.setBackground(UIUtils.getResources().getDrawable(R.mipmap.head_status2));
        tv_shop_user.setTextColor(UIUtils.getResources().getColor(R.color.tab_background));
        tv_normal_user.setTextColor(UIUtils.getResources().getColor(R.color.text_color_black));
        loadingDialog.show();
        FindStoreDescByUsridProtocol findStoreDescByUsridProtocol = new FindStoreDescByUsridProtocol();
        FindStoreDescByUsridRequest findStoreDescByUsridRequest = new FindStoreDescByUsridRequest();
        url = Constants.XINYONGSERVER_URL + findStoreDescByUsridProtocol.getApiFun();
        findStoreDescByUsridRequest.map.put("usr_id", userId);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, findStoreDescByUsridRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("findStoreDescByUsridResponse:" + json.toString());
                Gson gson = new Gson();
                findStoreDescByUsridResponse = gson.fromJson(json, FindStoreDescByUsridResponse.class);
                LogUtils.e("findStoreDescByUsridResponse:" + findStoreDescByUsridResponse.toString());
                if (findStoreDescByUsridResponse.code == -4) {
                    pr_normal.setVisibility(View.GONE);
                    rl_shop.setVisibility(View.VISIBLE);
                    pr_shoper.setVisibility(View.GONE);
                    rl_fail.setVisibility(View.GONE);
                    rl_commit_success.setVisibility(View.GONE);
                    rl_disable.setVisibility(View.GONE);

                } else if (findStoreDescByUsridResponse.code == -2) {
                    pr_normal.setVisibility(View.GONE);
                    rl_shop.setVisibility(View.GONE);
                    pr_shoper.setVisibility(View.GONE);
                    rl_fail.setVisibility(View.GONE);
                    rl_commit_success.setVisibility(View.VISIBLE);
                    rl_disable.setVisibility(View.GONE);
                } else if (findStoreDescByUsridResponse.code == 0) {
                    isLoaded = true;
                    String balance = df.format(findStoreDescByUsridResponse.store_account);
                    tv_balance.setText(balance);
                    String freeze_money = df.format(findStoreDescByUsridResponse.store_info.freeze_account);
                    tv_freeze_money.setText(freeze_money);
                    String totla_balance = df.format(findStoreDescByUsridResponse.store_account + findStoreDescByUsridResponse.store_info.freeze_account);
                    tv_total_balance.setText(totla_balance);
                    pr_normal.setVisibility(View.GONE);
                    rl_shop.setVisibility(View.GONE);
                    pr_shoper.setVisibility(View.VISIBLE);
                    rl_commit_success.setVisibility(View.GONE);
                    rl_fail.setVisibility(View.GONE);
                    rl_disable.setVisibility(View.GONE);

                    if (findStoreDescByUsridResponse.store_info.store_type == Constants.WOODSTORETYPE) {
                        rl_initiating_order.setVisibility(View.VISIBLE);
                        line3.setVisibility(View.VISIBLE);
                    } else {
                        rl_initiating_order.setVisibility(View.GONE);
                        line3.setVisibility(View.GONE);
                    }

                    if (isClickShop) {
                        isClickShop = false;
                        showArticle();
                    }



                } else if (findStoreDescByUsridResponse.code == -3) {
                    pr_normal.setVisibility(View.GONE);
                    rl_shop.setVisibility(View.GONE);
                    pr_shoper.setVisibility(View.GONE);
                    rl_commit_success.setVisibility(View.GONE);
                    rl_fail.setVisibility(View.VISIBLE);
                    rl_disable.setVisibility(View.GONE);
//                    tv_fail_msg.setText( findStoreDescByUsridResponse.msg);
                } else if (findStoreDescByUsridResponse.code == -1) {
                    pr_normal.setVisibility(View.GONE);
                    rl_shop.setVisibility(View.GONE);
                    pr_shoper.setVisibility(View.GONE);
                    rl_commit_success.setVisibility(View.GONE);
                    rl_fail.setVisibility(View.GONE);
                    rl_disable.setVisibility(View.VISIBLE);
                } else if (findStoreDescByUsridResponse.code == 2) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }

                rl_message.setVisibility(View.GONE);

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);
            }
        });
    }

    public void Update() {
        if (!isLoaded) {
            userId = SharedPrefrenceUtils.getString(mContext, "usrid");
            if (is_normal) {
                if (!TextUtils.isEmpty(userId)) {
                    runAsyncTask();
                }

            } else {
                if (!TextUtils.isEmpty(userId)) {
                    runFindShop();
                }

            }
        }
    }

    public void setLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }
}