package com.tesu.creditgold.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.minivision.livebodyauthentication.LiveBodyAuthenticationActivity;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.InterestsBean;
import com.tesu.creditgold.bean.XiaoShiBean;
import com.tesu.creditgold.util.BitmapUtils;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ShowLiveActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = ShowLiveActivity.class.getSimpleName();
    private View rootView;
    private TextView tv_top_back;
    private TextView tv_telephone;
    private Button btn_next;
    private int gather_model_id;
    private Dialog loadingDialog;
    private String userId;
    private FkBaseResponse fkbaseResponse;
    private Gson gson;
    private XiaoShiBean xiaoShiBean;
    protected static final int LIVE_BODE = 99; //活体检测
    private String usr_order_id;
    private String name;
    private String idNumber;
    private int store_type;
    private String amount;
    private String amortization_money;
    private int order_from; //订单来源 0：买家下单，1：卖家下单


    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_show_live, null);
        setContentView(rootView);

        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_telephone = (TextView) rootView.findViewById(R.id.tv_telephone);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);

        tv_top_back.setOnClickListener(this);
        tv_telephone.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        initData();
        return null;
    }

    private void initData() {
        Intent intent = getIntent();
        usr_order_id = intent.getStringExtra("usr_order_id");
        gather_model_id = intent.getIntExtra("gather_model_id", 0);
        name = intent.getStringExtra("name");
        idNumber = intent.getStringExtra("idNumber");
        store_type = intent.getIntExtra("store_type", 0);
        amount = intent.getStringExtra("amount");
        amortization_money = intent.getStringExtra("amortization_money");
        order_from = intent.getIntExtra("order_from", 0);

        loadingDialog = DialogUtils.createLoadDialog(ShowLiveActivity.this, false);
        userId = SharedPrefrenceUtils.getString(this, "usrid");
        gson = new Gson();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LIVE_BODE){
            if (data != null && data.getStringExtra("facialResult") != null) {
                String listOfFacialResult = data.getStringExtra("facialResult");
                Log.e(TAG, "listOfFacialResult:" + listOfFacialResult);
                Bitmap bitmap = null;
                try {
                    if(listOfFacialResult.toUpperCase().contains("ERROR")){
                        JSONArray json1 = new JSONArray(listOfFacialResult);
                        JSONObject j1 = json1.getJSONObject(0);
                        String errorMsg = j1.getString("errorMsg");  //错误信息
//                            if(!TextUtils.isEmpty(errorMsg)){
//                                DialogUtils.showAlertDialog(MainActivity.this,errorMsg);
//                                return;
//                            }
                        btn_next.setEnabled(true);
                        btn_next.setBackgroundResource(R.mipmap.btn_sub);
                        btn_next.setTextColor(UIUtils.getColor(R.color.title_text));
                        DialogUtils.showAlertDialog(ShowLiveActivity.this,"系统升级中...");
                        return;
                    }

                    JSONArray json = new JSONArray(listOfFacialResult);
                    int count=0;

                    for (int i = 0; i < json.length(); ++i) {
                        // 每一组的结果获取
                        JSONObject j = json.getJSONObject(i);
                        String name = j.getString("name");// 动作的名称
                        String result = j.getString("result");// 动作通过与否
                        String save_path = j.getString("save_path");// 动作通过时的图片保存位置


                        result = result.toUpperCase();
                        if(result.equals("YES")){
                            count++;
                        }
                        File f = new File(save_path);
                        if (i == json.length() - 1 && f.exists()) {
                            bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                        }
                    }
                    Log.e(TAG, "count:" + count);
                    if(count == 3){
                        uploadMessage(bitmap, true);
                    }else {
                        uploadMessage(bitmap,false);
//                            DialogUtils.showAlertDialog(MainActivity.this,"活体检测失败，请重新检测");
                    }
                } catch (Exception e) {
                    btn_next.setEnabled(true);
                    btn_next.setBackgroundResource(R.mipmap.btn_sub);
                    btn_next.setTextColor(UIUtils.getColor(R.color.title_text));
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }finally {
//                    if(!bitmap.isRecycled()){
//                        bitmap.recycle();
//                    }
                }
                Log.i("onActivityResult", "size of listOfFacialResult = " + listOfFacialResult.length());
                Log.i("onActivityResult", "listOfFacialResult = " + listOfFacialResult);
            }else {
                btn_next.setEnabled(true);
                btn_next.setBackgroundResource(R.mipmap.btn_sub);
                btn_next.setTextColor(UIUtils.getColor(R.color.title_text));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获得小视账号状态
     */
    private void getXiaoShiProductConfig() {
        loadingDialog.show();
        String url= Constants.FENGKONGSERVER_URL+"tsfkxt/user/getXiaoShiProductConfig.do";
        Map<String,String> params = new HashMap<String,String>();
        params.put("usrid", userId);
        params.put("usr_order_id", usr_order_id);
        MyVolley.uploadNoFileWholeUrl(MyVolley.GET, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Log.e(TAG, "获取小视账号:" + json);
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkbaseResponse.getCode() == 0) {
                    xiaoShiBean = gson.fromJson(gson.toJson(fkbaseResponse.getReturn_param()), XiaoShiBean.class);
                    if (xiaoShiBean != null) {
                        if (xiaoShiBean.getFlag() == 0) {
                            checkFace(xiaoShiBean.getXiao_shi_account(), xiaoShiBean.getXiao_shi_password());
                        } else if (xiaoShiBean.getFlag() == -1) {
                            btn_next.setEnabled(true);
                            btn_next.setBackgroundResource(R.mipmap.btn_sub);
                            btn_next.setTextColor(UIUtils.getColor(R.color.title_text));
                            DialogUtils.showAlertDialog(ShowLiveActivity.this, "该功能已经关闭。");
                        } else {
                            btn_next.setEnabled(true);
                            btn_next.setBackgroundResource(R.mipmap.btn_sub);
                            btn_next.setTextColor(UIUtils.getColor(R.color.title_text));
                            DialogUtils.showAlertDialog(ShowLiveActivity.this, "您的操作太频繁，请两个小时后再试。");
                        }
                    }
                } else {
                    btn_next.setEnabled(true);
                    btn_next.setBackgroundResource(R.mipmap.btn_sub);
                    btn_next.setTextColor(UIUtils.getColor(R.color.title_text));
                    DialogUtils.showAlertDialog(ShowLiveActivity.this, fkbaseResponse.getMsg());
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                btn_next.setEnabled(true);
                btn_next.setBackgroundResource(R.mipmap.btn_sub);
                btn_next.setTextColor(UIUtils.getColor(R.color.title_text));
                Log.e(TAG, "登陆返回错误:" + error);
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(ShowLiveActivity.this, error);
                }
            }
        });

    }

    /**
     * 活体检测
     */
    private void checkFace(String userName,String password){
        Intent intent = new Intent(ShowLiveActivity.this, LiveBodyAuthenticationActivity.class);
        writeToLogin(userName, password);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("flag", 1);
        startActivityForResult(intent, LIVE_BODE);
    }

    private void writeToLogin(String username, String password) {
        SharedPreferences sharedPrefe = getSharedPreferences("minivisionAccountSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefe.edit();
        editor.putString("userName", username);
        editor.putString("password", password);
        editor.apply();
    }

    private void uploadMessage(Bitmap bitmap, final boolean isSuccess){
        loadingDialog.show();
        String url=Constants.FENGKONGSERVER_URL+"tsfkxt/user/updateXiaoShiResult.do";
        Map<String,String> params = new HashMap<String,String>();
        params.put("usrinf_id", xiaoShiBean.getUsrinf_id() + "");
        if(isSuccess){
            params.put("pic_file", BitmapUtils.getBitmapStrBase64(bitmap));
            params.put("interface_result", "0");
        }else {
            params.put("interface_result", "-1");
            params.put("pic_file", "");
        }
        params.put("usr_order_id", usr_order_id);
        Log.e(TAG, "uploadMessage:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Log.e(TAG, "上传数据返回:" + json);
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkbaseResponse.getCode() == 0) {
                    HashMap<String, Double> hashMap = gson.fromJson(gson.toJson(fkbaseResponse.getReturn_param()), HashMap.class);
                    double pass_flag = hashMap.get("pass_flag");
                    if (pass_flag == 0) {
                        setCreditInf();
//                        setFinish();
//                        Intent intent = new Intent(ShowLiveActivity.this, InformationAuthenticationActivity.class);
//                        intent.putExtra("interestsBean", interestsBean);
//                        intent.putExtra("store_name", store_name);
//                        intent.putExtra("store_id", store_id);
//                        intent.putExtra("store_type", store_type);
//                        intent.putExtra("store_contract", store_contract);
//                        intent.putExtra("store_tel", store_tel);
//                        intent.putExtra("amount", amount);
//                        intent.putExtra("store_uid", store_uid);
//                        intent.putExtra("is_tiexi", is_tiexi);
//                        intent.putExtra("gather_model_id", gather_model_id);
//                        UIUtils.startActivityNextAnim(intent);
                    } else {
                        btn_next.setEnabled(true);
                        btn_next.setBackgroundResource(R.mipmap.btn_sub);
                        btn_next.setTextColor(UIUtils.getColor(R.color.title_text));
//                        DialogUtils.showAlertDialog(ShowLiveActivity.this, "人脸对比不通过");
                        if((TextUtils.isEmpty(fkbaseResponse.getMsg()) || fkbaseResponse.getMsg().equals("成功")) ){
//                            DialogUtils.showAlertDialog(ShowLiveActivity.this, "人脸对比不通过");
                            DialogUtils.showSingleAlertDialog(ShowLiveActivity.this,"人脸匹配不通过!","请重新按要求进行人脸识别");
                        }else {
//                            DialogUtils.showAlertDialog(ShowLiveActivity.this, fkbaseResponse.getMsg());
                            DialogUtils.showSingleAlertDialog(ShowLiveActivity.this, fkbaseResponse.getMsg(), "请重新按要求进行人脸识别");
                        }
                    }

                } else {
                    btn_next.setEnabled(true);
                    btn_next.setBackgroundResource(R.mipmap.btn_sub);
                    btn_next.setTextColor(UIUtils.getColor(R.color.title_text));
                    if(!TextUtils.isEmpty(fkbaseResponse.getMsg())){
//                        DialogUtils.showAlertDialog(ShowLiveActivity.this, fkbaseResponse.getMsg());
                        DialogUtils.showSingleAlertDialog(ShowLiveActivity.this, fkbaseResponse.getMsg(), "请重新按要求进行人脸识别");
                    }else {
//                        DialogUtils.showAlertDialog(ShowLiveActivity.this, "人脸检测失败");
                        DialogUtils.showSingleAlertDialog(ShowLiveActivity.this, "人脸检测失败", "请重新按要求进行人脸识别");
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                btn_next.setEnabled(true);
                btn_next.setBackgroundResource(R.mipmap.btn_sub);
                btn_next.setTextColor(UIUtils.getColor(R.color.title_text));
                Log.e(TAG, "上传数据返回错误:" + error);
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(ShowLiveActivity.this, error);
                }

            }
        });
    }

    /**
     * 写入授信资料
     */
    private void setCreditInf() {
        loadingDialog.show();
//        String url=Constants.FENGKONGSERVER_URL+"tsfkxt/user/setCreditInf_2.do";
        String url=Constants.FENGKONGSERVER_URL+"tsfkxt/user/setOrdersInf.do";
        Map<String,String> params = new HashMap<String,String>();
        params.put("usrid", userId);
        params.put("submit_step", "2");
        params.put("scene_id",gather_model_id+"");
        params.put("usr_order_id", usr_order_id);
        LogUtils.e("写入征信资料参数:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                btn_next.setEnabled(true);
                btn_next.setBackgroundResource(R.mipmap.btn_sub);
                btn_next.setTextColor(UIUtils.getColor(R.color.title_text));
                Log.e(TAG, "写入征信资料返回:" + json);
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkbaseResponse.getCode() == 0) {
                    setFinish();
                    Intent intent = new Intent(ShowLiveActivity.this, InformationAuthenticationActivity.class);
//                    intent.putExtra("interestsBean", interestsBean);
//                    intent.putExtra("store_name", store_name);
//                    intent.putExtra("store_id", store_id);
//                    intent.putExtra("store_type", store_type);
//                    intent.putExtra("store_contract", store_contract);
//                    intent.putExtra("store_tel", store_tel);
//                    intent.putExtra("store_uid", store_uid);
//                    intent.putExtra("is_tiexi", is_tiexi);
                    intent.putExtra("gather_model_id", gather_model_id);
                    intent.putExtra("usr_order_id", usr_order_id);
                    intent.putExtra("name", name);
                    intent.putExtra("idNumber", idNumber);
                    intent.putExtra("store_type", store_type);
                    intent.putExtra("amount", amount);
                    intent.putExtra("amortization_money", amortization_money);
                    intent.putExtra("order_from",order_from);
                    UIUtils.startActivityNextAnim(intent);
//                    getXiaoShiProductConfig();
                } else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(ShowLiveActivity.this, fkbaseResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                btn_next.setEnabled(true);
                btn_next.setBackgroundResource(R.mipmap.btn_sub);
                btn_next.setTextColor(UIUtils.getColor(R.color.title_text));
                Log.e(TAG, "写入征信资料返回错误:" + error);
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(ShowLiveActivity.this, error);
                }

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
            case R.id.tv_telephone:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                //url:统一资源定位符
                //uri:统一资源标示符（更广）
                intent.setData(Uri.parse("tel:4006626985"));
                //开启系统拨号器
                UIUtils.startActivity(intent);
                break;
            case R.id.btn_next:
                btn_next.setEnabled(false);
                btn_next.setBackgroundResource(R.mipmap.btn_sub_n);
                btn_next.setTextColor(UIUtils.getColor(R.color.text_color_gray));
                getXiaoShiProductConfig();
                break;
        }

    }
}
