package com.tesu.creditgold.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.bean.AmountDetailBean;
import com.tesu.creditgold.util.UIUtils;

import java.text.DecimalFormat;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 账户管理
 */
public class AmountTranscationDetailInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;
    private AmountDetailBean amountDetailBean;
    private TextView tv_amount_type;  //入账还是还款
    private TextView tv_amount;  //金额
    private TextView tv_transaction_type;  //交易类型
    private TextView tv_time;  //时间
    private TextView tv_remark;  //备注
    private DecimalFormat df = new DecimalFormat("###0.00");
    private TextView tv_title;
    private TextView tv_type;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_amount_transcation_detail_info, null);
        setContentView(rootView);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_amount_type = (TextView) rootView.findViewById(R.id.tv_amount_type);
        tv_amount = (TextView) rootView.findViewById(R.id.tv_amount);
        tv_transaction_type = (TextView) rootView.findViewById(R.id.tv_transaction_type);
        tv_time = (TextView) rootView.findViewById(R.id.tv_time);
        tv_remark = (TextView) rootView.findViewById(R.id.tv_remark);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_type = (TextView) rootView.findViewById(R.id.tv_type);
        initData();
        return null;
    }


    public void initData() {
        Intent intent = getIntent();
        amountDetailBean = intent.getParcelableExtra("amountDetailBean");
        if(amountDetailBean != null){
            int freezeMode =amountDetailBean.getFreeze_mode();
            String amount = df.format(amountDetailBean.getTransaction_amount());
            switch (amountDetailBean.getTransaction_type()){
                case 1:
                    tv_transaction_type.setText("授信金额");
                    tv_amount.setText("+ "+amount);
                    tv_amount.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                    tv_amount_type.setText("入账金额");
                    if(!TextUtils.isEmpty(amountDetailBean.getRemark())){
                        tv_remark.setText(amountDetailBean.getRemark());
                    }
                    tv_type.setText("备注");
                    tv_title.setText("授信金额");
                    break;
                case 2:
                    if(freezeMode == 2){
                        tv_transaction_type.setText("解冻");
                        tv_amount_type.setText("解冻金额");
                        tv_amount.setText("+ "+amount);
                        tv_amount.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                    }else {
                        tv_transaction_type.setText("支出");
                        tv_amount_type.setText("出账金额");
                        tv_amount.setText("- "+amount);
                        tv_amount.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                    }
                    tv_type.setText("订单号");
                    tv_title.setText("已用额度");
                    if(!TextUtils.isEmpty(amountDetailBean.getOrder_sn())){
                        tv_remark.setText(amountDetailBean.getOrder_sn());
                    }
                    break;
                case 3:
                    tv_transaction_type.setText("还款");
                    tv_amount_type.setText("还款金额");
                    tv_amount.setText("+ "+amount);
                    tv_amount.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                    tv_type.setText("订单号");
                    tv_title.setText("已还金额");
                    if(!TextUtils.isEmpty(amountDetailBean.getOrder_sn())){
                        tv_remark.setText(amountDetailBean.getOrder_sn());
                    }
                    break;
                case 4:
                    tv_transaction_type.setText("额度过期");
                    tv_amount.setText("- "+amount);
                    tv_amount.setTextColor(UIUtils.getColor(R.color.text_color_black));
                    break;
            }

            if(!TextUtils.isEmpty(amountDetailBean.getTransaction_time_format())){
                tv_time.setText(amountDetailBean.getTransaction_time_format());
            }

        }
        tv_top_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
        }
    }

}
