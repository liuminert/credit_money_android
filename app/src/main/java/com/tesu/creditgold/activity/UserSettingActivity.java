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
import com.tesu.creditgold.util.DataCleanManager;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;

import java.io.Serializable;
import java.util.List;

public class UserSettingActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_top_back;
    private View rootView;
    private RelativeLayout rl_account_management;
    private RelativeLayout rl_feedback;
    private RelativeLayout rl_clear_buffer;
    private RelativeLayout rl_about;
    private TextView tv_out_user;
    private AlertDialog alertDialog;
    private String token;
    private Dialog loadingDialog;
    private ControlTabFragment ctf;
    private String url;
    private FindStoreDescByUsridResponse findStoreDescByUsridResponse;
    private TextView tv_buffer;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_user_setting, null);
        setContentView(rootView);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        rl_feedback = (RelativeLayout) rootView.findViewById(R.id.rl_feedback);
        rl_account_management = (RelativeLayout) rootView.findViewById(R.id.rl_account_management);
        rl_clear_buffer = (RelativeLayout) rootView.findViewById(R.id.rl_clear_buffer);
        tv_out_user = (TextView) rootView.findViewById(R.id.tv_out_user);
        rl_about = (RelativeLayout) rootView.findViewById(R.id.rl_about);
        tv_buffer = (TextView) rootView.findViewById(R.id.tv_buffer);

        tv_top_back.setOnClickListener(this);
        rl_clear_buffer.setOnClickListener(this);
        rl_account_management.setOnClickListener(this);
        rl_feedback.setOnClickListener(this);
        tv_out_user.setOnClickListener(this);
        rl_about.setOnClickListener(this);

        showBuffer();

        if (ctf == null) {
            ctf = MainActivity.getCtf();
        }
        loadingDialog = DialogUtils.createLoadDialog(this, true);
        token = SharedPrefrenceUtils.getString(this, "token");

        Intent intent = getIntent();
        findStoreDescByUsridResponse = (FindStoreDescByUsridResponse) intent.getSerializableExtra("findStoreDescByUsridResponse");

        return null;
    }

    private void showBuffer() {
        try {
            String buffer = DataCleanManager.getTotalCacheSize(this);
            if(!TextUtils.isEmpty(buffer)){
                tv_buffer.setText(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_clear_buffer: {  //清除缓冲
                DataCleanManager.clearAllCache(UserSettingActivity.this);
                showBuffer();
                break;
            }
            case R.id.rl_account_management: {  //账户管理
                Intent intent = new Intent(UserSettingActivity.this,AccountManagerActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_feedback: {  //意见反馈
                Intent intent = new Intent(UserSettingActivity.this,FeedBackActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_about: //关于秒赊
                Intent intent = new Intent(UserSettingActivity.this,AboutCreditActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            case R.id.tv_out_user:

                alertDialog = DialogUtils.showAlertDoubleBtnDialog(UserSettingActivity.this, "确定要退出本账号？", this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
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
                if(loadingDialog != null && loadingDialog.isShowing()){
                    Activity activity = loadingDialog.getOwnerActivity();
                    if ( activity != null && !activity.isFinishing()) {
                        loadingDialog.dismiss();
                    }
                }
                Gson gson = new Gson();
                LogUtils.e("退出登录:" + json);
                FkBaseResponse fkBaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkBaseResponse.getCode() == 0) {
                    logout();

                } else {
                    DialogUtils.showAlertDialog(UserSettingActivity.this, fkBaseResponse.getMsg());
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                if(loadingDialog != null && loadingDialog.isShowing()){
                    Activity activity = loadingDialog.getOwnerActivity();
                    if ( activity != null && !activity.isFinishing()) {
                        loadingDialog.dismiss();
                    }
                }
                DialogUtils.showAlertDialog(UserSettingActivity.this, error);
            }
        });
    }

}
