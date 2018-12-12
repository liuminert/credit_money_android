package com.tesu.creditgold.widget;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.RecordInfoAdapter;
import com.tesu.creditgold.adapter.RecordInfoNewAdapter;
import com.tesu.creditgold.response.GetRepaymentListResponse;

import java.util.List;
import java.util.Random;

/**
 * 生成预订单对话框
 *
 * @version V1.0
 * @update 2014-10-27 下午3:59:49
 */
public class RecordInfoDialog extends Dialog implements
        View.OnClickListener {
    /**
     * 获取的验证码
     */
    private String getcode;
    /**
     * 上下文
     */
    private Context context = null;
    /**
     * 关闭按钮
     */
    private RelativeLayout rl_close;
    private  List<GetRepaymentListResponse.RepaymentBean> list;
    private ListView lv_record_info;
    private RecordInfoAdapter recordInfoAdapter;
    private RecordInfoNewAdapter recordInfoNewAdapter;
    //总利息
    private String interest_total;
    private TextView tv_order_id;
    private TextView tv_interest;
    private String order_sn;
    private TextView tv_top_back;

    public RecordInfoDialog(Context context, List<GetRepaymentListResponse.RepaymentBean> list,String interest_total,String order_sn) {
        super(context, R.style.MyDialog);
        // 设置点击屏幕Dialog不消失
        this.setCanceledOnTouchOutside(false);
        this.context = context;
        this.list=list;
        this.interest_total=interest_total;
        this.order_sn = order_sn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.record_info_dialog);

        // 初始化控件
        initView();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tv_interest=(TextView)findViewById(R.id.tv_interest);
        tv_order_id=(TextView)findViewById(R.id.tv_order_id);
        lv_record_info=(ListView)findViewById(R.id.lv_record_info);
        rl_close = (RelativeLayout) findViewById(R.id.rl_close);
        tv_top_back = (TextView) findViewById(R.id.tv_top_back);
        rl_close.setOnClickListener(this);
        tv_top_back.setOnClickListener(this);
        if(recordInfoAdapter==null){
            recordInfoAdapter=new RecordInfoAdapter(context,list);
        }
        recordInfoNewAdapter = new RecordInfoNewAdapter(context,list);
//        lv_record_info.setAdapter(recordInfoAdapter);
        lv_record_info.setAdapter(recordInfoNewAdapter);
        if(!TextUtils.isEmpty(order_sn)){
            tv_order_id.setText("交易单号:" + order_sn);
        }
        if(list.size()>0) {
            tv_interest.setText("计划每期还款: ￥" + list.get(0).repay_money);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 取消
            case R.id.rl_close: {
                this.dismiss();
                break;
            }
            case R.id.tv_top_back:
                this.dismiss();
                break;
            default:
                break;
        }

    }

}
