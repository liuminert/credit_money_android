package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.DetailsAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.protocol.FindTraRecordListProtocol;
import com.tesu.creditgold.protocol.IsBindBankCardProtocol;
import com.tesu.creditgold.request.FindStoreDescByUsridRequest;
import com.tesu.creditgold.request.FindTraRecordListRequest;
import com.tesu.creditgold.request.IsBindBankCardRequest;
import com.tesu.creditgold.response.FindTraRecordListResponse;
import com.tesu.creditgold.response.IsBindBankCardResponse;
import com.tesu.creditgold.support.PercentRelativeLayout;
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
 * 交易记录
 */
public class TransactionDetailsActivity extends BaseActivity implements View.OnClickListener,
        PullToRefreshBase.OnRefreshListener {

    private TextView tv_top_back;
    private View rootView;
    private PullToRefreshListView lv_details;
    private DetailsAdapter detailsAdapter;
    private String store_id;
    private String url;
    //接口请求菊花
    private Dialog loadingDialog;
    //判断是否刷新
    private boolean isRefresh = false;
    private List<FindTraRecordListResponse.TraRecord> traRecordList;
    private int pageNo=1;
    private PercentRelativeLayout rl_no_record;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_transaction_details, null);
        setContentView(rootView);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        rl_no_record = (PercentRelativeLayout) rootView.findViewById(R.id.rl_no_record);
        lv_details=(PullToRefreshListView)rootView.findViewById(R.id.lv_details);
        initData();
        return null;
    }


    public void initData() {
        traRecordList=new ArrayList<>();
        loadingDialog = DialogUtils.createLoadDialog(TransactionDetailsActivity.this, true);
        Intent intent=getIntent();
        store_id=intent.getStringExtra("store_id");
        tv_top_back.setOnClickListener(this);
        lv_details.setMode(PullToRefreshBase.Mode.BOTH);
        lv_details.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        lv_details.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
        lv_details.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以刷新");
        lv_details.setOnRefreshListener(this);
        runAsyncTask();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_withrawal:{
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

    public void runAsyncTask() {
        loadingDialog.show();
        FindTraRecordListProtocol findTraRecordListProtocol = new FindTraRecordListProtocol();
        FindTraRecordListRequest findTraRecordListRequest = new FindTraRecordListRequest();
        url = Constants.XINYONGSERVER_URL+findTraRecordListProtocol.getApiFun();
        findTraRecordListRequest.map.put("store_id", store_id);
        findTraRecordListRequest.map.put("pageNo", String.valueOf(pageNo));
        findTraRecordListRequest.map.put("pageSize", "10");
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, findTraRecordListRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                LogUtils.e("findTraRecordListResponse:" + json);
                final FindTraRecordListResponse findTraRecordListResponse = gson.fromJson(json, FindTraRecordListResponse.class);
                LogUtils.e("findTraRecordListResponse:" + findTraRecordListResponse.toString());
                if (findTraRecordListResponse.code == 0) {
                    loadingDialog.dismiss();
                    if(findTraRecordListResponse.tra_record_list.size()>0){
                        traRecordList.addAll(findTraRecordListResponse.tra_record_list);
                        if (detailsAdapter == null) {
                            detailsAdapter = new DetailsAdapter(TransactionDetailsActivity.this, traRecordList);
                            lv_details.setAdapter(detailsAdapter);
                        }
                        else{
                            detailsAdapter.notifyDataSetChanged();
                        }
                        lv_details.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(TransactionDetailsActivity.this, TransactionInfoActivity.class);
                                intent.putExtra("traRecod", traRecordList.get(position-1));
                                UIUtils.startActivityNextAnim(intent);
                            }
                        });
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
                    loadingDialog.dismiss();
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(TransactionDetailsActivity.this,
                                findTraRecordListResponse.msg);
                    }

                }
                if (isRefresh) {
                    isRefresh = false;
                    lv_details.onRefreshComplete();
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (isRefresh) {
                    isRefresh = false;
                    lv_details.onRefreshComplete();
                }
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(TransactionDetailsActivity.this, error);
                }
            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (!isRefresh) {
            isRefresh = true;
            if (lv_details.isHeaderShown()) {
                pageNo=1;
                traRecordList.clear();
                runAsyncTask();
            } else if (lv_details.isFooterShown()) {
                pageNo++;
                runAsyncTask();
            }
        }
    }
}
