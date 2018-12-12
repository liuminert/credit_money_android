package com.tesu.creditgold.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.RecordInfoAdapter;
import com.tesu.creditgold.adapter.RecordInfoNewAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.fragment.ControlTabFragment;
import com.tesu.creditgold.protocol.GetRepaymentListProtocol;
import com.tesu.creditgold.request.GetRepaymentListRequest;
import com.tesu.creditgold.response.GetRepaymentListResponse;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.UIUtils;

import java.io.Serializable;
import java.util.List;

public class RecodeDetailActivity extends BaseActivity implements View.OnClickListener,RecordInfoNewAdapter.CallBack {
    private View rootView;
    private TextView tv_top_back;
    private ControlTabFragment ctf;
    private Dialog loadingDialog;
    private TextView tv_order_id;

    private List<GetRepaymentListResponse.RepaymentBean> list;
    private ListView lv_record_info;
    private RecordInfoNewAdapter recordInfoNewAdapter;
    private String order_sn;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_recode_detail, null);
        setContentView(rootView);

        tv_order_id=(TextView)rootView.findViewById(R.id.tv_order_id);
        lv_record_info=(ListView)rootView.findViewById(R.id.lv_record_info);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);

        Intent intent = getIntent();
        order_sn = intent.getStringExtra("order_id");

        loadingDialog = DialogUtils.createLoadDialog(this, false);

        tv_top_back.setOnClickListener(this);

        if(!TextUtils.isEmpty(order_sn)){
            tv_order_id.setText("交易单号:" + order_sn);
        }

        runAsyncTask();
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            runAsyncTask();
        }
    }

    public void runAsyncTask() {
        loadingDialog.show();
        GetRepaymentListProtocol getRepaymentListProtocol = new GetRepaymentListProtocol();
        GetRepaymentListRequest getRepaymentListRequest = new GetRepaymentListRequest();
        String url = Constants.XINYONGSERVER_URL+getRepaymentListProtocol.getApiFun();
        getRepaymentListRequest.map.put("order_id", order_sn);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, getRepaymentListRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                LogUtils.e("还款详情:" + json);
                Gson gson = new Gson();
                GetRepaymentListResponse getRepaymentListResponse = gson.fromJson(json, GetRepaymentListResponse.class);
                if (getRepaymentListResponse != null) {
                    if (getRepaymentListResponse.code.equals("0")) {
                        loadingDialog.dismiss();
                        list = getRepaymentListResponse.return_param.repayment_list;
                        if(list != null && list.size()>0){
                            list.get(0).isShowing= true;
                            recordInfoNewAdapter = new RecordInfoNewAdapter(RecodeDetailActivity.this,list);
                            recordInfoNewAdapter.setCallBack(RecodeDetailActivity.this);
                            lv_record_info.setAdapter(recordInfoNewAdapter);
                        }
                    } else {
                        loadingDialog.dismiss();
                        if(!isFinishing()){
                            DialogUtils.showAlertDialog(RecodeDetailActivity.this,
                                    getRepaymentListResponse.msg);
                        }

                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(RecodeDetailActivity.this, error);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 取消
            case R.id.rl_close: {
                finish();
                break;
            }
            case R.id.tv_top_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void callback(int position) {
        lv_record_info.setSelection(position);
    }
}
