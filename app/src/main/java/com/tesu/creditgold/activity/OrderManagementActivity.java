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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.OrderAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.protocol.FindFrostmoneyListProtocol;
import com.tesu.creditgold.protocol.UpdateStoreInfoProtocol;
import com.tesu.creditgold.request.FindFrostmoneyListRequest;
import com.tesu.creditgold.request.UpdateStoreInfoRequest;
import com.tesu.creditgold.response.FindFrostmoneyListResponse;
import com.tesu.creditgold.response.UpdateStoreInfoResponse;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.BankUtils;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 订单管理
 */
public class OrderManagementActivity extends BaseActivity implements View.OnClickListener,
        PullToRefreshBase.OnRefreshListener {

    private TextView tv_top_back;
    private View rootView;

    private String url;
    //接口请求菊花
    private Dialog loadingDialog;
    private PullToRefreshListView lv_order;
    private int pageNo=1;
    private String store_id;
    private List<FindFrostmoneyListResponse.FrostmoneyBean> frostmoneyBeanList;
    private OrderAdapter orderAdapter;
    //判断是否刷新
    private boolean isRefresh = false;
    private TextView tv_title;
    private int is_defrost;
    private PercentRelativeLayout rl_no_record;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_order_management, null);
        setContentView(rootView);
        lv_order=(PullToRefreshListView)rootView.findViewById(R.id.lv_order);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        rl_no_record= (PercentRelativeLayout) rootView.findViewById(R.id.rl_no_record);
        initData();
        return null;
    }


    public void initData() {
        frostmoneyBeanList=new ArrayList<>();
        Intent intent=getIntent();
        is_defrost=intent.getIntExtra("is_defrost",-1);
        store_id=intent.getStringExtra("store_id");
        loadingDialog = DialogUtils.createLoadDialog(OrderManagementActivity.this, true);
        tv_top_back.setOnClickListener(this);
        lv_order.setMode(PullToRefreshBase.Mode.BOTH);
        lv_order.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        lv_order.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
        lv_order.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以刷新");
        lv_order.setOnRefreshListener(this);
        if(is_defrost!=-1){
            tv_title.setText("冻结金额明细");
            FindFrostmoneyList();
        }else {
            FindOrderList();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_roger:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.tv_top_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
        }
    }


    /**
     * 订单管理列表
     */
    private void FindOrderList() {
        loadingDialog.show();
        FindFrostmoneyListProtocol findFrostmoneyListProtocol=new FindFrostmoneyListProtocol();
        FindFrostmoneyListRequest findFrostmoneyListRequest=new FindFrostmoneyListRequest();
        url= Constants.XINYONGSERVER_URL+findFrostmoneyListProtocol.getApiFun();
        if(is_defrost!=-1) {
            findFrostmoneyListRequest.map.put("is_defrost", "0");
        }
        findFrostmoneyListRequest.map.put("store_id", store_id);
        findFrostmoneyListRequest.map.put("pageNo", String.valueOf(pageNo));
        findFrostmoneyListRequest.map.put("pageSize", "10");
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, findFrostmoneyListRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                LogUtils.e("订单管理:" + json.toString());
                FindFrostmoneyListResponse findFrostmoneyListResponse = gson.fromJson(json, FindFrostmoneyListResponse.class);
                if (findFrostmoneyListResponse.code == 0) {
                    if(findFrostmoneyListResponse.dataList.size()>0) {
                        frostmoneyBeanList.addAll(findFrostmoneyListResponse.dataList);
                        if(orderAdapter==null){
                            orderAdapter=new OrderAdapter(OrderManagementActivity.this,frostmoneyBeanList);
                            lv_order.setAdapter(orderAdapter);
                        }
                        else{
                            orderAdapter.notifyDataSetChanged();
                        }
                        rl_no_record.setVisibility(View.GONE);
                    }
                    else {
                        if(pageNo==1){
                            rl_no_record.setVisibility(View.VISIBLE);
                        }
                        else{
                            rl_no_record.setVisibility(View.GONE);
                        }
                    }
                } else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(OrderManagementActivity.this,
                                findFrostmoneyListResponse.msg);
                    }
                }
                if (isRefresh) {
                    isRefresh = false;
                    lv_order.onRefreshComplete();
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (isRefresh) {
                    isRefresh = false;
                    lv_order.onRefreshComplete();
                }
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(OrderManagementActivity.this,
                            error);
                }
            }
        });
    }

    /**
     * 冻结金额列表
     */
    private void FindFrostmoneyList() {
        loadingDialog.show();
        FindFrostmoneyListProtocol findFrostmoneyListProtocol=new FindFrostmoneyListProtocol();
        FindFrostmoneyListRequest findFrostmoneyListRequest=new FindFrostmoneyListRequest();
        url= Constants.XINYONGSERVER_URL+findFrostmoneyListProtocol.getApiFun();
        if(is_defrost!=-1) {
            findFrostmoneyListRequest.map.put("is_defrost", "0");
        }
        findFrostmoneyListRequest.map.put("store_id", store_id);
        findFrostmoneyListRequest.map.put("pageNo", String.valueOf(pageNo));
        findFrostmoneyListRequest.map.put("pageSize", "10");
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, findFrostmoneyListRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                LogUtils.e("订单管理:" + json.toString());
                FindFrostmoneyListResponse findFrostmoneyListResponse = gson.fromJson(json, FindFrostmoneyListResponse.class);
                if (findFrostmoneyListResponse.code == 0) {
                    if(findFrostmoneyListResponse.dataList.size()>0) {
                        frostmoneyBeanList.addAll(findFrostmoneyListResponse.dataList);
                        if(orderAdapter==null){
                            orderAdapter=new OrderAdapter(OrderManagementActivity.this,frostmoneyBeanList);
                            lv_order.setAdapter(orderAdapter);
                        }
                        else{
                            orderAdapter.notifyDataSetChanged();
                        }
                        rl_no_record.setVisibility(View.GONE);
                    }
                    else {
                        if(pageNo==1){
                            rl_no_record.setVisibility(View.VISIBLE);
                        }
                        else{
                            rl_no_record.setVisibility(View.GONE);
                        }
                    }
                } else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(OrderManagementActivity.this,
                                findFrostmoneyListResponse.msg);
                    }
                }
                if (isRefresh) {
                    isRefresh = false;
                    lv_order.onRefreshComplete();
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (isRefresh) {
                    isRefresh = false;
                    lv_order.onRefreshComplete();
                }
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(OrderManagementActivity.this,
                            error);
                }
            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (!isRefresh) {
            isRefresh = true;
            if (lv_order.isHeaderShown()) {
                pageNo = 1;
                frostmoneyBeanList.clear();
                FindFrostmoneyList();
            } else if (lv_order.isFooterShown()) {
                pageNo++;
                FindFrostmoneyList();
            }
        }
    }
}
