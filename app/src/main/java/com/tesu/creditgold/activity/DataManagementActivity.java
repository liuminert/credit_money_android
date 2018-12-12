package com.tesu.creditgold.activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.AddBusinessAdapter;
import com.tesu.creditgold.adapter.AddImageAdapter;
import com.tesu.creditgold.adapter.AddLicencePicAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.callback.ImageCallBack;
import com.tesu.creditgold.fragment.ControlTabFragment;
import com.tesu.creditgold.protocol.UpdateStoreInfoProtocol;
import com.tesu.creditgold.request.UpdateStoreInfoRequest;
import com.tesu.creditgold.response.FindStoreDescByUsridResponse;
import com.tesu.creditgold.response.ImageResponse;
import com.tesu.creditgold.response.UpdateStoreInfoResponse;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.BitmapUtils;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.MobileUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;

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
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 账户管理
 */
public class DataManagementActivity extends BaseActivity implements View.OnClickListener, ImageCallBack,View.OnTouchListener {
    //图片的类型，0代表门脸照，1代表执照,2代表场所照片，3代表身份证正面，4代表反面
    private int type;
    private TextView tv_top_back;
    private View rootView;
    private ImageView iv_license_image;
    private ImageView iv_ic_card_behind;
    private ImageView iv_ic_card_front;
    private TextView tv_name;
    private EditText et_name;
    private TextView tv_tell;
    private EditText et_tell;
    private TextView tv_introduce;
    private EditText et_introduce;
    private Button btn_commit;
    private FindStoreDescByUsridResponse.StoreIfo storeIfo;
    private TextView tv_store_name;
    private TextView tv_store_type;
    private TextView tv_address;
    /**
     * 照相选择界面
     */
    private PopupWindow pWindow;
    private View root;
    private LayoutInflater mInflater;
    private String timepath;
    /**
     * 照片参数
     */
    private static final int PHOTO_GRAPH = 1;// 拍照
    private static final int PHOTO_ZOOM = 2; // 缩放
    private static final int DESCRIPTION_RESOULT = 3;// 结果

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private File mFile;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private String path;
    // 图片储存成功后
    protected static final int INTERCEPT = 3;
    //接口请求菊花
    private Dialog loadingDialog;
    private String url;
    private String contract_name;
    private String contract_telephone;
    private String description;
    //是否修改
    private boolean isUpdate=true;
    private String image;
    private String business_image;

    private ImageView iv_shop_image;
    private ImageView iv_delete_shop;
    private ImageView iv_buessies_image;
    private ImageView iv_delete_bussies;
    private Bitmap bt_shop_image;
    private Bitmap bt_buessies_image;
    private PercentRelativeLayout rl_shop;
    private PercentRelativeLayout rl_shop_type;
    private PercentRelativeLayout rl_address;
    private PercentRelativeLayout rl;
    private PercentRelativeLayout rl_ic_card;

