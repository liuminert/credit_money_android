package com.tesu.creditgold.activity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.minivision.livebodyauthentication.LiveBodyAuthenticationActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.InterestsBean;
import com.tesu.creditgold.bean.PictureItemBean;
import com.tesu.creditgold.bean.XiaoShiBean;
import com.tesu.creditgold.fragment.ControlTabFragment;
import com.tesu.creditgold.response.GetUsrInfResponse;
import com.tesu.creditgold.support.PercentLinearLayout;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.BitmapUtils;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LimitInputTextWatcher;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.MobileUtils;
import com.tesu.creditgold.util.PictureOption;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.util.Utils;
import com.tesu.creditgold.widget.LimitEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IdentityAuthenticationActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = IdentityAuthenticationActivity.class.getSimpleName();
    private View rootView;
    private TextView tv_top_back;
    private ControlTabFragment ctf;
    private Dialog loadingDialog;
    private EditText et_name;
    private EditText et_id_number;
    private PercentRelativeLayout rl_add_front;
    private PercentRelativeLayout rl_add_back;
    private PercentRelativeLayout rl_add_hand;
    private Button btn_next;
    private PopupWindow pWindow;
    private View root;
    private LayoutInflater mInflater;
    private String timepath;
    private static final int PHOTO_GRAPH = 1;// 拍照
    private static final int PHOTO_ZOOM = 2; // 缩放
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private File mFile;
    private String path;
    private int type;  //照片类型。 0 正面照，  1 反面照，  2 手持身份证

    private ImageView iv_add_front_backgrond;
    private ImageView iv_add_back_backgrond;
    private ImageView iv_add_hand_backgrond;
    protected static final int LIVE_BODE = 99; //活体检测

    private String userId;
    private FkBaseResponse fkbaseResponse;
    private Gson gson;
    private PictureItemBean frontPictureItemBean;
    private PictureItemBean backPictureItemBean;
    private PictureItemBean handPictureItemBean;
    private String name;
    private String idNumber;
    private InterestsBean interestsBean;
    private String store_name;
    private int store_id;
    private static final int SHOWIMAGE = 101;
    private GetUsrInfResponse getUsrInfResponse;
    private ImageLoader imageLoader;
    private int gather_model_id;
    private String usr_order_id;
    private TextView tv_name;
    private TextView tv_id_number;
    private TextView tv_image;
    private int store_type;
    private String amount;
    private String amortization_money;
    private int order_from; //订单来源 0：买家下单，1：卖家下单

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
        rootView = View.inflate(this, R.layout.activity_identity_authentication, null);
        setContentView(rootView);

        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        et_name = (EditText) rootView.findViewById(R.id.et_name);
        et_id_number = (EditText) rootView.findViewById(R.id.et_id_number);
        rl_add_front = (PercentRelativeLayout) rootView.findViewById(R.id.rl_add_front);
        rl_add_back = (PercentRelativeLayout) rootView.findViewById(R.id.rl_add_back);
        rl_add_hand = (PercentRelativeLayout) rootView.findViewById(R.id.rl_add_hand);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        iv_add_front_backgrond = (ImageView) rootView.findViewById(R.id.iv_add_front_backgrond);
        iv_add_back_backgrond = (ImageView) rootView.findViewById(R.id.iv_add_back_backgrond);
        iv_add_hand_backgrond = (ImageView) rootView.findViewById(R.id.iv_add_hand_backgrond);
        tv_name = (TextView) rootView.findViewById(R.id.tv_name);
        tv_id_number = (TextView) rootView.findViewById(R.id.tv_id_number);
        tv_image = (TextView) rootView.findViewById(R.id.tv_image);
        if (ctf == null) {
            ctf = MainActivity.getCtf();
        }

//        et_name.addTextChangedListener(new LimitInputTextWatcher(et_name));

