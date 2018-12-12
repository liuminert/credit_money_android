package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.ShopAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.GetStoreInfoResponse;
import com.tesu.creditgold.bean.GetStoreListResponse;
import com.tesu.creditgold.bean.PictureItemBean;
import com.tesu.creditgold.bean.ShopBean;
import com.tesu.creditgold.support.PercentLinearLayout;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.PictureOption;
import com.tesu.creditgold.util.UIUtils;

import java.util.HashMap;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 关于我们页面
 */
public class ShopInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;
    private Button btn_pay;
    private int store_id;
    private GetStoreInfoResponse getStoreInfoResponse;
    private Gson gson;
    private ShopBean shopBean;
    private Dialog loadingDialog;

    private ImageView iv_shop;
    private TextView tv_shop_name;
    private TextView tv_address;
    private TextView tv_tell;
    private TextView tv_introduce;  //店铺介绍
    private TextView tv_certificate; //资质证书
    private TextView tv_tell_title; //联系方式
    private ImageLoader imageLoader;
    private TextView tv_description;
    private ImageView iv_licence;
//    private TextView tv_no_message;
    private int from_payment;
    private ImageView iv_watermark;
    private TextView tv_tell_name;
    private TextView tv_tell_phone;
    private TextView tv_tell_address;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_shopinfo, null);
        setContentView(rootView);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        btn_pay=(Button)rootView.findViewById(R.id.btn_pay);
        iv_shop= (ImageView) rootView.findViewById(R.id.iv_shop);
        tv_shop_name= (TextView) rootView.findViewById(R.id.tv_shop_name);
        tv_address= (TextView) rootView.findViewById(R.id.tv_address);
        tv_tell= (TextView) rootView.findViewById(R.id.tv_tell);
        tv_introduce= (TextView) rootView.findViewById(R.id.tv_introduce);
        tv_certificate= (TextView) rootView.findViewById(R.id.tv_certificate);
        tv_tell_title= (TextView) rootView.findViewById(R.id.tv_tell_title);
        tv_description= (TextView) rootView.findViewById(R.id.tv_description);
        iv_licence= (ImageView) rootView.findViewById(R.id.iv_licence);
//        tv_no_message= (TextView) rootView.findViewById(R.id.tv_no_message);
        iv_watermark= (ImageView) rootView.findViewById(R.id.iv_watermark);
        tv_tell_phone= (TextView) rootView.findViewById(R.id.tv_tell_phone);
        tv_tell_address= (TextView) rootView.findViewById(R.id.tv_tell_address);
        tv_tell_name= (TextView) rootView.findViewById(R.id.tv_tell_name);
        initData();
        return null;
    }

    @Override
    protected void onResume() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        super.onResume();
    }

    public void initData() {
        loadingDialog = DialogUtils.createLoadDialog(ShopInfoActivity.this, true);
        gson = new Gson();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));

        Intent intent=getIntent();
        store_id = intent.getIntExtra("store_id",0);
        from_payment = intent.getIntExtra("from_payment",0);

        findStoreDescById();
        tv_top_back.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
        tv_introduce.setOnClickListener(this);
        tv_certificate.setOnClickListener(this);
        tv_tell_title.setOnClickListener(this);

        iv_licence.setOnClickListener(this);
    }

    /**
     *根据id店铺详情
     */
    private void findStoreDescById() {
        String url = Constants.XINYONGSERVER_URL+"credit_money_background/user/findStoreDescById.do";
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("store_id", store_id + "");
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("获得店铺详情:" + json.toString());
                getStoreInfoResponse = gson.fromJson(json, GetStoreInfoResponse.class);
                if (getStoreInfoResponse.getCode() == 0) {
                    shopBean = getStoreInfoResponse.getData();
                    setMessage();

                } else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(ShopInfoActivity.this,
                                getStoreInfoResponse.getMsg());
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(ShopInfoActivity.this,
                            error);
                }
            }
        });
    }

    /**
     * 设置信息
     */
    private void setMessage() {
        if(shopBean != null){
            if(!TextUtils.isEmpty(shopBean.getStore_pic())){
                imageLoader.displayImage(shopBean.getStore_pic(), iv_shop, PictureOption.getSimpleOptions());
            }
            if(!TextUtils.isEmpty(shopBean.getStore_name())){
                tv_shop_name.setText(shopBean.getStore_name());
            }
            if(!TextUtils.isEmpty(shopBean.getAddress())){
                tv_address.setText(shopBean.getArea_info()+shopBean.getAddress());
                tv_tell_address.setText("地址:"+shopBean.getArea_info()+shopBean.getAddress());
            }
            if(!TextUtils.isEmpty(shopBean.getContract_telephone())){
                tv_tell.setText("电话:"+shopBean.getContract_telephone());
                tv_tell_phone.setText("电话:" + shopBean.getContract_telephone());
            }
            if(!TextUtils.isEmpty(shopBean.getDescription())){
                tv_description.setText(shopBean.getDescription());
            }
            if(!TextUtils.isEmpty(shopBean.getContract_name())){
                tv_tell_name.setText("姓名:"+shopBean.getContract_name());
            }

            if(!TextUtils.isEmpty(shopBean.getLicence_pic())){
                    imageLoader.displayImage(shopBean.getLicence_pic(), iv_licence, PictureOption.getSimpleOptions());
                    iv_licence.setVisibility(View.VISIBLE);
                    iv_watermark.setVisibility(View.VISIBLE);
                }else {
                    iv_licence.setVisibility(View.GONE);
                    iv_watermark.setVisibility(View.GONE);
                }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:{
                if(from_payment==1){
                    finish();

                }else{
                    Intent intent=new Intent(ShopInfoActivity.this,InstallmentPaymentActivity.class);
                    if(shopBean != null){
                        intent.putExtra("store_id",shopBean.getStore_id());
                        intent.putExtra("store_type",shopBean.getStore_type());
                        intent.putExtra("store_name",shopBean.getStore_name());
                        intent.putExtra("store_contract",shopBean.getContract_name());
                        intent.putExtra("store_tel",shopBean.getContract_telephone());
                        intent.putExtra("usr_id",shopBean.getUsr_id());
                        intent.putExtra("is_tiexi",shopBean.getIs_tiexi());
                        intent.putExtra("gather_model_id",shopBean.getGather_model_id());
                        if(!TextUtils.isEmpty(shopBean.getStore_grade())){
                            intent.putExtra("store_grade",shopBean.getStore_grade());
                        }
                    }
                    setFinish();
                    UIUtils.startActivityNextAnim(intent);
                }
                break;
            }

            case R.id.tv_top_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.iv_licence:
                if(shopBean != null && !TextUtils.isEmpty(shopBean.getLicence_pic())){
                    Intent intent = new Intent(ShopInfoActivity.this,ShowImageActivity.class);
                    PictureItemBean pictureItemBean = new PictureItemBean();
                    pictureItemBean.setPicWholeUrl(shopBean.getLicence_pic());
                    intent.putExtra("pictureItemBean",pictureItemBean);
                    intent.putExtra("isFromShopInof",true);
                    UIUtils.startActivityNextAnim(intent);
                }
                break;
        }
    }
}
