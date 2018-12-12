package com.tesu.creditgold.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.fragment.ControlTabFragment;
import com.tesu.creditgold.request.GetUsrInfRequest;
import com.tesu.creditgold.response.FindStoreDescByUsridResponse;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;

import java.io.Serializable;
import java.util.List;

public class ShopSettingActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_top_back;
    private View rootView;
    private RelativeLayout rl_account_management;
    private RelativeLayout rl_code;
    private RelativeLayout rl_basic_information;
    private TextView tv_out_shoper;
    private AlertDialog alertDialog;
    private String token;
    private Dialog loadingDialog;
    private ControlTabFragment ctf;
    private String url;
    private FindStoreDescByUsridResponse findStoreDescByUsridResponse;
    private TextView tv_show_store_rate;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_shop_setting, null);
        setContentView(rootView);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        rl_code = (RelativeLayout) rootView.findViewById(R.id.rl_code);
        rl_account_management = (RelativeLayout) rootView.findViewById(R.id.rl_account_management);
        rl_basic_information = (RelativeLayout) rootView.findViewById(R.id.rl_basic_information);
        tv_out_shoper = (TextView) rootView.findViewById(R.id.tv_out_shoper);
        tv_show_store_rate = (TextView) rootView.findViewById(R.id.tv_show_store_rate);

        tv_top_back.setOnClickListener(this);
        rl_basic_information.setOnClickListener(this);
        rl_account_management.setOnClickListener(this);
        rl_code.setOnClickListener(this);
        tv_out_shoper.setOnClickListener(this);

        if (ctf == null) {
            ctf = MainActivity.getCtf();
        }
        loadingDialog = DialogUtils.createLoadDialog(this, true);
        token = SharedPrefrenceUtils.getString(this, "token");

        Intent intent = getIntent();
        findStoreDescByUsridResponse = (FindStoreDescByUsridResponse) intent.getSerializableExtra("findStoreDescByUsridResponse");

        List<FindStoreDescByUsridResponse.StoreRate> storeIfoList = findStoreDescByUsridResponse.store_rate_list;
        String storeRate = "";
        if (storeIfoList != null) {
            for (FindStoreDescByUsridResponse.StoreRate storeRate1 : storeIfoList) {
                storeRate += storeRate1.period + "期" + storeRate1.store_charge_rate + "%   ";
            }
        }
        tv_show_store_rate.setText(storeRate);
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_basic_information: {
                Intent intent = new Intent(ShopSettingActivity.this, DataManagementActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("store_info", (Serializable) findStoreDescByUsridResponse.store_info);
                intent.putExtras(bundle);
                UIUtils.startActivityForResultNextAnim(intent, 7);
                setFinish();
                break;
            }
            case R.id.rl_account_management: {
                Intent intent = new Intent(ShopSettingActivity.this, AccountManagementActivity.class);
                if (findStoreDescByUsridResponse != null) {
                    intent.putExtra("is_set_withdraw_pwd", findStoreDescByUsridResponse.store_info.is_set_withdraw_pwd);
                    intent.putExtra("bank_username", findStoreDescByUsridResponse.bank_card_info.bank_username);
                    intent.putExtra("bank_card_number", findStoreDescByUsridResponse.bank_card_info.bank_card_number);
                    intent.putExtra("bank_name", findStoreDescByUsridResponse.bank_card_info.bank_name);
                    intent.putExtra("branch_name", findStoreDescByUsridResponse.bank_card_info.branch_name);
                }
                UIUtils.startActivityForResultNextAnim(intent, 7);
                setFinish();
                break;
            }
            case R.id.rl_code: {
                Intent intent = new Intent(ShopSettingActivity.this, CodeActivity.class);
                intent.putExtra("store_name", findStoreDescByUsridResponse.store_info.store_name);
                intent.putExtra("qr_pic", findStoreDescByUsridResponse.qr_pic);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.tv_out_shoper:

                alertDialog = DialogUtils.showAlertDoubleBtnDialog(ShopSettingActivity.this, "确定要退出本账号？", this);
                break;

            case R.id.tv_cancle:
                alertDialog.dismiss();
                break;
            case R.id.tv_ensure: {
                alertDialog.dismiss();
                if (TextUtils.isEmpty(token)) {
                    logout();
                } else {
                    logoutRequest();
                }
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.tv_top_back:
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
        }
    }

    /**
     * 退出登录后设置
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void logout() {
//        is_normal = true;
//        rl_disable.setVisibility(View.GONE);
//        rl_fail.setVisibility(View.GONE);
//        rl_commit_success.setVisibility(View.GONE);
//        pr_head.setBackground(UIUtils.getResources().getDrawable(R.mipmap.head_status));
//        tv_normal_user.setTextColor(UIUtils.getResources().getColor(R.color.tab_background));
//        tv_shop_user.setTextColor(UIUtils.getResources().getColor(R.color.text_color_black));
//        pr_normal.setVisibility(View.VISIBLE);
//        rl_shop.setVisibility(View.GONE);
//        pr_shoper.setVisibility(View.GONE);

//        SharedPrefrenceUtils.setBoolean(this,
//                "isLogin", false);
//        SharedPrefrenceUtils.setString(this, "usrid", "");
//        SharedPrefrenceUtils.setString(this, "mobile_phone", "");
//        SharedPrefrenceUtils.setString(this, "token", "");
//        ctf.setChecked(2);
//        ctf.mCurrentIndex = 2;
//        ctf.switchCurrentPage();

        ctf.getTabMyselfbyPager().logout();
    }

    public void logoutRequest() {
        loadingDialog.show();
        GetUsrInfRequest getUsrInfRequest = new GetUsrInfRequest();
        url = Constants.XINYONGSERVER_URL + "credit_money_background/user/applogout.do";
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
                    DialogUtils.showAlertDialog(ShopSettingActivity.this, fkBaseResponse.getMsg());
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(ShopSettingActivity.this, error);
            }
        });
    }

}
