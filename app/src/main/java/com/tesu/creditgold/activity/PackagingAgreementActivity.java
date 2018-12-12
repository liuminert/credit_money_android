package com.tesu.creditgold.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.BuildConfig;
import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.MyAppInfo;
import com.tesu.creditgold.bean.PictureItemBean;
import com.tesu.creditgold.bean.UsrOrderPicBean;
import com.tesu.creditgold.util.AppUtils;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.MobileUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.StringUtils;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.tongdun.android.shell.FMAgent;

public class PackagingAgreementActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = PackagingAgreementActivity.class.getSimpleName();
    private View rootView;
    private TextView tv_top_back;
    private Button btn_next;
    private Dialog loadingDialog;
    private String usr_order_id;
    private Gson gson;
    private String userId;
    private FkBaseResponse fkbaseResponse;
    private WebView wv_agreement;
    private String CSS_STYPE = "<head><style>img{max-width:340px !important;}</style></head>";
    private String borrower_address_book;
    private String borrower_call_records;
    private String borrower_sms;
    private String borrower_app_name;
    private List addressBookList;
    private List callHistoryList;
    private AlertDialog settingDialog;
//    private boolean isRquestAgain;
    private String device_inf;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_packaging_agreement, null);
        setContentView(rootView);

        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        wv_agreement = (WebView) rootView.findViewById(R.id.wv_agreement);

        loadingDialog = DialogUtils.createLoadDialog(PackagingAgreementActivity.this, false);
        gson = new Gson();
        userId = SharedPrefrenceUtils.getString(this, "usrid");

        tv_top_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        initData();
        return null;
    }

    private void initData() {
        Intent intent = getIntent();
        usr_order_id = intent.getStringExtra("usr_order_id");


        device_inf = MobileUtils.getDeviceInfo(this);
        if(!TextUtils.isEmpty(usr_order_id)){
            getOrdersContract();
        }

//        readPhoneMessage();

    }

    /**
     * 读取手机通讯录，通话记录和应用程序
     */
    private void readPhoneMessage() {
        loadingDialog.show();
        addressBookList = AppUtils.getPhoneNumberFromMobile(PackagingAgreementActivity.this);
        if(AppUtils.getSIMContacts(PackagingAgreementActivity.this) != null ){
            addressBookList.addAll(AppUtils.getSIMContacts(PackagingAgreementActivity.this));
        }
        callHistoryList = AppUtils.getCallHistoryList(PackagingAgreementActivity.this, getContentResolver());
        borrower_address_book = gson.toJson(addressBookList);//借款人手机通讯录
        borrower_call_records = gson.toJson(callHistoryList);  //借款人通话记录
//                borrower_sms = AppUtils.getSmsInPhone();  //借款人手机短信

        LogUtils.e("addressBookList:"+addressBookList.toString());
        LogUtils.e("callHistoryList:"+callHistoryList.toString());
        loadingDialog.dismiss();
    }

    /**
     * 获取订单合同接口
     */
    private void getOrdersContract() {
        loadingDialog.show();
        String url= Constants.FENGKONGSERVER_URL+"tsfkxt/user/get_orders_contract.do";
        Map<String,String> params = new HashMap<String,String>();
        params.put("usr_order_id", usr_order_id);
        LogUtils.e("params:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Log.e(TAG, "获取订单合同:" + json);
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkbaseResponse.getCode() == 0) {
                    HashMap<String, String> hs = gson.fromJson(gson.toJson(fkbaseResponse.getReturn_param()), HashMap.class);
                    String borrowed_contract = hs.get("borrowed_contract");
                    if (!TextUtils.isEmpty(borrowed_contract)) {
//                        wv_agreement.loadDataWithBaseURL(null, CSS_STYPE + borrowed_contract, "text/html", "utf-8", null);
                        wv_agreement.loadUrl(borrowed_contract);
                    }

                } else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(PackagingAgreementActivity.this, fkbaseResponse.getMsg());
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(PackagingAgreementActivity.this, error);
                }
            }
        });
    }

    /**
     * 写入授信资料
     */
    private void setCreditInf() {
        loadingDialog.show();
        btn_next.setEnabled(false);
        btn_next.setBackgroundResource(R.mipmap.btn_gray);

        borrower_app_name = SharedPrefrenceUtils.getString(PackagingAgreementActivity.this,"borrower_app_name");

//        String url= Constants.FENGKONGSERVER_URL+"tsfkxt/user/setCreditInf_2.do";
        String url= Constants.FENGKONGSERVER_URL+"tsfkxt/user/setOrdersInf.do";

        String blackBox = FMAgent.onEvent(PackagingAgreementActivity.this);

        Map<String,String> params = new HashMap<String,String>();
        params.put("device_inf", device_inf);  //手机设备信息
        params.put("usrid", userId);
        params.put("submit_step", "5");
        params.put("usr_order_id", usr_order_id);
        params.put("borrower_address_book", borrower_address_book);  //借款人手机通讯录
        params.put("borrower_call_records", borrower_call_records);  //借款人通话记录
//        params.put("borrower_sms", borrower_sms);  //借款人手机短信
        if(!TextUtils.isEmpty(borrower_app_name)){
            params.put("borrower_app_name", borrower_app_name);  //借款人手机应用程序名称
        }
        if(!TextUtils.isEmpty(blackBox)){
            params.put("tong_dun_black_box", blackBox);  //同盾blackBox
        }
        params.put("operation_system","0");
        LogUtils.e("最后写入征信资料参数params:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                btn_next.setEnabled(true);
                btn_next.setBackgroundResource(R.mipmap.btn_sub);
                Log.e(TAG, "最后写入征信资料返回:" + json);
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkbaseResponse.getCode() == 0) {
                    deleteMessage(usr_order_id);
//                    setUserOrder();
                    Intent intent = new Intent(PackagingAgreementActivity.this, SubmitSuccessActivity.class);
                    setFinish();
                    UIUtils.startActivityNextAnim(intent);
                } else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(PackagingAgreementActivity.this, fkbaseResponse.getMsg());
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                btn_next.setEnabled(true);
                btn_next.setBackgroundResource(R.mipmap.btn_sub);
                Log.e(TAG, "最后写入征信资料返回错误:" + error);
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(PackagingAgreementActivity.this, error);
                }
            }
        });
    }

    /**
     * 删除已经完成的订单信息
     * @param userOrderId
     */
    private void deleteMessage(String userOrderId) {
        if(!TextUtils.isEmpty(userOrderId)){
            String messageStr = SharedPrefrenceUtils.getString(this, userOrderId + "-step4");
            if(!TextUtils.isEmpty(messageStr)){
                SharedPrefrenceUtils.setString(this,userOrderId + "-step4","");
            }

            messageStr = SharedPrefrenceUtils.getString(this, userOrderId);
            if(!TextUtils.isEmpty(messageStr)){
                SharedPrefrenceUtils.setString(this,userOrderId,"");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
//            isRquestAgain = true;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_top_back:
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.btn_next:   //下一步
                readPhoneMessage();
                if(addressBookList== null || addressBookList.size()==0){
                    settingDialog =DialogUtils.showAlertDoubleBtnDialog(PackagingAgreementActivity.this, "通讯录不能为空，是否跳转到权限设置界面去开启", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()){
                                case R.id.tv_ensure:
                                    settingDialog.dismiss();
                                    gotoMiuiPermission();
                                break;
                                case R.id.tv_cancle:
                                    settingDialog.dismiss();
                                    break;
                            }

                        }
                    });
                    return;
                }
                if(callHistoryList== null || callHistoryList.size()==0){
                    settingDialog =DialogUtils.showAlertDoubleBtnDialog(PackagingAgreementActivity.this, "通话记录不能为空，是否跳转到权限设置界面去开启", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()){
                                case R.id.tv_ensure:
                                    settingDialog.dismiss();
                                    gotoMiuiPermission();
                                    break;
                                case R.id.tv_cancle:
                                    settingDialog.dismiss();
                                    break;
                            }

                        }
                    });
                    return;
                }

                setCreditInf();
                break;
        }
    }

