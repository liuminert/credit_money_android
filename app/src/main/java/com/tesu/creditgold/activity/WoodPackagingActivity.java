package com.tesu.creditgold.activity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.PictureAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.GetStoreTypeDescResponse;
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
import com.tesu.creditgold.util.MyOnFocusChangeListener;
import com.tesu.creditgold.util.PictureOption;
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

public class WoodPackagingActivity extends BaseActivity implements View.OnClickListener {
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

    private NoScrollGridView gv_add_shop;  //工厂大门照片
    private NoScrollGridView gv_add_product;  //主要产品照片
    private NoScrollGridView gv_factory_location;  //工厂地定位截图
    private NoScrollGridView gv_add_business_license;  //企业营业执照照片
    private NoScrollGridView gv_timber_goods;  //木材商品照
    private NoScrollGridView gv_group_photo;  //借款人、木材商和商品合照
    private NoScrollGridView gv_plant_lease_contract;  //厂房租赁完整合同

    private List<PictureItemBean> shopPictureItemBeanList;  //工厂大门照片list
    private List<PictureItemBean> productPictureItemBeanList;  //主要产品照片list
    private List<PictureItemBean> factoryLocationPictureItemBeanList;  //工厂地定位截图list
    private List<PictureItemBean> businessLicensePictureItemBeanList;  //企业营业执照片list
    private List<PictureItemBean> timberGoodsPictureItemBeanList;  //木材商品照片list
    private List<PictureItemBean> groupPictureItemBeanList;  //借款人、木材商和商品合照list
    private List<PictureItemBean> plantLeaseContractPictureItemBeanList;  //厂房租赁完整合同list
    private PictureAdapter mFurnitureShopPictureAdapter;
    private PictureAdapter mFurniturePictureAdapter;
    private PictureAdapter mFactoryLocationPictureAdapter;
    private PictureAdapter mBusinessLicensePictureAdapter;
    private PictureAdapter mTimberGoodsPictureAdapter;
    private PictureAdapter mGroupPictureAdapter;
    private PictureAdapter mPlantLeaseContractPictureAdapter;

    private int position;
    private int addType;  //添加照片类型  0 主要产品照片   2,工厂大门照片   1,工厂地定位截图   3,企业营业执照   4,木材商品照   5,借款人、木材商和商品合照
    // 6,厂房租赁完整合同  7,企业联合担保人/合伙人/实际掌控人 身份证正面照  8，企业联合担保人/合伙人/实际掌控人 身份证反面照
    private FkBaseResponse fkbaseResponse;
    private Gson gson;
    private String userId;
    private String usr_order_id;
    private TextView tv_title;

    private static final int SHOWIMAGE = 101;
    private GetStoreTypeDescResponse getStoreTypeDescResponse;
    private int store_type;

    private TextView tv_add_product;
    private TextView tv_add_shop;
    //    private PercentLinearLayout ll_tell;
    private EditText et_remark;
    private TextView tv_factory_location;

    private TextView tv_employees_number;
    private TextView tv_sales_volume;
    private TextView tv_business_license;
    private TextView tv_timber_goods;
    private TextView tv_group_photo;
    private TextView tv_plant_lease_contract;
    private TextView tv_bill;
    private EditText et_employees_number;
    private EditText et_sale_volume;
    private String employeesNumber;   //工厂员工数量
    private String saleVolume;   //企业年销售额
    private TextView tv_manager;

    // 图片储存成功后
    protected static final int INTERCEPT = 3;

    private boolean isFromRecord;

    private EditText et_partner_name;
    private EditText et_partner_bank_id;
    private EditText et_partner_id_card;
    private ImageView iv_partner_id_card_front_pic;
    private ImageView iv_partner_id_card_reverse_pic;

    private ImageLoader imageLoader;
    /**
     * 企业联合担保人/合伙人/实际掌控人 身份证号码
     */
    private String partner_id_card;

    /**
     * 企业联合担保人/合伙人/实际掌控人 身份证正面照
     */
    private String partner_id_card_front_pic;

    /**
     * 企业联合担保人/合伙人/实际掌控人 身份证反面照
     */
    private String partner_id_card_reverse_pic;

    /**
     * 企业联合担保人/合伙人/实际掌控人 银行卡
     */
    private String partner_bank_id;

    /**
     * 企业联合担保人/合伙人/实际掌控人 真实姓名
     */
    private String partner_name;

    private String imagePath;

    private ArrayList<PhotoUpImageItem> selectImages;

    private ScrollView sv_total;


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
        rootView = View.inflate(this, R.layout.activity_wood_packaging, null);
        setContentView(rootView);

        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        gv_add_product = (NoScrollGridView) rootView.findViewById(R.id.gv_add_product);
        gv_add_shop = (NoScrollGridView) rootView.findViewById(R.id.gv_add_shop);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_add_product = (TextView) rootView.findViewById(R.id.tv_add_product);
        tv_add_shop = (TextView) rootView.findViewById(R.id.tv_add_shop);
//        ll_tell = (PercentLinearLayout) rootView.findViewById(R.id.ll_tell);
        et_remark = (EditText) rootView.findViewById(R.id.et_remark);
        tv_factory_location = (TextView) rootView.findViewById(R.id.tv_factory_location);
        gv_factory_location = (NoScrollGridView) rootView.findViewById(R.id.gv_factory_location);
        gv_add_business_license = (NoScrollGridView) rootView.findViewById(R.id.gv_add_business_license);
        gv_group_photo = (NoScrollGridView) rootView.findViewById(R.id.gv_group_photo);
        gv_timber_goods = (NoScrollGridView) rootView.findViewById(R.id.gv_timber_goods);
        gv_plant_lease_contract = (NoScrollGridView) rootView.findViewById(R.id.gv_plant_lease_contract);
        tv_employees_number = (TextView) rootView.findViewById(R.id.tv_employees_number);
        tv_sales_volume = (TextView) rootView.findViewById(R.id.tv_sales_volume);
        tv_business_license = (TextView) rootView.findViewById(R.id.tv_business_license);
        tv_timber_goods = (TextView) rootView.findViewById(R.id.tv_timber_goods);
        tv_group_photo = (TextView) rootView.findViewById(R.id.tv_group_photo);
        tv_plant_lease_contract = (TextView) rootView.findViewById(R.id.tv_plant_lease_contract);
        tv_bill = (TextView) rootView.findViewById(R.id.tv_bill);
        et_employees_number = (EditText) rootView.findViewById(R.id.et_employees_number);
        et_sale_volume = (EditText) rootView.findViewById(R.id.et_sale_volume);
        tv_manager = (TextView) rootView.findViewById(R.id.tv_manager);
        et_partner_name = (EditText) rootView.findViewById(R.id.et_partner_name);
        et_partner_bank_id = (EditText) rootView.findViewById(R.id.et_partner_bank_id);
        et_partner_id_card = (EditText) rootView.findViewById(R.id.et_partner_id_card);
        iv_partner_id_card_front_pic = (ImageView) rootView.findViewById(R.id.iv_partner_id_card_front_pic);
        iv_partner_id_card_reverse_pic = (ImageView) rootView.findViewById(R.id.iv_partner_id_card_reverse_pic);
        sv_total = (ScrollView) rootView.findViewById(R.id.sv_total);

