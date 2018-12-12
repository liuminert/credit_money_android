package com.tesu.creditgold.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.util.UIUtils;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 账户管理
 */
public class HelpCenterActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;

    private RelativeLayout rl_relevant_agreement; //相关协议
    private RelativeLayout rl_customer_service; //客服热线
    private RelativeLayout rl_common_question; //常见问题
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_help_center, null);
        setContentView(rootView);
        rl_relevant_agreement= (RelativeLayout) rootView.findViewById(R.id.rl_relevant_agreement);
        rl_customer_service= (RelativeLayout) rootView.findViewById(R.id.rl_customer_service);
        rl_common_question= (RelativeLayout) rootView.findViewById(R.id.rl_common_question);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        initData();
        return null;
    }


    public void initData() {
        tv_top_back.setOnClickListener(this);
        rl_relevant_agreement.setOnClickListener(this);
        rl_customer_service.setOnClickListener(this);
        rl_common_question.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_relevant_agreement:  //相关协议
                Intent intent = new Intent(HelpCenterActivity.this, ContractActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            case R.id.tv_top_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.rl_customer_service: //客服热线
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_CALL);
                //url:统一资源定位符
                //uri:统一资源标示符（更广）
                intent1.setData(Uri.parse("tel:4006626985"));
                //开启系统拨号器
                UIUtils.startActivity(intent1);
                break;
            case R.id.rl_common_question: //常见问题
                Intent intent2 = new Intent(HelpCenterActivity.this,CommonQuestionsActivity.class);
                UIUtils.startActivityNextAnim(intent2);
                break;
        }
    }

}
