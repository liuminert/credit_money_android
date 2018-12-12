package com.tesu.creditgold.activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.DictionaryAdapter;
import com.tesu.creditgold.adapter.PictureAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.DictionaryBean;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.InterestsBean;
import com.tesu.creditgold.bean.PhotoUpImageItem;
import com.tesu.creditgold.bean.PictureItemBean;
import com.tesu.creditgold.bean.UsrOrderPicBean;
import com.tesu.creditgold.fragment.ControlTabFragment;
import com.tesu.creditgold.support.PercentLinearLayout;
import com.tesu.creditgold.util.BitmapUtils;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.MobileUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.StringUtils;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.util.Utils;
import com.tesu.creditgold.widget.NoScrollGridView;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomePackagingActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = HomePackagingActivity.class.getSimpleName();
    private View rootView;
    private TextView tv_top_back;
    private ControlTabFragment ctf;
    private Dialog loadingDialog;
    private NoScrollGridView gv_add_contract;
    private NoScrollGridView gv_add_property;
    private NoScrollGridView gv_add_company_address;
    private TextView tv_house_message;
    private TextView tv_fixed_assets;
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
    private List<PictureItemBean> contractPictureItemBeanList;  //装修合同list
    private List<PictureItemBean> propertyPictureItemBeanList;  //房产证list
    private List<PictureItemBean> companyAddressPictureItemBeanList;  //装修公司地定位截图list
    private PictureAdapter mContractPictureAdapter;
    private PictureAdapter mPropertyPictureAdapter;
    private PictureAdapter mCompanyAddressPictureAdapter;

    private int position;
    private int addType;  //添加照片类型  0 装修合同，  1 房产证   2,装修公司地定位截图
    private FkBaseResponse fkbaseResponse;
    private String typeName;  //数据字典类型
    private Gson gson;
    private PopupWindow dictonaryWindow;
    private View dictonaryRoot;
    private ListView lv_choose;
    private Button btn_cancel;
    private DictionaryBean houstMessageDitionaryBean;
    private DictionaryBean propertyDitionaryBean;
    private ArrayList<DictionaryBean> dictionaryBeanList;
    private int chooseType ;  //选择类型  0 住房情况     1 固定资产
    private String userId;
    private InterestsBean interestsBean;
    private String store_name;
    private int store_id;
    private static final int SHOWIMAGE = 101;
    private String store_contract;
    private String store_tel;
    private String amount;
//    private String name;
//    private String idNumber;
    private int store_uid;
    private int is_tiexi;
    private int store_type;
    private String usr_order_id;
    private EditText et_account;  //人行账号
    private EditText et_password;  //密码
    private String account;
    private String password;
    private TextView tv_add_contract;
    private TextView tv_add_property;
    private TextView tv_house_message_show;
    private TextView tv_fixed_assets_show;
    private TextView tv_account_password;
    private EditText et_remark;
    private int oldPostion;
    private TextView tv_password;

    // 图片储存成功后
    protected static final int INTERCEPT = 3;

    private boolean isFromRecord;
    private TextView tv_title;
    private TextView tv_complete;
    private String dictionaryTitle;

    private ArrayList<PhotoUpImageItem> selectImages;

    private ImageView iv_password_hide;

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
        rootView = View.inflate(this, R.layout.activity_home_packaging, null);
        setContentView(rootView);

        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        gv_add_contract = (NoScrollGridView) rootView.findViewById(R.id.gv_add_contract);
        gv_add_property = (NoScrollGridView) rootView.findViewById(R.id.gv_add_property);
        gv_add_company_address = (NoScrollGridView) rootView.findViewById(R.id.gv_add_company_address);
        tv_house_message = (TextView) rootView.findViewById(R.id.tv_house_message);
        tv_fixed_assets = (TextView) rootView.findViewById(R.id.tv_fixed_assets);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        et_account = (EditText) rootView.findViewById(R.id.et_account);
        et_password = (EditText) rootView.findViewById(R.id.et_password);
        tv_add_contract = (TextView) rootView.findViewById(R.id.tv_add_contract);
        tv_add_property = (TextView) rootView.findViewById(R.id.tv_add_property);
        tv_house_message_show = (TextView) rootView.findViewById(R.id.tv_house_message_show);
        tv_fixed_assets_show = (TextView) rootView.findViewById(R.id.tv_fixed_assets_show);
        tv_account_password = (TextView) rootView.findViewById(R.id.tv_account_password);
        et_remark = (EditText) rootView.findViewById(R.id.et_remark);
        tv_password = (TextView) rootView.findViewById(R.id.tv_password);
        iv_password_hide = (ImageView) rootView.findViewById(R.id.iv_password_hide);

        tv_add_contract.setText(Utils.setFountColor(tv_add_contract));
        tv_house_message_show.setText(Utils.setFountColor(tv_house_message_show));
        tv_fixed_assets_show.setText(Utils.setFountColor(tv_fixed_assets_show));
        tv_account_password.setText(Utils.setFountColor(tv_account_password));
        tv_password.setText(Utils.setFountColor(tv_password));
        tv_add_property.setText(Utils.setFountColorAndSize(this, tv_add_property, 7, 14));