    private ImageView line1;
    private ImageView line2;
    private ImageView line3;
    private ControlTabFragment ctf;

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
        rootView = View.inflate(this, R.layout.activity_data_management, null);
        setContentView(rootView);
        tv_store_name = (TextView) rootView.findViewById(R.id.tv_store_name);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        iv_license_image = (ImageView) rootView.findViewById(R.id.iv_license_image);
        iv_ic_card_front= (ImageView) rootView.findViewById(R.id.iv_ic_card_front);
        iv_ic_card_behind= (ImageView) rootView.findViewById(R.id.iv_ic_card_behind);
//        iv_buessies_image= (ImageView) rootView.findViewById(R.id.iv_buessies_image);
        tv_name = (TextView) rootView.findViewById(R.id.tv_name);
        et_name = (EditText) rootView.findViewById(R.id.et_name);
        tv_store_type = (TextView) rootView.findViewById(R.id.tv_store_type);
        tv_tell = (TextView) rootView.findViewById(R.id.tv_tell);
        et_tell = (EditText) rootView.findViewById(R.id.et_tell);
        tv_introduce = (TextView) rootView.findViewById(R.id.tv_introduce);
        tv_address = (TextView) rootView.findViewById(R.id.tv_address);
        et_introduce = (EditText) rootView.findViewById(R.id.et_introduce);
        btn_commit = (Button) rootView.findViewById(R.id.btn_commit);
        iv_shop_image = (ImageView) rootView.findViewById(R.id.iv_shop_image);
        iv_delete_shop = (ImageView) rootView.findViewById(R.id.iv_delete_shop);
        iv_buessies_image = (ImageView) rootView.findViewById(R.id.iv_buessies_image);
        iv_delete_bussies = (ImageView) rootView.findViewById(R.id.iv_delete_bussies);
        rl_shop = (PercentRelativeLayout) rootView.findViewById(R.id.rl_shop);
        rl_shop_type = (PercentRelativeLayout) rootView.findViewById(R.id.rl_shop_type);
        rl_address = (PercentRelativeLayout) rootView.findViewById(R.id.rl_address);
        rl = (PercentRelativeLayout) rootView.findViewById(R.id.rl);
        rl_ic_card = (PercentRelativeLayout) rootView.findViewById(R.id.rl_ic_card);
        line1 = (ImageView) rootView.findViewById(R.id.line1);
        line2 = (ImageView) rootView.findViewById(R.id.line2);
        line3 = (ImageView) rootView.findViewById(R.id.line3);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }
        root = mInflater.inflate(R.layout.alert_dialog, null);
        pWindow = new PopupWindow(root, ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.FILL_PARENT);
        root.findViewById(R.id.btn_Phone).setOnClickListener(itemsOnClick);
        root.findViewById(R.id.btn_TakePicture)
                .setOnClickListener(itemsOnClick);
        root.findViewById(R.id.bg_photo).getBackground().setAlpha(100);

        root.findViewById(R.id.btn_cancel).setOnClickListener(itemsOnClick);

        et_introduce.setMovementMethod(ScrollingMovementMethod.getInstance());
        et_introduce.setOnTouchListener(this);

        initData();
        return null;
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            pWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_TakePicture: {
                    timepath = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                        mFile = new File(dir, timepath + ".jpg");
                        if (ContextCompat.checkSelfPermission(DataManagementActivity.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(DataManagementActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_CAMERA);
                        } else {
                            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                                    MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile)), PHOTO_GRAPH);
                        }
                    }
                    break;
                }
                case R.id.btn_Phone: {
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            IMAGE_UNSPECIFIED);
                    UIUtils.startActivityForResult(intent, PHOTO_ZOOM);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 拍照
        if (requestCode == PHOTO_GRAPH) {
            // 设置文件保存路径
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            path = dir + "/" + timepath + ".jpg";
            File file = new File(path);
            if (file.exists()) {
                new Thread() {
                    public void run() {
                        try {
                            Bitmap photo = BitmapUtils.getSmallBitmap(path);
                            if(photo != null){
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                Bitmap new_photo = BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));
                                if (type == 0) {
                                    bt_shop_image = new_photo;
                                }  else if (type == 2) {
                                   bt_buessies_image = new_photo;
                                }

                                String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                        .format(new Date());
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
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    ;
                }.start();
                //通知相册刷新
                Uri uriData = Uri.parse("file://" + path);
                UIUtils.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uriData));
            }

        }

        // 读取相册缩放图片
        if (requestCode == PHOTO_ZOOM && data != null) {
            if(data.getData()!=null){
                // 图片信息需包含在返回数据中
                String[] proj = {MediaStore.Images.Media.DATA};
                Uri uri = data.getData();
                // 获取包含所需数据的Cursor对象
                @SuppressWarnings("deprecation")
                Cursor cursor = null;
                try {
                    cursor = managedQuery(uri, proj, null, null, null);
                    if(cursor == null){
                        uri = BitmapUtils.getPictureUri(data,DataManagementActivity.this);
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
                                        if (type == 0) {
                                            bt_shop_image = new_photo;
                                        }  else if (type == 2) {
                                            bt_buessies_image = new_photo;
                                        }

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

    public void initData() {
        if (ctf == null) {
            ctf = MainActivity.getCtf();
        }

        loadingDialog = DialogUtils.createLoadDialog(DataManagementActivity.this, true);
        Intent intent = getIntent();
        storeIfo = (FindStoreDescByUsridResponse.StoreIfo) intent.getSerializableExtra("store_info");
        tv_store_name.setText(storeIfo.store_name);
        tv_name.setText(storeIfo.contract_name);
        tv_tell.setText(storeIfo.contract_telephone);
        tv_store_type.setText(storeIfo.store_type_name);
        tv_address.setText(storeIfo.area_info+storeIfo.address);
        tv_top_back.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
        iv_shop_image.setOnClickListener(this);
        iv_buessies_image.setOnClickListener(this);
        iv_delete_shop.setOnClickListener(this);
        iv_delete_bussies.setOnClickListener(this);
        ImageLoader.getInstance().loadImage(storeIfo.store_pic, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                iv_shop_image.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        ImageLoader.getInstance().loadImage(storeIfo.licence_pic, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                iv_license_image.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        ImageLoader.getInstance().loadImage(storeIfo.businesssite_pic, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                iv_buessies_image.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        ImageLoader.getInstance().loadImage(storeIfo.id_front_pic, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                iv_ic_card_front.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        ImageLoader.getInstance().loadImage(storeIfo.id_back_pic, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                iv_ic_card_behind.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        tv_introduce.setText(storeIfo.description);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit: {
                if(isUpdate){
                    tv_introduce.setVisibility(View.GONE);
                    et_introduce.setVisibility(View.VISIBLE);
                    tv_name.setVisibility(View.GONE);
                    et_name.setVisibility(View.VISIBLE);
                    tv_tell.setVisibility(View.GONE);
                    et_tell.setVisibility(View.VISIBLE);
                    et_introduce.setText(tv_introduce.getText().toString());
                    et_name.setText(tv_name.getText().toString());
                    et_tell.setText(tv_tell.getText().toString());
                    btn_commit.setText("保存");
                    isUpdate=false;
                    iv_delete_bussies.setVisibility(View.VISIBLE);
                    iv_delete_shop.setVisibility(View.VISIBLE);

                    rl_shop.setBackgroundColor(UIUtils.getColor(R.color.LineGrayColor));
                    rl_shop_type.setBackgroundColor(UIUtils.getColor(R.color.LineGrayColor));
                    rl_address.setBackgroundColor(UIUtils.getColor(R.color.LineGrayColor));
                    rl.setBackgroundColor(UIUtils.getColor(R.color.LineGrayColor));
                    rl_ic_card.setBackgroundColor(UIUtils.getColor(R.color.LineGrayColor));
                    line1.setBackgroundColor(UIUtils.getColor(R.color.LineGrayColor));
                    line2.setBackgroundColor(UIUtils.getColor(R.color.LineGrayColor));
                    line3.setBackgroundColor(UIUtils.getColor(R.color.LineGrayColor));
                }
                else{
                    contract_name=et_name.getText().toString();
                    contract_telephone=et_tell.getText().toString();
                    description=et_introduce.getText().toString();
                    UpdateStoreInfo();
                }

                break;
            }
            case R.id.tv_top_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.iv_shop_image:
                if(!isUpdate){
                    type = 0;
                    pWindow.setAnimationStyle(R.style.AnimBottom);
                    pWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.ll_main),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.iv_delete_shop:
//                iv_shop_image.setImageResource(R.mipmap.upload);
                iv_shop_image.setImageBitmap(null);
                image = null;
                iv_delete_shop.setVisibility(View.GONE);
                break;
            case R.id.iv_buessies_image:
                if(!isUpdate){
                    type = 2;
                    pWindow.setAnimationStyle(R.style.AnimBottom);
                    pWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.ll_main),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.iv_delete_bussies:
//                iv_buessies_image.setImageResource(R.mipmap.upload);
                iv_buessies_image.setImageBitmap(null);
                business_image = null;
                iv_delete_bussies.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 上传图片
     */
    private void uploadImage(final String suoPath) {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL+"tsfkxt/user/uploadPicture.do";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("usrid", SharedPrefrenceUtils.getString(DataManagementActivity.this, "usrid"));
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
                    if (type == 0) {
                        image=imageResponse.return_param.pic_path;
                        iv_shop_image.setImageBitmap(bt_shop_image);
                        iv_delete_shop.setVisibility(View.VISIBLE);
                    }  else if (type == 2) {
                        business_image=imageResponse.return_param.pic_path;
                        iv_buessies_image.setImageBitmap(bt_buessies_image);
                        iv_delete_bussies.setVisibility(View.VISIBLE);
                    }
                } else if(imageResponse.code == 2){
                    Intent intent = new Intent(DataManagementActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(DataManagementActivity.this,
                                imageResponse.msg);
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(DataManagementActivity.this,
                            error);
                }
            }
        });

    }

    //申请合作
    /**
     * 创建用户订单并发标
     */
    private void UpdateStoreInfo() {
        loadingDialog.show();
        UpdateStoreInfoProtocol updateStoreInfoProtocol=new UpdateStoreInfoProtocol();
        UpdateStoreInfoRequest updateStoreInfoRequest=new UpdateStoreInfoRequest();
        url= Constants.XINYONGSERVER_URL+updateStoreInfoProtocol.getApiFun();
        updateStoreInfoRequest.map.put("usr_id", SharedPrefrenceUtils.getString(DataManagementActivity.this, "usrid"));

        if(!TextUtils.isEmpty(image)) {
            updateStoreInfoRequest.map.put("store_pic", image);
        }
        if(!TextUtils.isEmpty(business_image)) {
            updateStoreInfoRequest.map.put("businesssite_pic", business_image);
        }
        if (!TextUtils.isEmpty(description)) {
            updateStoreInfoRequest.map.put("description",description);
        }
        if (!TextUtils.isEmpty(contract_name)) {
            updateStoreInfoRequest.map.put("contract_name", contract_name);
        }
        if (!TextUtils.isEmpty(contract_telephone)) {
            updateStoreInfoRequest.map.put("contract_telephone",contract_telephone);
        }
        updateStoreInfoRequest.map.put("reg_phone",SharedPrefrenceUtils.getString(this,"mobile_phone"));

        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, updateStoreInfoRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                LogUtils.e("申请结果:" + json.toString());
                UpdateStoreInfoResponse updateStoreInfoResponse = gson.fromJson(json, UpdateStoreInfoResponse.class);
                if (updateStoreInfoResponse.code == 0) {
                    if (!TextUtils.isEmpty(description)) {
                        tv_introduce.setText(description);
                    }
                    if (!TextUtils.isEmpty(contract_name)) {
                        tv_name.setText(contract_name);
                    }
                    if (!TextUtils.isEmpty(contract_telephone)) {
                        tv_tell.setText(contract_telephone);
                    }
                    tv_introduce.setVisibility(View.VISIBLE);
                    et_introduce.setVisibility(View.GONE);
                    tv_name.setVisibility(View.VISIBLE);
                    et_name.setVisibility(View.GONE);
                    tv_tell.setVisibility(View.VISIBLE);
                    et_tell.setVisibility(View.GONE);
                    iv_delete_bussies.setVisibility(View.GONE);
                    iv_delete_shop.setVisibility(View.GONE);
                    rl_shop.setBackgroundColor(UIUtils.getColor(R.color.tab_background));
                    rl_shop_type.setBackgroundColor(UIUtils.getColor(R.color.tab_background));
                    rl_address.setBackgroundColor(UIUtils.getColor(R.color.tab_background));
                    rl.setBackgroundColor(UIUtils.getColor(R.color.tab_background));
                    rl_ic_card.setBackgroundColor(UIUtils.getColor(R.color.tab_background));

                    et_introduce.setText(et_introduce.getText().toString());
                    btn_commit.setText("修改");
                    isUpdate = true;

                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(DataManagementActivity.this,
                                "提交成功！");
                    }
                    ctf.getTabMyselfbyPager().runFindShop();
                    finishActivity();
                    finish();
                } else if(updateStoreInfoResponse.code == 2){
                    Intent intent = new Intent(DataManagementActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(DataManagementActivity.this,
                                updateStoreInfoResponse.msg);
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(DataManagementActivity.this,
                            error);
                }
            }
        });
    }

    @Override
    public void ImageType(int type) {
        this.type=type;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if ((v.getId() == R.id.et_introduce && canVerticalScroll(et_introduce))) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }

        return false;
    }

    /**
     * EditText竖直方向是否可以滚动
     * @param editText 需要判断的EditText
     * @return true：可以滚动  false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }
}
