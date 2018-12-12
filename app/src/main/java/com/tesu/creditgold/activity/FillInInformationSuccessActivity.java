package com.tesu.creditgold.activity;

import android.content.Intent;
import android.os.CountDownTimer;
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

public class FillInInformationSuccessActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = FillInInformationSuccessActivity.class.getSimpleName();
    private View rootView;
    private TextView tv_top_back;
    private Button btn_return;
    private boolean isAuoto = true;  //是否自动跳到订单列表

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_fill_in_information_success, null);
        setContentView(rootView);

        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        btn_return = (Button) rootView.findViewById(R.id.btn_return);


        setFinish();
        Countdowmtimer(3000);
        tv_top_back.setOnClickListener(this);
        btn_return.setOnClickListener(this);
        return null;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            isAuoto = false;
            finishActivity();
            overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 计时器
     */
    public void Countdowmtimer(long dodate) {
        new CountDownTimer(dodate, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            // 计时结束
            public void onFinish() {
                if(isAuoto){
                    Intent intent = new Intent(FillInInformationSuccessActivity.this,QuotaOrderListActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                    finishActivity();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_top_back:
                isAuoto = false;
                finishActivity();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.btn_return:
                isAuoto = false;
                Intent intent1 = new Intent(FillInInformationSuccessActivity.this,QuotaOrderListActivity.class);
                UIUtils.startActivityNextAnim(intent1);
                finishActivity();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
        }

    }
}
