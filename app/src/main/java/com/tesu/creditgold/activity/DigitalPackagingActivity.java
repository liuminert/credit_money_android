package com.tesu.creditgold.activity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.PictureAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
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
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.util.Utils;
import com.tesu.creditgold.widget.NoScrollGridView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DigitalPackagingActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = InformationAuthenticationActivity.class.getSimpleName();
    private View rootView;
    private TextView tv_top_back;
    private ControlTabFragment ctf;
    private Dialog loadingDialog;
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

    private NoScrollGridView gv_add_product;  //所购产品照片
    private NoScrollGridView gv_add_total;  //与产品,店员合照
    private NoScrollGridView gv_add_shop;  //门店照
    private NoScrollGridView gv_add_shop_address;  //门店地定位截图

    private List<PictureItemBean> productPictureItemBeanList;  //所购产品照片list
    private List<PictureItemBean> totalPictureItemBeanList;  //与产品,店员合照list
    private List<PictureItemBean> shopPictureItemBeanList;  //门店照list
    private List<PictureItemBean> shopAddressPictureItemBeanList;  //门店地定位截图list
    private PictureAdapter mFurniturePictureAdapter;
    private PictureAdapter mTotalPictureAdapter;
    private PictureAdapter mFurnitureShopPictureAdapter;
    private PictureAdapter mShopAddressPictureAdapter;

    private int position;
    private int addType;  //添加照片类型  0 家具照，  1 与产品,店员合照   2,门店照   3,门店地定位截图
    private FkBaseResponse fkbaseResponse;
    private Gson gson;
    private String userId;
    private InterestsBean interestsBean;
    private String store_name;
    private int store_id;
    private String store_contract;
    private String store_tel;
    private String amount;
