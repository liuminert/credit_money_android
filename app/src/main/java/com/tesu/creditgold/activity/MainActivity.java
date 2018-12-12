package com.tesu.creditgold.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.AddImageAdapter;
import com.tesu.creditgold.adapter.AddLicencePicAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.GetStoreDescStoreIdResponse;
import com.tesu.creditgold.bean.MyAppInfo;
import com.tesu.creditgold.bean.ScanResultBean;
import com.tesu.creditgold.fragment.ControlTabFragment;
import com.tesu.creditgold.protocol.GetAppVersionInfoProtocol;
import com.tesu.creditgold.request.GetAppVersionInfoRequest;
import com.tesu.creditgold.response.GetAppVersionInfoResponse;
import com.tesu.creditgold.response.GetArticleByIdResponse;
import com.tesu.creditgold.response.ImageResponse;
import com.tesu.creditgold.util.AppUtils;
import com.tesu.creditgold.util.BitmapUtils;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.FileUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.MobileUtils;
import com.tesu.creditgold.util.MyExceptionHandler;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/24 0024.
 */
public class MainActivity extends BaseActivity {

    public static boolean isForeground = false;
    public static ControlTabFragment ctf;
    private String path;
    public static String timepath;
    /**
     * 照片参数
     */
    private final int SCANER_CODE = 1;
    private static final int PHOTO_GRAPH = 4;// 拍照
    private static final int PHOTO_ZOOM = 5; // 缩放

    private String head_portrait_pic;
    private String url;
    private Dialog loadingDialog;
    private GetStoreDescStoreIdResponse getStoreDescStoreIdResponse;

    private AlertDialog alertDialog;
    private GetAppVersionInfoResponse getAppVersionInfoResponse;

    public static final String MESSAGE_RECEIVED_ACTION = "com.tesu.creditgold.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    private GetArticleByIdResponse getArticleByIdResponse;
    private String updateStr;

    // 图片储存成功后
    protected static final int INTERCEPT = 6;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case INTERCEPT:
                    uploadImage(path);
                    break;

            }
        }
    };

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = View.inflate(this, R.layout.activity_main, null);
        setContentView(rootView);

        initFragment();
        loadingDialog = DialogUtils.createLoadDialog(MainActivity.this, true);
//        runClientConfigAsyncTask();

        new Thread(){
            @Override
            public void run() {
                super.run();
                List<MyAppInfo> myAppInfos = AppUtils.scanLocalInstallAppList(getPackageManager());
                List<String>  appNames = new ArrayList<String>();
                if(myAppInfos != null){
                    int i = 0;
                    for(MyAppInfo myAppInfo : myAppInfos){

                        i++;
                        if(i>=300){
                            break;
                        }

                        appNames.add(myAppInfo.getAppName());
                    }
                }
                String borrower_app_name = new Gson().toJson(appNames);  //借款人手机应用程序名称
                SharedPrefrenceUtils.setString(MainActivity.this,"borrower_app_name",borrower_app_name);
            }
        }.start();

        getTimetemp();