//        Drawable drawable1 = getResources().getDrawable(R.mipmap.drop_down);
//        drawable1.setBounds(0, 0, 40, 30);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
//
//        tv_house_message.setCompoundDrawables(null, null, drawable1, null);
//        tv_fixed_assets.setCompoundDrawables(null, null, drawable1, null);

        loadingDialog = DialogUtils.createLoadDialog(HomePackagingActivity.this, false);
        if (ctf == null) {
            ctf = MainActivity.getCtf();
        }

        userId = SharedPrefrenceUtils.getString(this,"usrid");
        gson = new Gson();
        initData();
        return null;
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
                            new_photo.compress(Bitmap.CompressFormat.JPEG, getResources().getInteger(R.integer.bitmap_compress), baos);

                            String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                    .format(new Date());
//                            String suoPath = BitmapUtils.saveMyBitmap(suoName,new_photo);
//                            uploadImage(suoPath);

                            path = BitmapUtils.saveMyBitmap(suoName, new_photo);
                            handler.sendEmptyMessage(INTERCEPT);

                            if(!photo.isRecycled()){
                                photo.recycle();
                            }
                            if(!new_photo.isRecycled()){
                                new_photo.recycle();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();


            //通知相册刷新
            Uri uriData = Uri.parse("file://" + path);
            UIUtils.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uriData));

        }

        // 读取相册缩放图片
        if (requestCode == PHOTO_ZOOM && data != null) {
//            getSinglePhoto(data);
            getMultiplePhoto(data);
        }

        if(requestCode == SHOWIMAGE && data != null){
            if(addType == 0){
                contractPictureItemBeanList.remove(position);
                mContractPictureAdapter.notifyDataSetChanged();
            }else if(addType == 1){
                propertyPictureItemBeanList.remove(position);
                mPropertyPictureAdapter.notifyDataSetChanged();
            }else if(addType == 2){
                companyAddressPictureItemBeanList.remove(position);
                mCompanyAddressPictureAdapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 从相册读取多个照片
     * @param data
     */
    private void getMultiplePhoto(Intent data) {
        String selectImagesStr = data.getStringExtra("selectImages");
        if (!TextUtils.isEmpty(selectImagesStr)) {
            LogUtils.e("selectImagesStr:" + selectImagesStr);
            selectImages = gson.fromJson(selectImagesStr, new TypeToken<List<PhotoUpImageItem>>() {
            }.getType());
            if (selectImages != null && selectImages.size() > 0) {
                for(int i=0;i<selectImages.size();i++){
                    PhotoUpImageItem imageItem = selectImages.get(i);
                    try {
                        Bitmap photo = BitmapUtils.getSmallBitmap(imageItem.getImagePath());
                        if (photo != null) {
                            Bitmap new_photo = BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(imageItem.getImagePath()));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            new_photo.compress(Bitmap.CompressFormat.JPEG, getResources().getInteger(R.integer.bitmap_compress), baos);

                            String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                    .format(new Date());

                            String imgPath = BitmapUtils.saveMyBitmap(suoName+i, new_photo);

                            selectImages.get(i).setImagePath(imgPath);

                            if(i == selectImages.size()-1){
                                xUtilsUpload(i,true);
                            }else {
                                xUtilsUpload(i,false);
                            }

                            if (!photo.isRecycled()) {
                                photo.recycle();
                            }
                            if (!new_photo.isRecycled()) {
                                new_photo.recycle();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private void xUtilsUpload(final int i,final boolean isLast) {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/uploadPicture.do";
        RequestParams params = new RequestParams();
        params.addBodyParameter("usrid", userId);
//      传图片时，要写3个参数
//      imageFile：键名
//      new File(path)：要上传的图片，path图片路径
//      image/jpg：上传图片的扩展名
        params.addBodyParameter("pic_string", new File(selectImages.get(i).getImagePath()), "image/jpg");
        HttpUtils http = new HttpUtils(Constants.XutilTimeOut);
        http.configCurrentHttpCacheExpiry(Constants.XutilCache);
        http.send(HttpRequest.HttpMethod.POST,
                url,
                params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String resultStr = responseInfo.result;
                        if (isLast) {
                            loadingDialog.dismiss();
                        }
                        fkbaseResponse = gson.fromJson(resultStr, FkBaseResponse.class);
                        LogUtils.e("baseResponse:" + resultStr.toString());
                        HashMap<String, String> hashMap = gson.fromJson(gson.toJson(fkbaseResponse.getReturn_param()), HashMap.class);
                        String picUrl = hashMap.get("pic_path");
                        if (fkbaseResponse.getCode() == 0) {
                            PictureItemBean pictureItemBean = new PictureItemBean();
                            pictureItemBean.setPicturePath(selectImages.get(i).getImagePath());
                            pictureItemBean.setPictureUrl(picUrl);
                            if (addType == 0) {
                                contractPictureItemBeanList.add(0, pictureItemBean);
                                mContractPictureAdapter.notifyDataSetChanged();
                            } else if (addType == 1) {
                                propertyPictureItemBeanList.add(0, pictureItemBean);
                                mPropertyPictureAdapter.notifyDataSetChanged();

                            } else if (addType == 2) {
                                companyAddressPictureItemBeanList.add(0, pictureItemBean);
                                mCompanyAddressPictureAdapter.notifyDataSetChanged();
                            }
                        } else if (fkbaseResponse.getCode() == 2) {
                            Intent intent = new Intent(HomePackagingActivity.this, LoginActivity.class);
                            UIUtils.startActivityNextAnim(intent);
                        } else {
                            if(!isFinishing()){
                                DialogUtils.showAlertDialog(HomePackagingActivity.this,
                                        fkbaseResponse.getMsg());
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        if (isLast) {
                            loadingDialog.dismiss();
                        }
                        if (!isFinishing()) {
                            DialogUtils.showAlertDialog(HomePackagingActivity.this,
                                    msg);
                        }
                    }
                });
    }



    /**
     * 从相册读取一张照片
     * @param data
     */
    private void getSinglePhoto(Intent data) {
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
                    uri = BitmapUtils.getPictureUri(data, HomePackagingActivity.this);
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

            new Thread(){
                @Override
                public void run() {
                    try {
                        Bitmap photo = BitmapUtils.getSmallBitmap(path);
                        if(photo != null){
                            Bitmap new_photo=BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            new_photo.compress(Bitmap.CompressFormat.JPEG, getResources().getInteger(R.integer.bitmap_compress), baos);

                            String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                    .format(new Date());
//                                String suoPath = BitmapUtils.saveMyBitmap(suoName,new_photo);
//                                uploadImage(suoPath);

                            path = BitmapUtils.saveMyBitmap(suoName, new_photo);
                            handler.sendEmptyMessage(INTERCEPT);

                            if(!photo.isRecycled()){
                                photo.recycle();
                            }
                            if(!new_photo.isRecycled()){
                                new_photo.recycle();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }
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
                    PictureItemBean pictureItemBean = new PictureItemBean();
                    pictureItemBean.setPicturePath(suoPath);
                    pictureItemBean.setPictureUrl(picUrl);
                    if (addType == 0) {
                        contractPictureItemBeanList.add(0, pictureItemBean);
                        mContractPictureAdapter.notifyDataSetChanged();
                    } else if (addType == 1) {
                        propertyPictureItemBeanList.add(0, pictureItemBean);
                        mPropertyPictureAdapter.notifyDataSetChanged();

                    } else if (addType == 2) {
                        companyAddressPictureItemBeanList.add(0, pictureItemBean);
                        mCompanyAddressPictureAdapter.notifyDataSetChanged();
                    }
                } else if (fkbaseResponse.getCode() == 2) {
                    Intent intent = new Intent(HomePackagingActivity.this, LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                } else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(HomePackagingActivity.this,
                                fkbaseResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(HomePackagingActivity.this,
                            error);
                }
            }
        });

    }

    private void initData() {
        Intent intent = getIntent();
        interestsBean = (InterestsBean) intent.getSerializableExtra("interestsBean");
        usr_order_id = intent.getStringExtra("usr_order_id");
        store_name = intent.getStringExtra("store_name");
        store_id = intent.getIntExtra("store_id", 0);
        store_contract = intent.getStringExtra("store_contract");
        store_tel = intent.getStringExtra("store_tel");
        amount = intent.getStringExtra("amount");
//        name = intent.getStringExtra("name");
//        idNumber = intent.getStringExtra("idNumber");
        store_uid = intent.getIntExtra("store_uid", 0);
        is_tiexi = intent.getIntExtra("is_tiexi",0);
        store_type = intent.getIntExtra("store_type",0);

        isFromRecord = intent.getBooleanExtra("isFromRecord", false);
        if(isFromRecord && !TextUtils.isEmpty(usr_order_id)){
            String messageStr = SharedPrefrenceUtils.getString(this, usr_order_id+"-step4");
            if(!TextUtils.isEmpty(messageStr)){
                HashMap<String,String> hs = gson.fromJson(messageStr, HashMap.class);
                LogUtils.e("未提交数据:"+hs.toString());
                setMessage(hs);
            }else {
                initPhotos();
            }

        }else {
            initPhotos();
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
                switch (chooseType) {
                    case 0: //住房情况
                        houstMessageDitionaryBean = dictionaryBeanList.get(position);
                        tv_house_message.setText(houstMessageDitionaryBean.getDictionary_value_name());
                        tv_house_message.setTextColor(UIUtils.getColor(R.color.text_color_black));
                        oldPostion = lv_choose.getFirstVisiblePosition();
                        break;
                    case 1://固定资产
                        propertyDitionaryBean = dictionaryBeanList.get(position);
                        tv_fixed_assets.setText(propertyDitionaryBean.getDictionary_value_name());
                        tv_fixed_assets.setTextColor(UIUtils.getColor(R.color.text_color_black));
                        oldPostion = lv_choose.getFirstVisiblePosition();
                        break;
                }
                dictonaryWindow.dismiss();
            }
        });

        tv_top_back.setOnClickListener(this);
        tv_house_message.setOnClickListener(this);
        tv_fixed_assets.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        tv_complete.setOnClickListener(this);
        iv_password_hide.setOnClickListener(this);

        mContractPictureAdapter.setCallBack(new PictureAdapter.ICallBack() {
            @Override
            public void addImage(int pos) {
                addType = 0;
                position = pos;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }

            @Override
            public void deleteImage(int pos) {
                contractPictureItemBeanList.remove(pos);
                mContractPictureAdapter.notifyDataSetChanged();
            }

            @Override
            public void showImage(int pos) {
                addType = 0;
                position = pos;
                PictureItemBean pictureItemBean2 = contractPictureItemBeanList.get(pos);
                Intent intent1 = new Intent(HomePackagingActivity.this, ShowImageActivity.class);
                intent1.putExtra("pictureItemBean", pictureItemBean2);
                UIUtils.startActivityForResult(intent1, SHOWIMAGE);
            }
        });

        mPropertyPictureAdapter.setCallBack(new PictureAdapter.ICallBack() {
            @Override
            public void addImage(int pos) {
                addType =1;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }

            @Override
            public void deleteImage(int pos) {
                propertyPictureItemBeanList.remove(pos);
                mPropertyPictureAdapter.notifyDataSetChanged();

            }

            @Override
            public void showImage(int pos) {
                addType =1;
                position = pos;
                PictureItemBean pictureItemBean2 = propertyPictureItemBeanList.get(pos);
                Intent intent1 = new Intent(HomePackagingActivity.this,ShowImageActivity.class);
                intent1.putExtra("pictureItemBean",pictureItemBean2);
                UIUtils.startActivityForResult(intent1, SHOWIMAGE);
            }
        });
        mCompanyAddressPictureAdapter.setCallBack(new PictureAdapter.ICallBack() {
            @Override
            public void addImage(int pos) {
                addType = 2;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }

            @Override
            public void deleteImage(int pos) {
                companyAddressPictureItemBeanList.remove(pos);
                mCompanyAddressPictureAdapter.notifyDataSetChanged();

            }

            @Override
            public void showImage(int pos) {
                addType = 2;
                position = pos;
                PictureItemBean pictureItemBean2 = companyAddressPictureItemBeanList.get(pos);
                Intent intent1 = new Intent(HomePackagingActivity.this, ShowImageActivity.class);
                intent1.putExtra("pictureItemBean", pictureItemBean2);
                UIUtils.startActivityForResult(intent1, SHOWIMAGE);
            }
        });
    }

    /**
     * 设置上次未上传的数据
     */
    private void setMessage(HashMap<String, String> hs) {
        String remark = hs.get("remark");
        if(!TextUtils.isEmpty(remark)){
            et_remark.setText(remark);
        }

        account = hs.get("account");
        if(!TextUtils.isEmpty(account)){
            et_account.setText(account);
        }

        password = hs.get("password");
        if(!TextUtils.isEmpty(password)){
            et_password.setText(password);
        }

        houstMessageDitionaryBean = gson.fromJson(hs.get("houstMessageDitionaryBean"), DictionaryBean.class);
        if(houstMessageDitionaryBean != null){
            tv_house_message.setText(houstMessageDitionaryBean.getDictionary_value_name());
        }

        propertyDitionaryBean = gson.fromJson(hs.get("propertyDitionaryBean"), DictionaryBean.class);
        if(propertyDitionaryBean != null){
            tv_fixed_assets.setText(propertyDitionaryBean.getDictionary_value_name());
        }

        contractPictureItemBeanList = gson.fromJson(hs.get("contractPictureItemBeanList"),new TypeToken<List<PictureItemBean>>(){}.getType());
        if(contractPictureItemBeanList == null || contractPictureItemBeanList.size()==0){
            contractPictureItemBeanList = new ArrayList<PictureItemBean>();
            PictureItemBean pictureItemBean = new PictureItemBean();
            contractPictureItemBeanList.add(pictureItemBean);
        }
        mContractPictureAdapter = new PictureAdapter(this,contractPictureItemBeanList);
        gv_add_contract.setAdapter(mContractPictureAdapter);

        propertyPictureItemBeanList = gson.fromJson(hs.get("propertyPictureItemBeanList"),new TypeToken<List<PictureItemBean>>(){}.getType());
        if(propertyPictureItemBeanList == null || propertyPictureItemBeanList.size()==0){
            propertyPictureItemBeanList = new ArrayList<PictureItemBean>();
            PictureItemBean pictureItemBean1 = new PictureItemBean();
            propertyPictureItemBeanList.add(pictureItemBean1);
        }
        mPropertyPictureAdapter = new PictureAdapter(this,propertyPictureItemBeanList);
        gv_add_property.setAdapter(mPropertyPictureAdapter);

        companyAddressPictureItemBeanList = gson.fromJson(hs.get("companyAddressPictureItemBeanList"),new TypeToken<List<PictureItemBean>>(){}.getType());
        if(companyAddressPictureItemBeanList == null || companyAddressPictureItemBeanList.size()==0){
            companyAddressPictureItemBeanList = new ArrayList<PictureItemBean>();
            PictureItemBean pictureItemBean2 = new PictureItemBean();
            companyAddressPictureItemBeanList.add(pictureItemBean2);
        }
        mCompanyAddressPictureAdapter = new PictureAdapter(this,companyAddressPictureItemBeanList);
        gv_add_company_address.setAdapter(mCompanyAddressPictureAdapter);

    }

    private void initPhotos() {
        contractPictureItemBeanList = new ArrayList<PictureItemBean>();
        PictureItemBean pictureItemBean = new PictureItemBean();
        contractPictureItemBeanList.add(pictureItemBean);
        mContractPictureAdapter = new PictureAdapter(this,contractPictureItemBeanList);
        gv_add_contract.setAdapter(mContractPictureAdapter);

        propertyPictureItemBeanList = new ArrayList<PictureItemBean>();
        PictureItemBean pictureItemBean1 = new PictureItemBean();
        propertyPictureItemBeanList.add(pictureItemBean1);
        mPropertyPictureAdapter = new PictureAdapter(this,propertyPictureItemBeanList);
        gv_add_property.setAdapter(mPropertyPictureAdapter);

        companyAddressPictureItemBeanList = new ArrayList<PictureItemBean>();
        PictureItemBean pictureItemBean2 = new PictureItemBean();
        companyAddressPictureItemBeanList.add(pictureItemBean2);
        mCompanyAddressPictureAdapter = new PictureAdapter(this,companyAddressPictureItemBeanList);
        gv_add_company_address.setAdapter(mCompanyAddressPictureAdapter);
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
//                    Intent intent = new Intent(Intent.ACTION_PICK, null);
//                    intent.setDataAndType(
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                            IMAGE_UNSPECIFIED);
//                    startActivityForResult(intent, PHOTO_ZOOM);

                    Intent intent = new Intent(HomePackagingActivity.this, ChoosePhotoFolderActivity.class);
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            saveMessage();
            ctf.setChecked(ctf.beforeIndex);
            ctf.mCurrentIndex = ctf.beforeIndex;
            finish();
            overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_top_back:
                saveMessage();
                UIUtils.hideInputWindow(this);
                ctf.setChecked(ctf.beforeIndex);
                ctf.mCurrentIndex = ctf.beforeIndex;
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.tv_house_message:  //住房状态
                dictionaryTitle = "住房情况";
                chooseType = 0;
                typeName = "tszd_11";
                getDictonary();
                break;
            case R.id.tv_fixed_assets:  //固定资产
                dictionaryTitle = "固定资产";
                chooseType = 1;
                typeName = "tszd_12";
                getDictonary();
                break;
            case R.id.btn_next:   //下一步
                if(contractPictureItemBeanList.size()<=1){
                    UIUtils.showToastCenter(HomePackagingActivity.this, "请先上传装修合同照片");
                    return;
                }

                if(propertyPictureItemBeanList.size()<=1){
                    UIUtils.showToastCenter(HomePackagingActivity.this, "请先上传房产证照片");
                    return;
                }
                if(houstMessageDitionaryBean == null){
                    UIUtils.showToastCenter(HomePackagingActivity.this, "请选择住房情况");
                    return;
                }
                if(propertyDitionaryBean == null){
                    UIUtils.showToastCenter(HomePackagingActivity.this, "请选择固定资产");
                    return;
                }

                account = et_account.getText().toString();
                if(TextUtils.isEmpty(account)){
                    UIUtils.showToastCenter(HomePackagingActivity.this, "请填写人行征信账号");
                    return;
                }
                password = et_password.getText().toString();
                if(TextUtils.isEmpty(password)){
                    UIUtils.showToastCenter(HomePackagingActivity.this, "请填写人行征信密码");
                    return;
                }
                btn_next.setEnabled(false);
                btn_next.setBackgroundResource(R.mipmap.login_no);
                setCreditInf();
                break;
            case R.id.btn_cancel: //选择取消
            case R.id.tv_complete: //选择完成
                dictonaryWindow.dismiss();
                break;
            case R.id.iv_password_hide:
                if(et_password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_password_hide.setImageResource(R.mipmap.passwood_hide);
                }else {
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_password_hide.setImageResource(R.mipmap.passwood_visual);
                }
                break;
        }

    }

    private void saveMessage() {
        HashMap<String,String> hs = new HashMap<>();
        hs.put("usr_order_id", usr_order_id);

        String remark = et_remark.getText().toString();
        if(!TextUtils.isEmpty(remark)){
            hs.put("remark",remark);
        }

        account = et_account.getText().toString();
        if(!TextUtils.isEmpty(account)){
            hs.put("account",account);
        }

        password = et_password.getText().toString();
        if(!TextUtils.isEmpty(password)){
            hs.put("password",password);
        }

        if(houstMessageDitionaryBean != null){
            hs.put("houstMessageDitionaryBean",gson.toJson(houstMessageDitionaryBean));
        }

        if(propertyDitionaryBean != null){
            hs.put("propertyDitionaryBean",gson.toJson(propertyDitionaryBean));
        }

        hs.put("contractPictureItemBeanList",gson.toJson(contractPictureItemBeanList));
        hs.put("propertyPictureItemBeanList",gson.toJson(propertyPictureItemBeanList));
        hs.put("companyAddressPictureItemBeanList", gson.toJson(companyAddressPictureItemBeanList));

        SharedPrefrenceUtils.setString(this, usr_order_id+"-step4", gson.toJson(hs));
    }

    /**
     * 写入授信资料
     */
    private void setCreditInf() {
        loadingDialog.show();
//        String url= Constants.FENGKONGSERVER_URL+"tsfkxt/user/setCreditInf_2.do";
        String url= Constants.FENGKONGSERVER_URL+"tsfkxt/user/setOrdersInf.do";
        Map<String,String> params = new HashMap<String,String>();
        params.put("usrid", userId);
        params.put("submit_step", "4");
        params.put("pedestrian_inquiry_account",account);
        params.put("pedestrian_inquiry_pwd", password);
        params.put("usr_order_id", usr_order_id);
        params.put("house_type",houstMessageDitionaryBean.getDictionary_value_key()+"");
        params.put("fixed_assets",propertyDitionaryBean.getDictionary_value_key()+"");
        List<UsrOrderPicBean> usrOrderPicBeanList = new ArrayList<UsrOrderPicBean>();
        for(int i=0;i<contractPictureItemBeanList.size()-1;i++){
                PictureItemBean pictureItemBean = contractPictureItemBeanList.get(i);
                UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
                usrOrderPicBean.setPic_type(0);
                usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
                usrOrderPicBean.setPic_desc("装修合同");
                usrOrderPicBeanList.add(usrOrderPicBean);
        }
        for(int i=0;i<propertyPictureItemBeanList.size()-1;i++){
                PictureItemBean pictureItemBean = propertyPictureItemBeanList.get(i);
                UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
                usrOrderPicBean.setPic_type(5);
                usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
                usrOrderPicBean.setPic_desc("房产证");
                usrOrderPicBeanList.add(usrOrderPicBean);
        }
        for(int i=0;i<companyAddressPictureItemBeanList.size()-1;i++){
            PictureItemBean pictureItemBean = companyAddressPictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(12);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("装修公司地定位截图");
            usrOrderPicBeanList.add(usrOrderPicBean);
        }
        params.put("usr_order_pic_list", gson.toJson(usrOrderPicBeanList)); //
        if(!TextUtils.isEmpty(et_remark.getText().toString())){
            params.put("scene_remark", et_remark.getText().toString()); //
        }
        LogUtils.e("params:"+params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                btn_next.setEnabled(true);
                btn_next.setBackgroundResource(R.mipmap.btn_login);
                loadingDialog.dismiss();
                Log.e(TAG, "写入征信资料返回:" + json);
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkbaseResponse.getCode() == 0) {
                    Intent intent = new Intent(HomePackagingActivity.this,PackagingAgreementActivity.class);
                    intent.putExtra("usr_order_id",usr_order_id);
                    setFinish();
                    UIUtils.startActivityNextAnim(intent);

                } else if(fkbaseResponse.getCode() == 2){
                    Intent intent = new Intent(HomePackagingActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(HomePackagingActivity.this, fkbaseResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                btn_next.setEnabled(true);
                btn_next.setBackgroundResource(R.mipmap.btn_login);
                loadingDialog.dismiss();
                Log.e(TAG, "写入征信资料返回错误:" + error);
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(HomePackagingActivity.this, error);
                }

            }
        });
    }




    /**
     * 获取数据字典数据源
     */
    private void getDictonary() {
        loadingDialog.show();
        String url =Constants.FENGKONGSERVER_URL+"tsfkxt/user/getDictionaryDatasource.do";
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("type_code",typeName);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("数据字典数据源:" + json.toString());
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkbaseResponse.getCode() == 0) {
                    HashMap<String, Objects> hashMap = gson.fromJson(gson.toJson(fkbaseResponse.getReturn_param()), HashMap.class);
                    dictionaryBeanList = StringUtils.fromJsonList(gson.toJson(hashMap.get("data_list")), DictionaryBean.class);
                    LogUtils.e("dictionaryBeanList:" + dictionaryBeanList.toString());
                    DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(HomePackagingActivity.this, dictionaryBeanList);
                    lv_choose.setAdapter(dictionaryAdapter);
                    if(!TextUtils.isEmpty(dictionaryTitle)){
                        tv_title.setText(dictionaryTitle);
                    }
                    dictonaryWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                    int index;
                    switch (chooseType){//选择类型  0 住房情况     1 固定资产
                        case 0:
                            if(houstMessageDitionaryBean != null){
                                index = dictionaryBeanList.indexOf(houstMessageDitionaryBean);
                                if(index != -1){
                                    lv_choose.setSelection(oldPostion);
                                    dictionaryAdapter.setSelectItem(index);
                                }
                            }
                            break;
                        case 1:
                            if(propertyDitionaryBean != null){
                                index = dictionaryBeanList.indexOf(propertyDitionaryBean);
                                if(index != -1){
                                    lv_choose.setSelection(oldPostion);
                                    dictionaryAdapter.setSelectItem(index);
                                }
                            }
                            break;
                    }

                } else if(fkbaseResponse.getCode() == 2){
                    Intent intent = new Intent(HomePackagingActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(HomePackagingActivity.this,
                                fkbaseResponse.getMsg());
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(HomePackagingActivity.this,
                            error);
                }
            }
        });
    }

}
