package com.tesu.creditgold.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.bean.CommonQuestionBean;
import com.tesu.creditgold.util.UIUtils;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 账户管理
 */
public class CommonQuestionInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;

    private TextView tv_question_info;
    private CommonQuestionBean commonQuestionBean;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_common_question_info, null);
        setContentView(rootView);
        tv_question_info= (TextView) rootView.findViewById(R.id.tv_question_info);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        initData();
        return null;
    }


    public void initData() {
        Intent intent = getIntent();
        commonQuestionBean = intent.getParcelableExtra("commonQuestionBean");
        if(commonQuestionBean != null){
            if(!TextUtils.isEmpty(commonQuestionBean.getAnswer())){
                tv_question_info.setText(commonQuestionBean.getAnswer());
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
