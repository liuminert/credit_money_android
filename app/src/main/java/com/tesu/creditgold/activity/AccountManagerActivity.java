package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.protocol.UpdateStoreInfoProtocol;
import com.tesu.creditgold.request.UpdateStoreInfoRequest;
import com.tesu.creditgold.response.UpdateStoreInfoResponse;
import com.tesu.creditgold.util.BankUtils;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 账户管理
 */
public class AccountManagerActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;

    private RelativeLayout rl_bank_management;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_account_manager, null);
        setContentView(rootView);
        rl_bank_management= (RelativeLayout) rootView.findViewById(R.id.rl_bank_management);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        initData();
        return null;
    }


    public void initData() {
        tv_top_back.setOnClickListener(this);
        rl_bank_management.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_bank_management:
                Intent intent = new Intent(AccountManagerActivity.this, RepaymentAccountActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            case R.id.tv_top_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
        }
    }

}
