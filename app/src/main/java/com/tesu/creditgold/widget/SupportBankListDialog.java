package com.tesu.creditgold.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.RecordInfoAdapter;
import com.tesu.creditgold.adapter.SupportBankAdapter;
import com.tesu.creditgold.response.GetRepaymentListResponse;
import com.tesu.creditgold.response.GetSupportBankListResponse;

import java.util.List;

/**
 * 生成预订单对话框
 *
 * @version V1.0
 * @update 2014-10-27 下午3:59:49
 */
public class SupportBankListDialog extends Dialog implements
        View.OnClickListener {
    /**
     * 上下文
     */
    private Context context = null;
    /**
     * 关闭按钮
     */
    private RelativeLayout rl_close;
    private  List<GetSupportBankListResponse.SupportBankBean> list;
    private ListView lv_support_bank_list;
    private SupportBankAdapter supportBankAdapter;

    public SupportBankListDialog(Context context, List<GetSupportBankListResponse.SupportBankBean> list) {
        super(context, R.style.MyDialog);
        // 设置点击屏幕Dialog不消失
        this.setCanceledOnTouchOutside(false);
        this.context = context;
        this.list=list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.support_bank_dialog);

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
        lv_support_bank_list=(ListView)findViewById(R.id.lv_support_bank_list);
        rl_close = (RelativeLayout) findViewById(R.id.rl_close);
        rl_close.setOnClickListener(this);
        if(supportBankAdapter==null){
            supportBankAdapter=new SupportBankAdapter(context,list);
        }
        lv_support_bank_list.setAdapter(supportBankAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 取消
            case R.id.rl_close: {
                this.dismiss();
                break;
            }
            default:
                break;
        }

    }

}
