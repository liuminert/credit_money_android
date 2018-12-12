package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.GetUserStatusResponse;
import com.tesu.creditgold.fragment.ControlTabFragment;
import com.tesu.creditgold.protocol.AppSendMsgProtocol;
import com.tesu.creditgold.protocol.LoginProtocol;
import com.tesu.creditgold.protocol.ValidateWithdrawCodeProtocol;
import com.tesu.creditgold.request.AppSendMsgRequest;
import com.tesu.creditgold.request.LoginRequest;
import com.tesu.creditgold.request.ValidateWithdrawCodeRequest;
import com.tesu.creditgold.response.AppSendMsgResponse;
import com.tesu.creditgold.response.LoginResponse;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;

import java.util.HashMap;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 登录页面
 */
public class FindPwdActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;
    private ControlTabFragment ctf;
    private Button btn_next;
    HashMap<String, Object> res;

    private String url;
    //接口请求菊花
    private Dialog loadingDialog;
    private String mobile_phone;
    private String auth_code;
    private String auth_code_id;
    private TextView tv_login_phone;
    private EditText et_code;
    private Button btn_get_code;
    private int time = 180;
    private GetUserStatusResponse getUserStatusResponse;
    private Gson gson;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_find_pwd, null);
        setContentView(rootView);
        tv_login_phone = (TextView) rootView.findViewById(R.id.tv_login_phone);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        et_code=(EditText)rootView.findViewById(R.id.et_code);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        btn_get_code = (Button) rootView.findViewById(R.id.btn_get_code);
        initData();
        return null;
    }


    public void initData() {
        mobile_phone=getIntent().getStringExtra("mobile_phone");
        gson = new Gson();
        loadingDialog = DialogUtils.createLoadDialog(FindPwdActivity.this, true);
        btn_next.setOnClickListener(this);
        tv_top_back.setOnClickListener(this);
        btn_get_code.setOnClickListener(this);

        if(!TextUtils.isEmpty(mobile_phone)){
            tv_login_phone.setText( mobile_phone.substring(0,3)+"****"+mobile_phone.substring(mobile_phone.length()-4));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.btn_get_code: {
                runGetGode();
                break;
            }
            case R.id.btn_next: {
                auth_code = et_code.getText().toString().trim();

                if ( !TextUtils.isEmpty(auth_code)) {
                    runAsyncTask();
                } else {
                        DialogUtils.showAlertDialog(FindPwdActivity.this,
                                "验证码不能为空！");
                }
                break;
            }
            case R.id.tv3:{
                Intent intent=new Intent(FindPwdActivity.this,ContractActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
        }
    }



    public void runAsyncTask() {
        loadingDialog.show();
        ValidateWithdrawCodeProtocol validateWithdrawCodeProtocol = new ValidateWithdrawCodeProtocol();
        ValidateWithdrawCodeRequest validateWithdrawCodeRequest = new ValidateWithdrawCodeRequest();
        url = Constants.XINYONGSERVER_URL+validateWithdrawCodeProtocol.getApiFun();
        validateWithdrawCodeRequest.map.put("user_phone", mobile_phone);
        validateWithdrawCodeRequest.map.put("auth_code_id", auth_code_id);
        validateWithdrawCodeRequest.map.put("auth_code", auth_code);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, validateWithdrawCodeRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(json, LoginResponse.class);
                LogUtils.e("loginResponse:" + loginResponse.toString());
                if (loginResponse.code.equals("0")) {
                    loadingDialog.dismiss();
                    Intent intent=new Intent(FindPwdActivity.this,SetWithdrawalPasswordActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                    finish();
                } else if(loginResponse.code.equals("2")){
                    Intent intent = new Intent(FindPwdActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {
                    loadingDialog.dismiss();
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(FindPwdActivity.this,
                                loginResponse.msg);
                    }

                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(FindPwdActivity.this, error);
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
                btn_get_code.setText(  time + "s");
            }

            @Override
            // 计时结束
            public void onFinish() {
                time = 180;
                btn_get_code.setText("获取验证码");
//                SpannableStringBuilder style=new SpannableStringBuilder(tv_time.getText());
//                style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink_text)), 8, 9, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//                tv_time.setText(style);
            }
        }.start();
    }

    public void runGetGode() {
        loadingDialog.show();
        AppSendMsgProtocol appSendMsgProtocol = new AppSendMsgProtocol();
        AppSendMsgRequest appSendMsgRequest = new AppSendMsgRequest();
        url = Constants.XINYONGSERVER_URL+appSendMsgProtocol.getApiFun();
        appSendMsgRequest.map.put("user_phone", mobile_phone);
        appSendMsgRequest.map.put("msg_type", "1");
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, appSendMsgRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                AppSendMsgResponse appSendMsgResponse = gson.fromJson(json, AppSendMsgResponse.class);
                LogUtils.e("appSendMsgResponse:" + appSendMsgResponse.toString());
                if (appSendMsgResponse.code.equals("0")) {
                    loadingDialog.dismiss();
                    auth_code_id = appSendMsgResponse.auth_code_id;
                    Countdowmtimer(180000);
                } else if(appSendMsgResponse.code.equals("2")){
                    Intent intent = new Intent(FindPwdActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {
                    loadingDialog.dismiss();
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(FindPwdActivity.this,
                                appSendMsgResponse.msg);
                    }
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(FindPwdActivity.this, error);
                }
            }
        });
    }

}
