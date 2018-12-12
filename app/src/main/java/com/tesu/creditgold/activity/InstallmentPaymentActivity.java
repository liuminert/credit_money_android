package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.GetInterestsResponse;
import com.tesu.creditgold.bean.GetUserStatusResponse;
import com.tesu.creditgold.bean.InterestsBean;
import com.tesu.creditgold.bean.ScanResultBean;
import com.tesu.creditgold.bean.SetCreditInfResponse;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.MyOnFocusChangeListener;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.util.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 分期支付页面
 */
public class InstallmentPaymentActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = InstallmentPaymentActivity.class.getSimpleName();
    private TextView tv_top_back;
    private View rootView;
    private TextView tv_shop_name;
    private Button btn_ensure;
    private EditText et_money;
    private TextView tv_date_three;
    private TextView tv_date_six;
    private TextView tv_date_twelve;
    private TextView tv_feel;
    private TextView tv_poundage;
    private String amount;  //总金额
//    private int interest_type=0;  //类型，0：等额本息，1：等本降息,2:到期还本
    private int period;  //期数
    private int store_id; //店铺id
    private int store_type ; //店铺类型(1.家具 2.装修 3.旅游 100.其它)
    private String store_name;  //店铺名称
    private int interest_mode=0;  //计息方式：0：按月，1：按天，2：按季度，不填默认为月；
    private Dialog loadingDialog;
    private Gson gson;
    private GetInterestsResponse getInterestsResponse;
    private InterestsBean interestsBean;
    private String userId;
    private GetUserStatusResponse getUserStatusResponse;
    private String scanResult;
    private PercentRelativeLayout pr_name;
    private String store_contract;
    private String store_tel;
    private TextView tv_contract;
    private CheckBox is_checked;
    private int usr_id;
    private int is_tiexi;  //类型，0：等额本息，1：等本降息
    private int store_status; //店铺状态  0，被禁用    1，正常
    private int amountD;
    private int amountR;
    private int gather_model_id;
    private String usr_order_id;
    private boolean isSubmit;
    private boolean isGetInterestInfSuccess;
    private boolean isChangeAmount;
    private String store_grade;
    private String amortization_money;
    private TextView tv_contract2;
    private TextView tv_contract3;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_installment_payment, null);
        setContentView(rootView);
        tv_contract= (TextView) rootView.findViewById(R.id.tv_contract);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_shop_name = (TextView) rootView.findViewById(R.id.tv_shop_name);
        btn_ensure=(Button)rootView.findViewById(R.id.btn_ensure);
        et_money= (EditText) rootView.findViewById(R.id.et_money);
        tv_date_three= (TextView) rootView.findViewById(R.id.tv_date_three);
        tv_date_six= (TextView) rootView.findViewById(R.id.tv_date_six);
        tv_date_twelve= (TextView) rootView.findViewById(R.id.tv_date_twelve);
        tv_feel= (TextView) rootView.findViewById(R.id.tv_feel);
        tv_poundage= (TextView) rootView.findViewById(R.id.tv_poundage);
        pr_name= (PercentRelativeLayout) rootView.findViewById(R.id.pr_name);
        is_checked= (CheckBox) rootView.findViewById(R.id.is_checked);
        tv_contract2= (TextView) rootView.findViewById(R.id.tv_contract2);
        tv_contract3= (TextView) rootView.findViewById(R.id.tv_contract3);
        initData();
        return null;
    }


    public void initData() {
        loadingDialog = DialogUtils.createLoadDialog(InstallmentPaymentActivity.this, false);
        gson = new Gson();

        Intent intent=getIntent();
        scanResult =intent.getStringExtra("scanResult");
        store_id = intent.getIntExtra("store_id", 0);
        store_type = intent.getIntExtra("store_type", 0);
        store_name = intent.getStringExtra("store_name");
        store_contract = intent.getStringExtra("store_contract");
        store_tel = intent.getStringExtra("store_tel");
        usr_id = intent.getIntExtra("usr_id", 0);
        is_tiexi = intent.getIntExtra("is_tiexi", 0);
        store_grade = intent.getStringExtra("store_grade");

        LogUtils.e("store_type:"+store_type);

        gather_model_id = intent.getIntExtra("gather_model_id", 0);
        if(!TextUtils.isEmpty(store_name)){
                tv_shop_name.setText(store_name);
        }

        LogUtils.e("is_tiexi:"+is_tiexi);
        userId = SharedPrefrenceUtils.getString(this, "usrid");

        tv_top_back.setOnClickListener(this);
        btn_ensure.setOnClickListener(this);
        tv_date_three.setOnClickListener(this);
        tv_date_six.setOnClickListener(this);
        tv_date_twelve.setOnClickListener(this);
        pr_name.setOnClickListener(this);
        tv_contract.setOnClickListener(this);
        tv_contract2.setOnClickListener(this);
        tv_contract3.setOnClickListener(this);

        tv_contract.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_contract2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线

        et_money.setOnFocusChangeListener(new MyOnFocusChangeListener());

        is_checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_ensure.setBackgroundResource(R.mipmap.btn_sub);
                    btn_ensure.setEnabled(true);
                } else {
                    btn_ensure.setBackgroundResource(R.mipmap.btn_gray);
                    btn_ensure.setEnabled(false);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_contract:{
//                Intent intent = new Intent(InstallmentPaymentActivity.this,ContractActivity.class);
//                UIUtils.startActivityNextAnim(intent);
                Intent intent = new Intent(InstallmentPaymentActivity.this,XieYiWebActivity.class);
                intent.putExtra("article_id",Constants.LoanAgreementId);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.tv_contract2:
            case R.id.tv_contract3:
                Intent intent1 = new Intent(InstallmentPaymentActivity.this,XieYiWebActivity.class);
                intent1.putExtra("article_id",Constants.PersonalInformationId);
                UIUtils.startActivityNextAnim(intent1);
                break;
            case R.id.btn_ensure:{
                isSubmit = true;

                if(!is_checked.isChecked()){
                    UIUtils.showToastCenter(InstallmentPaymentActivity.this, "请勾选同意借款协议和个人信息使用授权书");
                    return;
                }

                if(interestsBean == null){
                    UIUtils.showToastCenter(InstallmentPaymentActivity.this, "请先选择期数");
                    return;
                }
//                Intent intent=new Intent(InstallmentPaymentActivity.this,IdentityAuthenticationActivity.class);
//                intent.putExtra("interestsBean",interestsBean);
//                intent.putExtra("store_name",store_name);
//                intent.putExtra("store_id",store_id);
//                UIUtils.startActivityNextAnim(intent);

                userId = SharedPrefrenceUtils.getString(this, "usrid");
                if(TextUtils.isEmpty(userId)){
                    Intent intent = new Intent(InstallmentPaymentActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                    return;
                }

                amount = et_money.getText().toString();
                if(TextUtils.isEmpty(amount)){
                    UIUtils.showToastCenter(InstallmentPaymentActivity.this, "请输入分期金额");
                    return;
                }

                amountD = Integer.valueOf(amount);
                amountR = amountD % 100;
                if(amountR !=0 ||amountD==0){
                    UIUtils.showToastCenter(InstallmentPaymentActivity.this, "请输入100的整数倍");
                    interestsBean = null;
                    tv_feel.setText("0");
                    tv_feel.setTextColor(UIUtils.getColor(R.color.black));
                    return;
                }

                btn_ensure.setEnabled(false);
                btn_ensure.setBackgroundResource(R.mipmap.btn_gray);
                getInterestInf();
//                getUserStatus();
                break;
            }
            case R.id.tv_top_back: {
                UIUtils.hideInputWindow(this);
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.tv_date_three:
                if(period == 3){
                    if(!TextUtils.isEmpty(amount)){
                        if(!TextUtils.isEmpty(et_money.getText().toString())){
                            if(!(amount.equals(et_money.getText().toString()))){
                                isChangeAmount = true;
                            }
                        }
                    }
                }
                if(period != 3 || !isGetInterestInfSuccess ||isChangeAmount){
                    isSubmit = false;
                    amount = et_money.getText().toString();
                    if(TextUtils.isEmpty(amount)){
                        UIUtils.showToastCenter(InstallmentPaymentActivity.this, "请输入分期金额");
                        return;
                    }

                    amountD = Integer.valueOf(amount);
                    amountR = amountD % 100;
                    if(amountR !=0 ||amountD==0){
                        UIUtils.showToastCenter(InstallmentPaymentActivity.this, "请输入100的整数倍");
                        interestsBean = null;
                        tv_feel.setText("0");
                        tv_feel.setTextColor(UIUtils.getColor(R.color.black));
                        return;
                    }

                    period = 3;
                    getInterestInf();
                }

                break;
            case R.id.tv_date_six:
                if(period == 6){
                    if(!TextUtils.isEmpty(amount)){
                        if(!TextUtils.isEmpty(et_money.getText().toString())){
                            if(!(amount.equals(et_money.getText().toString()))){
                                isChangeAmount = true;
                            }
                        }
                    }
                }
                if(period != 6 || !isGetInterestInfSuccess ||isChangeAmount){
                    isSubmit = false;
                    amount = et_money.getText().toString();
                    if(TextUtils.isEmpty(amount)){
                        UIUtils.showToastCenter(InstallmentPaymentActivity.this, "请输入分期金额");
                        return;
                    }
                    amountD = Integer.valueOf(amount);
                    amountR = amountD % 100;
                    if(amountR !=0 ||amountD==0){
                        UIUtils.showToastCenter(InstallmentPaymentActivity.this, "请输入100的整数倍");
                        interestsBean = null;
                        tv_feel.setText("0");
                        tv_feel.setTextColor(UIUtils.getColor(R.color.black));
                        return;
                    }

                    period = 6;
                    getInterestInf();
                }

                break;
            case R.id.tv_date_twelve:
                if(period == 12){
                    if(!TextUtils.isEmpty(amount)){
                        if(!TextUtils.isEmpty(et_money.getText().toString())){
                            if(!(amount.equals(et_money.getText().toString()))){
                                isChangeAmount = true;
                            }
                        }
                    }
                }
                if(period != 12 || !isGetInterestInfSuccess ||isChangeAmount){
                    isSubmit = false;
                    amount = et_money.getText().toString();
                    if(TextUtils.isEmpty(amount)){
                        UIUtils.showToastCenter(InstallmentPaymentActivity.this, "请输入分期金额");
                        return;
                    }
                    amountD = Integer.valueOf(amount);
                    amountR = amountD % 100;
                    if(amountR !=0 ||amountD==0){
                        UIUtils.showToastCenter(InstallmentPaymentActivity.this, "请输入100的整数倍");
                        interestsBean = null;
                        tv_feel.setText("0");
                        tv_feel.setTextColor(UIUtils.getColor(R.color.black));
                        return;
                    }

                    period = 12;
                    getInterestInf();
                }
                break;
            case R.id.pr_name:
                Intent intent = new Intent(InstallmentPaymentActivity.this,ShopInfoActivity.class);
                intent.putExtra("store_id",store_id);
                intent.putExtra("from_payment",1);
                setFinish();
                UIUtils.startActivityNextAnim(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100 && resultCode == 200){
            String sina_whithhold_url = data.getStringExtra("sina_whithhold_url");
//            if(!TextUtils.isEmpty(sina_whithhold_url)){
//                Uri uri = Uri.parse(sina_whithhold_url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                UIUtils.startActivityNextAnim(intent);
//            }
            Intent intent = new Intent(InstallmentPaymentActivity.this,XinaWebActivity.class);
            intent.putExtra("sina_whithhold_url", sina_whithhold_url);
//            UIUtils.startActivityNextAnim(intent);
            startActivityForResult(intent,101);
        }else if(requestCode == 101 && resultCode == 201){
            LogUtils.e("111111");
//            setCreditInf();
            getUserStatusForReturn();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 新浪返回后单独判断用户状态
     */
    private void getUserStatusForReturn() {
        loadingDialog.show();
        String url = Constants.XINYONGSERVER_URL + "credit_money_background/user/get_user_status.do";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("usrid", userId);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                getUserStatusResponse = gson.fromJson(json, GetUserStatusResponse.class);
                if (getUserStatusResponse.getCode() == 0) { //表示用户已登录，已实名，已绑卡，直接下一步
                    setCreditInf();
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                btn_ensure.setEnabled(true);
                btn_ensure.setBackgroundResource(R.mipmap.btn_sub);
            }
        });
    }



    /**
     * 点击确认分期买单，判断用户状态
     */
    private void getUserStatus() {
        loadingDialog.show();
        String url = Constants.XINYONGSERVER_URL+"credit_money_background/user/get_user_status.do";
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("usrid", userId);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("判断用户状态:" + json.toString());
                getUserStatusResponse = gson.fromJson(json, GetUserStatusResponse.class);
                if (getUserStatusResponse.getCode() == 0) { //表示用户已登录，已实名，已绑卡，直接下一步
//                    Intent intent = new Intent(InstallmentPaymentActivity.this, IdentityAuthenticationActivity.class);
//                    intent.putExtra("interestsBean", interestsBean);
//                    intent.putExtra("store_name", store_name);
//                    intent.putExtra("store_id", store_id);
//                    intent.putExtra("store_type", store_type);
//                    intent.putExtra("store_contract", store_contract);
//                    intent.putExtra("store_tel", store_tel);
//                    intent.putExtra("amount", amount);
//                    intent.putExtra("store_uid", usr_id);
//                    intent.putExtra("is_tiexi", is_tiexi);
//                    intent.putExtra("gather_model_id", gather_model_id);
//                    setFinish();
//                    UIUtils.startActivityNextAnim(intent);

                    setCreditInf();

                } else if (getUserStatusResponse.getCode() == -1) { //表示出错
                    btn_ensure.setEnabled(true);
                    btn_ensure.setBackgroundResource(R.mipmap.btn_sub);
                    DialogUtils.showAlertDialog(InstallmentPaymentActivity.this,
                            getUserStatusResponse.getMsg());

                } else if (getUserStatusResponse.getCode() == -2) { //表示用户已登录，未实名，未绑卡，跳到实名界面，调用实名认证接口(后台调用ccfax实名认证之后，就调用设置新浪委托代扣授权进行绑卡)
                    btn_ensure.setEnabled(true);
                    btn_ensure.setBackgroundResource(R.mipmap.btn_sub);
                    Intent intent = new Intent(InstallmentPaymentActivity.this, RealNameActivity.class);
                    UIUtils.startActivityForResult(intent, 100);

                } else if (getUserStatusResponse.getCode() == -3) { //表示用户已登录，已实名，未绑卡，跳到绑卡界面(sina_whithhold_url)，调用设置新浪委托代扣授权接口，进行绑卡
                    btn_ensure.setEnabled(true);
                    btn_ensure.setBackgroundResource(R.mipmap.btn_sub);
                    String sina_whithhold_url = getUserStatusResponse.getSina_whithhold_url();
//                    if(!TextUtils.isEmpty(sina_whithhold_url)){
//                        Uri uri = Uri.parse(sina_whithhold_url);
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        UIUtils.startActivityNextAnim(intent);
//                    }
                    Intent intent = new Intent(InstallmentPaymentActivity.this, XinaWebActivity.class);
                    intent.putExtra("sina_whithhold_url", sina_whithhold_url);
//                    UIUtils.startActivityNextAnim(intent);
                    UIUtils.startActivityForResult(intent, 101);
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                btn_ensure.setEnabled(true);
                btn_ensure.setBackgroundResource(R.mipmap.btn_sub);
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(InstallmentPaymentActivity.this,
                            error);
                }
            }
        });
    }

    /**
     * 写入授信资料
     */
    private void setCreditInf() {
        String phoneIp = Utils.getPhoneIp();
        String imei = Utils.getIEMI(this);
        loadingDialog.show();
//        String url= Constants.FENGKONGSERVER_URL+"tsfkxt/user/setCreditInf_2.do";
        String url= Constants.FENGKONGSERVER_URL+"tsfkxt/user/setOrdersInf.do";
        Map<String,String> params = new HashMap<String,String>();
        params.put("usrid", userId);
        params.put("order_type",store_type+"");
        params.put("order_source","0");
        params.put("usr_order_id","0");
        params.put("submit_step","0");
        params.put("scene_id",gather_model_id+"");
        params.put("mobile_phone",SharedPrefrenceUtils.getString(this,"mobile_phone"));
        params.put("store_uid",usr_id+""); //商家ID
        params.put("store_name",store_name); //商家名称
        params.put("store_contract",store_contract);  //商家联系人
        params.put("store_tel",store_tel);  //商家电话
        params.put("borrow_uid",userId); //借款人usrid
//        params.put("borrow_name","标题");  //标题
        params.put("borrow_money",amount); //借款金额
        params.put("borrow_interest",interestsBean.getInterest_total()); //利息
        params.put("each_amount",interestsBean.getPrincipal_interest()); //每期还款金额
        params.put("borrow_interest_rate",interestsBean.getBorrow_interest_rate()+""); //年化率（12.8%写入12.8）
        params.put("store_charge_rate",interestsBean.getStore_charge_rate()+""); //商家服务费费率（12.8%写入12.8）
        params.put("user_charge_rate",interestsBean.getUser_charge_rate()+""); //链金所手续费费率（12.8%写入12.8）
        params.put("borrow_duration",interestsBean.getPeriod()+""); //借款期限(月标和季标为月数，天标为天数)
        params.put("fee",interestsBean.getFactorage()); //每期服务费(金额)
//        params.put("borrow_info","标的详情"); //标的详情
        params.put("interest_type", is_tiexi+""); //类型，0：等额本息，1：等本降息
        params.put("borrow_use", "9"); //借款用途，1 ： '短期周转', 2： '生意周转', 3： '生活周转', 4 ：'购物消费', 5 ： '不提现借款', 6 ： '创业借款', 7 ： '其它借款'，8：'装修借款'（4：对应乐购分期，8对应：乐装分期），9：信用千金
        params.put("borrow_use_desc", "0");
        if(!TextUtils.isEmpty(store_grade)){
           params.put("store_grade",store_grade);
        }
        LogUtils.e("params:"+params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                btn_ensure.setEnabled(true);
                btn_ensure.setBackgroundResource(R.mipmap.btn_sub);
                Log.e(TAG, "写入征信资料返回:" + json);
                SetCreditInfResponse setCreditInfResponse = gson.fromJson(json,SetCreditInfResponse.class);
                if(setCreditInfResponse.getCode()==0){
                    usr_order_id = String.valueOf(setCreditInfResponse.getReturn_param().getUsr_order_id());
                    setFinish();
                    Intent intent = new Intent();
                    intent.setClass(InstallmentPaymentActivity.this, IdentityAuthenticationActivity.class);
                    intent.putExtra("gather_model_id", gather_model_id);
                    intent.putExtra("usr_order_id", usr_order_id);
                    intent.putExtra("store_type", store_type);
                    intent.putExtra("amount", amount);
                    intent.putExtra("amortization_money", amortization_money);
                    UIUtils.startActivityNextAnim(intent);
                }else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(InstallmentPaymentActivity.this, setCreditInfResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                btn_ensure.setEnabled(true);
                btn_ensure.setBackgroundResource(R.mipmap.btn_sub);
                Log.e(TAG, "写入征信资料返回错误:" + error);
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(InstallmentPaymentActivity.this, error);
                }

            }
        });
    }


    /**
     * 分期购利息计算
     */
    private void getInterestInf() {
        loadingDialog.show();
        String url =Constants.XINYONGSERVER_URL+"credit_money_background/user/getInterestInf.do";
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("amount",amount);
        params.put("interest_type",is_tiexi+"");
        params.put("period",period+"");
        params.put("store_id", store_id + "");
        params.put("interest_mode", interest_mode + "");
        isChangeAmount = false;
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
                        tv_feel.setText(amortization_money);
                        tv_feel.setTextColor(UIUtils.getColor(R.color.default_button_color));
                    }