        tv_add_product.setText(Utils.setFountColor(tv_add_product));
        tv_add_shop.setText(Utils.setFountColorAndSize(this, tv_add_shop, 8, 25));
        tv_business_license.setText(Utils.setFountColorAndSize(this, tv_business_license, 8, 14));
        tv_timber_goods.setText(Utils.setFountColorAndSize(this, tv_timber_goods, 7, 16));
        tv_group_photo.setText(Utils.setFountColorAndSize(this, tv_group_photo, 14, 23));
        tv_plant_lease_contract.setText(Utils.setFountColorAndSize(this, tv_plant_lease_contract, 10, 21));
        tv_factory_location.setText(Utils.setFountColorAndSize(this, tv_factory_location, 10, 19));
//        tv_factory_location.setText(Utils.setFountColor(tv_factory_location));
        tv_employees_number.setText(Utils.setFountColor(tv_employees_number));
        tv_sales_volume.setText(Utils.setFountColor(tv_sales_volume));
//        tv_group_photo.setText(Utils.setFountColor(tv_group_photo));
        tv_bill.setText(Utils.setFountColor(tv_bill));
        tv_manager.setText(Utils.setFountColor(tv_manager));

        gson = new Gson();
        loadingDialog = DialogUtils.createLoadDialog(WoodPackagingActivity.this, false);
        if (ctf == null) {
            ctf = MainActivity.getCtf();
        }

