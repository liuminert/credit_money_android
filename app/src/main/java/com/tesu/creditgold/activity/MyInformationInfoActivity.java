package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.MyInformationListAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.MyInformationBean;
import com.tesu.creditgold.response.GetMessageListResponse;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 账户管理
 */
public class MyInformationInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;

    private TextView tv_title;
    private TextView tv_information_content;
    private MyInformationBean myInformationBean;
    private Dialog loadingDialog;
    private FkBaseResponse fkBaseResponse;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_my_information_info, null);
        setContentView(rootView);
        tv_title= (TextView) rootView.findViewById(R.id.tv_title);
        tv_information_content= (TextView) rootView.findViewById(R.id.tv_information_content);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        initData();
        return null;
    }


    public void initData() {
        Intent intent = getIntent();
        myInformationBean = intent.getParcelableExtra("myInformationBean");
        loadingDialog = DialogUtils.createLoadDialog(this, true);

        if(myInformationBean != null){
            if(!TextUtils.isEmpty(myInformationBean.getMsg_title())){
                tv_title.setText(myInformationBean.getMsg_title());
            }
            if(!TextUtils.isEmpty(myInformationBean.getSend_msg())){
                tv_information_content.setText(myInformationBean.getSend_msg());
            }
            updateMessageReadFlag(myInformationBean.getId());
        }

        tv_top_back.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            onKeyDownMethod(keyCode, event);
            Intent intent = getIntent();
            setResult(200, intent);
            finish();
            overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_back: {
                Intent intent = getIntent();
                setResult(200,intent);
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
        }
    }

    /**
     * 消息浏览状态修改
     */
    private void updateMessageReadFlag(int messageId) {
        String tell = SharedPrefrenceUtils.getString(this, "mobile_phone");
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/message/updateMessageReadFlag.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("messageId", String.valueOf(messageId));
        params.put("readFlag", "1");
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                LogUtils.e("消息浏览状态修改:" + json);
                fkBaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkBaseResponse.getCode() == 0) {

                } else {
                    DialogUtils.showAlertDialog(MyInformationInfoActivity.this, fkBaseResponse.getMsg());
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(MyInformationInfoActivity.this, error);
            }
        });
    }

}
