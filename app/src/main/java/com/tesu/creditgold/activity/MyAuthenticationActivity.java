package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.BankListAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.response.GetUsrBankListResponse;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 还款账户
 */
public class MyAuthenticationActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_top_back;
    private View rootView;
    private String usrname;
    private String id_card;
    private TextView tv_name;
    private TextView tv_id_card;
    private String url;
    //接口请求菊花
    private Dialog loadingDialog;
    private PercentRelativeLayout rl_name;
    private GetUsrBankListResponse getUsrBankListResponse;
    private Gson gson;
    private String userId;
    private List<GetUsrBankListResponse.BankBean> bankBeanList;
    private PercentRelativeLayout rl_one;
    private PercentRelativeLayout rl_two;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_my_anthentication, null);
        setContentView(rootView);
        rl_name= (PercentRelativeLayout) rootView.findViewById(R.id.rl_name);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_name= (TextView) rootView.findViewById(R.id.tv_name);
        tv_id_card= (TextView) rootView.findViewById(R.id.tv_id_card);
        rl_one= (PercentRelativeLayout) rootView.findViewById(R.id.rl_one);
        rl_two= (PercentRelativeLayout) rootView.findViewById(R.id.rl_two);
        initData();
        return null;
    }


    public void initData() {
        loadingDialog = DialogUtils.createLoadDialog(MyAuthenticationActivity.this, true);
        gson = new Gson();
        userId = SharedPrefrenceUtils.getString(this, "usrid");
//        Intent intent=getIntent();
//        type=intent.getIntExtra("type", -1);
////        if(type==1){
//            rl_name.setVisibility(View.VISIBLE);
//            usrname=intent.getStringExtra("usrname");
//            id_card=intent.getStringExtra("id_card");
//
//        if (!TextUtils.isEmpty(id_card)&&!TextUtils.isEmpty(usrname)) {
//            tv_id_card.setText("身  份  证:" + id_card.replace(id_card.substring(4, 14), "**********"));
//            tv_name.setText("真实姓名:" +usrname);
//        }
//            runAsyncTask();
//        }
//        else{
//            rl_name.setVisibility(View.GONE);
//            pr_add_card.setVisibility(View.VISIBLE);
//        }
        tv_top_back.setOnClickListener(this);
//        pr_add_card.setOnClickListener(this);
//        getUsrBankList();
    }

    private void getUsrBankList() {
        String url= Constants.FENGKONGSERVER_URL+"tsfkxt/user/getUsrBankList.do";
        Map<String,String> params = new HashMap<String,String>();
        params.put("usrid", userId);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("获得用户银行列表:" + json);
                getUsrBankListResponse = gson.fromJson(json, GetUsrBankListResponse.class);
                if (getUsrBankListResponse.getCode() == 0) {
                     bankBeanList = getUsrBankListResponse.getReturn_param().getBank_list();
                    id_card = getUsrBankListResponse.getReturn_param().getId_card();
                    usrname = getUsrBankListResponse.getReturn_param().getUsrname();
                    if(!TextUtils.isEmpty(id_card)){
                        tv_id_card.setText(id_card.replace(id_card.substring(4, 14), "**********"));
                        rl_one.setVisibility(View.VISIBLE);
                        rl_two.setVisibility(View.GONE);
                    }else {
                        rl_one.setVisibility(View.GONE);
                        rl_two.setVisibility(View.VISIBLE);
                    }
                    if(!TextUtils.isEmpty(usrname)){
                        tv_name.setText(usrname);
                    }
                } else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(MyAuthenticationActivity.this, getUsrBankListResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                LogUtils.e("获得用户银行列表:" + error);
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(MyAuthenticationActivity.this, error);
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
        }
    }
}