        userId = SharedPrefrenceUtils.getString(this, "usrid");

        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));

        sv_total.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        sv_total.setFocusable(true);
        sv_total.setFocusableInTouchMode(true);
        sv_total.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                v.requestFocusFromTouch();
                return false;
            }
        });

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

            et_employees_number.setFocusable(false);
            et_sale_volume.setFocusable(false);
            et_remark.setFocusable(false);

            new Thread() {
                @Override
                public void run() {
                    try {
                        Bitmap photo = BitmapUtils.getSmallBitmap(path);
                        if (photo != null) {
                            Bitmap new_photo = BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            new_photo.compress(Bitmap.CompressFormat.JPEG, getResources().getInteger(R.integer.bitmap_compress), baos);

                            String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                    .format(new Date());
//                            String suoPath = BitmapUtils.saveMyBitmap(suoName,new_photo);
//                            uploadImage(suoPath);

                            path = BitmapUtils.saveMyBitmap(suoName, new_photo);
                            handler.sendEmptyMessage(INTERCEPT);

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
            }.start();


            //通知相册刷新
            Uri uriData = Uri.parse("file://" + path);
            UIUtils.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uriData));


        }

        // 读取相册缩放图片
        else if (requestCode == PHOTO_ZOOM && data != null) {
            if (addType == 7 || addType == 8) {
                getSinglePhoto(data);
            } else {
                getMultiplePhoto(data);
            }
        }

        if (requestCode == SHOWIMAGE && data != null) {
            if (addType == 0) {
                productPictureItemBeanList.remove(position);
                mFurniturePictureAdapter.notifyDataSetChanged();
            } else if (addType == 2) {
                shopPictureItemBeanList.remove(position);
                mFurnitureShopPictureAdapter.notifyDataSetChanged();
            } else if (addType == 1) {
                factoryLocationPictureItemBeanList.remove(position);
                mFactoryLocationPictureAdapter.notifyDataSetChanged();
            } else if (addType == 3) {
                businessLicensePictureItemBeanList.remove(position);
                mBusinessLicensePictureAdapter.notifyDataSetChanged();
            } else if (addType == 4) {
                timberGoodsPictureItemBeanList.remove(position);
                mTimberGoodsPictureAdapter.notifyDataSetChanged();
            } else if (addType == 5) {
                groupPictureItemBeanList.remove(position);
                mGroupPictureAdapter.notifyDataSetChanged();
            } else if (addType == 6) {
                plantLeaseContractPictureItemBeanList.remove(position);
                mPlantLeaseContractPictureAdapter.notifyDataSetChanged();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 从相册读取一张照片
     * @param data
     */
    private void getSinglePhoto(Intent data) {
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
                    uri = BitmapUtils.getPictureUri(data, WoodPackagingActivity.this);
                    cursor = managedQuery(uri, proj, null, null, null);
                }
                // 获取索引
                if (cursor != null) {
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
                    if (MobileUtils.getSDKVersionNumber() < 14) {
                        cursor.close();
                    }
                }
            }

            et_employees_number.setFocusable(false);
            et_sale_volume.setFocusable(false);
            et_remark.setFocusable(false);

            new Thread() {
                @Override
                public void run() {
                    try {
                        Bitmap photo = BitmapUtils.getSmallBitmap(path);
                        if (photo != null) {
                            Bitmap new_photo = BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            new_photo.compress(Bitmap.CompressFormat.JPEG, getResources().getInteger(R.integer.bitmap_compress), baos);

                            String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                    .format(new Date());
//                                String suoPath = BitmapUtils.saveMyBitmap(suoName,new_photo);
//                                uploadImage(suoPath);

                            path = BitmapUtils.saveMyBitmap(suoName, new_photo);
                            handler.sendEmptyMessage(INTERCEPT);

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
            }.start();

        }
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

                            String imgPath = BitmapUtils.saveMyBitmap(suoName + i, new_photo);

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
                        et_sale_volume.setFocusable(true);
                        et_sale_volume.setFocusableInTouchMode(true);
                        et_employees_number.setFocusable(true);
                        et_employees_number.setFocusableInTouchMode(true);
                        et_remark.setFocusable(true);
                        et_remark.setFocusableInTouchMode(true);

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
                                LogUtils.e("productPictureItemBeanList:" + productPictureItemBeanList.toString());
                            } else if (addType == 2) {
                                shopPictureItemBeanList.add(0, pictureItemBean);
                                mFurnitureShopPictureAdapter.notifyDataSetChanged();
                            } else if (addType == 1) {
                                factoryLocationPictureItemBeanList.add(0, pictureItemBean);
                                mFactoryLocationPictureAdapter.notifyDataSetChanged();
                            } else if (addType == 3) {
                                businessLicensePictureItemBeanList.add(0, pictureItemBean);
                                mBusinessLicensePictureAdapter.notifyDataSetChanged();
                            } else if (addType == 4) {
                                timberGoodsPictureItemBeanList.add(0, pictureItemBean);
                                mTimberGoodsPictureAdapter.notifyDataSetChanged();
                            } else if (addType == 5) {
                                groupPictureItemBeanList.add(0, pictureItemBean);
                                mGroupPictureAdapter.notifyDataSetChanged();
                            } else if (addType == 6) {
                                plantLeaseContractPictureItemBeanList.add(0, pictureItemBean);
                                mPlantLeaseContractPictureAdapter.notifyDataSetChanged();
                            } else if (addType == 7) {
                                if (!TextUtils.isEmpty(picUrl)) {
                                    partner_id_card_front_pic = picUrl;
                                    imageLoader.displayImage(Constants.BaseImageUrl + picUrl, iv_partner_id_card_front_pic, PictureOption.getSimpleOptions());
                                } else {
                                    if (!TextUtils.isEmpty(selectImages.get(i).getImagePath())) {
                                        Bitmap bitmap = BitmapUtils.getSmallBitmap(selectImages.get(i).getImagePath());
                                        iv_partner_id_card_front_pic.setImageBitmap(bitmap);
                                    }
                                }

                            } else if (addType == 8) {
                                if (!TextUtils.isEmpty(picUrl)) {
                                    partner_id_card_reverse_pic = picUrl;
                                    imageLoader.displayImage(Constants.BaseImageUrl + picUrl, iv_partner_id_card_reverse_pic, PictureOption.getSimpleOptions());
                                } else {
                                    if (!TextUtils.isEmpty(selectImages.get(i).getImagePath())) {
                                        Bitmap bitmap = BitmapUtils.getSmallBitmap(selectImages.get(i).getImagePath());
                                        iv_partner_id_card_reverse_pic.setImageBitmap(bitmap);
                                    }
                                }
                            }
                        } else {
                            if (!isFinishing()) {
                                DialogUtils.showAlertDialog(WoodPackagingActivity.this,
                                        fkbaseResponse.getMsg());
                            }
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        et_sale_volume.setFocusable(true);
                        et_sale_volume.setFocusableInTouchMode(true);
                        et_employees_number.setFocusable(true);
                        et_employees_number.setFocusableInTouchMode(true);
                        et_remark.setFocusable(true);
                        et_remark.setFocusableInTouchMode(true);

                        if (isLast) {
                            loadingDialog.dismiss();
                        }
                        if (!isFinishing()) {
                            DialogUtils.showAlertDialog(WoodPackagingActivity.this,
                                    msg);
                        }
                    }
                });


    }

    /**
     * 根据store_type_id查询店铺类型信息
     */
    private void getStoreTypeDesc() {
        loadingDialog.show();
        String url = Constants.XINYONGSERVER_URL + "credit_money_background/user/findStoreTypeDescByStoreTypeId.do";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("store_type_id", store_type + "");
        LogUtils.e("params" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                LogUtils.e("店铺类型信息" + json);
                loadingDialog.dismiss();
                getStoreTypeDescResponse = gson.fromJson(json, GetStoreTypeDescResponse.class);
                if (getStoreTypeDescResponse.getCode() == 0) {
//                    storeType = getStoreTypeDescResponse.getData().getType();
                    tv_title.setText(getStoreTypeDescResponse.getData().getType() + "分期资料");

                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(WoodPackagingActivity.this,
                                getStoreTypeDescResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(WoodPackagingActivity.this,
                            error);
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
        paramMap.put("usrid", userId);
        Map<String, String> filesMap = new HashMap<String, String>();
        filesMap.put("pic_string", suoPath);
        MyVolley.uploadWithFileWholeUrl(url, paramMap, filesMap, null, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                et_sale_volume.setFocusable(true);
                et_sale_volume.setFocusableInTouchMode(true);
                et_employees_number.setFocusable(true);
                et_employees_number.setFocusableInTouchMode(true);
                et_remark.setFocusable(true);
                et_remark.setFocusableInTouchMode(true);

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
                    } else if (addType == 2) {
                        shopPictureItemBeanList.add(0, pictureItemBean);
                        mFurnitureShopPictureAdapter.notifyDataSetChanged();
                    } else if (addType == 1) {
                        factoryLocationPictureItemBeanList.add(0, pictureItemBean);
                        mFactoryLocationPictureAdapter.notifyDataSetChanged();
                    } else if (addType == 3) {
                        businessLicensePictureItemBeanList.add(0, pictureItemBean);
                        mBusinessLicensePictureAdapter.notifyDataSetChanged();
                    } else if (addType == 4) {
                        timberGoodsPictureItemBeanList.add(0, pictureItemBean);
                        mTimberGoodsPictureAdapter.notifyDataSetChanged();
                    } else if (addType == 5) {
                        groupPictureItemBeanList.add(0, pictureItemBean);
                        mGroupPictureAdapter.notifyDataSetChanged();
                    } else if (addType == 6) {
                        plantLeaseContractPictureItemBeanList.add(0, pictureItemBean);
                        mPlantLeaseContractPictureAdapter.notifyDataSetChanged();
                    } else if (addType == 7) {
                        if (!TextUtils.isEmpty(picUrl)) {
                            partner_id_card_front_pic = picUrl;
                            imageLoader.displayImage(Constants.BaseImageUrl + picUrl, iv_partner_id_card_front_pic, PictureOption.getSimpleOptions());
                        } else {
                            if (!TextUtils.isEmpty(suoPath)) {
                                Bitmap bitmap = BitmapUtils.getSmallBitmap(suoPath);
                                iv_partner_id_card_front_pic.setImageBitmap(bitmap);
                            }
                        }

                    } else if (addType == 8) {
                        if (!TextUtils.isEmpty(picUrl)) {
                            partner_id_card_reverse_pic = picUrl;
                            imageLoader.displayImage(Constants.BaseImageUrl + picUrl, iv_partner_id_card_reverse_pic, PictureOption.getSimpleOptions());
                        } else {
                            if (!TextUtils.isEmpty(suoPath)) {
                                Bitmap bitmap = BitmapUtils.getSmallBitmap(suoPath);
                                iv_partner_id_card_reverse_pic.setImageBitmap(bitmap);
                            }
                        }
                    }
                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(WoodPackagingActivity.this,
                                fkbaseResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                et_sale_volume.setFocusable(true);
                et_sale_volume.setFocusableInTouchMode(true);
                et_employees_number.setFocusable(true);
                et_employees_number.setFocusableInTouchMode(true);
                et_remark.setFocusable(true);
                et_remark.setFocusableInTouchMode(true);

                loadingDialog.dismiss();
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(WoodPackagingActivity.this,
                            error);
                }
            }
        });

    }

    private void initData() {
        final Intent intent = getIntent();
        usr_order_id = intent.getStringExtra("usr_order_id");
        store_type = intent.getIntExtra("store_type", 0);
        isFromRecord = intent.getBooleanExtra("isFromRecord", false);

        if (isFromRecord && !TextUtils.isEmpty(usr_order_id)) {
            String messageStr = SharedPrefrenceUtils.getString(this, usr_order_id + "-step4");
            if (!TextUtils.isEmpty(messageStr)) {
                HashMap<String, String> hs = gson.fromJson(messageStr, HashMap.class);
                LogUtils.e("未提交数据:" + hs.toString());
                setMessage(hs);
            } else {
                initPhotos();
            }

        } else {
            initPhotos();

            String messageStr = SharedPrefrenceUtils.getString(this, userId + "WoodPackagingActivity");
            if(!TextUtils.isEmpty(messageStr)){
                HashMap<String, String> hs = gson.fromJson(messageStr, HashMap.class);
                LogUtils.e("与usrid相关数据:" + hs.toString());
                setUserMessage(hs);
            }
        }

        et_remark.setOnFocusChangeListener(new MyOnFocusChangeListener());

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
                Intent intent1 = new Intent(WoodPackagingActivity.this, ShowImageActivity.class);
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
                Intent intent1 = new Intent(WoodPackagingActivity.this, ShowImageActivity.class);
                intent1.putExtra("pictureItemBean", pictureItemBean2);
                UIUtils.startActivityForResult(intent1, SHOWIMAGE);
            }
        });
        mFactoryLocationPictureAdapter.setCallBack(new PictureAdapter.ICallBack() {
            @Override
            public void addImage(int pos) {
                addType = 1;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }

            @Override
            public void deleteImage(int pos) {
                factoryLocationPictureItemBeanList.remove(pos);
                mFactoryLocationPictureAdapter.notifyDataSetChanged();

            }

            @Override
            public void showImage(int pos) {
                addType = 1;
                position = pos;
                PictureItemBean pictureItemBean2 = factoryLocationPictureItemBeanList.get(pos);
                Intent intent1 = new Intent(WoodPackagingActivity.this, ShowImageActivity.class);
                intent1.putExtra("pictureItemBean", pictureItemBean2);
                UIUtils.startActivityForResult(intent1, SHOWIMAGE);
            }
        });

        mBusinessLicensePictureAdapter.setCallBack(new PictureAdapter.ICallBack() {
            @Override
            public void addImage(int pos) {
                addType = 3;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }

            @Override
            public void deleteImage(int pos) {
                businessLicensePictureItemBeanList.remove(pos);
                mBusinessLicensePictureAdapter.notifyDataSetChanged();

            }

            @Override
            public void showImage(int pos) {
                addType = 3;
                position = pos;
                PictureItemBean pictureItemBean2 = businessLicensePictureItemBeanList.get(pos);
                Intent intent1 = new Intent(WoodPackagingActivity.this, ShowImageActivity.class);
                intent1.putExtra("pictureItemBean", pictureItemBean2);
                UIUtils.startActivityForResult(intent1, SHOWIMAGE);
            }
        });

        mTimberGoodsPictureAdapter.setCallBack(new PictureAdapter.ICallBack() {
            @Override
            public void addImage(int pos) {
                addType = 4;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }

            @Override
            public void deleteImage(int pos) {
                timberGoodsPictureItemBeanList.remove(pos);
                mTimberGoodsPictureAdapter.notifyDataSetChanged();

            }

            @Override
            public void showImage(int pos) {
                addType = 4;
                position = pos;
                PictureItemBean pictureItemBean2 = timberGoodsPictureItemBeanList.get(pos);
                Intent intent1 = new Intent(WoodPackagingActivity.this, ShowImageActivity.class);
                intent1.putExtra("pictureItemBean", pictureItemBean2);
                UIUtils.startActivityForResult(intent1, SHOWIMAGE);
            }
        });

        mGroupPictureAdapter.setCallBack(new PictureAdapter.ICallBack() {
            @Override
            public void addImage(int pos) {
                addType = 5;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }

            @Override
            public void deleteImage(int pos) {
                groupPictureItemBeanList.remove(pos);
                mGroupPictureAdapter.notifyDataSetChanged();

            }

            @Override
            public void showImage(int pos) {
                addType = 5;
                position = pos;
                PictureItemBean pictureItemBean2 = groupPictureItemBeanList.get(pos);
                Intent intent1 = new Intent(WoodPackagingActivity.this, ShowImageActivity.class);
                intent1.putExtra("pictureItemBean", pictureItemBean2);
                UIUtils.startActivityForResult(intent1, SHOWIMAGE);
            }
        });

        mPlantLeaseContractPictureAdapter.setCallBack(new PictureAdapter.ICallBack() {
            @Override
            public void addImage(int pos) {
                addType = 6;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }

            @Override
            public void deleteImage(int pos) {
                plantLeaseContractPictureItemBeanList.remove(pos);
                mPlantLeaseContractPictureAdapter.notifyDataSetChanged();

            }

            @Override
            public void showImage(int pos) {
                addType = 6;
                position = pos;
                PictureItemBean pictureItemBean2 = plantLeaseContractPictureItemBeanList.get(pos);
                Intent intent1 = new Intent(WoodPackagingActivity.this, ShowImageActivity.class);
                intent1.putExtra("pictureItemBean", pictureItemBean2);
                UIUtils.startActivityForResult(intent1, SHOWIMAGE);
            }
        });

        tv_top_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        iv_partner_id_card_front_pic.setOnClickListener(this);
        iv_partner_id_card_reverse_pic.setOnClickListener(this);
