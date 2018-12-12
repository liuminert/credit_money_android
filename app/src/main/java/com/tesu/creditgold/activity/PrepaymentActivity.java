package com.tesu.creditgold.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.GetStoreInfoResponse;
import com.tesu.creditgold.fragment.ControlTabFragment;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.UIUtils;

import java.util.HashMap;

public class PrepaymentActivity extends BaseActivity implements View.OnClickListener{
    private View rootView;
    private TextView tv_top_back;
    private ControlTabFragment ctf;
    private Dialog loadingDialog;
    private TextView tv_periods;
    private TextView tv_amount;
    private TextView tv_bank_card;
    private Button btn_submit;
    private String bank_id;
    private int cur_period;
    private String repay_money;
    private String yxt_repayment_id;
    private Dialog mDialog;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_prepayment, null);
        setContentView(rootView);

        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_periods = (TextView) rootView.findViewById(R.id.tv_periods);
        tv_amount = (TextView) rootView.findViewById(R.id.tv_amount);
        tv_bank_card = (TextView) rootView.findViewById(R.id.tv_bank_card);
        btn_submit = (Button) rootView.findViewById(R.id.btn_submit);

        loadingDialog = DialogUtils.createLoadDialog(PrepaymentActivity.this, false);
        if (ctf == null) {
            ctf = MainActivity.getCtf();
        }

        tv_top_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        Intent intent = getIntent();
        bank_id = intent.getStringExtra("bank_id");
        cur_period = intent.getIntExtra("cur_period", 0);
        repay_money = intent.getStringExtra("repay_money");
        yxt_repayment_id = intent.getStringExtra("yxt_repayment_id");

        if(!TextUtils.isEmpty(bank_id)){
            tv_bank_card.setText("请确保还款账户（银行卡尾号"+bank_id+"）余额充足。");
        }
        if(!TextUtils.isEmpty(repay_money)){
            tv_amount.setText("还款金额："+repay_money);
        }
        if(cur_period != 0){
            tv_periods.setText("申请期数："+cur_period+"期");
        }


        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_top_back:
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.btn_submit:
                if(!TextUtils.isEmpty(yxt_repayment_id)){
                    preRepayment();
                }
                break;
        }
    }

    private void preRepayment() {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL+"tsfkxt/user/preRepayment.do";
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("yxt_repayment_id", yxt_repayment_id);
        LogUtils.e("立即还款params:"+params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("立即还款:" + json.toString());
                FkBaseResponse fkBaseResponse = new Gson().fromJson(json, FkBaseResponse.class);
                if (fkBaseResponse.getCode() == 0) {
                    PrepaymentActivity.this.finish();
                } else {
                    if(!isFinishing()){
                        mDialog = DialogUtils.showAlertDialog(PrepaymentActivity.this, fkBaseResponse.getMsg(), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()){
                                    case R.id.tv_roger:
                                        mDialog.dismiss();
                                        PrepaymentActivity.this.finish();
                                        break;
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    mDialog = DialogUtils.showAlertDialog(PrepaymentActivity.this, error, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()){
                                case R.id.tv_roger:
                                    mDialog.dismiss();
                                    PrepaymentActivity.this.finish();
                                    break;
                            }
                        }
                    });
                }
            }
        });
    }

}

