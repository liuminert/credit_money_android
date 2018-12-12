package com.tesu.creditgold.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.PictureItemBean;
import com.tesu.creditgold.protocol.AppSendMsgProtocol;
import com.tesu.creditgold.request.AppSendMsgRequest;
import com.tesu.creditgold.response.AppSendMsgResponse;
import com.tesu.creditgold.response.CheckBankCodeResponse;
import com.tesu.creditgold.support.PercentLinearLayout;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.BitmapUtils;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.MobileUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChangebankCardActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_top_back;
    private View rootView;
    private PercentLinearLayout ll_one;
    private EditText et_auth;
    private Button btn_get_code;
    private TextView tv_phone_number;
    private Button btn_next;
    private PercentLinearLayout ll_two;
    private TextView tv_employees_number;
    private EditText et_bank_number;
    private TextView tv_repeat_employees_number;
    private EditText et_repeat_bank_number;
    private TextView tv_bank_photo;
    private PercentRelativeLayout rl_add_image;
    private ImageView iv_bank_photo;
    private ImageView iv_add_photo;
    private Button btn_change;
    private TextView tv_message;

    private String mobile_phone;
    private Dialog loadingDialog;
    private String url;
    private int time = 180;
    private String usrid;
    private String auth_code_id;
    private String auth_code;
    private String bankNo;
    private PopupWindow pWindow;
    private View root;
    private LayoutInflater mInflater;

    private FkBaseResponse fkbaseResponse;
    private Gson gson;

    private String timepath;
    private static final int PHOTO_GRAPH = 1;// 拍照
    private static final int PHOTO_ZOOM = 2; // 缩放
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private File mFile;
    private String path;
    private String picUrl;

    private String newbankNo;
    private String repeatbankNo;

    private boolean hasGetCode;
    private EditText et_phone_number;

    // 图片储存成功后
    protected static final int INTERCEPT = 3;

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
        rootView = View.inflate(this, R.layout.activity_changebank_card, null);
        setContentView(rootView);

        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        ll_one = (PercentLinearLayout) rootView.findViewById(R.id.ll_one);
        et_auth = (EditText) rootView.findViewById(R.id.et_auth);
        btn_get_code = (Button) rootView.findViewById(R.id.btn_get_code);
        tv_phone_number = (TextView) rootView.findViewById(R.id.tv_phone_number);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        ll_two = (PercentLinearLayout) rootView.findViewById(R.id.ll_two);
        tv_employees_number = (TextView) rootView.findViewById(R.id.tv_employees_number);
        et_bank_number = (EditText) rootView.findViewById(R.id.et_bank_number);
        tv_repeat_employees_number = (TextView) rootView.findViewById(R.id.tv_repeat_employees_number);
        et_repeat_bank_number = (EditText) rootView.findViewById(R.id.et_repeat_bank_number);
        tv_bank_photo = (TextView) rootView.findViewById(R.id.tv_bank_photo);
        rl_add_image = (PercentRelativeLayout) rootView.findViewById(R.id.rl_add_image);
        iv_bank_photo = (ImageView) rootView.findViewById(R.id.iv_bank_photo);
        iv_add_photo = (ImageView) rootView.findViewById(R.id.iv_add_photo);
        btn_change = (Button) rootView.findViewById(R.id.btn_change);
        tv_message = (TextView) rootView.findViewById(R.id.tv_message);
        et_phone_number = (EditText) rootView.findViewById(R.id.et_phone_number);

        tv_top_back.setOnClickListener(this);
        btn_get_code.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        rl_add_image.setOnClickListener(this);
        btn_change.setOnClickListener(this);

