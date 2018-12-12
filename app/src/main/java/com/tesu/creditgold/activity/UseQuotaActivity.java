package com.tesu.creditgold.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.util.Utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class UseQuotaActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_top_back;
    private View rootView;
    private TextView tv_available_quota;  //可用额度
    private TextView tv_term_of_validity;  //有效期
    private TextView tv_total_quota;  //总额度
    private TextView tv_used_quota;  //已用额度
    private Button btn_submit;  //立即使用
    private double total_credit_amount;  //累计授信额度
    private double used_credit;
    private double loan_unused;
    private double freeze_loan_quota;
    private double returned_amount;  //已还额度
    private double unreturned_amount;  //待还额度

    private String available_quota;
    private long limit_validity_time;

    private DecimalFormat df = new DecimalFormat("###0.00");
    private int loan_isexpired;  ////额度是否过期,0 没有过期，  1 过期
    private ImageView iv_transaction_details;

    private TextView tv_paid_quota;
    private TextView tv_unpaid_quota;
    private PercentRelativeLayout rl_total;  //累计总额度
    private PercentRelativeLayout rl_used;  //已用额度
    private PercentRelativeLayout rl_paid;  //已还金额
    private Button btn_update;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_use_quota, null);
        setContentView(rootView);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_available_quota = (TextView) rootView.findViewById(R.id.tv_available_quota);
        tv_term_of_validity = (TextView) rootView.findViewById(R.id.tv_term_of_validity);
        tv_total_quota = (TextView) rootView.findViewById(R.id.tv_total_quota);
        tv_used_quota = (TextView) rootView.findViewById(R.id.tv_used_quota);
        btn_submit = (Button) rootView.findViewById(R.id.btn_submit);
        iv_transaction_details = (ImageView) rootView.findViewById(R.id.iv_transaction_details);
        tv_paid_quota = (TextView) rootView.findViewById(R.id.tv_paid_quota);
        tv_unpaid_quota = (TextView) rootView.findViewById(R.id.tv_unpaid_quota);
        rl_total = (PercentRelativeLayout) rootView.findViewById(R.id.rl_total);
        rl_used = (PercentRelativeLayout) rootView.findViewById(R.id.rl_used);
        rl_paid = (PercentRelativeLayout) rootView.findViewById(R.id.rl_paid);
        btn_update = (Button) rootView.findViewById(R.id.btn_update);

        initData();

        tv_top_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        iv_transaction_details.setOnClickListener(this);
        rl_total.setOnClickListener(this);
        rl_used.setOnClickListener(this);
        rl_paid.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        return null;
    }

    private void initData() {
        Intent intent = getIntent();
        total_credit_amount = intent.getDoubleExtra("total_credit_amount", 0);
        returned_amount = intent.getDoubleExtra("returned_amount", 0);
        unreturned_amount = intent.getDoubleExtra("unreturned_amount", 0);
        used_credit = intent.getDoubleExtra("used_credit", 0);
        loan_unused = intent.getDoubleExtra("loan_unused", 0);
        freeze_loan_quota = intent.getDoubleExtra("freeze_loan_quota", 0);
        limit_validity_time = intent.getLongExtra("limit_validity_time", 0);
        loan_isexpired = intent.getIntExtra("loan_isexpired", 0);

        if(loan_isexpired == 1){
            tv_term_of_validity.setText("已失效");
            btn_update.setText("重新申请");
        }else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            tv_term_of_validity.setText("有效期至"+format.format(limit_validity_time));
            btn_update.setText("提升额度");
        }

        tv_total_quota.setText(df.format(total_credit_amount));
        tv_used_quota.setText(df.format(used_credit));
        tv_paid_quota.setText(df.format(returned_amount));
        tv_unpaid_quota.setText(df.format(unreturned_amount));

        available_quota = df.format(loan_unused);
        int size = available_quota.length();
        tv_available_quota.setText(available_quota);
        if(size -2 >0){
            tv_available_quota.setText(Utils.setFountSize(tv_available_quota,size-2,size,50));
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_top_back:
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.btn_submit:
                if(loan_isexpired == 1){
                    Intent intent = new Intent(UseQuotaActivity.this,ApplicationLimitActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                    setFinish();
                }else {
                    Intent intent = new Intent(UseQuotaActivity.this,UseQuotaFillInfomationActivity.class);
                    intent.putExtra("available_quota",available_quota);
                    UIUtils.startActivityNextAnim(intent);
                    setFinish();
                }
                break;
            case R.id.iv_transaction_details:  //交易详情
                Intent intent = new Intent(UseQuotaActivity.this,AmountTranscationDetalsActivity.class);
                intent.putExtra("isfromDetals",true);
                UIUtils.startActivityNextAnim(intent);
                break;
            case R.id.rl_total: //累计总额度
//                Intent intent1 =  new Intent(UseQuotaActivity.this,AmountTranscationDetalsActivity.class);
//                intent1.putExtra("transaction_type",1);
//                UIUtils.startActivityNextAnim(intent1);
                break;
            case R.id.rl_used: //已用额度
//                Intent intent2 =  new Intent(UseQuotaActivity.this,AmountTranscationDetalsActivity.class);
//                intent2.putExtra("transaction_type",2);
//                UIUtils.startActivityNextAnim(intent2);
                break;
            case R.id.rl_paid: //已还金额
//                Intent intent3 =  new Intent(UseQuotaActivity.this,AmountTranscationDetalsActivity.class);
//                intent3.putExtra("transaction_type",3);
//                UIUtils.startActivityNextAnim(intent3);
                break;
            case R.id.btn_update:  //提交额度
                if(freeze_loan_quota !=0){
                    UIUtils.showToastCenter(UseQuotaActivity.this,"您有未处理完的订单，暂时无法提升额度!");
                    return;
                }
                Intent intent4 = new Intent(UseQuotaActivity.this,ApplicationLimitActivity.class);
                UIUtils.startActivityNextAnim(intent4);
                setFinish();
                break;
        }

    }
}
