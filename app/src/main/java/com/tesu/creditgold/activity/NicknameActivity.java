package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.response.ImageResponse;
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
public class NicknameActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;
    private TextView tv_save;
    private EditText et_nickname;
    private ImageView iv_close;
    private String nickname;
    private Dialog loadingDialog;
    private String url;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_nickname, null);
        setContentView(rootView);
        tv_save= (TextView) rootView.findViewById(R.id.tv_save);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        et_nickname = (EditText) rootView.findViewById(R.id.et_nickname);
        iv_close = (ImageView) rootView.findViewById(R.id.iv_close);
        initData();
        return null;
    }


    public void initData() {
        loadingDialog = DialogUtils.createLoadDialog(NicknameActivity.this, true);

        tv_top_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        iv_close.setOnClickListener(this);

        et_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nickname = et_nickname.getText().toString();
                if (TextUtils.isEmpty(nickname)) {
                    iv_close.setVisibility(View.GONE);
                } else {
                    iv_close.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save: //保存
                nickname = et_nickname.getText().toString();
                if(TextUtils.isEmpty(nickname)){
                    UIUtils.showToastCenter(NicknameActivity.this,"请先填写昵称");
                    return;
                }
                saveNickname();
                break;
            case R.id.tv_top_back:
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.iv_close:
                et_nickname.setText("");
                break;
        }
    }


    /**
     * 上传昵称
     */
    private void saveNickname() {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/updateUsrInfo.do";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("usrId", SharedPrefrenceUtils.getString(NicknameActivity.this, "usrid"));
        paramMap.put("nickName", nickname);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, paramMap, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                LogUtils.e("修改昵称:" + json.toString());
                FkBaseResponse baseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (baseResponse.getCode() == 0) {
                    Intent intent = getIntent();
                    intent.putExtra("nickname",nickname);
                    setResult(200, intent);
                    finish();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                } else {
                    if (!TextUtils.isEmpty(baseResponse.getMsg())) {
                        if (!isFinishing()) {
                            DialogUtils.showAlertDialog(NicknameActivity.this,
                                    baseResponse.getMsg());
                        }
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (!TextUtils.isEmpty(error)) {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(NicknameActivity.this,
                                error);
                    }
                }
            }
        });

    }

}
