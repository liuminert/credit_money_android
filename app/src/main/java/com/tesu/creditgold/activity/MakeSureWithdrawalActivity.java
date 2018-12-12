package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.PictureItemBean;
import com.tesu.creditgold.protocol.UserWithdrawalsProtocol;
import com.tesu.creditgold.request.UserWithdrawalsRequest;
import com.tesu.creditgold.response.UserWithdrawalsResponse;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.FileUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.UIUtils;

import java.util.List;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 提现
 */
public class MakeSureWithdrawalActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;
    private Button btn_withrawal;
    private String amount;
    private String store_id;
    private String url;
    //接口请求菊花
    private Dialog loadingDialog;
    //提现密码
    private String pwd;
    private EditText et_pwd;
    private TextView tv_money;
    private TextView tv1;
    private String mobile_phone;
    private String trans_certificate_pic;
    private List<PictureItemBean> pictureItemBeanList;

    private ImageView iv_password_hide;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_makesure_withdrawal, null);
        setContentView(rootView);
        tv_money = (TextView) rootView.findViewById(R.id.tv_money);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        btn_withrawal = (Button) rootView.findViewById(R.id.btn_withrawal);
        et_pwd = (EditText) rootView.findViewById(R.id.et_pwd);
        tv1 = (TextView) rootView.findViewById(R.id.tv1);
        iv_password_hide = (ImageView) rootView.findViewById(R.id.iv_password_hide);

        initData();
        return null;
    }


    public void initData() {
        loadingDialog = DialogUtils.createLoadDialog(MakeSureWithdrawalActivity.this, true);
        Intent intent = getIntent();
        amount = intent.getStringExtra("amount");
        store_id = intent.getStringExtra("store_id");
        mobile_phone =intent.getStringExtra("mobile_phone");
        pictureItemBeanList = (List<PictureItemBean>) intent.getSerializableExtra("pictureItemBeanList");
        if(pictureItemBeanList != null && pictureItemBeanList.size()>1){
            pictureItemBeanList.remove(pictureItemBeanList.size()-1);
            trans_certificate_pic = new Gson().toJson(pictureItemBeanList);
        }
        tv_top_back.setOnClickListener(this);
        btn_withrawal.setOnClickListener(this);
        iv_password_hide.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv_money.setText(amount);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv1:{
                Intent intent=new Intent(MakeSureWithdrawalActivity.this,FindPwdActivity.class);
                intent.putExtra("mobile_phone",mobile_phone);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.tv_roger:{
                setFinish();
                finishActivity();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.btn_withrawal: {
                    if (!TextUtils.isEmpty(et_pwd.getText().toString())) {
                        pwd = et_pwd.getText().toString();
                        runAsyncTask();
                    }else {
                        UIUtils.showToastCenter(MakeSureWithdrawalActivity.this,"请输入提现密码");
                    }

                break;
            }
            case R.id.tv_top_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;

            }
            case R.id.iv_password_hide:
                if(et_pwd.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                    et_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_password_hide.setImageResource(R.mipmap.passwood_hide);
                }else {
                    et_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_password_hide.setImageResource(R.mipmap.passwood_visual);
                }
                break;
        }
    }

    public void runAsyncTask() {
        loadingDialog.show();
        UserWithdrawalsProtocol userWithdrawalsProtocol = new UserWithdrawalsProtocol();
        UserWithdrawalsRequest userWithdrawalsRequest = new UserWithdrawalsRequest();
        url = Constants.XINYONGSERVER_URL+userWithdrawalsProtocol.getApiFun();
        userWithdrawalsRequest.map.put("store_id", store_id);
        userWithdrawalsRequest.map.put("amount", amount);
        userWithdrawalsRequest.map.put("withdraw_pwd", FileUtils.getMD5(pwd));
        userWithdrawalsRequest.map.put("trans_certificate_pic", trans_certificate_pic);
        LogUtils.e("提现"+userWithdrawalsRequest.map.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, userWithdrawalsRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                UserWithdrawalsResponse userWithdrawalsResponse = gson.fromJson(json, UserWithdrawalsResponse.class);
                LogUtils.e("提现:" + json.toString());
                if (userWithdrawalsResponse.code == 0) {
                    loadingDialog.dismiss();
//                    if(!isFinishing()){
//                        DialogUtils.showAlertDialog(MakeSureWithdrawalActivity.this,
//                                userWithdrawalsResponse.msg, MakeSureWithdrawalActivity.this);
//                    }
                    Intent intent = new Intent(MakeSureWithdrawalActivity.this,WithdrawalSucessActivity.class);
                    setFinish();
                    UIUtils.startActivityNextAnim(intent);
                } else {
                    loadingDialog.dismiss();
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(MakeSureWithdrawalActivity.this,
                                userWithdrawalsResponse.msg);
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(MakeSureWithdrawalActivity.this, error);
                }
            }
        });
    }

}
