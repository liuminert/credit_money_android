package com.tesu.creditgold.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.DictionaryAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.DictionaryBean;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.GetInterestsResponse;
import com.tesu.creditgold.bean.InterestsBean;
import com.tesu.creditgold.bean.PictureItemBean;
import com.tesu.creditgold.bean.ShopBean;
import com.tesu.creditgold.bean.UsrOrderPicBean;
import com.tesu.creditgold.response.GetQuotaOrderInfoResponse;
import com.tesu.creditgold.support.PercentLinearLayout;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UseQuotaFillInfomationActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = UseQuotaFillInfomationActivity.class.getSimpleName();
    private TextView tv_top_back;
    private View rootView;
    private EditText et_order_id;  //订单号
    private TextView et_total_balance;  //交易金额
    private TextView tv_periods;  //期数
    private TextView tv_balance;  //每期应还
    private Button btn_submit;
    private String order_id;
    private Dialog loadingDialog;
    private Gson gson;
    private GetQuotaOrderInfoResponse getQuotaOrderInfoResponse;
    private double total_balance;
    private String periods;
    private GetInterestsResponse getInterestsResponse;
    private InterestsBean interestsBean;
    private int is_tiexi;
    private int store_id;
    private int interest_mode = 0;  //计息方式：0：按月，1：按天，2：按季度，不填默认为月；
    private String amortization_money;
    private String userId;
    private FkBaseResponse fkbaseResponse;
    private LayoutInflater mInflater;
    private PopupWindow dictonaryWindow;
    private View dictonaryRoot;
    private ListView lv_choose;
    private Button btn_cancel;
    private TextView tv_title;
    private TextView tv_complete;
    private ArrayList<DictionaryBean> dictionaryBeanList;
    private int oldPosition;
    private DictionaryBean dictionaryBean;
    private String usr_order_id;
//    private String available_quota;
    private ShopBean shopBean;
    private int order_from = 1;   //订单来源 0：买家下单，1：卖家下单
    private CheckBox is_checked;
    private TextView tv_contract;
    private TextView tv_contract2;
    private TextView tv_contract3;


    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_use_quota_fill_infomation, null);
        setContentView(rootView);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        et_order_id = (EditText) rootView.findViewById(R.id.et_order_id);
        et_total_balance = (TextView) rootView.findViewById(R.id.et_total_balance);
        tv_periods = (TextView) rootView.findViewById(R.id.tv_periods);
        tv_balance = (TextView) rootView.findViewById(R.id.tv_balance);
        btn_submit = (Button) rootView.findViewById(R.id.btn_submit);
        is_checked= (CheckBox) rootView.findViewById(R.id.is_checked);
        tv_contract= (TextView) rootView.findViewById(R.id.tv_contract);
        tv_contract2= (TextView) rootView.findViewById(R.id.tv_contract2);
        tv_contract3= (TextView) rootView.findViewById(R.id.tv_contract3);

        initData();

//        et_order_id.setOnFocusChangeListener(new android.view.View.
//                OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    // 此处为得到焦点时的处理内容
//                } else {
//                    // 此处为失去焦点时的处理内容
//                    order_id = et_order_id.getText().toString();
//                    if (!TextUtils.isEmpty(order_id)) {
//                        getOrderInfo();
//                    }
//                }
//            }
//        });
        is_checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_submit.setBackgroundResource(R.mipmap.btn_sub);
