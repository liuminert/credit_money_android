package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.protocol.SetWithdrawPwdProtocol;
import com.tesu.creditgold.protocol.UpdateStoreInfoProtocol;
import com.tesu.creditgold.request.SetWithdrawPwdRequest;
import com.tesu.creditgold.request.UpdateStoreInfoRequest;
import com.tesu.creditgold.response.SetWithdrawPwdResponse;
import com.tesu.creditgold.response.UpdateStoreInfoResponse;
import com.tesu.creditgold.util.BankUtils;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.FileUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/12/30 10:00
 * 设置提现密码
 */
public class SetWithdrawalPasswordActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;

    private String url;
    //接口请求菊花
    private Dialog loadingDialog;
    private EditText et_pwd;
    private EditText et_repeat_pwd;
    private Button btn_commit;
    private TextView tv_bank_name;
    private String bank_username;
    private String bank_card_number;
    private String bank_name;
    private String branch_name;
    //1的时候，最后要跳到提现页面
    private int type;
    private String mobile_phone;
    private Double store_account;
    private String store_id;
    private TextView tv_show_message;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_withdrawal_password, null);
        setContentView(rootView);
        tv_bank_name= (TextView) rootView.findViewById(R.id.tv_bank_name);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        et_pwd=(EditText)rootView.findViewById(R.id.et_pwd);
        et_repeat_pwd=(EditText)rootView.findViewById(R.id.et_repeat_pwd);
        btn_commit=(Button)rootView.findViewById(R.id.btn_commit);
        tv_show_message=(TextView)rootView.findViewById(R.id.tv_show_message);
        initData();
        return null;
    }


    public void initData() {
        Intent intent=getIntent();
        type=intent.getIntExtra("type", 0);
        bank_username=intent.getStringExtra("bank_username");
        bank_card_number=intent.getStringExtra("bank_card_number");
        bank_name=intent.getStringExtra("bank_name");
        branch_name=intent.getStringExtra("branch_name");
        mobile_phone= intent.getStringExtra("mobile_phone");
        store_account = intent.getDoubleExtra("store_account", 0);
        store_id = intent.getStringExtra("store_id");
        loadingDialog = DialogUtils.createLoadDialog(SetWithdrawalPasswordActivity.this, true);
        tv_top_back.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_roger:{
                if(type==1){
                    Intent intent=new Intent(SetWithdrawalPasswordActivity.this,WithdrawalActivity.class);
                    intent.putExtra("bank_username", bank_username);
                    intent.putExtra("bank_name",bank_name);
                    intent.putExtra("bank_card_number", bank_card_number);
                    intent.putExtra("branch_name",branch_name);
                    intent.putExtra("is_set_withdraw_pwd", 1);
                    intent.putExtra("store_account", store_account);
                    intent.putExtra("store_id", store_id);
                    intent.putExtra("mobile_phone", mobile_phone);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {
                    Intent intent = getIntent();
                    intent.putExtra("pwd", et_pwd.getText().toString().trim());
                    intent.putExtra("bank_username", bank_username);
                    intent.putExtra("bank_name", bank_name);
                    intent.putExtra("bank_card_number", bank_card_number);
                    intent.putExtra("branch_name", branch_name);
                    setResult(1, intent);
                }
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.btn_commit:{
                String pwd=et_pwd.getText().toString().trim();
                String repeat_pwd=et_repeat_pwd.getText().toString().trim();
                if(!TextUtils.isEmpty(repeat_pwd)&&!TextUtils.isEmpty(pwd)) {
                    if(repeat_pwd.equals(pwd)) {
                        tv_show_message.setVisibility(View.INVISIBLE);
                        SetWithdrawPwd();
                    }
                    else{
                        tv_show_message.setVisibility(View.VISIBLE);
                    }
                }
                break;
            }
            case R.id.tv_top_back: {
                Intent intent=getIntent();
                intent.putExtra("bank_username", bank_username);
                intent.putExtra("bank_name",bank_name);
                intent.putExtra("bank_card_number", bank_card_number);
                intent.putExtra("branch_name",bank_name);
                setResult(1,intent);
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
        }
    }


    //申请合作
    /**
     * 创建用户订单并发标
     */
    private void SetWithdrawPwd() {
        loadingDialog.show();
        SetWithdrawPwdProtocol setWithdrawPwdProtocol=new SetWithdrawPwdProtocol();
        SetWithdrawPwdRequest setWithdrawPwdRequest=new SetWithdrawPwdRequest();
        url= Constants.XINYONGSERVER_URL+setWithdrawPwdProtocol.getApiFun();
        setWithdrawPwdRequest.map.put("usrid", SharedPrefrenceUtils.getString(SetWithdrawalPasswordActivity.this, "usrid"));
        setWithdrawPwdRequest.map.put("withdraw_pwd", FileUtils.getMD5(et_pwd.getText().toString().trim()));

        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, setWithdrawPwdRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                LogUtils.e("申请结果:" + json.toString());
                SetWithdrawPwdResponse setWithdrawPwdResponse = gson.fromJson(json, SetWithdrawPwdResponse.class);
                if (setWithdrawPwdResponse.code == 0) {
                    DialogUtils.showAlertDialog(SetWithdrawalPasswordActivity.this,
                            "成功", SetWithdrawalPasswordActivity.this);
                } else {
                    DialogUtils.showAlertDialog(SetWithdrawalPasswordActivity.this,
                            setWithdrawPwdResponse.msg);
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(SetWithdrawalPasswordActivity.this,
                            error);
                }
            }
        });
    }
}
