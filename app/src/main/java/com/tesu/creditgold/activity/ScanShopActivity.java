package com.tesu.creditgold.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;

public class ScanShopActivity extends BaseActivity {
    private TextView tv_top_back;
    private View rootView;
    private TextView tv_disabled;
    private String shop_message;


    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_scan_shop, null);
        setContentView(rootView);

        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_disabled = (TextView) rootView.findViewById(R.id.tv_disabled);

        tv_top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });

        return null;
    }
}