//                    btn_submit.setEnabled(true);
                } else {
                    btn_submit.setBackgroundResource(R.mipmap.btn_gray);
//                    btn_submit.setEnabled(false);
                }
            }
        });

        tv_top_back.setOnClickListener(this);
        et_total_balance.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        tv_periods.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        tv_complete.setOnClickListener(this);
        tv_contract.setOnClickListener(this);
        tv_contract2.setOnClickListener(this);
        tv_contract3.setOnClickListener(this);
        return null;
    }

    /**
     * 通过订单号获取订单信息
     */
    private void getOrderInfo() {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/getUsrOrderInf.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("order_sn", order_id);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Log.e(TAG, "获得额度订单详情返回:" + json);
                getQuotaOrderInfoResponse = gson.fromJson(json, GetQuotaOrderInfoResponse.class);
                if (getQuotaOrderInfoResponse.getCode() == 0) {
                    total_balance = getQuotaOrderInfoResponse.getReturn_param().getOrder_money();
                    if (total_balance != 0) {
                        et_total_balance.setText(total_balance + "");
                    }
                    shopBean = getQuotaOrderInfoResponse.getReturn_param().getStore_inf();
                    if (shopBean != null) {
                        is_tiexi = shopBean.getIs_tiexi();
                        store_id = shopBean.getStore_id();
                    }
                    usr_order_id = getQuotaOrderInfoResponse.getReturn_param().getUsr_order_id();

                    DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(UseQuotaFillInfomationActivity.this, dictionaryBeanList);
                    lv_choose.setAdapter(dictionaryAdapter);
                    tv_title.setText("选择期数");
                    dictonaryWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    int index;
                    if (dictionaryBean != null) {
                        index = dictionaryBeanList.indexOf(dictionaryBean);
                        if (index != -1) {
                            lv_choose.setSelection(oldPosition);
                            dictionaryAdapter.setSelectItem(index);
                        }
                    }
                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(UseQuotaFillInfomationActivity.this, getQuotaOrderInfoResponse.getMsg());
                    }

                    shopBean = null;
                    et_total_balance.setText("");
                    is_tiexi = 0;
                    store_id = 0;
                    usr_order_id = null;
                    tv_periods.setText("");
                    dictionaryBean = null;
                    tv_balance.setText("");
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                Log.e(TAG, "获得额度订单详情返回:" + error);
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(UseQuotaFillInfomationActivity.this, error);
                }

                shopBean = null;
                et_total_balance.setText("");
                is_tiexi = 0;
                store_id = 0;
                usr_order_id = null;
                tv_periods.setText("");
                dictionaryBean = null;
                tv_balance.setText("");

            }
        });

    }

    /**
     * 分期购利息计算
     */
    private void getInterestInf() {
        loadingDialog.show();
        String url = Constants.XINYONGSERVER_URL + "credit_money_background/user/getInterestInf.do";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("amount", total_balance + "");
        params.put("interest_type", is_tiexi + "");
        params.put("period", periods + "");
        params.put("store_id", store_id + "");
        params.put("interest_mode", interest_mode + "");
        LogUtils.e("分期购parmas:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("分期购利息计算:" + json.toString());
                getInterestsResponse = gson.fromJson(json, GetInterestsResponse.class);
                if (getInterestsResponse.getCode() == 0) {
                    HashMap<String, Object> hs = gson.fromJson(gson.toJson(getInterestsResponse.getReturn_param()), HashMap.class);
                    Object result = hs.get("interests");
                    interestsBean = gson.fromJson(gson.toJson(result), InterestsBean.class);
                    double total = Double.valueOf(interestsBean.getPrincipal_interest()) + Double.valueOf(interestsBean.getFactorage());
                    DecimalFormat df = new DecimalFormat("###.00");
                    amortization_money = df.format(total);
                    if (!TextUtils.isEmpty(amortization_money)) {
                        tv_balance.setText(amortization_money);
                    }

                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(UseQuotaFillInfomationActivity.this,
                                getInterestsResponse.getMsg());
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(UseQuotaFillInfomationActivity.this,
                            error);
                }
            }
        });
    }

    private void initData() {
//        Intent intent = getIntent();
//        available_quota = intent.getStringExtra("available_quota");

        dictionaryBeanList = new ArrayList<DictionaryBean>();

        DictionaryBean dictionaryBean1 = new DictionaryBean();
        dictionaryBean1.setDictionary_value_key(3);
        dictionaryBean1.setDictionary_value_name("3个月");
        dictionaryBeanList.add(dictionaryBean1);

        DictionaryBean dictionaryBean2 = new DictionaryBean();
        dictionaryBean2.setDictionary_value_key(6);
        dictionaryBean2.setDictionary_value_name("6个月");
        dictionaryBeanList.add(dictionaryBean2);

        DictionaryBean dictionaryBean3 = new DictionaryBean();
        dictionaryBean3.setDictionary_value_key(12);
        dictionaryBean3.setDictionary_value_name("12个月");
        dictionaryBeanList.add(dictionaryBean3);

        gson = new Gson();
        loadingDialog = DialogUtils.createLoadDialog(UseQuotaFillInfomationActivity.this, false);
        userId = SharedPrefrenceUtils.getString(this, "usrid");

        if (mInflater == null) {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        dictonaryRoot = mInflater.inflate(R.layout.choose_dialog, null);
        dictonaryWindow = new PopupWindow(dictonaryRoot, ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.FILL_PARENT);
        dictonaryWindow.setFocusable(true);
        lv_choose = (ListView) dictonaryRoot.findViewById(R.id.lv_choose);
        btn_cancel = (Button) dictonaryRoot.findViewById(R.id.btn_cancel);
        tv_title = (TextView) dictonaryRoot.findViewById(R.id.tv_title);
        tv_complete = (TextView) dictonaryRoot.findViewById(R.id.tv_complete);

        lv_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dictonaryWindow.dismiss();

                oldPosition = lv_choose.getFirstVisiblePosition();
                dictionaryBean = dictionaryBeanList.get(position);
                if (dictionaryBean != null) {
                    tv_periods.setText(dictionaryBean.getDictionary_value_name());
                    periods = String.valueOf(dictionaryBean.getDictionary_value_key());
                    getInterestInf();
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_contract:{
                Intent intent = new Intent(UseQuotaFillInfomationActivity.this,XieYiWebActivity.class);
                intent.putExtra("article_id",Constants.LoanAgreementId);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.tv_contract2:
            case R.id.tv_contract3:
                Intent intent1 = new Intent(UseQuotaFillInfomationActivity.this,XieYiWebActivity.class);
                intent1.putExtra("article_id",Constants.PersonalInformationId);
                UIUtils.startActivityNextAnim(intent1);
                break;
            case R.id.tv_top_back:
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.btn_submit:
//                Intent intent = new Intent(UseQuotaFillInfomationActivity.this,ApplicationLimitSuccessActivity.class);
//                UIUtils.startActivityNextAnim(intent);

                if(!is_checked.isChecked()){
                    UIUtils.showToastCenter(UseQuotaFillInfomationActivity.this, "请勾选同意借款协议和个人信息使用授权书");
                    return;
                }

                String totalMoney = et_total_balance.getText().toString();
                if (TextUtils.isEmpty(totalMoney)) {
                    UIUtils.showToastCenter(UseQuotaFillInfomationActivity.this, "订单金额不能为空");
                    return;
                }
                if (TextUtils.isEmpty(periods) || interestsBean == null) {
                    UIUtils.showToastCenter(UseQuotaFillInfomationActivity.this, "请选择期数");
                    return;
                }
//                double quota = Double.valueOf(available_quota);
//                double money = Double.valueOf(totalMoney);
//                if (money > quota) {
//                    UIUtils.showToastCenter(UseQuotaFillInfomationActivity.this, "订单总额不能超过可用额度");
//                    return;
//                }
//                btn_submit.setEnabled(false);
                btn_submit.setBackgroundResource(R.mipmap.btn_gray);
                setCreditInf();
                break;
            case R.id.tv_periods:
                if(shopBean == null){
                    order_id = et_order_id.getText().toString();
                    if (!TextUtils.isEmpty(order_id)) {
                        getOrderInfo();
                    }
                }else {
                    DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(UseQuotaFillInfomationActivity.this, dictionaryBeanList);
                    lv_choose.setAdapter(dictionaryAdapter);
                    tv_title.setText("选择期数");
                    dictonaryWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    int index;
                    if (dictionaryBean != null) {
                        index = dictionaryBeanList.indexOf(dictionaryBean);
                        if (index != -1) {
                            lv_choose.setSelection(oldPosition);
                            dictionaryAdapter.setSelectItem(index);
                        }
                    }
                }
                break;
            case R.id.btn_cancel: //选择取消
            case R.id.tv_complete: //选择完成
                dictonaryWindow.dismiss();
                break;
            case R.id.et_total_balance:
                order_id = et_order_id.getText().toString();
                if (!TextUtils.isEmpty(order_id)) {
                    getOrderInfo();
                }
                break;
        }
    }

    /**
     * 写入授信资料
     */
    private void setCreditInf() {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/setOrdersInf.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("usrid", userId);
        params.put("submit_step", "0");
        params.put("amortization_cnt", periods);
        params.put("usr_order_id", usr_order_id + "");
        params.put("order_source","0");
        params.put("borrow_interest", interestsBean.getInterest_total()); //利息
        params.put("each_amount", interestsBean.getPrincipal_interest()); //每期还款金额
        params.put("borrow_interest_rate", interestsBean.getBorrow_interest_rate() + ""); //年化率（12.8%写入12.8）
        params.put("store_charge_rate", interestsBean.getStore_charge_rate() + ""); //商家服务费费率（12.8%写入12.8）
        params.put("user_charge_rate", interestsBean.getUser_charge_rate() + ""); //链金所手续费费率（12.8%写入12.8）
        params.put("borrow_duration", interestsBean.getPeriod() + ""); //借款期限(月标和季标为月数，天标为天数)
        params.put("fee", interestsBean.getFactorage()); //每期服务费(金额)
        params.put("interest_type", is_tiexi + ""); //类型，0：等额本息，1：等本降息
        params.put("borrow_use", "9"); //借款用途，1 ： '短期周转', 2： '生意周转', 3： '生活周转', 4 ：'购物消费', 5 ： '不提现借款', 6 ： '创业借款', 7 ： '其它借款'，8：'装修借款'（4：对应乐购分期，8对应：乐装分期），9：信用千金
        LogUtils.e("params:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                if(is_checked.isChecked()){
//                    btn_submit.setEnabled(true);
                    btn_submit.setBackgroundResource(R.mipmap.btn_sub);
                }
                loadingDialog.dismiss();
                Log.e(TAG, "写入征信资料返回:" + json);
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkbaseResponse.getCode() == 0) {
//                    Intent intent = new Intent(UseQuotaFillInfomationActivity.this, PackagingAgreementActivity.class);
                    Intent intent = new Intent(UseQuotaFillInfomationActivity.this, IdentityAuthenticationActivity.class);
                    intent.putExtra("usr_order_id", usr_order_id);
                    intent.putExtra("gather_model_id", shopBean.getGather_model_id());
                    intent.putExtra("store_type", shopBean.getStore_type());
                    intent.putExtra("amount", total_balance);
                    intent.putExtra("amortization_money", amortization_money);
                    intent.putExtra("order_from",order_from);
                    setFinish();
                    UIUtils.startActivityNextAnim(intent);
                } else if (fkbaseResponse.getCode() == 2) {
                    Intent intent = new Intent(UseQuotaFillInfomationActivity.this, LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(UseQuotaFillInfomationActivity.this, fkbaseResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                if(is_checked.isChecked()){
//                    btn_submit.setEnabled(true);
                    btn_submit.setBackgroundResource(R.mipmap.btn_sub);
                }
                loadingDialog.dismiss();
                Log.e(TAG, "写入征信资料返回错误:" + error);
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(UseQuotaFillInfomationActivity.this, error);
                }

            }
        });
    }
}
