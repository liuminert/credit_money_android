package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.MyInformationListAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.MyInformationBean;
import com.tesu.creditgold.fragment.ControlTabFragment;
import com.tesu.creditgold.response.GetMessageListResponse;
import com.tesu.creditgold.response.GetUnReadCountResponse;
import com.tesu.creditgold.support.PercentLinearLayout;
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
 * 账户管理
 */
public class MyInformationListActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;

    private TextView tv_read;
    private ListView lv_information;
    private Dialog loadingDialog;
    private GetMessageListResponse getMessageListResponse;
    private List<MyInformationBean> myInformationBeanList;
    private MyInformationListAdapter myInformationListAdapter;
    private ControlTabFragment ctf;
    private Gson gson;
    private FkBaseResponse fkBaseResponse;
    private LinearLayout ll_one;
    private PercentLinearLayout ll_two;
    private String tell;
    private boolean isFromReceiver;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_my_information_list, null);
        setContentView(rootView);
        tv_read= (TextView) rootView.findViewById(R.id.tv_read);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        lv_information = (ListView) rootView.findViewById(R.id.lv_information);
        ll_one = (LinearLayout) rootView.findViewById(R.id.ll_one);
        ll_two = (PercentLinearLayout) rootView.findViewById(R.id.ll_two);
        initData();
        return null;
    }


    public void initData() {
        loadingDialog = DialogUtils.createLoadDialog(this, true);
        gson = new Gson();
        tell = SharedPrefrenceUtils.getString(this, "mobile_phone");

        if (ctf == null) {
            ctf = MainActivity.getCtf();
        }

        Intent intent = getIntent();
        isFromReceiver = intent.getBooleanExtra("isFromReceiver",false);
        if(isFromReceiver){
            if(!TextUtils.isEmpty(tell)){
                ctf.getTabMyselfbyPager().getUnReadCount(tell);
            }
        }

        tv_top_back.setOnClickListener(this);
        tv_read.setOnClickListener(this);

        lv_information.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyInformationBean myInformationBean = myInformationBeanList.get(position);
                Intent intent = new Intent(MyInformationListActivity.this, MyInformationInfoActivity.class);
                intent.putExtra("myInformationBean", myInformationBean);
                UIUtils.startActivityForResult(intent, 100);

            }
        });

        if(!TextUtils.isEmpty(tell)){
            getMessageListByPhone();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100){
            getMessageListByPhone();
            if(!TextUtils.isEmpty(tell)){
                ctf.getTabMyselfbyPager().getUnReadCount(tell);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_read:
                if(myInformationBeanList != null && myInformationBeanList.size()>0){
                    for(int i=0;i<myInformationBeanList.size();i++){
                        MyInformationBean myInformationBean = myInformationBeanList.get(i);
                        if(i == myInformationBeanList.size()-1){
                            updateMessageReadFlag(myInformationBean.getId(),true);
                        }else{
                            updateMessageReadFlag(myInformationBean.getId(),false);
                        }
                    }
                }
                break;
            case R.id.tv_top_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
        }
    }

    /**
     * 消息列表查询
     */
    private void getMessageListByPhone() {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/message/messageListByPhone.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("plateform", "android");
        params.put("alias", tell);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("消息列表查询:" + json);
                getMessageListResponse = gson.fromJson(json, GetMessageListResponse.class);
                if (getMessageListResponse.getCode() == 0) {
                    myInformationBeanList = getMessageListResponse.getData();
                    if(myInformationBeanList != null){
                        if(myInformationBeanList.size()>0){
                            myInformationListAdapter = new MyInformationListAdapter(MyInformationListActivity.this,myInformationBeanList);
                            lv_information.setAdapter(myInformationListAdapter);

                            ll_one.setVisibility(View.VISIBLE);
                            ll_two.setVisibility(View.GONE);
                        }else {
                            ll_one.setVisibility(View.GONE);
                            ll_two.setVisibility(View.VISIBLE);
                        }

                    }

                } else {
                    DialogUtils.showAlertDialog(MyInformationListActivity.this, getMessageListResponse.getMsg());
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(MyInformationListActivity.this, error);
            }
        });
    }

    /**
     * 消息浏览状态修改
     */
    private void updateMessageReadFlag(int messageId, final boolean isLast) {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/message/updateMessageReadFlag.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("messageId", String.valueOf(messageId));
        params.put("readFlag", "1");
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                if(isLast){
                    loadingDialog.dismiss();
                    getMessageListByPhone();
                    if(!TextUtils.isEmpty(tell)){
                        ctf.getTabMyselfbyPager().getUnReadCount(tell);
                    }
                }
                LogUtils.e("消息浏览状态修改:" + json);
                fkBaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkBaseResponse.getCode() == 0) {

                } else {
                    DialogUtils.showAlertDialog(MyInformationListActivity.this, fkBaseResponse.getMsg());
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                if(isLast){
                    loadingDialog.dismiss();
                    getMessageListByPhone();
                    if(!TextUtils.isEmpty(tell)){
                        ctf.getTabMyselfbyPager().getUnReadCount(tell);
                    }
                }
                DialogUtils.showAlertDialog(MyInformationListActivity.this, error);
            }
        });
    }

}
