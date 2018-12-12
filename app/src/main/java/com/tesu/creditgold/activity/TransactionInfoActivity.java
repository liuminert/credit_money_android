package com.tesu.creditgold.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.DetailsAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.response.FindTraRecordListResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 交易明细
 */
public class TransactionInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;
    private FindTraRecordListResponse.TraRecord traRecord;
    private TextView tv_money;
    private TextView tv_type;
    private TextView tv_time;
    private TextView tv_order_num;
    private TextView tv_info;
    private TextView tv_title;
    private TextView tv_show_type;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_transaction_info, null);
        setContentView(rootView);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_money = (TextView) rootView.findViewById(R.id.tv_money);
        tv_type = (TextView) rootView.findViewById(R.id.tv_type);
        tv_time = (TextView) rootView.findViewById(R.id.tv_time);
        tv_order_num = (TextView) rootView.findViewById(R.id.tv_order_num);
        tv_info = (TextView) rootView.findViewById(R.id.tv_info);
        tv_title= (TextView) rootView.findViewById(R.id.tv_title);
        tv_show_type= (TextView) rootView.findViewById(R.id.tv_show_type);
        initData();
        return null;
    }


    public void initData() {
        Intent intent=getIntent();
        traRecord = (FindTraRecordListResponse.TraRecord) intent.getSerializableExtra("traRecod");
        if(traRecord != null){
            if(!TextUtils.isEmpty(traRecord.amount)){
                tv_money.setText("- "+(traRecord.amount).substring(1));
            }
            switch (traRecord.type){
                case 1:
                    tv_show_type.setText("入账金额");
                    tv_type.setText("收入");
                    tv_title.setText("付款方");
                    tv_info.setText(traRecord.remark);
                    tv_money.setText("+ "+traRecord.amount);
                    break;
                case 2:
                    tv_show_type.setText("出账金额");
                    tv_type.setText("支出");
                    tv_title.setText("提现银行卡");
                    if(traRecord.remark.length()>5) {
                        tv_info.setText(traRecord.remark.substring(0, 4) + "*******" + traRecord.remark.substring(traRecord.remark.length() - 4));
                    }
                    else{
                        tv_info.setText(traRecord.remark);
                    }
                    break;
                case 3:
                    tv_show_type.setText("出账金额");
                    tv_type.setText("支出");
                    tv_title.setText("备注");
                    tv_info.setText(traRecord.remark);
                    break;
                case 100:
                    tv_show_type.setText("出账金额:");
                    tv_type.setText("支出");
                    tv_title.setText("备注");
                    tv_info.setText(traRecord.remark);
                    break;
            }

            if(!TextUtils.isEmpty(traRecord.time)){
                tv_time.setText(traRecord.time);
            }
            if(!TextUtils.isEmpty(traRecord.trade_no)){
                tv_order_num.setText(traRecord.trade_no);
            }
        }
        tv_top_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_withrawal:{
//                Intent intent=new Intent(WithdrawalActivity.this,IdentityAuthenticationActivity.class);
//                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.tv_top_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
        }
    }
}