//        ll_tell.setOnClickListener(this);

//        getStoreTypeDesc();
    }

    /**
     * 设置上次未上传的数据
     */
    private void setMessage(HashMap<String, String> hs) {
        employeesNumber = hs.get("employeesNumber");
        if (!TextUtils.isEmpty(employeesNumber)) {
            et_employees_number.setText(employeesNumber);
        }

        saleVolume = hs.get("saleVolume");
        if (!TextUtils.isEmpty(saleVolume)) {
            et_sale_volume.setText(saleVolume);
        }

        String remark = hs.get("remark");
        if (!TextUtils.isEmpty(remark)) {
            et_remark.setText(remark);
        }

        partner_id_card = hs.get("partner_id_card");
        if(!TextUtils.isEmpty(partner_id_card)){
            et_partner_id_card.setText(partner_id_card);
        }

        partner_id_card_front_pic = hs.get("partner_id_card_front_pic");
        if(!TextUtils.isEmpty(partner_id_card_front_pic)){
            imageLoader.displayImage(Constants.BaseImageUrl + partner_id_card_front_pic, iv_partner_id_card_front_pic, PictureOption.getSimpleOptions());
        }

        partner_id_card_reverse_pic = hs.get("partner_id_card_reverse_pic");
        if(!TextUtils.isEmpty(partner_id_card_reverse_pic)){
            imageLoader.displayImage(Constants.BaseImageUrl + partner_id_card_reverse_pic, iv_partner_id_card_reverse_pic, PictureOption.getSimpleOptions());
        }

        partner_bank_id = hs.get("partner_bank_id");
        if(!TextUtils.isEmpty(partner_bank_id)){
            et_partner_bank_id.setText(partner_bank_id);
        }

        partner_name = hs.get("partner_name");
        if(!TextUtils.isEmpty(partner_name)){
            et_partner_name.setText(partner_name);
        }


        productPictureItemBeanList = gson.fromJson(hs.get("productPictureItemBeanList"), new TypeToken<List<PictureItemBean>>() {
        }.getType());
        if (productPictureItemBeanList == null || productPictureItemBeanList.size() == 0) {
            productPictureItemBeanList = new ArrayList<PictureItemBean>();
            PictureItemBean pictureItemBean = new PictureItemBean();
            productPictureItemBeanList.add(pictureItemBean);
        }
        mFurniturePictureAdapter = new PictureAdapter(this, productPictureItemBeanList);
        gv_add_product.setAdapter(mFurniturePictureAdapter);

        shopPictureItemBeanList = gson.fromJson(hs.get("shopPictureItemBeanList"), new TypeToken<List<PictureItemBean>>() {
        }.getType());
        if (shopPictureItemBeanList == null || shopPictureItemBeanList.size() == 0) {
            shopPictureItemBeanList = new ArrayList<PictureItemBean>();
            PictureItemBean pictureItemBean2 = new PictureItemBean();
            shopPictureItemBeanList.add(pictureItemBean2);
        }
        mFurnitureShopPictureAdapter = new PictureAdapter(this, shopPictureItemBeanList);
        gv_add_shop.setAdapter(mFurnitureShopPictureAdapter);

        factoryLocationPictureItemBeanList = gson.fromJson(hs.get("factoryLocationPictureItemBeanList"), new TypeToken<List<PictureItemBean>>() {
        }.getType());
        if (factoryLocationPictureItemBeanList == null || factoryLocationPictureItemBeanList.size() == 0) {
            factoryLocationPictureItemBeanList = new ArrayList<PictureItemBean>();
            PictureItemBean pictureItemBean1 = new PictureItemBean();
            factoryLocationPictureItemBeanList.add(pictureItemBean1);
        }
        mFactoryLocationPictureAdapter = new PictureAdapter(this, factoryLocationPictureItemBeanList);
        gv_factory_location.setAdapter(mFactoryLocationPictureAdapter);

        businessLicensePictureItemBeanList = gson.fromJson(hs.get("businessLicensePictureItemBeanList"), new TypeToken<List<PictureItemBean>>() {
        }.getType());
        if (businessLicensePictureItemBeanList == null || businessLicensePictureItemBeanList.size() == 0) {
            businessLicensePictureItemBeanList = new ArrayList<PictureItemBean>();
            PictureItemBean pictureItemBean3 = new PictureItemBean();
            businessLicensePictureItemBeanList.add(pictureItemBean3);
        }
        mBusinessLicensePictureAdapter = new PictureAdapter(this, businessLicensePictureItemBeanList);
        gv_add_business_license.setAdapter(mBusinessLicensePictureAdapter);

        timberGoodsPictureItemBeanList = gson.fromJson(hs.get("timberGoodsPictureItemBeanList"), new TypeToken<List<PictureItemBean>>() {
        }.getType());
        if (timberGoodsPictureItemBeanList == null || timberGoodsPictureItemBeanList.size() == 0) {
            timberGoodsPictureItemBeanList = new ArrayList<PictureItemBean>();
            PictureItemBean pictureItemBean4 = new PictureItemBean();
            timberGoodsPictureItemBeanList.add(pictureItemBean4);
        }
        mTimberGoodsPictureAdapter = new PictureAdapter(this, timberGoodsPictureItemBeanList);
        gv_timber_goods.setAdapter(mTimberGoodsPictureAdapter);

        groupPictureItemBeanList = gson.fromJson(hs.get("groupPictureItemBeanList"), new TypeToken<List<PictureItemBean>>() {
        }.getType());
        if (groupPictureItemBeanList == null || groupPictureItemBeanList.size() == 0) {
            groupPictureItemBeanList = new ArrayList<PictureItemBean>();
            PictureItemBean pictureItemBean5 = new PictureItemBean();
            groupPictureItemBeanList.add(pictureItemBean5);
        }
        mGroupPictureAdapter = new PictureAdapter(this, groupPictureItemBeanList);
        gv_group_photo.setAdapter(mGroupPictureAdapter);

        plantLeaseContractPictureItemBeanList = gson.fromJson(hs.get("plantLeaseContractPictureItemBeanList"), new TypeToken<List<PictureItemBean>>() {
        }.getType());
        if (plantLeaseContractPictureItemBeanList == null || plantLeaseContractPictureItemBeanList.size() == 0) {
            plantLeaseContractPictureItemBeanList = new ArrayList<PictureItemBean>();
            PictureItemBean pictureItemBean6 = new PictureItemBean();
            plantLeaseContractPictureItemBeanList.add(pictureItemBean6);
        }
        mPlantLeaseContractPictureAdapter = new PictureAdapter(this, plantLeaseContractPictureItemBeanList);
        gv_plant_lease_contract.setAdapter(mPlantLeaseContractPictureAdapter);
    }

    /**
     * 设置用户相关的数据
     */
    private void setUserMessage(HashMap<String, String> hs) {
        employeesNumber = hs.get("employeesNumber");
        if (!TextUtils.isEmpty(employeesNumber)) {
            et_employees_number.setText(employeesNumber);
        }

        saleVolume = hs.get("saleVolume");
        if (!TextUtils.isEmpty(saleVolume)) {
            et_sale_volume.setText(saleVolume);
        }

//        String remark = hs.get("remark");
//        if (!TextUtils.isEmpty(remark)) {
//            et_remark.setText(remark);
//        }

        partner_id_card = hs.get("partner_id_card");
        if(!TextUtils.isEmpty(partner_id_card)){
            et_partner_id_card.setText(partner_id_card);
        }

        partner_id_card_front_pic = hs.get("partner_id_card_front_pic");
        if(!TextUtils.isEmpty(partner_id_card_front_pic)){
            imageLoader.displayImage(Constants.BaseImageUrl + partner_id_card_front_pic, iv_partner_id_card_front_pic, PictureOption.getSimpleOptions());
        }

        partner_id_card_reverse_pic = hs.get("partner_id_card_reverse_pic");
        if(!TextUtils.isEmpty(partner_id_card_reverse_pic)){
            imageLoader.displayImage(Constants.BaseImageUrl + partner_id_card_reverse_pic, iv_partner_id_card_reverse_pic, PictureOption.getSimpleOptions());
        }

        partner_bank_id = hs.get("partner_bank_id");
        if(!TextUtils.isEmpty(partner_bank_id)){
            et_partner_bank_id.setText(partner_bank_id);
        }

        partner_name = hs.get("partner_name");
        if(!TextUtils.isEmpty(partner_name)){
            et_partner_name.setText(partner_name);
        }


//        productPictureItemBeanList = gson.fromJson(hs.get("productPictureItemBeanList"), new TypeToken<List<PictureItemBean>>() {
//        }.getType());
//        if (productPictureItemBeanList == null || productPictureItemBeanList.size() == 0) {
//            productPictureItemBeanList = new ArrayList<PictureItemBean>();
//            PictureItemBean pictureItemBean = new PictureItemBean();
//            productPictureItemBeanList.add(pictureItemBean);
//        }
//        mFurniturePictureAdapter = new PictureAdapter(this, productPictureItemBeanList);
//        gv_add_product.setAdapter(mFurniturePictureAdapter);
//
//        shopPictureItemBeanList = gson.fromJson(hs.get("shopPictureItemBeanList"), new TypeToken<List<PictureItemBean>>() {
//        }.getType());
//        if (shopPictureItemBeanList == null || shopPictureItemBeanList.size() == 0) {
//            shopPictureItemBeanList = new ArrayList<PictureItemBean>();
//            PictureItemBean pictureItemBean2 = new PictureItemBean();
//            shopPictureItemBeanList.add(pictureItemBean2);
//        }
//        mFurnitureShopPictureAdapter = new PictureAdapter(this, shopPictureItemBeanList);
//        gv_add_shop.setAdapter(mFurnitureShopPictureAdapter);
//
//        factoryLocationPictureItemBeanList = gson.fromJson(hs.get("factoryLocationPictureItemBeanList"), new TypeToken<List<PictureItemBean>>() {
//        }.getType());
//        if (factoryLocationPictureItemBeanList == null || factoryLocationPictureItemBeanList.size() == 0) {
//            factoryLocationPictureItemBeanList = new ArrayList<PictureItemBean>();
//            PictureItemBean pictureItemBean1 = new PictureItemBean();
//            factoryLocationPictureItemBeanList.add(pictureItemBean1);
//        }
//        mFactoryLocationPictureAdapter = new PictureAdapter(this, factoryLocationPictureItemBeanList);
//        gv_factory_location.setAdapter(mFactoryLocationPictureAdapter);
//
//        businessLicensePictureItemBeanList = gson.fromJson(hs.get("businessLicensePictureItemBeanList"), new TypeToken<List<PictureItemBean>>() {
//        }.getType());
//        if (businessLicensePictureItemBeanList == null || businessLicensePictureItemBeanList.size() == 0) {
//            businessLicensePictureItemBeanList = new ArrayList<PictureItemBean>();
//            PictureItemBean pictureItemBean3 = new PictureItemBean();
//            businessLicensePictureItemBeanList.add(pictureItemBean3);
//        }
//        mBusinessLicensePictureAdapter = new PictureAdapter(this, businessLicensePictureItemBeanList);
//        gv_add_business_license.setAdapter(mBusinessLicensePictureAdapter);
//
//        timberGoodsPictureItemBeanList = gson.fromJson(hs.get("timberGoodsPictureItemBeanList"), new TypeToken<List<PictureItemBean>>() {
//        }.getType());
//        if (timberGoodsPictureItemBeanList == null || timberGoodsPictureItemBeanList.size() == 0) {
//            timberGoodsPictureItemBeanList = new ArrayList<PictureItemBean>();
//            PictureItemBean pictureItemBean4 = new PictureItemBean();
//            timberGoodsPictureItemBeanList.add(pictureItemBean4);
//        }
//        mTimberGoodsPictureAdapter = new PictureAdapter(this, timberGoodsPictureItemBeanList);
//        gv_timber_goods.setAdapter(mTimberGoodsPictureAdapter);
//
//        groupPictureItemBeanList = gson.fromJson(hs.get("groupPictureItemBeanList"), new TypeToken<List<PictureItemBean>>() {
//        }.getType());
//        if (groupPictureItemBeanList == null || groupPictureItemBeanList.size() == 0) {
//            groupPictureItemBeanList = new ArrayList<PictureItemBean>();
//            PictureItemBean pictureItemBean5 = new PictureItemBean();
//            groupPictureItemBeanList.add(pictureItemBean5);
//        }
//        mGroupPictureAdapter = new PictureAdapter(this, groupPictureItemBeanList);
//        gv_group_photo.setAdapter(mGroupPictureAdapter);
//
//        plantLeaseContractPictureItemBeanList = gson.fromJson(hs.get("plantLeaseContractPictureItemBeanList"), new TypeToken<List<PictureItemBean>>() {
//        }.getType());
//        if (plantLeaseContractPictureItemBeanList == null || plantLeaseContractPictureItemBeanList.size() == 0) {
//            plantLeaseContractPictureItemBeanList = new ArrayList<PictureItemBean>();
//            PictureItemBean pictureItemBean6 = new PictureItemBean();
//            plantLeaseContractPictureItemBeanList.add(pictureItemBean6);
//        }
//        mPlantLeaseContractPictureAdapter = new PictureAdapter(this, plantLeaseContractPictureItemBeanList);
//        gv_plant_lease_contract.setAdapter(mPlantLeaseContractPictureAdapter);
    }

    /**
     * 初始化图片列表
     */
    private void initPhotos() {
        productPictureItemBeanList = new ArrayList<PictureItemBean>();
        PictureItemBean pictureItemBean = new PictureItemBean();
        productPictureItemBeanList.add(pictureItemBean);
        mFurniturePictureAdapter = new PictureAdapter(this, productPictureItemBeanList);
        gv_add_product.setAdapter(mFurniturePictureAdapter);

        shopPictureItemBeanList = new ArrayList<PictureItemBean>();
        PictureItemBean pictureItemBean2 = new PictureItemBean();
        shopPictureItemBeanList.add(pictureItemBean2);
        mFurnitureShopPictureAdapter = new PictureAdapter(this, shopPictureItemBeanList);
        gv_add_shop.setAdapter(mFurnitureShopPictureAdapter);

        factoryLocationPictureItemBeanList = new ArrayList<PictureItemBean>();
        PictureItemBean pictureItemBean1 = new PictureItemBean();
        factoryLocationPictureItemBeanList.add(pictureItemBean1);
        mFactoryLocationPictureAdapter = new PictureAdapter(this, factoryLocationPictureItemBeanList);
        gv_factory_location.setAdapter(mFactoryLocationPictureAdapter);

        businessLicensePictureItemBeanList = new ArrayList<PictureItemBean>();
        PictureItemBean pictureItemBean3 = new PictureItemBean();
        businessLicensePictureItemBeanList.add(pictureItemBean3);
        mBusinessLicensePictureAdapter = new PictureAdapter(this, businessLicensePictureItemBeanList);
        gv_add_business_license.setAdapter(mBusinessLicensePictureAdapter);

        timberGoodsPictureItemBeanList = new ArrayList<PictureItemBean>();
        PictureItemBean pictureItemBean4 = new PictureItemBean();
        timberGoodsPictureItemBeanList.add(pictureItemBean4);
        mTimberGoodsPictureAdapter = new PictureAdapter(this, timberGoodsPictureItemBeanList);
        gv_timber_goods.setAdapter(mTimberGoodsPictureAdapter);

        groupPictureItemBeanList = new ArrayList<PictureItemBean>();
        PictureItemBean pictureItemBean5 = new PictureItemBean();
        groupPictureItemBeanList.add(pictureItemBean5);
        mGroupPictureAdapter = new PictureAdapter(this, groupPictureItemBeanList);
        gv_group_photo.setAdapter(mGroupPictureAdapter);

        plantLeaseContractPictureItemBeanList = new ArrayList<PictureItemBean>();
        PictureItemBean pictureItemBean6 = new PictureItemBean();
        plantLeaseContractPictureItemBeanList.add(pictureItemBean6);
        mPlantLeaseContractPictureAdapter = new PictureAdapter(this, plantLeaseContractPictureItemBeanList);
        gv_plant_lease_contract.setAdapter(mPlantLeaseContractPictureAdapter);
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
                    if (addType == 7 || addType == 8) {
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                IMAGE_UNSPECIFIED);
                        startActivityForResult(intent, PHOTO_ZOOM);
                    } else {
                        Intent intent = new Intent(WoodPackagingActivity.this, ChoosePhotoFolderActivity.class);
                        startActivityForResult(intent, PHOTO_ZOOM);
                    }

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
        switch (v.getId()) {
            case R.id.tv_top_back:
                saveMessage();
                ctf.setChecked(ctf.beforeIndex);
                ctf.mCurrentIndex = ctf.beforeIndex;
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.btn_next:   //下一步
                employeesNumber = et_employees_number.getText().toString();
                if (TextUtils.isEmpty(employeesNumber)) {
                    UIUtils.showToastCenter(WoodPackagingActivity.this, "请输入工厂员工数量");
                    return;
                }

                saleVolume = et_sale_volume.getText().toString();
                if (TextUtils.isEmpty(saleVolume)) {
                    UIUtils.showToastCenter(WoodPackagingActivity.this, "请输入企业年销售额");
                    return;
                }

                partner_name = et_partner_name.getText().toString();
                if (TextUtils.isEmpty(partner_name)) {
                    UIUtils.showToastCenter(WoodPackagingActivity.this, "企业联合担保人/合伙人/实际掌控人 真实姓名不能为空");
                    return;
                }

                partner_bank_id = et_partner_bank_id.getText().toString();
                if (TextUtils.isEmpty(partner_bank_id)) {
                    UIUtils.showToastCenter(WoodPackagingActivity.this, "企业联合担保人/合伙人/实际掌控人 银行卡不能为空");
                    return;
                }

                partner_id_card = et_partner_id_card.getText().toString();
                if (TextUtils.isEmpty(partner_id_card)) {
                    UIUtils.showToastCenter(WoodPackagingActivity.this, "企业联合担保人/合伙人/实际掌控人 身份证号码不能为空");
                    return;
                }

                if (TextUtils.isEmpty(partner_id_card_front_pic)) {
                    UIUtils.showToastCenter(WoodPackagingActivity.this, "企业联合担保人/合伙人/实际掌控人 身份证正面照不能为空");
                    return;
                }

                if (TextUtils.isEmpty(partner_id_card_reverse_pic)) {
                    UIUtils.showToastCenter(WoodPackagingActivity.this, "企业联合担保人/合伙人/实际掌控人 身份证反面照不能为空");
                    return;
                }

                if (businessLicensePictureItemBeanList.size() <= 1) {
                    UIUtils.showToastCenter(WoodPackagingActivity.this, "请先上传企业营业执照");
                    return;
                }
                if (shopPictureItemBeanList.size() <= 2) {
                    UIUtils.showToastCenter(WoodPackagingActivity.this, "工厂大门照片至少要2张");
                    return;
                }
                if (timberGoodsPictureItemBeanList.size() <= 2) {
                    UIUtils.showToastCenter(WoodPackagingActivity.this, "木材商品照片至少要2张");
                    return;
                }
                if (groupPictureItemBeanList.size() <= 1) {
                    UIUtils.showToastCenter(WoodPackagingActivity.this, "请先上传借款人、木材商和商品合照");
                    return;
                }
                if (factoryLocationPictureItemBeanList.size() <= 1) {
                    UIUtils.showToastCenter(WoodPackagingActivity.this, "请先上传工厂地理定位截图");
                    return;
                }
//                if (plantLeaseContractPictureItemBeanList.size() <= 1) {
//                    UIUtils.showToastCenter(WoodPackagingActivity.this, "请先上传厂房租赁完整合同照片");
//                    return;
//                }
                if (productPictureItemBeanList.size() <= 6) {
                    UIUtils.showToastCenter(WoodPackagingActivity.this, "主要产品及生产设备照片至少要6张");
                    return;
                }
//                setUserOrder();
                btn_next.setEnabled(false);
                btn_next.setBackgroundResource(R.mipmap.login_no);
                setCreditInf();
                break;
            case R.id.iv_partner_id_card_front_pic:
                addType = 7;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.iv_partner_id_card_reverse_pic:
                addType = 8;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
//            case R.id.ll_tell:
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_CALL);
//                //url:统一资源定位符
//                //uri:统一资源标示符（更广）
//                intent.setData(Uri.parse("tel:4006626985" ));
//                //开启系统拨号器
//                UIUtils.startActivity(intent);
//                break;
        }

    }

    /**
     * 保存未提交的数据
     */
    private void saveMessage() {
        HashMap<String, String> hs = new HashMap<>();
        hs.put("usr_order_id", usr_order_id);

        employeesNumber = et_employees_number.getText().toString();
        if (!TextUtils.isEmpty(employeesNumber)) {
            hs.put("employeesNumber", employeesNumber);
        }

        saleVolume = et_sale_volume.getText().toString();
        if (!TextUtils.isEmpty(saleVolume)) {
            hs.put("saleVolume", saleVolume);
        }

        String remark = et_remark.getText().toString();
        if (!TextUtils.isEmpty(remark)) {
            hs.put("remark", remark);
        }

        hs.put("productPictureItemBeanList", gson.toJson(productPictureItemBeanList));
        hs.put("shopPictureItemBeanList", gson.toJson(shopPictureItemBeanList));
        hs.put("factoryLocationPictureItemBeanList", gson.toJson(factoryLocationPictureItemBeanList));
        hs.put("businessLicensePictureItemBeanList", gson.toJson(businessLicensePictureItemBeanList));
        hs.put("timberGoodsPictureItemBeanList", gson.toJson(timberGoodsPictureItemBeanList));
        hs.put("groupPictureItemBeanList", gson.toJson(groupPictureItemBeanList));
        hs.put("plantLeaseContractPictureItemBeanList", gson.toJson(plantLeaseContractPictureItemBeanList));

        partner_id_card = et_partner_id_card.getText().toString();
        if(!TextUtils.isEmpty(partner_id_card)){
            hs.put("partner_id_card", partner_id_card);
        }
        if(!TextUtils.isEmpty(partner_id_card_front_pic)){
            hs.put("partner_id_card_front_pic", partner_id_card_front_pic);
        }
        if(!TextUtils.isEmpty(partner_id_card_reverse_pic)){
            hs.put("partner_id_card_reverse_pic", partner_id_card_reverse_pic);
        }
        partner_bank_id = et_partner_bank_id.getText().toString();
        if(!TextUtils.isEmpty(partner_bank_id)){
            hs.put("partner_bank_id", partner_bank_id);
        }
        partner_name = et_partner_name.getText().toString();
        if(!TextUtils.isEmpty(partner_name)){
            hs.put("partner_name", partner_name);
        }

        SharedPrefrenceUtils.setString(this, usr_order_id + "-step4", gson.toJson(hs));
    }

    /**
     * 保存与usrid相关的数据
     */
    private void saveUserMessage() {
        HashMap<String, String> hs = new HashMap<>();

        employeesNumber = et_employees_number.getText().toString();
        if (!TextUtils.isEmpty(employeesNumber)) {
            hs.put("employeesNumber", employeesNumber);
        }

        saleVolume = et_sale_volume.getText().toString();
        if (!TextUtils.isEmpty(saleVolume)) {
            hs.put("saleVolume", saleVolume);
        }

//        String remark = et_remark.getText().toString();
//        if (!TextUtils.isEmpty(remark)) {
//            hs.put("remark", remark);
//        }

//        hs.put("productPictureItemBeanList", gson.toJson(productPictureItemBeanList));
//        hs.put("shopPictureItemBeanList", gson.toJson(shopPictureItemBeanList));
//        hs.put("factoryLocationPictureItemBeanList", gson.toJson(factoryLocationPictureItemBeanList));
//        hs.put("businessLicensePictureItemBeanList", gson.toJson(businessLicensePictureItemBeanList));
//        hs.put("timberGoodsPictureItemBeanList", gson.toJson(timberGoodsPictureItemBeanList));
//        hs.put("groupPictureItemBeanList", gson.toJson(groupPictureItemBeanList));
//        hs.put("plantLeaseContractPictureItemBeanList", gson.toJson(plantLeaseContractPictureItemBeanList));

        partner_id_card = et_partner_id_card.getText().toString();
        if(!TextUtils.isEmpty(partner_id_card)){
            hs.put("partner_id_card", partner_id_card);
        }
        if(!TextUtils.isEmpty(partner_id_card_front_pic)){
            hs.put("partner_id_card_front_pic", partner_id_card_front_pic);
        }
        if(!TextUtils.isEmpty(partner_id_card_reverse_pic)){
            hs.put("partner_id_card_reverse_pic", partner_id_card_reverse_pic);
        }
        partner_bank_id = et_partner_bank_id.getText().toString();
        if(!TextUtils.isEmpty(partner_bank_id)){
            hs.put("partner_bank_id", partner_bank_id);
        }
        partner_name = et_partner_name.getText().toString();
        if(!TextUtils.isEmpty(partner_name)){
            hs.put("partner_name", partner_name);
        }

        SharedPrefrenceUtils.setString(this, userId + "WoodPackagingActivity", gson.toJson(hs));
    }

    /**
     * 写入授信资料
     */
    private void setCreditInf() {
        loadingDialog.show();
//        String url= Constants.FENGKONGSERVER_URL+"tsfkxt/user/setCreditInf_2.do";
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/setOrdersInf.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("usrid", userId);
        params.put("submit_step", "4");
        params.put("usr_order_id", usr_order_id);
        params.put("factory_employee_number", employeesNumber);
        params.put("enterprise_sales", saleVolume);
        params.put("partner_name", partner_name);
        params.put("partner_bank_id", partner_bank_id);
        params.put("partner_id_card", partner_id_card);
        params.put("partner_id_card_front_pic", partner_id_card_front_pic);
        params.put("partner_id_card_reverse_pic", partner_id_card_reverse_pic);
        List<UsrOrderPicBean> usrOrderPicBeanList = new ArrayList<UsrOrderPicBean>();
        for (int i = 0; i < productPictureItemBeanList.size() - 1; i++) {
            PictureItemBean pictureItemBean = productPictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(18);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("主要产品及有生产过程的现场照片");
            usrOrderPicBeanList.add(usrOrderPicBean);
        }
        for (int i = 0; i < shopPictureItemBeanList.size() - 1; i++) {
            PictureItemBean pictureItemBean = shopPictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(14);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("工厂大门照片");
            usrOrderPicBeanList.add(usrOrderPicBean);
        }
        for (int i = 0; i < factoryLocationPictureItemBeanList.size() - 1; i++) {
            PictureItemBean pictureItemBean = factoryLocationPictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(12);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("工厂地定位截图");
            usrOrderPicBeanList.add(usrOrderPicBean);
        }
        for (int i = 0; i < businessLicensePictureItemBeanList.size() - 1; i++) {
            PictureItemBean pictureItemBean = businessLicensePictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(100);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("企业营业执照");
            usrOrderPicBeanList.add(usrOrderPicBean);
        }
        for (int i = 0; i < timberGoodsPictureItemBeanList.size() - 1; i++) {
            PictureItemBean pictureItemBean = timberGoodsPictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(19);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("木材商品照");
            usrOrderPicBeanList.add(usrOrderPicBean);
        }
        for (int i = 0; i < groupPictureItemBeanList.size() - 1; i++) {
            PictureItemBean pictureItemBean = groupPictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(4);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("借款人、木材商和商品合照");
            usrOrderPicBeanList.add(usrOrderPicBean);
        }
        for (int i = 0; i < plantLeaseContractPictureItemBeanList.size() - 1; i++) {
            PictureItemBean pictureItemBean = plantLeaseContractPictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(0);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("厂房租赁完整合同");
            usrOrderPicBeanList.add(usrOrderPicBean);
        }
        params.put("usr_order_pic_list", gson.toJson(usrOrderPicBeanList)); //
        if (!TextUtils.isEmpty(et_remark.getText().toString())) {
            params.put("scene_remark", et_remark.getText().toString()); //
        }
        LogUtils.e("params:" + params.toString());
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
                    saveUserMessage();

                    Intent intent = new Intent(WoodPackagingActivity.this, PackagingAgreementActivity.class);
                    intent.putExtra("usr_order_id", usr_order_id);
                    setFinish();
                    UIUtils.startActivityNextAnim(intent);
                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(WoodPackagingActivity.this, fkbaseResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                btn_next.setEnabled(true);
                btn_next.setBackgroundResource(R.mipmap.btn_login);
                loadingDialog.dismiss();
                Log.e(TAG, "写入征信资料返回错误:" + error);
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(WoodPackagingActivity.this, error);
                }

            }
        });
    }


}
