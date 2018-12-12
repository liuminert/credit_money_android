package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.fragment.ControlTabFragment;
import com.tesu.creditgold.protocol.GetUsrInfProtocol;
import com.tesu.creditgold.protocol.IsBindBankCardProtocol;
import com.tesu.creditgold.protocol.UpdateStoreInfoProtocol;
import com.tesu.creditgold.request.GetUsrInfRequest;
import com.tesu.creditgold.request.IsBindBankCardRequest;
import com.tesu.creditgold.request.UpdateStoreInfoRequest;
import com.tesu.creditgold.response.GetUsrInfResponse;
import com.tesu.creditgold.response.IsBindBankCardResponse;
import com.tesu.creditgold.response.UpdateStoreInfoResponse;
import com.tesu.creditgold.util.BankUtils;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 账户管理
 */
public class AccountManagementActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;

    private String url;
    //接口请求菊花
    private Dialog loadingDialog;
    private EditText et_name;
    private EditText et_bank_card;
    private Button btn_commit;
    private EditText et_bank_name;
    private String bank_username;
    private String bank_card_number;
    private String bank_name;
    private int is_set_withdraw_pwd;
    private EditText et_branch;
    private TextView tv_hint;
    private String branch_name;
    private boolean isChange;
    private String mobile_phone;
    private Double store_account;
    private String store_id;
    private ControlTabFragment ctf;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_account_management, null);
        setContentView(rootView);
        et_branch= (EditText) rootView.findViewById(R.id.et_branch);
        et_bank_name= (EditText) rootView.findViewById(R.id.et_bank_name);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_hint= (TextView) rootView.findViewById(R.id.tv_hint);
        et_name=(EditText)rootView.findViewById(R.id.et_name);
        et_bank_card=(EditText)rootView.findViewById(R.id.et_bank_card);
        btn_commit=(Button)rootView.findViewById(R.id.btn_commit);
        initData();
        return null;
    }


    public void initData() {
        if (ctf == null) {
            ctf = MainActivity.getCtf();
        }

        Intent intent=getIntent();
        is_set_withdraw_pwd=intent.getIntExtra("is_set_withdraw_pwd", 0);
        bank_username=intent.getStringExtra("bank_username");
        bank_card_number=intent.getStringExtra("bank_card_number");
        bank_name=intent.getStringExtra("bank_name");
        branch_name=intent.getStringExtra("branch_name");
        mobile_phone= intent.getStringExtra("mobile_phone");
        store_account = intent.getDoubleExtra("store_account", 0);
        store_id = intent.getStringExtra("store_id");
        loadingDialog = DialogUtils.createLoadDialog(AccountManagementActivity.this, true);
        tv_top_back.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
        et_bank_card.addTextChangedListener(textWatcher);

        if(TextUtils.isEmpty(bank_name) && TextUtils.isEmpty(bank_username) && TextUtils.isEmpty(bank_card_number) && TextUtils.isEmpty(branch_name)){
            isChange = true;
            et_branch.setEnabled(true);
            et_bank_card.setEnabled(true);
            et_bank_name.setEnabled(true);
            et_name.setEnabled(true);
            et_bank_card.setText(bank_card_number);
            btn_commit.setText("提交");
            tv_hint.setVisibility(View.VISIBLE);
        }else {
            isChange = false;
            et_branch.setEnabled(false);
            et_bank_card.setEnabled(false);
            et_bank_name.setEnabled(false);
            et_name.setEnabled(false);
            btn_commit.setText("修改");
            tv_hint.setVisibility(View.INVISIBLE);
        }
        if(!TextUtils.isEmpty(bank_name)){
            et_bank_name.setText(bank_name);
            et_bank_name.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        }
        if(!TextUtils.isEmpty(bank_username)){
            et_name.setText(bank_username);
            et_name.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);

        }
        if(!TextUtils.isEmpty(bank_card_number)){
//            et_bank_card.setText( bank_card_number.replace(bank_card_number.substring(4, 14), "**********"));
            if(bank_card_number.length()>4){
                et_bank_card.setText( bank_card_number.substring(0,4)+"*******"+bank_card_number.substring(bank_card_number.length()-4));
            }else {
                et_bank_card.setText(bank_card_number);
            }
            et_bank_card.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        }
        if(!TextUtils.isEmpty(branch_name)){
            et_branch.setText( branch_name);
            et_branch.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        }
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = et_name.getText().toString();
                if(!TextUtils.isEmpty(str)){
                    et_name.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
                }else {
                    et_name.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
                }

            }
        });
        et_bank_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = et_bank_name.getText().toString();
                if(!TextUtils.isEmpty(str)){
                    et_bank_name.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
                }else {
                    et_bank_name.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
                }

            }
        });
        et_bank_card.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = et_bank_card.getText().toString();
                if(!TextUtils.isEmpty(str)){
                    et_bank_card.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
                }else {
                    et_bank_card.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
                }

            }
        });
        et_branch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = et_branch.getText().toString();
                if(!TextUtils.isEmpty(str)){
                    et_branch.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
                }else {
                    et_branch.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
                }

            }
        });
