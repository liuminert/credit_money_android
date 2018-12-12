package com.tesu.creditgold.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.GetStoreDescByUsridResponse;
import com.tesu.creditgold.bean.ShopBean;
import com.tesu.creditgold.response.GetUsrOrderListResponse;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.UIUtils;

import java.util.HashMap;

public class ContinueOrderActivity extends BaseActivity implements View.OnClickListener{
    private View rootView;
    private TextView tv_top_back;
    private TextView tv_shop_name;
    private TextView tv_amount;
    private TextView tv_period;
    private TextView tv_money;
    private Button btn_reset;
    private Button btn_continue;
    private Dialog loadingDialog;
    private GetUsrOrderListResponse.OrderBean orderBean;
    private Gson gson;
    private GetStoreDescByUsridResponse getStoreDescByUsridResponse;
    private ShopBean shopBean;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_continue_order, null);
        setContentView(rootView);

        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_shop_name = (TextView) rootView.findViewById(R.id.tv_shop_name);
        tv_amount = (TextView) rootView.findViewById(R.id.tv_amount);
        tv_period = (TextView) rootView.findViewById(R.id.tv_period);
        tv_money = (TextView) rootView.findViewById(R.id.tv_money);
        btn_reset = (Button) rootView.findViewById(R.id.btn_reset);
        btn_continue = (Button) rootView.findViewById(R.id.btn_continue);

        tv_top_back.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        btn_continue.setOnClickListener(this);

        loadingDialog = DialogUtils.createLoadDialog(this, true);
        gson = new Gson();
        initData();

        return null;
    }

    private void initData() {
        Intent intent=getIntent();
        orderBean = (GetUsrOrderListResponse.OrderBean) intent.getSerializableExtra("orderBean");
        if(orderBean != null){
            LogUtils.e("orderBean:"+orderBean.toString());
            if(!TextUtils.isEmpty(orderBean.store_name)){
                tv_shop_name.setText(orderBean.store_name);
            }
            tv_amount.setText(orderBean.order_money+"元");
            tv_period.setText(orderBean.amortization_cnt+"个月");
            if(!TextUtils.isEmpty(orderBean.amortization_money)){
                tv_money.setText(orderBean.amortization_money);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_top_back:
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.btn_reset:
                if(orderBean != null){
                    if(orderBean.order_from==1){
                        Intent intent = new Intent(ContinueOrderActivity.this,UseQuotaFillInfomationActivity.class);
                        UIUtils.startActivityNextAnim(intent);
                    }else {
                        findStoreDescById(orderBean.store_uid);
                    }
                }
                break;
            case R.id.btn_continue:
                if(orderBean != null){
                    goToActivity();
                }
                break;
        }

    }

    /**
     *根据id店铺详情
     */
    private void findStoreDescById(String usr_id) {
        String url = Constants.XINYONGSERVER_URL+"credit_money_background/user/findStoreDescByUsrid.do";
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("usr_id", usr_id);
        LogUtils.e("usr_id:" + usr_id);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("获得店铺详情:" + json.toString());
                getStoreDescByUsridResponse = gson.fromJson(json, GetStoreDescByUsridResponse.class);
                if (getStoreDescByUsridResponse.getCode() == 0) {
                    shopBean = getStoreDescByUsridResponse.getStore_info();
                    Intent intent = new Intent(ContinueOrderActivity.this, InstallmentPaymentActivity.class);
                    if (shopBean != null) {
                        intent.putExtra("store_id", shopBean.getStore_id());
                        intent.putExtra("store_type", shopBean.getStore_type());
                        intent.putExtra("store_name", shopBean.getStore_name());
                        intent.putExtra("store_contract", shopBean.getContract_name());
                        intent.putExtra("store_tel", shopBean.getContract_telephone());
                        intent.putExtra("usr_id", shopBean.getUsr_id());
                        intent.putExtra("is_tiexi", shopBean.getIs_tiexi());
                        intent.putExtra("gather_model_id", shopBean.getGather_model_id());
                    }
                    UIUtils.startActivityNextAnim(intent);

                } else {
                    DialogUtils.showAlertDialog(ContinueOrderActivity.this,
                            getStoreDescByUsridResponse.getMsg());
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(ContinueOrderActivity.this,
                        error);
            }
        });
    }

    /**
     * 继续提交，跳转到不同activity
     */
    private void goToActivity() {
        switch (orderBean.submit_step){
            case 0:{
                Intent intent=new Intent(this, IdentityAuthenticationActivity.class);
                intent.putExtra("gather_model_id", orderBean.scene_id);
                intent.putExtra("usr_order_id", orderBean.usr_order_id);
                intent.putExtra("name", orderBean.usrname);
                intent.putExtra("idNumber", orderBean.id_card);
                intent.putExtra("store_type", orderBean.order_type);
                intent.putExtra("amount", orderBean.order_money+"");
                intent.putExtra("amortization_money", orderBean.amortization_money);
                intent.putExtra("order_from",orderBean.order_from);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case 1:{
                Intent intent=new Intent(this, ShowLiveActivity.class);
                intent.putExtra("gather_model_id", orderBean.scene_id);
                intent.putExtra("usr_order_id", orderBean.usr_order_id);
                intent.putExtra("name", orderBean.usrname);
                intent.putExtra("idNumber", orderBean.id_card);
                intent.putExtra("store_type", orderBean.order_type);
                intent.putExtra("amount", orderBean.order_money+"");
                intent.putExtra("amortization_money", orderBean.amortization_money);
                intent.putExtra("order_from",orderBean.order_from);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case 2:{
                Intent intent=new Intent(this, InformationAuthenticationActivity.class);
                intent.putExtra("gather_model_id", orderBean.scene_id);
                intent.putExtra("usr_order_id", orderBean.usr_order_id);
                intent.putExtra("name", orderBean.usrname);
                intent.putExtra("idNumber", orderBean.id_card);
                intent.putExtra("store_type", orderBean.order_type);
                intent.putExtra("isFromRecord",true);
                intent.putExtra("amount", orderBean.order_money+"");
                intent.putExtra("amortization_money", orderBean.amortization_money);
                intent.putExtra("order_from",orderBean.order_from);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case 3:{
                Intent intent=new Intent();
                if(orderBean.order_from == 1){
                    intent.setClass(this, PackagingAgreementActivity.class);
                }else {
                    switch (orderBean.scene_id) {//1 旅游，  2 家具，  3 生活，  4 装修，  5 3C数码，  6 教育，  7 医美，  8 电商，  9 车类，  10 烟酒补品，  11 名品，  12 其他  13 木材分期
                        case 1:
                            intent.setClass(this, TourismPackagingActivity.class);
                            break;
                        case 2:
                            intent.setClass(this, FurniturePackagingActivity.class);
                            break;
                        case 3:
                            intent.setClass(this, LifePackagingActivity.class);
                            break;
                        case 4:
                            intent.setClass(this, HomePackagingActivity.class);
                            break;
                        case 5:
                            intent.setClass(this, DigitalPackagingActivity.class);
                            break;
                        case 6:
                            intent.setClass(this, EducationPackagingActivity.class);
                            break;
                        case 7:
                            intent.setClass(this, MedicalBeautyPackagingActivity.class);
                            break;
                        case 8:
                            intent.setClass(this, OnlinePackagingActivity.class);
                            break;
                        case 9:
                            intent.setClass(this, CarPackagingActivity.class);
                            break;
                        case 10:
                        case 11:
                        case 12:
                            intent.setClass(this, OtherPackagingActivity.class);
                            break;
                        case 13:
                            intent.setClass(this, WoodPackagingActivity.class);
                            break;
                        default:
                            intent.setClass(this, OtherPackagingActivity.class);
                            break;
                    }
                }
                intent.putExtra("gather_model_id", ""+orderBean.scene_id);
                intent.putExtra("usr_order_id", orderBean.usr_order_id);
                intent.putExtra("store_type", orderBean.order_type);
                intent.putExtra("isFromRecord",true);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case 4:{
                Intent intent=new Intent(this, PackagingAgreementActivity.class);
                intent.putExtra("gather_model_id", orderBean.scene_id);
                intent.putExtra("usr_order_id", orderBean.usr_order_id);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case 5:{
                break;
            }
        }
        setFinish();
    }
}