//        LogUtils.e("DeviceInfo:"+MobileUtils.getDeviceInfo(this));

        return rootView;
    }


    public void getTimetemp() {
        loadingDialog.show();
        HashMap<String,String> hs = new HashMap<String,String>();
        url = Constants.XINYONGSERVER_URL+"credit_money_background/token/timetemp.do";
        MyVolley.uploadNoFileWholeUrl1(MyVolley.POST, url, hs, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                LogUtils.e("json:" + json.toString());
                loadingDialog.dismiss();
                FkBaseResponse fkBaseResponse = new Gson().fromJson(json, FkBaseResponse.class);
                if (fkBaseResponse.getCode() == 0) {
                    String timeStr = fkBaseResponse.getMsg();
                    long timetemp = 0;
                    try {
                        long servTime = Long.valueOf(timeStr);
                        long cuTime = System.currentTimeMillis();
                        timetemp = servTime - cuTime;
                    } catch (Exception e) {
                        e.printStackTrace();
                        timetemp = 0;
                    }

                    SharedPrefrenceUtils.setString(MainActivity.this, "timetemp", String.valueOf(timetemp));
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (!TextUtils.isEmpty(error)) {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(MainActivity.this, error);
                    }
                }
            }
        });
    }



    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        // 1. 开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        // 添加主页fragment
        ctf = new ControlTabFragment();
        transaction.replace(R.id.main_container, ctf);
        transaction.commit();

    }

    public static ControlTabFragment getCtf() {
        if(ctf==null){
            ctf = new ControlTabFragment();
        }
        return ctf;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 监听返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            onKeyDownBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SCANER_CODE) {
                ctf.mCurrentIndex = 0;
                ctf.switchCurrentPage();
                ctf.setChecked(0);

                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("result");
                LogUtils.e("二维码扫描结果:" + scanResult);

//                Intent intent = new Intent(this, InstallmentPaymentActivity.class);
//                intent.putExtra("scanResult", scanResult);
//                UIUtils.startActivityNextAnim(intent);
                int storeId = 0;
                try {
                    storeId = Integer.valueOf(scanResult);
                } catch (NumberFormatException e) {
                    try {
                        ScanResultBean scanResultBean = new Gson().fromJson(scanResult, ScanResultBean.class);
                        storeId = scanResultBean.getStore_id();
                    } catch (JsonSyntaxException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
                getStoreDesc(storeId);


            }

        } else if (resultCode == 2) {
            ctf.mCurrentIndex = 0;
            ctf.switchCurrentPage();
            ctf.setChecked(0);
        } else if (requestCode == 7) {
            ctf.getTabMyselfbyPager().runFindShop();
        }
        else if (requestCode == 8) {
            ctf.getTabMyselfbyPager().runAsyncTask();
        }

        if (requestCode == PHOTO_GRAPH) {
            // 设置文件保存路径
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            path = dir + "/" + timepath + ".jpg";
            File file = new File(path);
            if (file.exists()) {
                try {
                    Bitmap photo = BitmapUtils.getSmallBitmap(path);
                    if(photo != null){
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        Bitmap new_photo = BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));
                        String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                .format(new Date());
                        path = BitmapUtils.saveMyBitmap(suoName, new_photo);
                        handler.sendEmptyMessage(INTERCEPT);

//                        if(!photo.isRecycled()){
//                            photo.recycle();
//                        }
//                        if(!new_photo.isRecycled()){
//                            new_photo.recycle();
//                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            //通知相册刷新
            Uri uriData = Uri.parse("file://" + path);
            UIUtils.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uriData));
        }

        // 读取相册缩放图片
        if (requestCode == PHOTO_ZOOM && data != null) {
            if(data.getData()!=null){
                // 图片信息需包含在返回数据中
                String[] proj = {MediaStore.Images.Media.DATA};
                // 获取包含所需数据的Cursor对象
                @SuppressWarnings("deprecation")
                Uri uri = data.getData();
                // 获取包含所需数据的Cursor对象
                @SuppressWarnings("deprecation")
                Cursor cursor = null;
                try {
                    cursor = managedQuery(uri, proj, null, null, null);
                    if (cursor == null) {
                        uri = BitmapUtils.getPictureUri(data, MainActivity.this);
                        cursor = managedQuery(uri, proj, null, null, null);
                    }
                    if(cursor != null){
                        // 获取索引
                        int photocolumn = cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        // 将光标一直开头
                        cursor.moveToFirst();
                        // 根据索引值获取图片路径
                        path = cursor.getString(photocolumn);
                    }else {
                        path = uri.getPath();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (cursor != null) {
                        if(MobileUtils.getSDKVersionNumber() < 14) {
                            cursor.close();
                        }
                    }
                }

                    if (!TextUtils.isEmpty(path)) {
                        new Thread() {
                            public void run() {
                                try {
                                    Bitmap photo = BitmapUtils.getSmallBitmap(path);
                                    if(photo != null){
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                        Bitmap new_photo = BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));
                                        String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                                .format(new Date());
                                        path = BitmapUtils.saveMyBitmap(suoName, new_photo);
                                        handler.sendEmptyMessage(INTERCEPT);

//                                        if(!photo.isRecycled()){
//                                            photo.recycle();
//                                        }
//                                        if(!new_photo.isRecycled()){
//                                            new_photo.recycle();
//                                        }
                                    }
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }

                            ;
                        }.start();

                    }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 根据扫描后的storeId获得店铺信息
     *
     * @param storeId
     */
    private void getStoreDesc(int storeId) {
        loadingDialog.show();
        String url = Constants.XINYONGSERVER_URL + "credit_money_background/user/findStoreDescByStoreId.do";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("store_id", storeId + "");
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                LogUtils.e("获取店铺信息:" + json);
                loadingDialog.dismiss();
                Gson gson = new Gson();
                getStoreDescStoreIdResponse = gson.fromJson(json, GetStoreDescStoreIdResponse.class);
                if (getStoreDescStoreIdResponse.getCode() == 0) {
                    ScanResultBean scanResultBean = getStoreDescStoreIdResponse.getData();
                    switch (scanResultBean.getStore_status()) {
                        case 0:
//                            if(!isFinishing()){
//                                DialogUtils.showAlertDialog(MainActivity.this, "该店铺已经被禁用");
//                            }
                            Intent intent1 = new Intent(MainActivity.this, ScanShopActivity.class);
                            intent1.putExtra("shop_message", "该店铺已经被禁用");
                            UIUtils.startActivityNextAnim(intent1);
                            break;
                        case 1:
                            Intent intent = new Intent(MainActivity.this, InstallmentPaymentActivity.class);
                            intent.putExtra("store_id", scanResultBean.getStore_id());
                            intent.putExtra("store_type", scanResultBean.getStore_type_id());
                            intent.putExtra("store_name", scanResultBean.getStore_name());
                            intent.putExtra("store_contract", scanResultBean.getStore_contract());
                            intent.putExtra("store_tel", scanResultBean.getStore_tel());
                            intent.putExtra("usr_id", scanResultBean.getUsr_id());
                            intent.putExtra("is_tiexi", scanResultBean.getIs_tiexi());
                            intent.putExtra("gather_model_id", scanResultBean.getGather_model_id());
                            if (!TextUtils.isEmpty(scanResultBean.getStore_grade())) {
                                intent.putExtra("store_grade", scanResultBean.getStore_grade());
                            }

                            UIUtils.startActivityNextAnim(intent);
                            break;
                        case 2:
//                            if(!isFinishing()){
//                                DialogUtils.showAlertDialog(MainActivity.this, "该店铺为待审核状态");
//                            }
                            Intent intent2 = new Intent(MainActivity.this, ScanShopActivity.class);
                            intent2.putExtra("shop_message", "该店铺为待审核状态");
                            UIUtils.startActivityNextAnim(intent2);
                            break;
                        case 3:
//                            if(!isFinishing()){
//                                DialogUtils.showAlertDialog(MainActivity.this, "该店铺审核不通过");
//                            }
                            Intent intent3 = new Intent(MainActivity.this, ScanShopActivity.class);
                            intent3.putExtra("shop_message", "该店铺审核不通过");
                            UIUtils.startActivityNextAnim(intent3);
                            break;
                        case 100:
//                            if(!isFinishing()){
//                                DialogUtils.showAlertDialog(MainActivity.this, "该店铺状态不正常");
//                            }
                            Intent intent4 = new Intent(MainActivity.this, ScanShopActivity.class);
                            intent4.putExtra("shop_message", "该店铺状态不正常");
                            UIUtils.startActivityNextAnim(intent4);
                            break;
                    }

                } else {
                    if (!TextUtils.isEmpty(getStoreDescStoreIdResponse.getMsg())) {
                        if (!isFinishing()) {
                            DialogUtils.showAlertDialog(MainActivity.this, getStoreDescStoreIdResponse.getMsg());
                        }
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (!TextUtils.isEmpty(error)) {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(MainActivity.this, error);
                    }
                }

            }
        });
    }


    /**
     * 上传图片
     */
    private void uploadImage(final String suoPath) {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/uploadPicture.do";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("usrid", SharedPrefrenceUtils.getString(MainActivity.this, "usrid"));
        Map<String, String> filesMap = new HashMap<String, String>();
        filesMap.put("pic_string", path);
        MyVolley.uploadWithFileWholeUrl(url, paramMap, filesMap, null, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                ImageResponse imageResponse = gson.fromJson(json, ImageResponse.class);
                LogUtils.e("baseResponse:" + json.toString());
                if (imageResponse.code == 0) {
                    head_portrait_pic = imageResponse.return_param.pic_path;
                    setCreditInf();
                } else {
                    if (!TextUtils.isEmpty(imageResponse.msg)) {
                        if (!isFinishing()) {
                            DialogUtils.showAlertDialog(MainActivity.this,
                                    imageResponse.msg);
                        }
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (!TextUtils.isEmpty(error)) {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(MainActivity.this,
                                error);
                    }
                }
            }
        });

    }

    /**
     * 修改头像
     */
    private void setCreditInf() {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/setCreditInf_2.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("usrid", SharedPrefrenceUtils.getString(MainActivity.this, "usrid"));
        params.put("head_portrait_pic", head_portrait_pic);
        LogUtils.e("params:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                FkBaseResponse fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkbaseResponse.getCode() == 0) {
//                    ctf.getTabMyselfbyPager().initData();
                    ctf.getTabMyselfbyPager().runAsyncTask();
                } else {
                    if (!TextUtils.isEmpty(fkbaseResponse.getMsg())) {
                        if (!isFinishing()) {
                            DialogUtils.showAlertDialog(MainActivity.this, fkbaseResponse.getMsg());
                        }
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (!TextUtils.isEmpty(error)) {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(MainActivity.this, error);
                    }
                }

            }
        });
    }

    public void runClientConfigAsyncTask() {
        GetAppVersionInfoProtocol getAppVersionInfoProtocol = new GetAppVersionInfoProtocol();
        GetAppVersionInfoRequest getAppVersionInfoRequest = new GetAppVersionInfoRequest();
        getAppVersionInfoRequest.map.put("platform", "android");
        String url = Constants.XINYONGSERVER_URL +  getAppVersionInfoProtocol.getApiFun();
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, getAppVersionInfoRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                try {
                    Gson gson = new Gson();
                    getAppVersionInfoResponse = gson.fromJson(json, GetAppVersionInfoResponse.class);
                    LogUtils.e("更新"+json.toString());
                    if (getAppVersionInfoResponse.code == 0) {
                        if (getAppVersionInfoResponse.data.newest_version > Double.parseDouble(Utils.getVersionName(MainActivity.this))) {
                            getArticleById(Constants.updateStrId);
                        }
//                        }
                    }
                    else {
                        if(!TextUtils.isEmpty(getAppVersionInfoResponse.msg)){
                            if(!isFinishing()){
                                DialogUtils.showAlertDialog(MainActivity.this, getAppVersionInfoResponse.msg);
                            }
                        }
                    }
                } catch (JsonSyntaxException e) {

                }


            }

            @Override
            public void dealWithError(String address, String error) {
            }
        });
    }

    /**
     * 根据id获取合同
     */
    private void getArticleById(int article_id) {
        loadingDialog.show();
        String url= Constants.XINYONGSERVER_URL+"credit_money_background/user/getArticleById.do";
        Map<String,String> params = new HashMap<String,String>();
        params.put("article_id", article_id+"");
        LogUtils.e("params:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("获取合同:" + json);
                getArticleByIdResponse = new Gson().fromJson(json, GetArticleByIdResponse.class);
                if (getArticleByIdResponse.getCode() == 0) {
                    updateStr = getArticleByIdResponse.getData().getArticle_content();
                    if(!isFinishing()){
                        if(getAppVersionInfoResponse.data.force_update==0){
                            alertDialog = DialogUtils.showUpdateDialog(MainActivity.this,updateStr, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    switch (v.getId()){
                                        case R.id.tv_cancel:
                                            alertDialog.dismiss();
                                            break;
                                        case R.id.tv_update:
                                            Uri uri = Uri.parse(getAppVersionInfoResponse.data.download_url);
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            startActivity(intent);
                                            break;
                                    }
                                }
                            },false);
                        }else {
                            alertDialog = DialogUtils.showUpdateDialog(MainActivity.this, updateStr, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    switch (v.getId()){
                                        case R.id.tv_cancel:
                                            alertDialog.dismiss();
                                            finish();
                                            break;
                                        case R.id.tv_update:
                                            Uri uri = Uri.parse(getAppVersionInfoResponse.data.download_url);
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            startActivity(intent);
                                            break;
                                    }
                                }
                            }, true);
                        }
                    }
                } else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(MainActivity.this, getArticleByIdResponse.getMsg());
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(MainActivity.this, error);
                }
            }
        });
    }
}