//        et_name.setEnabled(false);
//        et_branch.setEnabled(false);
//        et_bank_card.setEnabled(false);
//        et_bank_name.setEnabled(false);
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
//
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
                if (et_bank_card.getText().length() >= 16) {
                    String cardNumber = et_bank_card.getText().toString();//卡号
                    String name = BankUtils.getNameOfBank(cardNumber);
                    if(TextUtils.isEmpty(bank_name)) {
                        et_bank_name.setText(name);
                    }
                    else{
                        et_bank_name.setText(bank_name);
                        bank_name="";
                    }
                }
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_roger:{
                if(is_set_withdraw_pwd==1){
                    Intent intent=getIntent();
                    intent.putExtra("bank_username", et_name.getText().toString());
                    intent.putExtra("bank_name", et_bank_name.getText().toString());
                    intent.putExtra("bank_card_number", et_bank_card.getText().toString());
                    intent.putExtra("branch_name", et_branch.getText().toString());
                    setResult(1, intent);
                }
                else{
                    Intent intent=new Intent(AccountManagementActivity.this,SetWithdrawalPasswordActivity.class);
                    intent.putExtra("bank_username", et_name.getText().toString());
                    intent.putExtra("bank_name", et_bank_name.getText().toString());
                    intent.putExtra("bank_card_number", et_bank_card.getText().toString());
                    intent.putExtra("branch_name", et_branch.getText().toString());
                    intent.putExtra("store_account", store_account);
                    intent.putExtra("store_id", store_id);
                    intent.putExtra("mobile_phone", mobile_phone);
                    intent.putExtra("type", 1);
                    UIUtils.startActivityForResultNextAnim(intent,1);
                }
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.btn_commit:{
                if(isChange){
                    if(!TextUtils.isEmpty(et_name.getText().toString())&&!TextUtils.isEmpty(et_bank_card.getText().toString())&&!TextUtils.isEmpty(et_bank_name.getText().toString())&&!TextUtils.isEmpty(et_branch.getText().toString())) {
                        if(et_bank_card.getText().toString().length()<=14){
                            UIUtils.showToastCenter(AccountManagementActivity.this,"银行卡号位数不够");
                            return;
                        }
                        UpdateStoreInfo();
                    }else{
                        if(TextUtils.isEmpty(et_bank_name.getText().toString())){
                            UIUtils.showToastCenter(AccountManagementActivity.this, "银行不能为空");
                        }else if(TextUtils.isEmpty(et_name.getText().toString())){
                            UIUtils.showToastCenter(AccountManagementActivity.this, "用户名不能为空");
                        }else if(TextUtils.isEmpty(et_bank_card.getText().toString())){
                            UIUtils.showToastCenter(AccountManagementActivity.this, "银行卡号不能为空");
                        }else{
                            UIUtils.showToastCenter(AccountManagementActivity.this, "开户支行不能为空");
                        }
                    }

                }else{

//                    et_branch.setInputType(InputType.TYPE_CLASS_TEXT);
//                    et_bank_card.setInputType(InputType.TYPE_CLASS_TEXT);
//                    et_bank_name.setInputType(InputType.TYPE_CLASS_TEXT);
//                    et_name.setInputType(InputType.TYPE_CLASS_TEXT);
//                    et_name.setCursorVisible(true);
                    et_branch.setEnabled(true);
                    et_bank_card.setEnabled(true);
                    et_bank_name.setEnabled(true);
                    et_name.setEnabled(true);
                    et_bank_card.setText(bank_card_number);
                    btn_commit.setText("提交");
                    tv_hint.setVisibility(View.VISIBLE);
                    isChange=true;
                }
//                Intent intent=new Intent(WithdrawalActivity.this,IdentityAuthenticationActivity.class);
//                UIUtils.startActivityNextAnim(intent);

                break;
            }
            case R.id.tv_top_back: {
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
    private void UpdateStoreInfo() {
        if(et_bank_card.getText().toString().contains("*")){
            UIUtils.showToastCenter(AccountManagementActivity.this, "银行卡号不能带*");
            return;
        }
        loadingDialog.show();
        UpdateStoreInfoProtocol updateStoreInfoProtocol=new UpdateStoreInfoProtocol();
        UpdateStoreInfoRequest updateStoreInfoRequest=new UpdateStoreInfoRequest();
        url= Constants.XINYONGSERVER_URL+updateStoreInfoProtocol.getApiFun();
        updateStoreInfoRequest.map.put("usr_id", SharedPrefrenceUtils.getString(AccountManagementActivity.this, "usrid"));
        updateStoreInfoRequest.map.put("bank_card_number", et_bank_card.getText().toString());
        updateStoreInfoRequest.map.put("bank_name",et_bank_name.getText().toString());
        updateStoreInfoRequest.map.put("bank_username",et_name.getText().toString());
        updateStoreInfoRequest.map.put("branch_name", et_branch.getText().toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, updateStoreInfoRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                LogUtils.e("申请结果:" + json.toString());
                UpdateStoreInfoResponse updateStoreInfoResponse = gson.fromJson(json, UpdateStoreInfoResponse.class);
                if (updateStoreInfoResponse.code == 0) {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(AccountManagementActivity.this,
                                "成功", AccountManagementActivity.this);
                    }
                    ctf.getTabMyselfbyPager().runFindShop();
                    finishActivity();
                    finish();
                }else if(updateStoreInfoResponse.code == 2){
                    Intent intent = new Intent(AccountManagementActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(AccountManagementActivity.this,
                                updateStoreInfoResponse.msg);
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(AccountManagementActivity.this,
                            error);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==1) {
                bank_card_number = data.getStringExtra("bank_card_number");
                bank_name = data.getStringExtra("bank_name");
                bank_username = data.getStringExtra("bank_username");
                Intent intent=getIntent();
                intent.putExtra("bank_username", bank_username);
                intent.putExtra("bank_name", bank_name);
                intent.putExtra("bank_card_number", bank_card_number);
                setResult(1, intent);
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        }
    }
}
