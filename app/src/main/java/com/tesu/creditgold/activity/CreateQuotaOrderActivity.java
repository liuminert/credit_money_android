package com.tesu.creditgold.activity;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.util.UIUtils;

public class CreateQuotaOrderActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_top_back;
    private View rootView;
    private Button btn_create;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_create_quota_order, null);
        setContentView(rootView);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        btn_create = (Button) rootView.findViewById(R.id.btn_create);

        tv_top_back.setOnClickListener(this);
        btn_create.setOnClickListener(this);
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_top_back:
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.btn_create:
                Intent intent = new Intent(CreateQuotaOrderActivity.this,FillInInformationActivity.class);
                UIUtils.startActivityNextAnim(intent);
                setFinish();
                break;
        }

    }
}