//    private String name;
//    private String idNumber;
    private int store_uid;
    private int is_tiexi;
    private int store_type;
    private String usr_order_id;

    private static final int SHOWIMAGE = 101;

    private TextView tv_add_product;
    private TextView tv_add_shop;
    private TextView tv_add_total;
    private EditText et_remark;

    // 图片储存成功后
    protected static final int INTERCEPT = 3;

    private ArrayList<PhotoUpImageItem> selectImages;

    private boolean isFromRecord;

    private String suoPath;

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
        rootView = View.inflate(this, R.layout.activity_digital_packaging, null);
        setContentView(rootView);

        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        gv_add_product = (NoScrollGridView) rootView.findViewById(R.id.gv_add_product);
        gv_add_total = (NoScrollGridView) rootView.findViewById(R.id.gv_add_total);
        gv_add_shop = (NoScrollGridView) rootView.findViewById(R.id.gv_add_shop);
        gv_add_shop_address = (NoScrollGridView) rootView.findViewById(R.id.gv_add_shop_address);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        tv_add_product = (TextView) rootView.findViewById(R.id.tv_add_product);
        tv_add_shop = (TextView) rootView.findViewById(R.id.tv_add_shop);
        tv_add_total = (TextView) rootView.findViewById(R.id.tv_add_total);
        et_remark = (EditText) rootView.findViewById(R.id.et_remark);

        tv_add_product.setText(Utils.setFountColor(tv_add_product));
        tv_add_shop.setText(Utils.setFountColor(tv_add_shop));
        tv_add_total.setText(Utils.setFountColor(tv_add_total));

        gson = new Gson();
        loadingDialog = DialogUtils.createLoadDialog(DigitalPackagingActivity.this, false);
        if (ctf == null) {
            ctf = MainActivity.getCtf();
        }

        userId = SharedPrefrenceUtils.getString(this,"usrid");

        initData();
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        else if (requestCode == PHOTO_ZOOM && data != null) {
//            getSinglePhoto(data);
            getMultiplePhoto(data);
        }

        if(requestCode == SHOWIMAGE && data != null){
            if(addType == 0){
                productPictureItemBeanList.remove(position);
                mFurniturePictureAdapter.notifyDataSetChanged();
            }else if(addType == 1){
                totalPictureItemBeanList.remove(position);
                mTotalPictureAdapter.notifyDataSetChanged();
            }else if(addType == 2){
                shopPictureItemBeanList.remove(position);
                mFurnitureShopPictureAdapter.notifyDataSetChanged();
            }else if(addType == 3){
                shopAddressPictureItemBeanList.remove(position);
                mShopAddressPictureAdapter.notifyDataSetChanged();
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
                    uri = BitmapUtils.getPictureUri(data, DigitalPackagingActivity.this);
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


        }
    }

    private void xUtilsUpload(final int i,final boolean isLast) {
        suoPath = selectImages.get(i).getImagePath();
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/uploadPicture.do";
        RequestParams params = new RequestParams();
        params.addBodyParameter("usrid", userId);
//      传图片时，要写3个参数
//      imageFile：键名
//      new File(path)：要上传的图片，path图片路径
//      image/jpg：上传图片的扩展名
        params.addBodyParameter("pic_string", new File(suoPath), "image/jpg");
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
                                productPictureItemBeanList.add(0, pictureItemBean);
                                mFurniturePictureAdapter.notifyDataSetChanged();
                            } else if (addType == 1) {
                                totalPictureItemBeanList.add(0, pictureItemBean);
                                mTotalPictureAdapter.notifyDataSetChanged();
                            } else if (addType == 2) {
                                shopPictureItemBeanList.add(0, pictureItemBean);
                                mFurnitureShopPictureAdapter.notifyDataSetChanged();
                            } else if (addType == 3) {
                                shopAddressPictureItemBeanList.add(0, pictureItemBean);
                                mShopAddressPictureAdapter.notifyDataSetChanged();
                            }
                        } else if (fkbaseResponse.getCode() == 2) {
                            Intent intent = new Intent(DigitalPackagingActivity.this, LoginActivity.class);
                            UIUtils.startActivityNextAnim(intent);
                        } else {
                            if(!isFinishing()){
                                DialogUtils.showAlertDialog(DigitalPackagingActivity.this,
                                        fkbaseResponse.getMsg());
                            }
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        if (isLast) {
                            loadingDialog.dismiss();
                        }
                        if(!isFinishing()){
                            DialogUtils.showAlertDialog(DigitalPackagingActivity.this,
                                    msg);
                        }
                    }
                });


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
                        productPictureItemBeanList.add(0, pictureItemBean);
                        mFurniturePictureAdapter.notifyDataSetChanged();
                    } else if (addType == 1) {
                        totalPictureItemBeanList.add(0, pictureItemBean);
                        mTotalPictureAdapter.notifyDataSetChanged();
                    } else if (addType == 2) {
                        shopPictureItemBeanList.add(0, pictureItemBean);
                        mFurnitureShopPictureAdapter.notifyDataSetChanged();
                    } else if (addType == 3) {
                        shopAddressPictureItemBeanList.add(0, pictureItemBean);
                        mShopAddressPictureAdapter.notifyDataSetChanged();
                    }
                } else if (fkbaseResponse.getCode() == 2) {
                    Intent intent = new Intent(DigitalPackagingActivity.this, LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                } else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(DigitalPackagingActivity.this,
                                fkbaseResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(DigitalPackagingActivity.this,
                            error);
                }
            }
        });

    }

    private void initData() {
        final Intent intent = getIntent();
        interestsBean = (InterestsBean) intent.getSerializableExtra("interestsBean");
        store_name = intent.getStringExtra("store_name");
        usr_order_id = intent.getStringExtra("usr_order_id");
        store_id = intent.getIntExtra("store_id", 0);
        store_contract = intent.getStringExtra("store_contract");
        store_tel = intent.getStringExtra("store_tel");
        amount = intent.getStringExtra("amount");
//        name = intent.getStringExtra("name");
//        idNumber = intent.getStringExtra("idNumber");
        store_uid = intent.getIntExtra("store_uid", 0);
        is_tiexi = intent.getIntExtra("is_tiexi", 0);
        store_type = intent.getIntExtra("store_type", 0);
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


        mFurniturePictureAdapter.setCallBack(new PictureAdapter.ICallBack() {
            @Override
            public void addImage(int pos) {
                addType = 0;
                position = pos;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }

            @Override
            public void deleteImage(int pos) {
                productPictureItemBeanList.remove(pos);
                mFurniturePictureAdapter.notifyDataSetChanged();
            }

            @Override
            public void showImage(int pos) {
                addType = 0;
                position = pos;
                PictureItemBean pictureItemBean2 = productPictureItemBeanList.get(pos);
                Intent intent1 = new Intent(DigitalPackagingActivity.this, ShowImageActivity.class);
                intent1.putExtra("pictureItemBean", pictureItemBean2);
                UIUtils.startActivityForResult(intent1, SHOWIMAGE);
            }
        });

        mTotalPictureAdapter.setCallBack(new PictureAdapter.ICallBack() {
            @Override
            public void addImage(int pos) {
                addType = 1;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }

            @Override
            public void deleteImage(int pos) {
                totalPictureItemBeanList.remove(pos);
                mTotalPictureAdapter.notifyDataSetChanged();

            }

            @Override
            public void showImage(int pos) {
                addType = 1;
                position = pos;
                PictureItemBean pictureItemBean2 = totalPictureItemBeanList.get(pos);
                Intent intent1 = new Intent(DigitalPackagingActivity.this, ShowImageActivity.class);
                intent1.putExtra("pictureItemBean", pictureItemBean2);
                UIUtils.startActivityForResult(intent1, SHOWIMAGE);
            }
        });
        mFurnitureShopPictureAdapter.setCallBack(new PictureAdapter.ICallBack() {
            @Override
            public void addImage(int pos) {
                addType = 2;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }

            @Override
            public void deleteImage(int pos) {
                shopPictureItemBeanList.remove(pos);
                mFurnitureShopPictureAdapter.notifyDataSetChanged();

            }

            @Override
            public void showImage(int pos) {
                addType = 2;
                position = pos;
                PictureItemBean pictureItemBean2 = shopPictureItemBeanList.get(pos);
                Intent intent1 = new Intent(DigitalPackagingActivity.this,ShowImageActivity.class);
                intent1.putExtra("pictureItemBean",pictureItemBean2);
                UIUtils.startActivityForResult(intent1, SHOWIMAGE);
            }
        });
        mShopAddressPictureAdapter.setCallBack(new PictureAdapter.ICallBack() {
            @Override
            public void addImage(int pos) {
                addType = 3;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }

            @Override
            public void deleteImage(int pos) {
                shopAddressPictureItemBeanList.remove(pos);
                mShopAddressPictureAdapter.notifyDataSetChanged();

            }

            @Override
            public void showImage(int pos) {
                addType = 3;
                position = pos;
                PictureItemBean pictureItemBean2 = shopAddressPictureItemBeanList.get(pos);
                Intent intent1 = new Intent(DigitalPackagingActivity.this, ShowImageActivity.class);
                intent1.putExtra("pictureItemBean", pictureItemBean2);
                UIUtils.startActivityForResult(intent1, SHOWIMAGE);
            }
        });

        tv_top_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    /**
     * 设置上次未上传的数据
     */
    private void setMessage(HashMap<String, String> hs) {
        String remark = hs.get("remark");
        if(!TextUtils.isEmpty(remark)){
            et_remark.setText(remark);
        }

        productPictureItemBeanList = gson.fromJson(hs.get("productPictureItemBeanList"),new TypeToken<List<PictureItemBean>>(){}.getType());
        if(productPictureItemBeanList == null || productPictureItemBeanList.size()==0){
            productPictureItemBeanList = new ArrayList<PictureItemBean>();
            PictureItemBean pictureItemBean = new PictureItemBean();
            productPictureItemBeanList.add(pictureItemBean);
        }
        mFurniturePictureAdapter = new PictureAdapter(this,productPictureItemBeanList);
        gv_add_product.setAdapter(mFurniturePictureAdapter);

        totalPictureItemBeanList = gson.fromJson(hs.get("totalPictureItemBeanList"),new TypeToken<List<PictureItemBean>>(){}.getType());
        if(totalPictureItemBeanList == null || totalPictureItemBeanList.size()==0){
            totalPictureItemBeanList = new ArrayList<PictureItemBean>();
            PictureItemBean pictureItemBean1 = new PictureItemBean();
            totalPictureItemBeanList.add(pictureItemBean1);
        }
        mTotalPictureAdapter = new PictureAdapter(this,totalPictureItemBeanList);
        gv_add_total.setAdapter(mTotalPictureAdapter);

        shopPictureItemBeanList = gson.fromJson(hs.get("shopPictureItemBeanList"),new TypeToken<List<PictureItemBean>>(){}.getType());
        if(shopPictureItemBeanList == null || shopPictureItemBeanList.size()==0){
            shopPictureItemBeanList = new ArrayList<PictureItemBean>();
            PictureItemBean pictureItemBean2 = new PictureItemBean();
            shopPictureItemBeanList.add(pictureItemBean2);
        }
        mFurnitureShopPictureAdapter = new PictureAdapter(this,shopPictureItemBeanList);
        gv_add_shop.setAdapter(mFurnitureShopPictureAdapter);

        shopAddressPictureItemBeanList = gson.fromJson(hs.get("shopAddressPictureItemBeanList"),new TypeToken<List<PictureItemBean>>(){}.getType());
        if(shopAddressPictureItemBeanList == null || shopAddressPictureItemBeanList.size()==0){
            shopAddressPictureItemBeanList = new ArrayList<PictureItemBean>();
            PictureItemBean pictureItemBean3 = new PictureItemBean();
            shopAddressPictureItemBeanList.add(pictureItemBean3);
        }
        mShopAddressPictureAdapter = new PictureAdapter(this,shopAddressPictureItemBeanList);
        gv_add_shop_address.setAdapter(mShopAddressPictureAdapter);
    }

    /**
     * 初始化图片列表
     */
    private void initPhotos() {
        productPictureItemBeanList = new ArrayList<PictureItemBean>();
        PictureItemBean pictureItemBean = new PictureItemBean();
        productPictureItemBeanList.add(pictureItemBean);
        mFurniturePictureAdapter = new PictureAdapter(this,productPictureItemBeanList);
        gv_add_product.setAdapter(mFurniturePictureAdapter);

        totalPictureItemBeanList = new ArrayList<PictureItemBean>();
        PictureItemBean pictureItemBean1 = new PictureItemBean();
        totalPictureItemBeanList.add(pictureItemBean1);
        mTotalPictureAdapter = new PictureAdapter(this,totalPictureItemBeanList);
        gv_add_total.setAdapter(mTotalPictureAdapter);

        shopPictureItemBeanList = new ArrayList<PictureItemBean>();
        PictureItemBean pictureItemBean2 = new PictureItemBean();
        shopPictureItemBeanList.add(pictureItemBean2);
        mFurnitureShopPictureAdapter = new PictureAdapter(this,shopPictureItemBeanList);
        gv_add_shop.setAdapter(mFurnitureShopPictureAdapter);

        shopAddressPictureItemBeanList = new ArrayList<PictureItemBean>();
        PictureItemBean pictureItemBean3 = new PictureItemBean();
        shopAddressPictureItemBeanList.add(pictureItemBean3);
        mShopAddressPictureAdapter = new PictureAdapter(this,shopAddressPictureItemBeanList);
        gv_add_shop_address.setAdapter(mShopAddressPictureAdapter);
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

                    Intent intent = new Intent(DigitalPackagingActivity.this, ChoosePhotoFolderActivity.class);
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

    /**
     * 保存未提交的数据
     */
    private void saveMessage() {
        HashMap<String,String> hs = new HashMap<>();
        hs.put("usr_order_id", usr_order_id);

        String remark = et_remark.getText().toString();
        if(!TextUtils.isEmpty(remark)){
            hs.put("remark",remark);
        }

        hs.put("productPictureItemBeanList",gson.toJson(productPictureItemBeanList));
        hs.put("totalPictureItemBeanList", gson.toJson(totalPictureItemBeanList));
        hs.put("shopPictureItemBeanList", gson.toJson(shopPictureItemBeanList));
        hs.put("shopAddressPictureItemBeanList", gson.toJson(shopAddressPictureItemBeanList));

        SharedPrefrenceUtils.setString(this, usr_order_id+"-step4", gson.toJson(hs));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_top_back:
                saveMessage();
                ctf.setChecked(ctf.beforeIndex);
                ctf.mCurrentIndex = ctf.beforeIndex;
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.btn_next:   //下一步
                if(productPictureItemBeanList.size()<=1){
                    UIUtils.showToastCenter(DigitalPackagingActivity.this, "请先上传产品照片");
                    return;
                }
                if(shopPictureItemBeanList.size()<=1){
                    UIUtils.showToastCenter(DigitalPackagingActivity.this, "请先上传数码店铺照片");
                    return;
                }
                if(totalPictureItemBeanList.size()<=1){
                    UIUtils.showToastCenter(DigitalPackagingActivity.this, "请先上传与产品,店员合照");
                    return;
                }

//                setUserOrder();
                btn_next.setEnabled(false);
                btn_next.setBackgroundResource(R.mipmap.login_no);
                setCreditInf();
                break;
        }

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
        params.put("usr_order_id", usr_order_id);
        List<UsrOrderPicBean> usrOrderPicBeanList = new ArrayList<UsrOrderPicBean>();
        for(int i=0;i<productPictureItemBeanList.size()-1;i++){
                PictureItemBean pictureItemBean = productPictureItemBeanList.get(i);
                UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
                usrOrderPicBean.setPic_type(3);
                usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
                usrOrderPicBean.setPic_desc("产品照");
                usrOrderPicBeanList.add(usrOrderPicBean);
        }
        for(int i=0;i<totalPictureItemBeanList.size()-1;i++){
            PictureItemBean pictureItemBean = totalPictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(4);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("与产品、店员的合照");
            usrOrderPicBeanList.add(usrOrderPicBean);
        }
        for(int i=0;i<shopPictureItemBeanList.size()-1;i++){
            PictureItemBean pictureItemBean = shopPictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(11);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("数码店铺照片");
            usrOrderPicBeanList.add(usrOrderPicBean);
        }
        for(int i=0;i<shopAddressPictureItemBeanList.size()-1;i++){
            PictureItemBean pictureItemBean = shopAddressPictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(12);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("门店地定位截图");
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
//                    setUserOrder();
                    Intent intent = new Intent(DigitalPackagingActivity.this,PackagingAgreementActivity.class);
                    intent.putExtra("usr_order_id",usr_order_id);
                    setFinish();
                    UIUtils.startActivityNextAnim(intent);
                } else if(fkbaseResponse.getCode() == 2){
                    Intent intent = new Intent(DigitalPackagingActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(DigitalPackagingActivity.this, fkbaseResponse.getMsg());
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
                    DialogUtils.showAlertDialog(DigitalPackagingActivity.this, error);
                }

            }
        });
    }

}
