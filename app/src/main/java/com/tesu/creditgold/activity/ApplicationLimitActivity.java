package com.tesu.creditgold.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.BuildConfig;
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
import com.tesu.creditgold.adapter.DictionaryAdapter;
import com.tesu.creditgold.adapter.PictureAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.DictionaryBean;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.PhotoUpImageItem;
import com.tesu.creditgold.bean.PictureItemBean;
import com.tesu.creditgold.bean.UsrOrderPicBean;
import com.tesu.creditgold.response.GetSupportBankListResponse;
import com.tesu.creditgold.response.GetUsrBankListResponse;
import com.tesu.creditgold.support.PercentLinearLayout;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.AppUtils;
import com.tesu.creditgold.util.BitmapUtils;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.MobileUtils;
import com.tesu.creditgold.util.PictureOption;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.StringUtils;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.util.Utils;
import com.tesu.creditgold.widget.NoScrollGridView;
import com.tesu.creditgold.widget.SupportBankListDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ApplicationLimitActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ApplicationLimitActivity.class.getSimpleName();
    private TextView tv_top_back;
    private View rootView;

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

    private List<PictureItemBean> shopPictureItemBeanList;  //工厂大门照片list
    private List<PictureItemBean> productPictureItemBeanList;  //主要产品照片list
    private List<PictureItemBean> factoryLocationPictureItemBeanList;  //工厂地定位截图list

    private PictureAdapter mFurnitureShopPictureAdapter;
    private PictureAdapter mFurniturePictureAdapter;
    private PictureAdapter mFactoryLocationPictureAdapter;

    private int position;
    private int addType;  //添加照片类型  0 主要产品照片   2,工厂大门照片   1,工厂地定位截图  3,身份证正面   4,身份证反面

    private Dialog loadingDialog;

    // 图片储存成功后
    protected static final int INTERCEPT = 3;

    private Gson gson;
    private String userId;
    private FkBaseResponse fkbaseResponse;

    private ImageView iv_ic_card_front;
    private ImageView iv_ic_card_back;
    private EditText et_name;  //姓名
    private EditText et_id_card;  //身份证号
    private EditText et_telephone;  //手机号
    private EditText et_expected_limit;  //期望额度
    private EditText et_organization;  //单位名称
    private EditText et_year_sales;  //年销售额
    private EditText et_number_of_employees;  //员工人数
    private EditText et_workshop_area;  //厂房具体面积
    private EditText et_main_category;  //企业主营品类
    private EditText et_bank_id;  //还款银行卡号

    private String id_card_front_pic;
    private String id_card_back_pic;
    private String name;
    private String id_card;
    private String telephone;
    private String expected_limit;
    private String organization;
    private String year_sales;
    private String number_of_employees;
    private String workshop_area;
    private String main_category;
    private String bank_id;

    private ArrayList<PhotoUpImageItem> selectImages;

    private ImageLoader imageLoader;

    private static final int SHOWIMAGE = 101;

    private int chooseType;  //选择类型  0,职业  1,现单位工作年限 2,年收入

    private PercentRelativeLayout rl_position;  //职位
    private PercentRelativeLayout rl_industry_experience;  //行业经验
    private PercentRelativeLayout rl_year_income; //年收入

    private String dictionaryTitle;
    private String typeName;  //数据字典类型

    private ArrayList<DictionaryBean> dictionaryBeanList;
    private DictionaryBean jobDictonaryBean;  //职业
    private DictionaryBean jobYearsDictonaryBean;  //现单位工作年限
    private DictionaryBean yearIncomeDictonaryBean;  //年收入
    private PopupWindow dictonaryWindow;
    private View dictonaryRoot;
    private ListView lv_choose;
    private Button btn_cancel;
    private TextView tv_title;
    private TextView tv_complete;
    private int oldPosition;

    private TextView tv_position;
    private TextView tv_industry_experience;
    private TextView tv_year_income;

    private TextView tv_support_id_card;
    private GetSupportBankListResponse getSupportBankListResponse;

    private Button btn_submit;

    private ScrollView sv_total;

    private String borrower_address_book;
    private String borrower_call_records;
    private String borrower_app_name;
    private List addressBookList;
    private List callHistoryList;
    private AlertDialog settingDialog;
    private AlertDialog personalInformationDialog;

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
        rootView = View.inflate(this, R.layout.activity_application_limit, null);
        setContentView(rootView);

        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);

        gv_add_product = (NoScrollGridView) rootView.findViewById(R.id.gv_add_product);
        gv_add_shop = (NoScrollGridView) rootView.findViewById(R.id.gv_add_shop);
        gv_factory_location = (NoScrollGridView) rootView.findViewById(R.id.gv_factory_location);

        iv_ic_card_front = (ImageView) rootView.findViewById(R.id.iv_ic_card_front);
        iv_ic_card_back = (ImageView) rootView.findViewById(R.id.iv_ic_card_back);
        rl_position = (PercentRelativeLayout) rootView.findViewById(R.id.rl_position);
        rl_industry_experience = (PercentRelativeLayout) rootView.findViewById(R.id.rl_industry_experience);
        rl_year_income = (PercentRelativeLayout) rootView.findViewById(R.id.rl_year_income);
        tv_position = (TextView) rootView.findViewById(R.id.tv_position);
        tv_industry_experience = (TextView) rootView.findViewById(R.id.tv_industry_experience);
        tv_year_income = (TextView) rootView.findViewById(R.id.tv_year_income);
        tv_support_id_card = (TextView) rootView.findViewById(R.id.tv_support_id_card);
        btn_submit = (Button) rootView.findViewById(R.id.btn_submit);
        et_name = (EditText) rootView.findViewById(R.id.et_name);
        et_id_card = (EditText) rootView.findViewById(R.id.et_id_card);
        et_telephone = (EditText) rootView.findViewById(R.id.et_telephone);
        et_expected_limit = (EditText) rootView.findViewById(R.id.et_expected_limit);
        et_organization = (EditText) rootView.findViewById(R.id.et_organization);
        et_year_sales = (EditText) rootView.findViewById(R.id.et_year_sales);
        et_number_of_employees = (EditText) rootView.findViewById(R.id.et_number_of_employees);
        et_workshop_area = (EditText) rootView.findViewById(R.id.et_workshop_area);
        et_main_category = (EditText) rootView.findViewById(R.id.et_main_category);
        et_bank_id = (EditText) rootView.findViewById(R.id.et_bank_id);
        sv_total = (ScrollView) rootView.findViewById(R.id.sv_total);


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

    private void initData() {
        gson = new Gson();
        loadingDialog = DialogUtils.createLoadDialog(ApplicationLimitActivity.this, false);

        userId = SharedPrefrenceUtils.getString(this, "usrid");

        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));

        if(!TextUtils.isEmpty(userId)){
            String messageStr = SharedPrefrenceUtils.getString(this, userId+"applicationLimit");
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
                pWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.relative),
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
                Intent intent1 = new Intent(ApplicationLimitActivity.this, ShowImageActivity.class);
                intent1.putExtra("pictureItemBean", pictureItemBean2);
                UIUtils.startActivityForResult(intent1, SHOWIMAGE);
            }
        });

        mFurnitureShopPictureAdapter.setCallBack(new PictureAdapter.ICallBack() {
            @Override
            public void addImage(int pos) {
                addType = 2;
                pWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.relative),
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
                Intent intent1 = new Intent(ApplicationLimitActivity.this, ShowImageActivity.class);
                intent1.putExtra("pictureItemBean", pictureItemBean2);
                UIUtils.startActivityForResult(intent1, SHOWIMAGE);
            }
        });
        mFactoryLocationPictureAdapter.setCallBack(new PictureAdapter.ICallBack() {
            @Override
            public void addImage(int pos) {
                addType = 1;
                pWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.relative),
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
                Intent intent1 = new Intent(ApplicationLimitActivity.this, ShowImageActivity.class);
                intent1.putExtra("pictureItemBean", pictureItemBean2);
                UIUtils.startActivityForResult(intent1, SHOWIMAGE);
            }
        });

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
                    case 0: //职业
                        jobDictonaryBean = dictionaryBeanList.get(position);
                        tv_position.setText(jobDictonaryBean.getDictionary_value_name());
                        tv_position.setTextColor(UIUtils.getColor(R.color.text_color_black));
                        oldPosition = lv_choose.getFirstVisiblePosition();
                        break;
                    case 1: //现单位工作年限
                        jobYearsDictonaryBean = dictionaryBeanList.get(position);
                        tv_industry_experience.setText(jobYearsDictonaryBean.getDictionary_value_name());
                        tv_industry_experience.setTextColor(UIUtils.getColor(R.color.text_color_black));
                        oldPosition = lv_choose.getFirstVisiblePosition();
                        break;
                    case 2: //年收入
                        yearIncomeDictonaryBean = dictionaryBeanList.get(position);
                        tv_year_income.setText(yearIncomeDictonaryBean.getDictionary_value_name());
                        tv_year_income.setTextColor(UIUtils.getColor(R.color.text_color_black));
                        oldPosition = lv_choose.getFirstVisiblePosition();
                        break;
                }
                dictonaryWindow.dismiss();
            }
        });

        personalInformationDialog = DialogUtils.showPersonalInformationDialog(this, null, this);



        tv_top_back.setOnClickListener(this);
        iv_ic_card_front.setOnClickListener(this);
        iv_ic_card_back.setOnClickListener(this);
        rl_position.setOnClickListener(this);
        rl_industry_experience.setOnClickListener(this);
        rl_year_income.setOnClickListener(this);
        tv_support_id_card.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        tv_complete.setOnClickListener(this);
    }

    private void setMessage(HashMap<String, String> hs) {
        name = hs.get("name");
        if (!TextUtils.isEmpty(name)) {
            et_name.setText(name);
        }
        id_card = hs.get("id_card");
        if (!TextUtils.isEmpty(id_card)) {
            et_id_card.setText(id_card);
        }
        telephone = hs.get("telephone");
        if (!TextUtils.isEmpty(telephone)) {
            et_telephone.setText(telephone);
        }
        expected_limit = hs.get("expected_limit");
        if (!TextUtils.isEmpty(expected_limit)) {
            et_expected_limit.setText(expected_limit);
        }
        organization = hs.get("organization");
        if (!TextUtils.isEmpty(organization)) {
            et_organization.setText(organization);
        }
        jobDictonaryBean = gson.fromJson(hs.get("jobDictonaryBean"), DictionaryBean.class);
        if (jobDictonaryBean != null) {
            tv_position.setText(jobDictonaryBean.getDictionary_value_name());
            tv_position.setTextColor(UIUtils.getColor(R.color.text_color_black));
        }
        jobYearsDictonaryBean = gson.fromJson(hs.get("jobYearsDictonaryBean"), DictionaryBean.class);
        if (jobYearsDictonaryBean != null) {
            tv_industry_experience.setText(jobYearsDictonaryBean.getDictionary_value_name());
            tv_industry_experience.setTextColor(UIUtils.getColor(R.color.text_color_black));
        }
        yearIncomeDictonaryBean = gson.fromJson(hs.get("yearIncomeDictonaryBean"), DictionaryBean.class);
        if (yearIncomeDictonaryBean != null) {
            tv_year_income.setText(yearIncomeDictonaryBean.getDictionary_value_name());
            tv_year_income.setTextColor(UIUtils.getColor(R.color.text_color_black));
        }
        year_sales = hs.get("year_sales");
        if (!TextUtils.isEmpty(year_sales)) {
            et_year_sales.setText(year_sales);
        }
        number_of_employees = hs.get("number_of_employees");
        if (!TextUtils.isEmpty(number_of_employees)) {
            et_number_of_employees.setText(number_of_employees);
        }
        workshop_area = hs.get("workshop_area");
        if (!TextUtils.isEmpty(workshop_area)) {
            et_workshop_area.setText(workshop_area);
        }
        main_category = hs.get("main_category");
        if (!TextUtils.isEmpty(main_category)) {
            et_main_category.setText(main_category);
        }
        bank_id = hs.get("bank_id");
        if (!TextUtils.isEmpty(bank_id)) {
            et_bank_id.setText(bank_id);
        }

        id_card_front_pic = hs.get("id_card_front_pic");
        if (!TextUtils.isEmpty(id_card_front_pic)) {
            imageLoader.displayImage(Constants.BaseImageUrl + id_card_front_pic, iv_ic_card_front, PictureOption.getSimpleOptions());
        }
        id_card_back_pic = hs.get("id_card_back_pic");
        if (!TextUtils.isEmpty(id_card_back_pic)) {
            imageLoader.displayImage(Constants.BaseImageUrl + id_card_back_pic, iv_ic_card_back, PictureOption.getSimpleOptions());
        }

        factoryLocationPictureItemBeanList = gson.fromJson(hs.get("factoryLocationPictureItemBeanList"), new TypeToken<List<PictureItemBean>>() {
        }.getType());
        if(factoryLocationPictureItemBeanList == null || factoryLocationPictureItemBeanList.size()==0){
            factoryLocationPictureItemBeanList = new ArrayList<PictureItemBean>();
            PictureItemBean pictureItemBean = new PictureItemBean();
            factoryLocationPictureItemBeanList.add(pictureItemBean);
        }
        mFactoryLocationPictureAdapter = new PictureAdapter(this, factoryLocationPictureItemBeanList);
        gv_factory_location.setAdapter(mFactoryLocationPictureAdapter);

        shopPictureItemBeanList = gson.fromJson(hs.get("shopPictureItemBeanList"), new TypeToken<List<PictureItemBean>>() {
        }.getType());
        if(shopPictureItemBeanList == null || shopPictureItemBeanList.size()==0){
            shopPictureItemBeanList = new ArrayList<PictureItemBean>();
            PictureItemBean pictureItemBean = new PictureItemBean();
            shopPictureItemBeanList.add(pictureItemBean);
        }
        mFurnitureShopPictureAdapter = new PictureAdapter(this, shopPictureItemBeanList);
        gv_add_shop.setAdapter(mFurnitureShopPictureAdapter);

        productPictureItemBeanList = gson.fromJson(hs.get("productPictureItemBeanList"), new TypeToken<List<PictureItemBean>>() {
        }.getType());
        if(productPictureItemBeanList == null || productPictureItemBeanList.size()==0){
            productPictureItemBeanList = new ArrayList<PictureItemBean>();
            PictureItemBean pictureItemBean = new PictureItemBean();
            productPictureItemBeanList.add(pictureItemBean);
        }
        mFurniturePictureAdapter = new PictureAdapter(this, productPictureItemBeanList);
        gv_add_product.setAdapter(mFurniturePictureAdapter);
    }

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            if (addType == 3 || addType == 4) {
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
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            onKeyDownMethod(keyCode, event);
			saveMessage();
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
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.iv_ic_card_front:
                addType = 3;
                pWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.iv_ic_card_back:
                addType = 4;
                pWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.rl_position: //职位
                dictionaryTitle = "职位";
                chooseType = 0;
                typeName = "tszd_8";
                getDictonary();
                break;
            case R.id.rl_industry_experience: //行业经验
                dictionaryTitle = "行业经验";
                chooseType = 1;
                typeName = "tszd_9";
                getDictonary();
                break;
            case R.id.rl_year_income: //年收入
                dictionaryTitle = "年收入";
                chooseType = 2;
                typeName = "tszd_10";
                getDictonary();
                break;
            case R.id.tv_support_id_card://支持银行
                getSupportBankList();
                break;
            case R.id.btn_submit: //提交申请
                submit();
//                Intent intent = new Intent(ApplicationLimitActivity.this,ApplicationLimitSuccessActivity.class);
//                UIUtils.startActivityNextAnim(intent);
//                setFinish();
                break;
            case R.id.btn_cancel: //选择取消
            case R.id.tv_complete: //选择完成
                dictonaryWindow.dismiss();
                break;
            case R.id.btn_agree:
                if(personalInformationDialog != null){
                    personalInformationDialog.dismiss();
                }
                break;
            case R.id.tv_article:
            case R.id.tv_article1:
//                if(personalInformationDialog != null){
//                    personalInformationDialog.dismiss();
//                }
                Intent intent = new Intent(this, XieYiWebActivity.class);
                intent.putExtra("article_id", Constants.PersonalInformationId);
                UIUtils.startActivityNextAnim(intent);
                break;
        }

    }

    private void submit() {
        name = et_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "请输入法人且是主要经营者的姓名");
            return;
        }
        id_card = et_id_card.getText().toString();
        if (TextUtils.isEmpty(id_card)) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "请输入您的身份证号");
            return;
        }
        telephone = et_telephone.getText().toString();
        if (TextUtils.isEmpty(telephone)) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "请输入您的手机号码");
            return;
        }
        if (!StringUtils.isMobileNO(telephone)) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "请输入正确的手机号码");
            return;
        }
        expected_limit = et_expected_limit.getText().toString();
        if (TextUtils.isEmpty(expected_limit)) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "请输入您的期望额度");
            return;
        }
        organization = et_organization.getText().toString();
        if (TextUtils.isEmpty(organization)) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "请输入现单位名称");
            return;
        }
        if (jobDictonaryBean == null) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "请选择职位");
            return;
        }
        if (jobYearsDictonaryBean == null) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "请选择行业经验年限");
            return;
        }
        if (yearIncomeDictonaryBean == null) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "请选择年收入");
            return;
        }
        year_sales = et_year_sales.getText().toString();
        if (TextUtils.isEmpty(year_sales)) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "请输入企业具体年销售额");
            return;
        }
        number_of_employees = et_number_of_employees.getText().toString();
        if (TextUtils.isEmpty(number_of_employees)) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "请输入企业具体员工人数");
            return;
        }
        workshop_area = et_workshop_area.getText().toString();
        if (TextUtils.isEmpty(workshop_area)) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "请输入厂房具体面积");
            return;
        }
        main_category = et_main_category.getText().toString();
        if (TextUtils.isEmpty(main_category)) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "请输入企业主营品类");
            return;
        }
        bank_id = et_bank_id.getText().toString();
        if (TextUtils.isEmpty(bank_id)) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "请输入还款银行卡号");
            return;
        }
        if (TextUtils.isEmpty(id_card_front_pic)) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "请上传法人身份证正面照");
            return;
        }
        if (TextUtils.isEmpty(id_card_back_pic)) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "请上传法人身份证反面照");
            return;
        }
        if (factoryLocationPictureItemBeanList.size() <= 1) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "请先上传工厂地理定位截图");
            return;
        }
        if (shopPictureItemBeanList.size() <= 1) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "请先上传工厂大门照片");
            return;
        }
        if (productPictureItemBeanList.size() <= 4) {
            UIUtils.showToastCenter(ApplicationLimitActivity.this, "主要产品及生产设备照片至少要4张");
            return;
        }

        readPhoneMessage();
        if(addressBookList== null || addressBookList.size()==0){
            settingDialog =DialogUtils.showAlertDoubleBtnDialog(ApplicationLimitActivity.this, "通讯录不能为空，是否跳转到权限设置界面去开启", new View.OnClickListener() {
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
            settingDialog =DialogUtils.showAlertDoubleBtnDialog(ApplicationLimitActivity.this, "通话记录不能为空，是否跳转到权限设置界面去开启", new View.OnClickListener() {
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

        btn_submit.setEnabled(false);
        btn_submit.setBackgroundResource(R.mipmap.btn_gray);

        setCreditInf();
    }

    /**
     * 读取手机通讯录，通话记录和应用程序
     */
    private void readPhoneMessage() {
        loadingDialog.show();
        addressBookList = AppUtils.getPhoneNumberFromMobile(ApplicationLimitActivity.this);
        if(AppUtils.getSIMContacts(ApplicationLimitActivity.this) != null ){
            addressBookList.addAll(AppUtils.getSIMContacts(ApplicationLimitActivity.this));
        }
        callHistoryList = AppUtils.getCallHistoryList(ApplicationLimitActivity.this, getContentResolver());
        borrower_address_book = gson.toJson(addressBookList);//借款人手机通讯录
        borrower_call_records = gson.toJson(callHistoryList);  //借款人通话记录

        LogUtils.e("addressBookList:"+addressBookList.toString());
        LogUtils.e("callHistoryList:"+callHistoryList.toString());
        loadingDialog.dismiss();
    }

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


    /**
     * 提交额度申请
     */
    private void setCreditInf() {
        borrower_app_name = SharedPrefrenceUtils.getString(ApplicationLimitActivity.this,"borrower_app_name");

        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/setCreditInf_3.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("usrid", userId);
        params.put("usrname", name);
        params.put("id_card", id_card);
        params.put("mobile_phone", telephone);
        params.put("expected_amount", expected_limit);

        params.put("borrower_address_book", borrower_address_book);  //借款人手机通讯录
        params.put("borrower_call_records", borrower_call_records);  //借款人通话记录
        if(!TextUtils.isEmpty(borrower_app_name)){
            params.put("borrower_app_name", borrower_app_name);  //借款人手机应用程序名称
        }

        params.put("com_name", organization);
        params.put("profession_level", jobDictonaryBean.getDictionary_value_key() + "");
        params.put("cur_hire_duration", jobYearsDictonaryBean.getDictionary_value_key() + "");
        params.put("income", yearIncomeDictonaryBean.getDictionary_value_key() + "");
        params.put("enterprise_sales", year_sales);
        params.put("factory_employee_number", number_of_employees);
        params.put("factory_area", workshop_area);
        params.put("main_category", main_category);
        params.put("bank_id", bank_id);
        params.put("id_card_front_pic", id_card_front_pic);
        params.put("id_card_reverse_pic", id_card_back_pic);

        List<UsrOrderPicBean> usrOrderPicBeanList = new ArrayList<UsrOrderPicBean>();
        for (int i = 0; i < factoryLocationPictureItemBeanList.size() - 1; i++) {
            PictureItemBean pictureItemBean = factoryLocationPictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(16);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("工厂地理位置截图");
            usrOrderPicBeanList.add(usrOrderPicBean);
        }
        params.put("factory_position_pic", gson.toJson(usrOrderPicBeanList));
        usrOrderPicBeanList.clear();
        for (int i = 0; i < shopPictureItemBeanList.size() - 1; i++) {
            PictureItemBean pictureItemBean = shopPictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(14);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("工厂大门照");
            usrOrderPicBeanList.add(usrOrderPicBean);
        }
        params.put("factory_gate_pic", gson.toJson(usrOrderPicBeanList));
        usrOrderPicBeanList.clear();
        for (int i = 0; i < productPictureItemBeanList.size() - 1; i++) {
            PictureItemBean pictureItemBean = productPictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(18);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("主营产品及有工人生产过程的照片");
            usrOrderPicBeanList.add(usrOrderPicBean);
        }
        params.put("main_category_and_production_pic", gson.toJson(usrOrderPicBeanList));

        LogUtils.e("额度申请参数:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                saveMessage();
                loadingDialog.dismiss();
                btn_submit.setEnabled(true);
                btn_submit.setBackgroundResource(R.mipmap.btn_sub);
                btn_submit.setTextColor(UIUtils.getColor(R.color.title_text));
                Log.e(TAG, "额度申请返回:" + json);
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkbaseResponse.getCode() == 0) {
                    Intent intent = new Intent(ApplicationLimitActivity.this, ApplicationLimitSuccessActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                    setFinish();
                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(ApplicationLimitActivity.this, fkbaseResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                btn_submit.setEnabled(true);
                btn_submit.setBackgroundResource(R.mipmap.btn_sub);
                btn_submit.setTextColor(UIUtils.getColor(R.color.title_text));
                Log.e(TAG, "额度申请返回错误:" + error);
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(ApplicationLimitActivity.this, error);
                }

            }
        });
    }

    /**
     * 保存申请资料
     */
    private void saveMessage() {
        HashMap<String, String> hs = new HashMap<>();

        name = et_name.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            hs.put("name", name);
        }
        id_card = et_id_card.getText().toString();
        if (!TextUtils.isEmpty(id_card)) {
            hs.put("id_card", id_card);
        }
        telephone = et_telephone.getText().toString();
        if (!TextUtils.isEmpty(telephone)) {
            hs.put("telephone", telephone);
        }
        expected_limit = et_expected_limit.getText().toString();
        if (!TextUtils.isEmpty(expected_limit)) {
            hs.put("expected_limit", expected_limit);
        }
        organization = et_organization.getText().toString();
        if (!TextUtils.isEmpty(organization)) {
            hs.put("organization", organization);
        }
        if (jobDictonaryBean != null) {
            hs.put("jobDictonaryBean", gson.toJson(jobDictonaryBean));
        }
        if (jobYearsDictonaryBean != null) {
            hs.put("jobYearsDictonaryBean", gson.toJson(jobYearsDictonaryBean));
        }
        if (yearIncomeDictonaryBean != null) {
            hs.put("yearIncomeDictonaryBean", gson.toJson(yearIncomeDictonaryBean));
        }
        year_sales = et_year_sales.getText().toString();
        if (!TextUtils.isEmpty(year_sales)) {
            hs.put("year_sales", year_sales);
        }
        number_of_employees = et_number_of_employees.getText().toString();
        if (!TextUtils.isEmpty(number_of_employees)) {
            hs.put("number_of_employees", number_of_employees);
        }
        workshop_area = et_workshop_area.getText().toString();
        if (!TextUtils.isEmpty(workshop_area)) {
            hs.put("workshop_area", workshop_area);
        }
        main_category = et_main_category.getText().toString();
        if (!TextUtils.isEmpty(main_category)) {
            hs.put("main_category", main_category);
        }
        bank_id = et_bank_id.getText().toString();
        if (!TextUtils.isEmpty(bank_id)) {
            hs.put("bank_id", bank_id);
        }

        if (!TextUtils.isEmpty(id_card_front_pic)) {
            hs.put("id_card_front_pic", id_card_front_pic);
        }
        if (!TextUtils.isEmpty(id_card_back_pic)) {
            hs.put("id_card_back_pic", id_card_back_pic);
        }

        hs.put("factoryLocationPictureItemBeanList", gson.toJson(factoryLocationPictureItemBeanList));
        hs.put("shopPictureItemBeanList", gson.toJson(shopPictureItemBeanList));
        hs.put("productPictureItemBeanList", gson.toJson(productPictureItemBeanList));

        SharedPrefrenceUtils.setString(this, userId+"applicationLimit", gson.toJson(hs));

    }

    /**
     * 从相册读取一张照片
     *
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
                    uri = BitmapUtils.getPictureUri(data, ApplicationLimitActivity.this);
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
     *
     * @param data
     */
    private void getMultiplePhoto(Intent data) {
        String selectImagesStr = data.getStringExtra("selectImages");
        if (!TextUtils.isEmpty(selectImagesStr)) {
            LogUtils.e("selectImagesStr:" + selectImagesStr);
            selectImages = gson.fromJson(selectImagesStr, new TypeToken<List<PhotoUpImageItem>>() {
            }.getType());
            if (selectImages != null && selectImages.size() > 0) {
                for (int i = 0; i < selectImages.size(); i++) {
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

                            if (i == selectImages.size() - 1) {
                                xUtilsUpload(i, true);
                            } else {
                                xUtilsUpload(i, false);
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

    private void xUtilsUpload(final int i, final boolean isLast) {
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
                                productPictureItemBeanList.add(0, pictureItemBean);
                                mFurniturePictureAdapter.notifyDataSetChanged();
                                LogUtils.e("productPictureItemBeanList:" + productPictureItemBeanList.toString());
                            } else if (addType == 2) {
                                shopPictureItemBeanList.add(0, pictureItemBean);
                                mFurnitureShopPictureAdapter.notifyDataSetChanged();
                            } else if (addType == 1) {
                                factoryLocationPictureItemBeanList.add(0, pictureItemBean);
                                mFactoryLocationPictureAdapter.notifyDataSetChanged();
                            }
                        } else {
                            if (!isFinishing()) {
                                DialogUtils.showAlertDialog(ApplicationLimitActivity.this,
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
                            DialogUtils.showAlertDialog(ApplicationLimitActivity.this,
                                    msg);
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
                        LogUtils.e("productPictureItemBeanList:" + productPictureItemBeanList.toString());
                    } else if (addType == 2) {
                        shopPictureItemBeanList.add(0, pictureItemBean);
                        mFurnitureShopPictureAdapter.notifyDataSetChanged();
                    } else if (addType == 1) {
                        factoryLocationPictureItemBeanList.add(0, pictureItemBean);
                        mFactoryLocationPictureAdapter.notifyDataSetChanged();
                    } else if (addType == 3) {
                        if (!TextUtils.isEmpty(picUrl)) {
                            id_card_front_pic = picUrl;
                            imageLoader.displayImage(Constants.BaseImageUrl + picUrl, iv_ic_card_front, PictureOption.getSimpleOptions());
                        } else {
                            if (!TextUtils.isEmpty(suoPath)) {
                                Bitmap bitmap = BitmapUtils.getSmallBitmap(suoPath);
                                iv_ic_card_front.setImageBitmap(bitmap);
                            }
                        }
                    } else if (addType == 4) {
                        if (!TextUtils.isEmpty(picUrl)) {
                            id_card_back_pic = picUrl;
                            imageLoader.displayImage(Constants.BaseImageUrl + picUrl, iv_ic_card_back, PictureOption.getSimpleOptions());
                        } else {
                            if (!TextUtils.isEmpty(suoPath)) {
                                Bitmap bitmap = BitmapUtils.getSmallBitmap(suoPath);
                                iv_ic_card_back.setImageBitmap(bitmap);
                            }
                        }
                    }
                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(ApplicationLimitActivity.this,
                                fkbaseResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {

                loadingDialog.dismiss();
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(ApplicationLimitActivity.this,
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
                    if (addType == 3 || addType == 4) {
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                IMAGE_UNSPECIFIED);
                        startActivityForResult(intent, PHOTO_ZOOM);
                    } else {
                        Intent intent = new Intent(ApplicationLimitActivity.this, ChoosePhotoFolderActivity.class);
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

    /**
     * 获取数据字典数据源
     */
    private void getDictonary() {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/getDictionaryDatasource.do";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("type_code", typeName);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("数据字典数据源参数:" + typeName);
                LogUtils.e("数据字典数据源:" + json.toString());
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkbaseResponse.getCode() == 0) {
                    HashMap<String, Objects> hashMap = gson.fromJson(gson.toJson(fkbaseResponse.getReturn_param()), HashMap.class);
                    dictionaryBeanList = StringUtils.fromJsonList(gson.toJson(hashMap.get("data_list")), DictionaryBean.class);
                    LogUtils.e("dictionaryBeanList:" + dictionaryBeanList.toString());
                    DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(ApplicationLimitActivity.this, dictionaryBeanList);
                    lv_choose.setAdapter(dictionaryAdapter);
                    if (!TextUtils.isEmpty(dictionaryTitle)) {
                        tv_title.setText(dictionaryTitle);
                    }
                    dictonaryWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.relative),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);


                    int index;
                    switch (chooseType) {  //选择类型  0,婚姻状况   1,现工作单位行业  2,职业  3,现单位工作年限  4,年收入
                        //5,联系人1关系  6,联系人2关系  7,联系人3关系  11,学历
                        case 0:
                            if (jobDictonaryBean != null) {
                                index = dictionaryBeanList.indexOf(jobDictonaryBean);
                                if (index != -1) {
                                    lv_choose.setSelection(oldPosition);
                                    dictionaryAdapter.setSelectItem(index);
                                }
                            }
                            break;
                        case 1:
                            if (jobYearsDictonaryBean != null) {
                                index = dictionaryBeanList.indexOf(jobYearsDictonaryBean);
                                if (index != -1) {
                                    lv_choose.setSelection(oldPosition);
                                    dictionaryAdapter.setSelectItem(index);
                                }
                            }
                            break;
                        case 2:
                            if (yearIncomeDictonaryBean != null) {
                                index = dictionaryBeanList.indexOf(yearIncomeDictonaryBean);
                                if (index != -1) {
                                    lv_choose.setSelection(oldPosition);
                                    dictionaryAdapter.setSelectItem(index);
                                }
                            }
                            break;
                    }


                } else if (fkbaseResponse.getCode() == 2) {
                    Intent intent = new Intent(ApplicationLimitActivity.this, LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(ApplicationLimitActivity.this,
                                fkbaseResponse.getMsg());
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(ApplicationLimitActivity.this,
                            error);
                }
            }
        });
    }

    private void getSupportBankList() {
        loadingDialog.show();
        String url = Constants.XINYONGSERVER_URL + "credit_money_background/user/getSupportBankList.do";
        HashMap<String, String> params = new HashMap<String, String>();
//        if(!TextUtils.isEmpty(amortization_money)){
//            params.put("amount",amortization_money);
//        }
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("获得银行列表:" + json.toString());
                getSupportBankListResponse = gson.fromJson(json, GetSupportBankListResponse.class);
                if (getSupportBankListResponse.getCode() == 0) {
                    List<GetSupportBankListResponse.SupportBankBean> supportBankBeanList = getSupportBankListResponse.getDataList();
                    SupportBankListDialog supportBankListDialog = new SupportBankListDialog(ApplicationLimitActivity.this, supportBankBeanList);
                    supportBankListDialog.show();

                } else if (getSupportBankListResponse.getCode() == 2) {
                    Intent intent = new Intent(ApplicationLimitActivity.this, LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(ApplicationLimitActivity.this,
                                getSupportBankListResponse.getResultText());
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(ApplicationLimitActivity.this,
                            error);
                }
            }
        });
    }
}