//    private void getAppDetailSettingIntent(Context context) {
//        Intent localIntent = new Intent();
//        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (Build.VERSION.SDK_INT >= 9) {
//            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
//        } else if (Build.VERSION.SDK_INT <= 8) {
//            localIntent.setAction(Intent.ACTION_VIEW);
//            localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
//            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
//        }
//        startActivity(localIntent);
//    }

    /**
     * 跳转到miui的权限管理页面
     */
    private void gotoMiuiPermission() {
        Intent i = new Intent("miui.intent.action.APP_PERM_EDITOR");
        ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        i.setComponent(componentName);
        i.putExtra("extra_pkgname", getPackageName());
        try {
            UIUtils.startActivityForResult(i,100);

        } catch (Exception e) {
            e.printStackTrace();
            gotoMeizuPermission();
        }
    }

    /**
     * 跳转到魅族的权限管理系统
     */
    private void gotoMeizuPermission() {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        try {
            UIUtils.startActivityForResult(intent, 100);
        } catch (Exception e) {
            e.printStackTrace();
            gotoHuaweiPermission();
        }
    }

    /**
     * 华为的权限管理页面
     */
    private void gotoHuaweiPermission() {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            intent.setComponent(comp);
            UIUtils.startActivityForResult(intent, 100);
        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.startActivityForResult(getAppDetailSettingIntent(), 100);
        }

    }

    /**
     * 获取应用详情页面intent
     *
     * @return
     */
    private Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        return localIntent;
    }
}