//        tv_name.setText(Utils.setFountColor(tv_name));
//        tv_id_number.setText(Utils.setFountColor(tv_id_number));
//        tv_image.setText(Utils.setFountColor(tv_image));

        userId = SharedPrefrenceUtils.getString(this, "usrid");
        gson = new Gson();
        initData();
        return null;
    }

    private void initData() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));

        Intent intent = getIntent();
        interestsBean = (InterestsBean) intent.getSerializableExtra("interestsBean");
        usr_order_id = intent.getStringExtra("usr_order_id");
        store_name = intent.getStringExtra("store_name");
        store_id = intent.getIntExtra("store_id", 0);
        gather_model_id = intent.getIntExtra("gather_model_id", 0);
        store_type = intent.getIntExtra("store_type",0);
        amount = intent.getStringExtra("amount");
        amortization_money = intent.getStringExtra("amortization_money");
        order_from = intent.getIntExtra("order_from",0);

        loadingDialog = DialogUtils.createLoadDialog(IdentityAuthenticationActivity.this, false);

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

        tv_top_back.setOnClickListener(this);
        rl_add_back.setOnClickListener(this);
        rl_add_front.setOnClickListener(this);
        rl_add_hand.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        getgetUsrInf();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.e("pWindow:"+pWindow.isShowing());
        if (keyCode == 4) {
            if(pWindow.isShowing()){
                pWindow.dismiss();
                return  false;
            }else{
//                if (ctf == null) {
//                    ctf = MainActivity.getCtf();
//                }
//                ctf.setChecked(ctf.beforeIndex);
//                ctf.mCurrentIndex = ctf.beforeIndex;
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_top_back:
                ctf.setChecked(ctf.beforeIndex);
                ctf.mCurrentIndex = ctf.beforeIndex;
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.rl_add_front:
                type = 0;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.rl_add_back:
                type = 1;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.rl_add_hand:
                type = 2;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;

            case R.id.iv_add_front_backgrond:
                if(frontPictureItemBean != null){
                    type = 0;
                    Intent intent1 = new Intent(IdentityAuthenticationActivity.this,ShowImageActivity.class);
                    intent1.putExtra("pictureItemBean", frontPictureItemBean);
                    UIUtils.startActivityForResult(intent1,SHOWIMAGE);
                }
                break;
            case R.id.iv_add_back_backgrond:
                if(backPictureItemBean != null){
                    type = 1;
                    Intent intent1 = new Intent(IdentityAuthenticationActivity.this,ShowImageActivity.class);
                    intent1.putExtra("pictureItemBean", backPictureItemBean);
                    UIUtils.startActivityForResult(intent1,SHOWIMAGE);
                }
                break;
            case R.id.iv_add_hand_backgrond:
                if(handPictureItemBean != null){
                    type = 2;
                    Intent intent1 = new Intent(IdentityAuthenticationActivity.this,ShowImageActivity.class);
                    intent1.putExtra("pictureItemBean", handPictureItemBean);
                    UIUtils.startActivityForResult(intent1,SHOWIMAGE);
                }
                break;
            case R.id.btn_next:
                name = et_name.getText().toString();
                if(TextUtils.isEmpty(name)){
                    UIUtils.showToastCenter(IdentityAuthenticationActivity.this, "姓名不能为空");
                    return;
                }

                idNumber = et_id_number.getText().toString();
                if(TextUtils.isEmpty(idNumber)){
                    UIUtils.showToastCenter(IdentityAuthenticationActivity.this, "身份证号不能为空");
                    return;
                }

                if(frontPictureItemBean == null){
                    UIUtils.showToastCenter(IdentityAuthenticationActivity.this, "正面照不能为空");
                    return;
                }

                if(backPictureItemBean == null){
                    UIUtils.showToastCenter(IdentityAuthenticationActivity.this, "反面照不能为空");
                    return;
                }
                if(handPictureItemBean == null){
                    UIUtils.showToastCenter(IdentityAuthenticationActivity.this, "手持身份证不能为空");
                    return;
                }

//                getXiaoShiProductConfig();
                btn_next.setEnabled(false);
                btn_next.setBackgroundResource(R.mipmap.btn_sub_n);
                btn_next.setTextColor(UIUtils.getColor(R.color.text_color_gray));
                setCreditInf();

                break;

        }

    }

    /**
     * 获取用户信息详情接口
     */
    private void getgetUsrInf() {
        loadingDialog.show();
        String url= Constants.FENGKONGSERVER_URL+"tsfkxt/user/getUsrInf.do";
        Map<String,String> params = new HashMap<String,String>();
        params.put("usrid", userId);
        MyVolley.uploadNoFileWholeUrl(MyVolley.GET, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Log.e(TAG, "获取用户信息:" + json);
                getUsrInfResponse = gson.fromJson(json, GetUsrInfResponse.class);
                if (getUsrInfResponse.code.equals("0")) {
                    GetUsrInfResponse.UsrInDate usrInDate = getUsrInfResponse.return_param;
                    if (usrInDate != null) {
                        if(usrInDate.is_real_name_check==0){
                            et_name.setEnabled(true);
                            et_id_number.setEnabled(true);

                        }else {
                            if (!TextUtils.isEmpty(usrInDate.borrower_usrname)) {
                                et_name.setText(usrInDate.borrower_usrname);
                            } else if (!TextUtils.isEmpty(usrInDate.usrname)) {
                                et_name.setText(usrInDate.usrname);
                            }

                            if (!TextUtils.isEmpty(usrInDate.borrower_id_card)) {
                                et_id_number.setText(usrInDate.borrower_id_card);
                            } else if (!TextUtils.isEmpty(usrInDate.id_card)) {
                                et_id_number.setText(usrInDate.id_card);
                            }
                            et_name.setEnabled(false);
                            et_id_number.setEnabled(false);
                        }

                        String frontUrl = usrInDate.id_card_front_pic;
                        if (!TextUtils.isEmpty(frontUrl) && !frontUrl.contains("null")) {
                            imageLoader.displayImage(usrInDate.id_card_front_pic, iv_add_front_backgrond, PictureOption.getSimpleOptions());
                            frontPictureItemBean = new PictureItemBean();
                            frontPictureItemBean.setPicWholeUrl(frontUrl);
                            frontUrl = extractString(frontUrl);
                            frontPictureItemBean.setPictureUrl(frontUrl);
                        }

                        String backUrl = usrInDate.id_card_reverse_pic;
                        if (!TextUtils.isEmpty(backUrl) && !backUrl.contains("null")) {
                            imageLoader.displayImage(usrInDate.id_card_reverse_pic, iv_add_back_backgrond, PictureOption.getSimpleOptions());
                            backPictureItemBean = new PictureItemBean();
                            backPictureItemBean.setPicWholeUrl(backUrl);
                            backUrl = extractString(backUrl);
                            backPictureItemBean.setPictureUrl(backUrl);
                        }

                        String handUrl = usrInDate.with_id_card_pic;
                        if (!TextUtils.isEmpty(handUrl) && !handUrl.contains("null")) {
                            imageLoader.displayImage(usrInDate.with_id_card_pic, iv_add_hand_backgrond, PictureOption.getSimpleOptions());
                            handPictureItemBean = new PictureItemBean();
                            handPictureItemBean.setPicWholeUrl(handUrl);
                            handUrl = extractString(handUrl);
                            handPictureItemBean.setPictureUrl(handUrl);
                        }
                        LogUtils.e("handUrl:" + handUrl);

                    }
                } else if(getUsrInfResponse.code.equals("2")){
                    Intent intent = new Intent(IdentityAuthenticationActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(IdentityAuthenticationActivity.this, getUsrInfResponse.msg);
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                Log.e(TAG, "获取用户信息错误:" + error);
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(IdentityAuthenticationActivity.this, error);
                }
            }
        });

    }

    /**
     * 截取第三个"/"后面的内容
     * @param s
     * @return
     */
    private String extractString(String s){
        for(int i = 0; i < 3; i++){
            s = s.substring(s.indexOf("/")+1 );
        }
        return s;

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
        params.put("submit_step", "1");
        params.put("usr_order_id", usr_order_id);
        params.put("scene_id",gather_model_id+"");
        params.put("borrower_usrname", name);
        params.put("borrower_id_card", idNumber);
        params.put("id_card_front_pic", frontPictureItemBean.getPictureUrl());
        params.put("id_card_reverse_pic", backPictureItemBean.getPictureUrl());
        params.put("with_id_card_pic", handPictureItemBean.getPictureUrl());
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
                    Intent intent = new Intent(IdentityAuthenticationActivity.this, ShowLiveActivity.class);
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
                } else if(fkbaseResponse.getCode() == 2){
                    Intent intent = new Intent(IdentityAuthenticationActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(IdentityAuthenticationActivity.this, fkbaseResponse.getMsg());
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
                    DialogUtils.showAlertDialog(IdentityAuthenticationActivity.this, error);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 拍照
        if (requestCode == PHOTO_GRAPH) {
            // 设置文件保存路径
            File dir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            path = dir + "/" + timepath + ".jpg";

            new Thread(){
                @Override
                public void run() {
                    try {
                        Bitmap photo = BitmapUtils.getSmallBitmap(path);
                        if(photo != null){
                            Bitmap new_photo=BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            new_photo.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                            String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                    .format(new Date());
//                            String suoPath = BitmapUtils.saveMyBitmap(suoName,new_photo);
//                            uploadImage(suoPath);

                            path = BitmapUtils.saveMyBitmap(suoName, new_photo);
                            handler.sendEmptyMessage(INTERCEPT);

//                            if(!photo.isRecycled()){
//                                photo.recycle();
//                            }
//                            if(!new_photo.isRecycled()){
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
            if(data.getData()!=null){
                // 图片信息需包含在返回数据中
                String[] proj = { MediaStore.Images.Media.DATA };
                Uri uri = data.getData();
                // 获取包含所需数据的Cursor对象
                @SuppressWarnings("deprecation")
                Cursor cursor = null;
                try {
                    cursor = managedQuery(uri, proj, null, null, null);
                    if(cursor == null){
                        uri = BitmapUtils.getPictureUri(data,IdentityAuthenticationActivity.this);
                        cursor = managedQuery(uri, proj, null, null, null);
                    }
                    if(cursor != null) {
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

                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Bitmap photo = BitmapUtils.getSmallBitmap(path);
                            if(photo != null){
                                Bitmap new_photo=BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                new_photo.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                                String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                        .format(new Date());
//                                String suoPath = BitmapUtils.saveMyBitmap(suoName,new_photo);
//                                uploadImage(suoPath);

                                path = BitmapUtils.saveMyBitmap(suoName, new_photo);
                                handler.sendEmptyMessage(INTERCEPT);

//                                if(!photo.isRecycled()){
//                                    photo.recycle();
//                                }
//                                if(!new_photo.isRecycled()){
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

        if(requestCode == SHOWIMAGE && data != null){
            if(type == 0){
                frontPictureItemBean = null;
                iv_add_front_backgrond.setImageResource(R.mipmap.id_card_image01);
            }else if(type == 1){
                backPictureItemBean = null;
                iv_add_back_backgrond.setImageResource(R.mipmap.id_card_image02);
            }else if(type == 2){
                handPictureItemBean = null;
                iv_add_hand_backgrond.setImageResource(R.mipmap.id_card_image03);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**上传图片*/
    private void uploadImage(final String suoPath) {
        loadingDialog.show();
        String url =Constants.FENGKONGSERVER_URL+"tsfkxt/user/uploadPicture.do";
        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put("usrid",userId);
        Map<String,String> filesMap = new HashMap<String,String>();
        filesMap.put("pic_string", path);
        MyVolley.uploadWithFileWholeUrl(url, paramMap, filesMap, null, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                LogUtils.e("baseResponse:" + json.toString());
                HashMap<String, String> hashMap = gson.fromJson(gson.toJson(fkbaseResponse.getReturn_param()), HashMap.class);
                String picUrl = hashMap.get("pic_path");
                if (fkbaseResponse.getCode() == 0) {
                    Bitmap photo = BitmapUtils.getSmallBitmap(suoPath);
                    switch (type) {
                        case 0:
                            frontPictureItemBean = new PictureItemBean();
                            frontPictureItemBean.setPicturePath(suoPath);
                            frontPictureItemBean.setPictureUrl(picUrl);
                            iv_add_front_backgrond.setImageBitmap(photo);
                            break;
                        case 1:
                            backPictureItemBean = new PictureItemBean();
                            backPictureItemBean.setPicturePath(suoPath);
                            backPictureItemBean.setPictureUrl(picUrl);
                            iv_add_back_backgrond.setImageBitmap(photo);
                            break;
                        case 2:
                            handPictureItemBean = new PictureItemBean();
                            handPictureItemBean.setPicturePath(suoPath);
                            handPictureItemBean.setPictureUrl(picUrl);
                            iv_add_hand_backgrond.setImageBitmap(photo);
                            break;
                    }

                    UIUtils.showToastCenter(IdentityAuthenticationActivity.this, "上传成功");

                } else if(fkbaseResponse.getCode() == 2){
                    Intent intent = new Intent(IdentityAuthenticationActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {

                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(IdentityAuthenticationActivity.this,
                                fkbaseResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();

                if(!isFinishing()){
                    DialogUtils.showAlertDialog(IdentityAuthenticationActivity.this,
                            error);
                }
            }
        });

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


}