//        tv_employees_number.setText(Utils.setFountColor(tv_employees_number));
//        tv_repeat_employees_number.setText(Utils.setFountColor(tv_repeat_employees_number));
//        tv_bank_photo.setText(Utils.setFountColor(tv_bank_photo));

        initData();

        return null;
    }

    private void initData() {
        mobile_phone = SharedPrefrenceUtils.getString(this, "mobile_phone");
        usrid = SharedPrefrenceUtils.getString(this, "usrid");
        loadingDialog = DialogUtils.createLoadDialog(ChangebankCardActivity.this, true);

        Intent intent = getIntent();
        bankNo = intent.getStringExtra("bankNo");

        gson = new Gson();

        if(!TextUtils.isEmpty(mobile_phone)){
            et_phone_number.setText(mobile_phone.substring(0, 3) + "*****" + mobile_phone.substring(mobile_phone.length() - 4));
            et_phone_number.setEnabled(false);
        }

        if (mInflater == null) {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        root = mInflater.inflate(R.layout.alert_dialog, null);
        pWindow = new PopupWindow(root, ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.FILL_PARENT);
        root.findViewById(R.id.btn_Phone).setOnClickListener(itemsOnClick);
        root.findViewById(R.id.btn_TakePicture)
                .setOnClickListener(itemsOnClick);
        root.findViewById(R.id.bg_photo).getBackground().setAlpha(100);

        root.findViewById(R.id.btn_cancel).setOnClickListener(itemsOnClick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 拍照
        if (requestCode == PHOTO_GRAPH) {
            // 设置文件保存路径
            File dir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            path = dir + "/" + timepath + ".jpg";

            new Thread() {
                @Override
                public void run() {
                    try {
                        Bitmap photo = BitmapUtils.getSmallBitmap(path);
                        if (photo != null) {
                            Bitmap new_photo = BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            new_photo.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                            String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                    .format(new Date());
//                            String suoPath = BitmapUtils.saveMyBitmap(suoName,new_photo);
//                            uploadImage(suoPath);

                            path = BitmapUtils.saveMyBitmap(suoName, new_photo);
                            handler.sendEmptyMessage(INTERCEPT);

//                            if (!photo.isRecycled()) {
//                                photo.recycle();
//                            }
//                            if (!new_photo.isRecycled()) {
//                                new_photo.recycle();
//                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.e(e.getMessage());
                    }
                }
            }.start();


            //通知相册刷新
            Uri uriData = Uri.parse("file://" + path);
            UIUtils.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uriData));
        }

        // 读取相册缩放图片
        else if (requestCode == PHOTO_ZOOM && data != null) {
            if (data.getData() != null) {
                // 图片信息需包含在返回数据中
                String[] proj = {MediaStore.Images.Media.DATA};
                Uri uri = data.getData();
                // 获取包含所需数据的Cursor对象
                @SuppressWarnings("deprecation")
                Cursor cursor = null;
                try {
                    cursor = managedQuery(uri, proj, null, null, null);
                    if (cursor == null) {
                        uri = BitmapUtils.getPictureUri(data, ChangebankCardActivity.this);
                        cursor = managedQuery(uri, proj, null, null, null);
                    }
                    if (cursor != null) {
                        // 获取索引
                        int photocolumn = cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        // 将光标一直开头
                        cursor.moveToFirst();
                        // 根据索引值获取图片路径
                        path = cursor.getString(photocolumn);
                    } else {
                        path = uri.getPath();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        if(MobileUtils.getSDKVersionNumber() < 14) {
                            cursor.close();
                        }
                    }
                }

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Bitmap photo = BitmapUtils.getSmallBitmap(path);
                            if (photo != null) {
                                Bitmap new_photo = BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                new_photo.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                                String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                        .format(new Date());
//                                String suoPath = BitmapUtils.saveMyBitmap(suoName,new_photo);
//                                uploadImage(suoPath);

                                path = BitmapUtils.saveMyBitmap(suoName, new_photo);
                                handler.sendEmptyMessage(INTERCEPT);

//                                if (!photo.isRecycled()) {
//                                    photo.recycle();
//                                }
//                                if (!new_photo.isRecycled()) {
//                                    new_photo.recycle();
//                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_back:
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.btn_get_code:
                if (TextUtils.isEmpty(mobile_phone)) {
                    UIUtils.showToastCenter(ChangebankCardActivity.this, "手机号码不能为空");
                    return;
                }

                hasGetCode = true;
                btn_get_code.setEnabled(false);
                runGetCode();

                break;
            case R.id.btn_next:
                if(!hasGetCode){
                    UIUtils.showToastCenter(ChangebankCardActivity.this, "请先获取验证码");
                    return;
                }
                auth_code = et_auth.getText().toString();
                if (TextUtils.isEmpty(auth_code)) {
                    UIUtils.showToastCenter(ChangebankCardActivity.this, "请输入验证码");
                    return;
                }

                checkCode();
                break;
            case R.id.rl_add_image:
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.btn_change:
                newbankNo = et_bank_number.getText().toString();
                if (TextUtils.isEmpty(newbankNo)) {
                    UIUtils.showToastCenter(ChangebankCardActivity.this, "银行账号不能为空");
                    return;
                }
                repeatbankNo = et_repeat_bank_number.getText().toString();
                if (TextUtils.isEmpty(repeatbankNo)) {
                    UIUtils.showToastCenter(ChangebankCardActivity.this, "确认银行账号不能为空");
                    return;
                }
                if (!newbankNo.equals(repeatbankNo)) {
                    UIUtils.showToastCenter(ChangebankCardActivity.this, "两次输入银行账号不一致");
                    return;
                }
                if (TextUtils.isEmpty(picUrl)) {
                    UIUtils.showToastCenter(ChangebankCardActivity.this, "银行卡正面照不能为空");
                    return;
                }
                changeBank();
                break;
        }

    }

    /**
     * 修改银行卡
     */
    private void changeBank() {
        loadingDialog.show();
        url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/modifyBankCard.do";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("usrid", usrid);
        params.put("old_bank_id", bankNo);
        params.put("new_bank_id", newbankNo);
        params.put("bank_card_image_url", picUrl);
        LogUtils.e("修改银行卡参数：" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("修改银行卡号:" + json);
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkbaseResponse.getCode() == 0) {
                    UIUtils.showToastCenter(ChangebankCardActivity.this,fkbaseResponse.getMsg());
                    finishActivity();
                    finish();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                } else if(fkbaseResponse.getCode() == 2){
                    Intent intent = new Intent(ChangebankCardActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(ChangebankCardActivity.this,
                                fkbaseResponse.getMsg());
                    }
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(ChangebankCardActivity.this, error);
                }
            }
        });
    }

    /**
     * 校验验证码
     */
    private void checkCode() {
        loadingDialog.show();
        url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/checkAuthCode.do";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("auth_code_id", auth_code_id);
        params.put("auth_code", auth_code);
        LogUtils.e("校验" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();

                LogUtils.e("校验验证码:" + json);
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkbaseResponse.getCode() == 0) {
                    ll_one.setVisibility(View.GONE);
                    ll_two.setVisibility(View.VISIBLE);
                } else if(fkbaseResponse.getCode() == 2){
                    Intent intent = new Intent(ChangebankCardActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(ChangebankCardActivity.this,
                                fkbaseResponse.getMsg());
                    }
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(ChangebankCardActivity.this, error);
                }
            }
        });
    }

    /**
     * 获取验证码
     */
    public void runGetCode() {
        loadingDialog.show();
        url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/getMessageAuthCode.do";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("usrid", usrid);
        params.put("msg_type", "2");
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                LogUtils.e("appSendMsgResponse:" + json);
                Gson gson = new Gson();
                CheckBankCodeResponse checkBankCodeResponse = gson.fromJson(json, CheckBankCodeResponse.class);
                if (checkBankCodeResponse.getCode() == 0) {
                    loadingDialog.dismiss();
                    auth_code_id = String.valueOf(checkBankCodeResponse.getReturn_param().getAuth_code_id());

                    tv_message.setVisibility(View.VISIBLE);
                    tv_phone_number.setVisibility(View.VISIBLE);
                    tv_phone_number.setText(mobile_phone.substring(0, 3) + "*****" + mobile_phone.substring(mobile_phone.length() - 4));

                    Countdowmtimer(180000);
                } else if(checkBankCodeResponse.getCode() == 2){
                    Intent intent = new Intent(ChangebankCardActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {
                    loadingDialog.dismiss();
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(ChangebankCardActivity.this,
                                checkBankCodeResponse.getMsg());
                    }
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                btn_get_code.setEnabled(true);
                hasGetCode = false;

                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(ChangebankCardActivity.this, error);
                }
            }
        });
    }

    /**
     * 计时器
     */
    public void Countdowmtimer(long dodate) {
        new CountDownTimer(dodate, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time = time - 1;
                btn_get_code.setText(time + "s");
            }

            @Override
            // 计时结束
            public void onFinish() {
                time = 180;
                btn_get_code.setEnabled(true);
                btn_get_code.setText("获取验证码");
                tv_message.setVisibility(View.INVISIBLE);
                tv_phone_number.setVisibility(View.INVISIBLE);
                hasGetCode = false;
            }
        }.start();
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            pWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_TakePicture: {
                    timepath = new SimpleDateFormat("yyyyMMdd_HHmmss")
                            .format(new Date());

                    if (Environment.MEDIA_MOUNTED.equals(Environment
                            .getExternalStorageState())) {
                        File dir = Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                        mFile = new File(dir, timepath + ".jpg");

                        startActivityForResult(new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                                        MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile)),
                                PHOTO_GRAPH);
                    }
                    break;
                }
                case R.id.btn_Phone: {
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            IMAGE_UNSPECIFIED);
                    startActivityForResult(intent, PHOTO_ZOOM);
                    break;
                }
                case R.id.btn_cancel: {
                    pWindow.dismiss();
                    break;
                }
                default:
                    break;
            }

        }

    };

    /**
     * 上传图片
     */
    private void uploadImage(final String suoPath) {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/uploadPicture.do";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("usrid", usrid);
        Map<String, String> filesMap = new HashMap<String, String>();
        filesMap.put("pic_string", path);
        MyVolley.uploadWithFileWholeUrl(url, paramMap, filesMap, null, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                LogUtils.e("baseResponse:" + json.toString());
                HashMap<String, String> hashMap = gson.fromJson(gson.toJson(fkbaseResponse.getReturn_param()), HashMap.class);
                picUrl = hashMap.get("pic_path");
                if (fkbaseResponse.getCode() == 0) {
                    Bitmap photo = BitmapUtils.getSmallBitmap(suoPath);
                    iv_bank_photo.setImageBitmap(photo);
                    iv_add_photo.setVisibility(View.GONE);

                    UIUtils.showToastCenter(ChangebankCardActivity.this, "上传成功");

                } else if(fkbaseResponse.getCode() == 2){
                    Intent intent = new Intent(ChangebankCardActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {
                    iv_add_photo.setVisibility(View.VISIBLE);
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(ChangebankCardActivity.this,
                                fkbaseResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                picUrl = null;
                iv_add_photo.setVisibility(View.VISIBLE);
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(ChangebankCardActivity.this,
                            error);
                }
            }
        });

    }

}
