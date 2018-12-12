package com.tesu.creditgold.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.QuotaOrderAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.QuotaOrderBean;
import com.tesu.creditgold.protocol.GetUsrInfProtocol;
import com.tesu.creditgold.request.GetUsrInfRequest;
import com.tesu.creditgold.response.GetUsrInfResponse;
import com.tesu.creditgold.support.PercentLinearLayout;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class QuotaOrderListActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_top_back;
    private View rootView;
    private PercentLinearLayout ll_create_order;
    private ListView lv_order;
    private QuotaOrderAdapter mAdapter;
    private String orderBeanListStr;
    private List<GetUsrInfResponse.OrderBean> orderBeanList;
    private Gson gson;
    private Dialog loadingDialog;
    private String url;
    private String userId;
    private GetUsrInfResponse getUsrInfResponse;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_quota_order_list, null);
        setContentView(rootView);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        ll_create_order = (PercentLinearLayout) rootView.findViewById(R.id.ll_create_order);
        lv_order = (ListView) rootView.findViewById(R.id.lv_order);

        initData();

        ll_create_order.setOnClickListener(this);
        tv_top_back.setOnClickListener(this);
        return null;
    }

    private void initData() {
        userId = SharedPrefrenceUtils.getString(this, "usrid");
        loadingDialog = DialogUtils.createLoadDialog(this, true);
        gson = new Gson();
        Intent intent = getIntent();
        orderBeanListStr = intent.getStringExtra("orderBeanListStr");
        if(!TextUtils.isEmpty(orderBeanListStr)){
            orderBeanList = gson.fromJson(orderBeanListStr,new TypeToken<List<GetUsrInfResponse.OrderBean>>(){}.getType());
            mAdapter = new QuotaOrderAdapter(this,orderBeanList);
            lv_order.setAdapter(mAdapter);
        }else {
            getQuotaOrderList();
        }

        lv_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetUsrInfResponse.OrderBean orderBean = orderBeanList.get(position);
                Intent intent = new Intent(QuotaOrderListActivity.this, QuotaOrderDetailActivity.class);
                intent.putExtra("order_sn", orderBean.order_sn);
                UIUtils.startActivityForResult(intent,100);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode == 200){
            getQuotaOrderList();
        }
    }

    /**
     * 获取额度订单列表
     */
    public void getQuotaOrderList() {
        loadingDialog.show();
        GetUsrInfProtocol getUsrInfProtocol = new GetUsrInfProtocol();
        GetUsrInfRequest getUsrInfRequest = new GetUsrInfRequest();
        url = Constants.FENGKONGSERVER_URL+getUsrInfProtocol.getApiFun();
        getUsrInfRequest.map.put("usrid", userId);
        getUsrInfRequest.map.put("order_from", "1");
        LogUtils.e("userId:" + userId);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, getUsrInfRequest.map, new MyVolley.VolleyCallback() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                LogUtils.e("获取额度订单列表:" + json);
                getUsrInfResponse = gson.fromJson(json, GetUsrInfResponse.class);
                if (getUsrInfResponse.code.equals("0")) {
                    orderBeanList = getUsrInfResponse.return_param.order_list;

                    mAdapter = new QuotaOrderAdapter(QuotaOrderListActivity.this,orderBeanList);
                    lv_order.setAdapter(mAdapter);


                } else {
                    DialogUtils.showAlertDialog(QuotaOrderListActivity.this,
                            getUsrInfResponse.msg);

                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(QuotaOrderListActivity.this, error);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_top_back:
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.ll_create_order:
                Intent intent = new Intent(QuotaOrderListActivity.this,FillInInformationActivity.class);
                UIUtils.startActivityNextAnim(intent);
                setFinish();
                break;
        }
    }
}
