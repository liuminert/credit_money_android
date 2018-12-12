package com.tesu.creditgold.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.GetUserStatusResponse;
import com.tesu.creditgold.fragment.ControlTabFragment;
import com.tesu.creditgold.protocol.AppSendMsgProtocol;
import com.tesu.creditgold.protocol.LoginProtocol;
import com.tesu.creditgold.request.AppSendMsgRequest;
import com.tesu.creditgold.request.LoginRequest;
import com.tesu.creditgold.response.AppSendMsgResponse;
import com.tesu.creditgold.response.LoginResponse;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;
    private ControlTabFragment ctf;
    private Button btn_login;
    HashMap<String, Object> res;

    private String url;
    //接口请求菊花
    private Dialog loadingDialog;
    private String mobile_phone;
    private String auth_code;
    private String auth_code_id;
    private EditText et_login_phone;
    private EditText et_pwd;
    private Button btn_get_code;
    private int time = 180;
    private GetUserStatusResponse getUserStatusResponse;
    private Gson gson;
    private TextView tv3;
    private CheckBox cb;
    private PercentRelativeLayout rl_photo_close;
    private PercentRelativeLayout rl_pass_close;

    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    LogUtils.e("Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    LogUtils.e("Unhandled msg - " + msg.what);
            }
        }
    };

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_login, null);
        setContentView(rootView);
        cb=(CheckBox)rootView.findViewById(R.id.cb);
        tv3 = (TextView) rootView.findViewById(R.id.tv3);
        et_pwd = (EditText) rootView.findViewById(R.id.et_pwd);
        et_login_phone = (EditText) rootView.findViewById(R.id.et_login_phone);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        btn_login = (Button) rootView.findViewById(R.id.btn_login);
        btn_get_code = (Button) rootView.findViewById(R.id.btn_get_code);
        rl_photo_close = (PercentRelativeLayout) rootView.findViewById(R.id.rl_photo_close);
        rl_pass_close = (PercentRelativeLayout) rootView.findViewById(R.id.rl_pass_close);
        if (ctf == null) {
            ctf = MainActivity.getCtf();
        }

        tv3.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线

        initData();
        return null;
    }


    public void initData() {
        gson = new Gson();
        loadingDialog = DialogUtils.createLoadDialog(LoginActivity.this, true);
        tv_top_back.setOnClickListener(this);
        btn_get_code.setOnClickListener(this);
        rl_photo_close.setOnClickListener(this);
        rl_pass_close.setOnClickListener(this);
        tv3.setOnClickListener(this);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_login.setOnClickListener(LoginActivity.this);
                    btn_login.setClickable(true);
                    btn_login.setBackground(UIUtils.getDrawable(R.mipmap.btn_login));
                } else {
                    btn_login.setClickable(false);
                    btn_login.setBackground(UIUtils.getDrawable(R.mipmap.btn_login_no));
                }
            }
        });
        et_login_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mobile_phone = et_login_phone.getText().toString();
                if (TextUtils.isEmpty(mobile_phone)) {
                    rl_photo_close.setVisibility(View.GONE);
                } else {
                    rl_photo_close.setVisibility(View.VISIBLE);
                }

            }
        });
        et_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                auth_code = et_pwd.getText().toString();
                if(TextUtils.isEmpty(auth_code)){
                    rl_pass_close.setVisibility(View.GONE);
                }else{
                    rl_pass_close.setVisibility(View.VISIBLE);
                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_back: {
                ctf.setChecked(0);
                ctf.mCurrentIndex = 0;
                ctf.switchCurrentPage();
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.btn_get_code: {
                mobile_phone = et_login_phone.getText().toString().trim();
                if (TextUtils.isEmpty(mobile_phone)) {
                    DialogUtils.showAlertDialog(LoginActivity.this,
                            "手机号码不能为空！");
                } else {
                    runGetGode();
                }
                break;
            }
            case R.id.btn_login: {
                mobile_phone = et_login_phone.getText().toString().trim();
                auth_code = et_pwd.getText().toString().trim();

//                if (!TextUtils.isEmpty(mobile_phone)
//                        && !TextUtils.isEmpty(auth_code)) {
//                    runAsyncTask();
//                } else {
//                    if (TextUtils.isEmpty(mobile_phone)) {
//                        DialogUtils.showAlertDialog(LoginActivity.this,
//                                "手机号码不能为空！");
//                    } else if (TextUtils.isEmpty(auth_code)) {
//                        DialogUtils.showAlertDialog(LoginActivity.this,
//                                "验证码不能为空！");
//                    }
//                }


                SharedPrefrenceUtils.setBoolean(LoginActivity.this,
                        "isLogin", true);
                SharedPrefrenceUtils.setString(LoginActivity.this,
                        "mobile_phone", mobile_phone);
                ctf.setChecked(2);
                ctf.mCurrentIndex = 2;
                ctf.switchCurrentPage();
                ctf.getTabMyselfbyPager().setLoaded(false);
                ctf.getTabMyselfbyPager().Update();
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.tv3:{
//                Intent intent=new Intent(LoginActivity.this,ContractActivity.class);
//                UIUtils.startActivityNextAnim(intent);

                Intent intent = new Intent(LoginActivity.this,XieYiWebActivity.class);
                intent.putExtra("article_id", 106);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_photo_close:
                et_login_phone.setText("");
                break;
            case R.id.rl_pass_close:
                et_pwd.setText("");
                break;
        }
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    LogUtils.e(logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    LogUtils.e(logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    LogUtils.e(logs);
            }
//            UIUtils.showToastCenter(LoginActivity.this,logs);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            onKeyDownMethod(keyCode, event);
            ctf.setChecked(0);
            ctf.mCurrentIndex = 0;
            ctf.switchCurrentPage();
            finish();
            overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
        }
        return super.onKeyDown(keyCode, event);
    }


    public void runAsyncTask() {
        loadingDialog.show();
        LoginProtocol loginProtocol = new LoginProtocol();
        LoginRequest loginRequest = new LoginRequest();
        url = Constants.XINYONGSERVER_URL+loginProtocol.getApiFun();
        loginRequest.map.put("user_phone", mobile_phone);
        loginRequest.map.put("auth_code_id", auth_code_id);
        loginRequest.map.put("auth_code", auth_code);
        loginRequest.map.put("equipment_source","3");
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, loginRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                LogUtils.e("json:"+json.toString());
                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(json, LoginResponse.class);
                LogUtils.e("loginResponse:" + loginResponse.toString());
                if (loginResponse.code.equals("0")) {
                    loadingDialog.dismiss();
                    SharedPrefrenceUtils.setBoolean(LoginActivity.this,
                            "isLogin", true);
                    SharedPrefrenceUtils.setString(LoginActivity.this,
                            "usrid", String.valueOf(loginResponse.usrid));
                    SharedPrefrenceUtils.setString(LoginActivity.this,
                            "mobile_phone", mobile_phone);

//                    getUserStatus(String.valueOf(loginResponse.usrid));

                    String token = loginResponse.token;
                    if(!TextUtils.isEmpty(token)){
                        SharedPrefrenceUtils.setString(LoginActivity.this,
                                "token", token);
                    }

                    mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, "a_"+mobile_phone));

                    ctf.setChecked(2);
                    ctf.mCurrentIndex = 2;
                    ctf.switchCurrentPage();
                    ctf.getTabMyselfbyPager().setLoaded(false);
                    ctf.getTabMyselfbyPager().Update();
                    finish();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                } else {
                    loadingDialog.dismiss();
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(LoginActivity.this,
                                loginResponse.msg);
                    }

                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(LoginActivity.this, error);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100 ){
//            if(resultCode == 200) {
                ctf.setChecked(2);
                ctf.mCurrentIndex = 2;
                ctf.switchCurrentPage();
//            }
            finish();
            overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 判断用户状态
     */
    private void getUserStatus(String userId) {
        loadingDialog.show();
        String url =Constants.XINYONGSERVER_URL+"credit_money_background/user/get_user_status.do";
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("usrid", userId);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("判断用户状态:" + json.toString());
                getUserStatusResponse = gson.fromJson(json, GetUserStatusResponse.class);
                if (getUserStatusResponse.getCode() == 0) { //表示用户已登录，已实名，已绑卡，直接下一步
                    ctf.setChecked(2);
                    ctf.mCurrentIndex = 2;
                    ctf.switchCurrentPage();
                    finish();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);

                } else if (getUserStatusResponse.getCode() == -1) { //表示出错
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(LoginActivity.this,
                                getUserStatusResponse.getMsg());
                    }

                } else if (getUserStatusResponse.getCode() == -2) { //表示用户已登录，未实名，未绑卡，跳到实名界面.
                    Intent intent = new Intent(LoginActivity.this, RealNameActivity.class);
//                    UIUtils.startActivityNextAnim(intent);
                    UIUtils.startActivityForResult(intent, 100);

                } else if (getUserStatusResponse.getCode() == -3) { //表示用户已登录，已实名，未绑卡，直接下一步
                    ctf.setChecked(2);
                    ctf.mCurrentIndex = 2;
                    ctf.switchCurrentPage();
                    finish();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(LoginActivity.this,
                            error);
                }
            }
        });
    }

    /**
     * 计时器
     */
    public void Countdowmtimer(long dodate) {
        new CountDownTimer(dodate, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time = time - 1;
                btn_get_code.setText(  time + "s重新获取");
            }

            @Override
            // 计时结束
            public void onFinish() {
                btn_get_code.setEnabled(true);
                time = 180;
                btn_get_code.setText("获取验证码");
//                SpannableStringBuilder style=new SpannableStringBuilder(tv_time.getText());
//                style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink_text)), 8, 9, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//                tv_time.setText(style);
            }
        }.start();
    }

    public void runGetGode() {
        btn_get_code.setEnabled(false);
        Countdowmtimer(180000);

        loadingDialog.show();
        AppSendMsgProtocol appSendMsgProtocol = new AppSendMsgProtocol();
        AppSendMsgRequest appSendMsgRequest = new AppSendMsgRequest();
        url = Constants.XINYONGSERVER_URL+appSendMsgProtocol.getApiFun();
        appSendMsgRequest.map.put("user_phone", mobile_phone);
        appSendMsgRequest.map.put("msg_type", "0");
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, appSendMsgRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                AppSendMsgResponse appSendMsgResponse = gson.fromJson(json, AppSendMsgResponse.class);
                LogUtils.e("appSendMsgResponse:" + appSendMsgResponse.toString());
                if (appSendMsgResponse.code.equals("0")) {
                    loadingDialog.dismiss();
                    auth_code_id = appSendMsgResponse.auth_code_id;

                } else {
                    loadingDialog.dismiss();
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(LoginActivity.this,
                                appSendMsgResponse.msg);
                    }
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(LoginActivity.this, error);
                }
            }
        });
    }

}