//                    if (!TextUtils.isEmpty(interestsBean.getFactorage())) {
//                        tv_poundage.setText("+手续费:" + interestsBean.getFactorage());
//                    }
                    switch (period) {
                        case 3:
                            tv_date_three.setBackgroundResource(R.mipmap.staging_frame);
                            tv_date_three.setTextColor(getResources().getColor(R.color.yellow_light));
                            tv_date_six.setBackgroundResource(R.drawable.border_shape_rect);
                            tv_date_six.setTextColor(getResources().getColor(R.color.text_color_black));
                            tv_date_twelve.setBackgroundResource(R.drawable.border_shape_rect);
                            tv_date_twelve.setTextColor(getResources().getColor(R.color.text_color_black));
                            break;
                        case 6:
                            tv_date_three.setBackgroundResource(R.drawable.border_shape_rect);
                            tv_date_three.setTextColor(getResources().getColor(R.color.text_color_black));
                            tv_date_six.setBackgroundResource(R.mipmap.staging_frame);
                            tv_date_six.setTextColor(getResources().getColor(R.color.yellow_light));
                            tv_date_twelve.setBackgroundResource(R.drawable.border_shape_rect);
                            tv_date_twelve.setTextColor(getResources().getColor(R.color.text_color_black));
                            break;
                        case 12:
                            tv_date_three.setBackgroundResource(R.drawable.border_shape_rect);
                            tv_date_three.setTextColor(getResources().getColor(R.color.text_color_black));
                            tv_date_six.setBackgroundResource(R.drawable.border_shape_rect);
                            tv_date_six.setTextColor(getResources().getColor(R.color.text_color_black));
                            tv_date_twelve.setBackgroundResource(R.mipmap.staging_frame);
                            tv_date_twelve.setTextColor(getResources().getColor(R.color.yellow_light));
                            break;
                    }
                    isGetInterestInfSuccess = true;

                    if (isSubmit) {
//                        getUserStatus();
                        setCreditInf();
                    }
                } else {
                    if(isSubmit && is_checked.isChecked()){
                        btn_ensure.setEnabled(true);
                        btn_ensure.setBackgroundResource(R.mipmap.btn_sub);
                    }
                    isGetInterestInfSuccess = false;
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(InstallmentPaymentActivity.this,
                                getInterestsResponse.getMsg());
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(isSubmit && is_checked.isChecked()){
                    btn_ensure.setEnabled(true);
                    btn_ensure.setBackgroundResource(R.mipmap.btn_sub);
                }
                isGetInterestInfSuccess = false;
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(InstallmentPaymentActivity.this,
                            error);
                }
            }
        });
    }

}
