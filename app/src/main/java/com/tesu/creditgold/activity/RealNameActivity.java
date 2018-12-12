package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
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
import com.tesu.creditgold.protocol.CreateUsrProtocol;
import com.tesu.creditgold.request.CreateUsrRequest;
import com.tesu.creditgold.response.CreateUsrResponse;
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
public class RealNameActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;
    private ControlTabFragment ctf;
    private Button btn_login;
    HashMap<String, Object> res;

    private String url;
    //接口请求菊花
    private Dialog loadingDialog;
    //身份证
    private String id_card;
    //用户姓名，真实姓名
    private String usrname;
    private EditText et_user_name;
    private EditText et_id_card;
    private String usid;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_realname, null);
        setContentView(rootView);
        et_id_card= (EditText) rootView.findViewById(R.id.et_id_card);
        et_user_name= (EditText) rootView.findViewById(R.id.et_user_name);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        btn_login = (Button) rootView.findViewById(R.id.btn_login);
        if (ctf == null) {
            ctf = MainActivity.getCtf();
        }
        initData();
        return null;
    }


    public void initData() {
        loadingDialog = DialogUtils.createLoadDialog(RealNameActivity.this, true);
        btn_login.setOnClickListener(this);
        tv_top_back.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_back: {
                Intent intent = getIntent();
                setResult(400, intent);
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.btn_login: {
                usrname = et_user_name.getText().toString().trim();
                id_card = et_id_card.getText().toString().trim();

                usid = SharedPrefrenceUtils.getString(RealNameActivity.this,"usrid");
                if(TextUtils.isEmpty(usid)){
                    UIUtils.showToastCenter(RealNameActivity.this, "请先登陆");
                    return;
                }

                if (!TextUtils.isEmpty(usrname)
                        && !TextUtils.isEmpty(id_card)) {
                    runAsyncTask();
                } else {
                    if (TextUtils.isEmpty(usrname)) {
                        DialogUtils.showAlertDialog(RealNameActivity.this,
                                "真实姓名不能为空！");
                    } else if (TextUtils.isEmpty(id_card)) {
                        DialogUtils.showAlertDialog(RealNameActivity.this,
                                "身份证号码不能为空！");
                    }
                }
                break;
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            onKeyDownMethod(keyCode, event);
            Intent intent = getIntent();
            setResult(400, intent);
            finish();
            overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void runAsyncTask() {
        loadingDialog.show();
        CreateUsrProtocol createUsrProtocol = new CreateUsrProtocol();
        CreateUsrRequest createUsrRequest = new CreateUsrRequest();
        url = Constants.XINYONGSERVER_URL+createUsrProtocol.getApiFun();
        createUsrRequest.map.put("usrid", usid);
        createUsrRequest.map.put("usrname", usrname);
        createUsrRequest.map.put("id_card", id_card);
        createUsrRequest.map.put("return_url", "http://www.baidu.com");
        createUsrRequest.map.put("type", "1");
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, createUsrRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                CreateUsrResponse createUsrResponse = gson.fromJson(json, CreateUsrResponse.class);
                LogUtils.e("sendVerificationCodeResponse:" + json.toString());
                if (createUsrResponse.code.equals("0")) {
                    loadingDialog.dismiss();
                    Intent intent = getIntent();
                    intent.putExtra("usrname", usrname);
                    intent.putExtra("id_card", id_card);
                    intent.putExtra("sina_whithhold_url", createUsrResponse.sina_whithhold_url);
                    setResult(200, intent);
                    finish();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                } else {
                    loadingDialog.dismiss();
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(RealNameActivity.this,
                                createUsrResponse.msg);
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(RealNameActivity.this, error);
                }
            }
        });
    }


}
