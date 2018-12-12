package com.tesu.creditgold.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.fragment.ControlTabFragment;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.UIUtils;

public class SubmitSuccessActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = SubmitSuccessActivity.class.getSimpleName();
    private View rootView;
    private TextView tv_top_back;
    private ControlTabFragment ctf;
    private Button btn_return;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_submit_success, null);
        setContentView(rootView);

        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        btn_return = (Button) rootView.findViewById(R.id.btn_return);

        if (ctf == null) {
            ctf = MainActivity.getCtf();
        }

        setFinish();

        tv_top_back.setOnClickListener(this);
        btn_return.setOnClickListener(this);
        return null;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            LogUtils.e(TAG + ":onKeyDown");
            onKeyDownMethod(keyCode, event);
            ctf.setChecked(ctf.beforeIndex);
            ctf.mCurrentIndex = ctf.beforeIndex;
            if(ctf.getTabMyselfbyPager() != null){
                ctf.getTabMyselfbyPager().runAsyncTask();
            }
            finishActivity();
            overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_top_back:
                ctf.setChecked(ctf.beforeIndex);
                ctf.mCurrentIndex = ctf.beforeIndex;
                if(ctf.getTabMyselfbyPager() != null){
                    ctf.getTabMyselfbyPager().runAsyncTask();
                }
                finishActivity();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.btn_return:
                ctf.setChecked(ctf.beforeIndex);
                ctf.mCurrentIndex = ctf.beforeIndex;
                if(ctf.getTabMyselfbyPager() != null){
                    ctf.getTabMyselfbyPager().runAsyncTask();
                }
                finishActivity();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
        }

    }
}
