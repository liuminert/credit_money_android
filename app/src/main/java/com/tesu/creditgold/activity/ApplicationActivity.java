package com.tesu.creditgold.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.AddBusinessAdapter;
import com.tesu.creditgold.adapter.AddIdCardBehindAdapter;
import com.tesu.creditgold.adapter.AddIdCardFrontAdapter;
import com.tesu.creditgold.adapter.AddImageAdapter;
import com.tesu.creditgold.adapter.AddLicencePicAdapter;
import com.tesu.creditgold.adapter.DictionaryAdapter;
import com.tesu.creditgold.adapter.RecordAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.AreaBean;
import com.tesu.creditgold.bean.DictionaryBean;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.PictureItemBean;
import com.tesu.creditgold.callback.ImageCallBack;
import com.tesu.creditgold.protocol.GetAreaListProtocol;
import com.tesu.creditgold.protocol.GetStoreTypeProtocol;
import com.tesu.creditgold.protocol.UpdateStoreInfoProtocol;
import com.tesu.creditgold.request.GetAreaListRequest;
import com.tesu.creditgold.request.GetStoreTypeRequest;
import com.tesu.creditgold.request.UpdateStoreInfoRequest;
import com.tesu.creditgold.response.GetAreaListResponse;
import com.tesu.creditgold.response.GetStoreTypeResponse;
import com.tesu.creditgold.response.ImageResponse;
import com.tesu.creditgold.response.UpdateStoreInfoResponse;
import com.tesu.creditgold.support.PercentLinearLayout;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.BitmapUtils;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.MobileUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.StringUtils;
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
import java.util.Objects;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 合作申请
 */
public class ApplicationActivity extends BaseActivity implements View.OnClickListener, ImageCallBack,View.OnTouchListener {

    //图片的类型，0代表门脸照，1代表执照,2代表场所照片，3代表身份证正面，4代表反面
    private int type;
    private TextView tv_top_back;
    private View rootView;
    private TextView tv_shop_type;
//    private TextView tv_city;
    private TextView tv_area;


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
    private LinearLayout ll_commit;
    private RelativeLayout rl_commit_success;
    private Button btn_commit;
    private PopupWindow dictonaryWindow;
    private View dictonaryRoot;
    private ListView lv_choose;
    private Button btn_cancel;
    private String url;
    //接口请求菊花
    private Dialog loadingDialog;
    private ArrayList<DictionaryBean> dictionaryBeanList;
    private int select_area_id;
    private int area_id = -1;
//    private TextView tv_province;
    private EditText et_store_name;
    private EditText et_contract_name;
    private EditText et_contract_telphone;
    private EditText et_address;
    private EditText et_description;
    private String address;
    private String description;
    private String contract_telphone;
    private String store_name;
    private String contract_name;
    private int store_type = -1;
    //0门店.1省，2市,3区
    private int choose_type;
    //判断是否选择了省
    private boolean isCheckProvince = false;
    //判断是否选择了市
    private boolean isCheckCity = false;
    //判断是否选择了区
    private boolean isCheckArea = false;

    private GetAreaListResponse getAreaListResponse;
    private int province_id = -1;
    private int city_id = -1;
    private String province;
    private String city;
    private String area;
    private CheckBox cb;
    private TextView tv_contract;
    private TextView tv2;
    private TextView tv_ceng_ruo;
    private int oldPosition;
    private AreaBean provinceBean;
    private AreaBean cityBean;
    private AreaBean areaBean;

    //身份证正面
    private ImageView iv_ic_card_front;
    private String id_card_front_str;
    private Bitmap bt_id_card_front;

    //身份证反面
    private ImageView iv_ic_card_back;
    private String id_card_back_str;
    private Bitmap bt_id_card_back;

    private TextView tv_title;
    private TextView tv_complete;
    private String dictionaryTitle;

    //门脸照
    private ImageView iv_shop_image;
    private String shop_image_str;
    private Bitmap bt_shop_image;

    //营业执照
    private ImageView iv_business_license;
    private String business_license_str;
    private Bitmap bt_business_license;

    //营业场所照
    private ImageView iv_business_image;
    private String business_image_str;
    private Bitmap bt_business_image;

    private Button btn_return;

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
        rootView = View.inflate(this, R.layout.activity_application, null);
        setContentView(rootView);
        cb = (CheckBox) rootView.findViewById(R.id.ch);
        et_address = (EditText) rootView.findViewById(R.id.et_address);
        et_contract_name = (EditText) rootView.findViewById(R.id.et_contract_name);
        et_contract_telphone = (EditText) rootView.findViewById(R.id.et_contract_telphone);
        et_description = (EditText) rootView.findViewById(R.id.et_description);
        et_store_name = (EditText) rootView.findViewById(R.id.et_store_name);
        ll_commit = (LinearLayout) rootView.findViewById(R.id.ll_commit);
        rl_commit_success = (RelativeLayout) rootView.findViewById(R.id.rl_commit_success);
        btn_commit = (Button) rootView.findViewById(R.id.btn_commit);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_shop_type = (TextView) rootView.findViewById(R.id.tv_shop_type);
//        tv_province = (TextView) rootView.findViewById(R.id.tv_province);
        tv_contract = (TextView) rootView.findViewById(R.id.tv_contract);
        tv2 = (TextView) rootView.findViewById(R.id.tv2);
//        tv_city = (TextView) rootView.findViewById(R.id.tv_city);
        tv_area = (TextView) rootView.findViewById(R.id.tv_area);
        tv_ceng_ruo = (TextView) rootView.findViewById(R.id.tv_ceng_ruo);
        iv_ic_card_front = (ImageView) rootView.findViewById(R.id.iv_ic_card_front);
        iv_ic_card_back = (ImageView) rootView.findViewById(R.id.iv_ic_card_back);
        iv_shop_image = (ImageView) rootView.findViewById(R.id.iv_shop_image);
        iv_business_license = (ImageView) rootView.findViewById(R.id.iv_business_license);
        iv_business_image = (ImageView) rootView.findViewById(R.id.iv_business_image);
        btn_return = (Button) rootView.findViewById(R.id.btn_return);
        sv_total = (ScrollView) rootView.findViewById(R.id.sv_total);
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
        dictonaryRoot = mInflater.inflate(R.layout.choose_dialog, null);
        dictonaryWindow = new PopupWindow(dictonaryRoot, ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.FILL_PARENT);
        dictonaryWindow.setFocusable(true);
        lv_choose = (ListView) dictonaryRoot.findViewById(R.id.lv_choose);
        btn_cancel = (Button) dictonaryRoot.findViewById(R.id.btn_cancel);
        tv_title = (TextView) dictonaryRoot.findViewById(R.id.tv_title);
        tv_complete = (TextView) dictonaryRoot.findViewById(R.id.tv_complete);

        tv_contract.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线

        et_description.setMovementMethod(ScrollingMovementMethod.getInstance());
        et_description.setOnTouchListener(this);

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
                        if (ContextCompat.checkSelfPermission(ApplicationActivity.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(ApplicationActivity.this,
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


    public void initData() {
        loadingDialog = DialogUtils.createLoadDialog(ApplicationActivity.this, false);
        Drawable drawable1 = getResources().getDrawable(R.mipmap.drop_down);
        drawable1.setBounds(0, 0, 40, 30);//第一0是距左边距离，第二0是距上边距离，40分别是长宽

//        tv_shop_type.setCompoundDrawables(null, null, drawable1, null);
//        tv_province.setCompoundDrawables(null, null, drawable1, null);
//        tv_city.setCompoundDrawables(null, null, drawable1, null);
//        tv_area.setCompoundDrawables(null, null, drawable1, null);
        tv2.setOnClickListener(this);
        tv_contract.setOnClickListener(this);
        tv_top_back.setOnClickListener(this);
        tv_shop_type.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
//        tv_province.setOnClickListener(this);
//        tv_city.setOnClickListener(this);
        tv_area.setOnClickListener(this);
        tv_ceng_ruo.setOnClickListener(this);
        iv_ic_card_front.setOnClickListener(this);
        iv_ic_card_back.setOnClickListener(this);
        tv_complete.setOnClickListener(this);
        iv_shop_image.setOnClickListener(this);
        iv_business_license.setOnClickListener(this);
        iv_business_image.setOnClickListener(this);
        btn_return.setOnClickListener(this);

        et_contract_telphone.setText(SharedPrefrenceUtils.getString(ApplicationActivity.this, "mobile_phone"));
        lv_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (choose_type == 0) {
                    store_type = dictionaryBeanList.get(position).getDictionary_value_key();
                    tv_shop_type.setText(dictionaryBeanList.get(position).getDictionary_value_name());
                    tv_shop_type.setTextColor(UIUtils.getColor(R.color.text_color_black));
                    oldPosition = lv_choose.getFirstVisiblePosition();
                } else if (choose_type == 1) {
                    province = dictionaryBeanList.get(position).getDictionary_value_name();
                    province_id = getAreaListResponse.dataList.get(position).area_id;
//                    tv_province.setText(dictionaryBeanList.get(position).getDictionary_value_name());
                    isCheckProvince = true;
                    isCheckCity = false;
                    isCheckArea = false;
                    area_id = -1;
                    city_id = -1;
//                    tv_city.setText("市");
                    tv_area.setText("区/县");
                    oldPosition = lv_choose.getFirstVisiblePosition();

                } else if (choose_type == 2)

                {
                    city = dictionaryBeanList.get(position).getDictionary_value_name();
                    city_id = getAreaListResponse.dataList.get(position).area_id;
//                    tv_city.setText(dictionaryBeanList.get(position).getDictionary_value_name());
                    isCheckProvince = true;
                    area_id = -1;
                    isCheckCity = true;
                    isCheckArea = false;
                    tv_area.setText("区/县");
                    oldPosition = lv_choose.getFirstVisiblePosition();
                } else

                {
                    area = dictionaryBeanList.get(position).getDictionary_value_name();
                    area_id = getAreaListResponse.dataList.get(position).area_id;
                    tv_area.setText(dictionaryBeanList.get(position).getDictionary_value_name());
                    isCheckProvince = true;
                    isCheckCity = true;
                    isCheckArea = true;
                    oldPosition = lv_choose.getFirstVisiblePosition();
                }
                dictonaryWindow.dismiss();
            }
        });
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_commit.setOnClickListener(ApplicationActivity.this);
                    btn_commit.setClickable(true);
                    btn_commit.setBackground(UIUtils.getDrawable(R.mipmap.btn_sub));
                    btn_commit.setTextColor(UIUtils.getColor(R.color.tab_background));
                } else {
                    btn_commit.setClickable(false);
                    btn_commit.setBackground(UIUtils.getDrawable(R.mipmap.btn_sub_n));
                    btn_commit.setTextColor(UIUtils.getColor(R.color.text_color_gray));
                }
            }
        });
    }

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
                            if (photo != null) {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                Bitmap new_photo = BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));
                                if (type == 0) {
                                    bt_shop_image = new_photo;
                                } else if (type == 1) {
                                    bt_business_license = new_photo;
                                } else if (type == 2) {
                                    bt_business_image = new_photo;
                                } else if (type == 3) {
                                    bt_id_card_front = new_photo;
                                } else {
                                    bt_id_card_back = new_photo;
                                }

                                String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                        .format(new Date());
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
                        uri = BitmapUtils.getPictureUri(data, ApplicationActivity.this);
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


                if (!TextUtils.isEmpty(path)) {

                    new Thread() {
                        public void run() {
                            try {
                                Bitmap photo = BitmapUtils.getSmallBitmap(path);
                                if (photo != null) {
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    Bitmap new_photo = BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));
                                    if (type == 0) {
                                        bt_shop_image = new_photo;
                                    } else if (type == 1) {
                                        bt_business_license = new_photo;
                                    } else if (type == 2) {
                                        bt_business_image = new_photo;
                                    } else if (type == 3) {
                                        bt_id_card_front = new_photo;
                                    } else {
                                        bt_id_card_back = new_photo;
                                    }

                                    String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                            .format(new Date());
                                    path = BitmapUtils.saveMyBitmap(suoName, new_photo);
                                    handler.sendEmptyMessage(INTERCEPT);

//                                    if (!photo.isRecycled()) {
//                                        photo.recycle();
//                                    }
//                                    if (!new_photo.isRecycled()) {
//                                        new_photo.recycle();
//                                    }
                                }

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }

            }
        }

        if(requestCode==110){
            if (resultCode == 200) {
                areaBean = (AreaBean) data.getSerializableExtra("areaBean");
                provinceBean = (AreaBean) data.getSerializableExtra("provinceBean");
                cityBean = (AreaBean) data.getSerializableExtra("cityBean");
                if (areaBean != null && provinceBean != null && cityBean != null) {

                    province = provinceBean.getArea_name();
                    province_id = provinceBean.getArea_id();
                    city = cityBean.getArea_name();
                    city_id = cityBean.getArea_id();
                    area = areaBean.getArea_name();
                    area_id = areaBean.getArea_id();

                    tv_area.setText(province+" " + city +" " + area);
                    tv_area.setTextColor(UIUtils.getColor(R.color.text_color_black));
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv2: {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                //url:统一资源定位符
                //uri:统一资源标示符（更广）
                intent.setData(Uri.parse("tel:4006626985"));
                //开启系统拨号器
                UIUtils.startActivity(intent);
                break;
            }
            case R.id.tv_contract: {
//                Intent intent=new Intent(ApplicationActivity.this,ContractActivity.class);
//                UIUtils.startActivityNextAnim(intent);

                Intent intent = new Intent(ApplicationActivity.this, XieYiWebActivity.class);
                intent.putExtra("article_id", 99);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.tv_ceng_ruo: {
                Intent intent = new Intent(ApplicationActivity.this, XieYiWebActivity.class);
                intent.putExtra("article_id", Constants.ShowArtcicle);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.tv_area: {
                Intent intent = new Intent(ApplicationActivity.this, AreaChooseActivity.class);
                intent.putExtra("provinceBean",provinceBean);
                intent.putExtra("cityBean",cityBean);
                intent.putExtra("areaBean",areaBean);
                UIUtils.startActivityForResult(intent, 110);
                break;
            }
            case R.id.btn_cancel:
            case R.id.tv_complete: //选择完成
            {
                switch (choose_type) {
//                    case 1:
//                        if (tv_province.getText().toString().equals("省")) {
//                            isCheckProvince = false;
//                            province_id = -1;
//                            area_id = -1;
//                            city_id = -1;
//                        }
//                        break;
//                    case 2:
//                        if (tv_city.getText().toString().equals("市")) {
//                            isCheckCity = false;
//                            area_id = -1;
//                            city_id = -1;
//                        }
//                        break;
                    case 3:
                        if (tv_area.getText().toString().equals("区/县")) {
                            isCheckArea = false;
                            area_id = -1;
                        }
                        break;
                }
                dictonaryWindow.dismiss();
                break;
            }
            case R.id.tv_shop_type: {
                dictionaryTitle = "门店类型";
                choose_type = 0;
                dictionaryBeanList = new ArrayList<>();
                GetStoreType();
                break;
            }
            case R.id.btn_commit: {
                description = et_description.getText().toString();
                store_name = et_store_name.getText().toString();
                address = et_address.getText().toString();
                contract_name = et_contract_name.getText().toString();
                contract_telphone = et_contract_telphone.getText().toString();

                if(TextUtils.isEmpty(store_name)){
                    DialogUtils.showAlertDialog(ApplicationActivity.this, "门店名称不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(contract_name)) {
                    DialogUtils.showAlertDialog(ApplicationActivity.this, "请输入联系人姓名！");
                    return;
                }
                if(TextUtils.isEmpty(contract_telphone)){
                    DialogUtils.showAlertDialog(ApplicationActivity.this, "电话号码不能为空！");
                    return;
                }
                if (!StringUtils.isMobileNO(contract_telphone)) {
                    DialogUtils.showAlertDialog(ApplicationActivity.this, "请输入正常的电话号码！");
                    return;
                }
                if (store_type == -1) {
                    DialogUtils.showAlertDialog(ApplicationActivity.this, "请选择合作门店类型！");
                    return;
                }
                if (TextUtils.isEmpty(shop_image_str)) {
                    DialogUtils.showAlertDialog(ApplicationActivity.this, "请添加一张门脸照！");
                    return;
                }
                if (TextUtils.isEmpty(business_license_str)) {
                    DialogUtils.showAlertDialog(ApplicationActivity.this, "请添加一张营业执照！");
                    return;
                }
                if(TextUtils.isEmpty(business_image_str)){
                    DialogUtils.showAlertDialog(ApplicationActivity.this, "请添加一张营业场所照！");
                    return;
                }
                if (TextUtils.isEmpty(id_card_front_str)) {
                    DialogUtils.showAlertDialog(ApplicationActivity.this, "请添加一张身份证正面照！");
                    return;
                }
                if (TextUtils.isEmpty(id_card_back_str)) {
                    DialogUtils.showAlertDialog(ApplicationActivity.this, "请添加一张身份证反面照！");
                    return;
                }

                btn_commit.setClickable(false);
                UpdateStoreInfo();
                break;
            }
            case R.id.tv_top_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.iv_ic_card_front:
                type = 3;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.ll_main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.iv_ic_card_back:
                type = 4;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.ll_main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.iv_shop_image:
                type = 0;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.ll_main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.iv_business_license:
                type = 1;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.ll_main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.iv_business_image:
                type = 2;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.ll_main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.btn_return:
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
        }
    }


    //获取门店类型
    public void GetStoreType() {
        loadingDialog.show();
        GetStoreTypeProtocol getStoreTypeProtocol = new GetStoreTypeProtocol();
        GetStoreTypeRequest getStoreTypeRequest = new GetStoreTypeRequest();
        url = Constants.XINYONGSERVER_URL + getStoreTypeProtocol.getApiFun();
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, getStoreTypeRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                GetStoreTypeResponse getStoreTypeResponse = gson.fromJson(json, GetStoreTypeResponse.class);
                if (getStoreTypeResponse.code == 0) {

                    for (int i = 0; i < getStoreTypeResponse.dataList.size(); i++) {
                        DictionaryBean dictionaryBean = new DictionaryBean();
                        dictionaryBean.setDictionary_value_key(getStoreTypeResponse.dataList.get(i).store_type_id);
                        dictionaryBean.setDictionary_value_name(getStoreTypeResponse.dataList.get(i).name);
                        dictionaryBeanList.add(dictionaryBean);
                    }
                    DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(ApplicationActivity.this, dictionaryBeanList);
                    lv_choose.setAdapter(dictionaryAdapter);
                    if (!TextUtils.isEmpty(dictionaryTitle)) {
                        tv_title.setText(dictionaryTitle);
                    }

                    dictonaryWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.ll_main),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    loadingDialog.dismiss();

                    if (choose_type == 0) {
                        int index;
                        if (store_type != -1) {
                            DictionaryBean dictionaryBean = new DictionaryBean();
                            dictionaryBean.setDictionary_value_key(store_type);
                            index = dictionaryBeanList.indexOf(dictionaryBean);
                            if (index != -1) {
                                lv_choose.setSelection(oldPosition);
                                dictionaryAdapter.setSelectItem(index);
                            }
                        }
                    }
                } else if(getStoreTypeResponse.code == 2){
                    Intent intent = new Intent(ApplicationActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {
                    loadingDialog.dismiss();
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(ApplicationActivity.this, error);
                }
            }
        });
    }

    //获取地区
    public void GetAreaList() {
        loadingDialog.show();
        GetAreaListProtocol getAreaListProtocol = new GetAreaListProtocol();
        GetAreaListRequest getAreaListRequest = new GetAreaListRequest();
        getAreaListRequest.map.put("area_id", String.valueOf(select_area_id));
        url = Constants.XINYONGSERVER_URL + getAreaListProtocol.getApiFun();
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, getAreaListRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                getAreaListResponse = gson.fromJson(json, GetAreaListResponse.class);
                if (getAreaListResponse.code == 0) {
                    for (int i = 0; i < getAreaListResponse.dataList.size(); i++) {
                        DictionaryBean dictionaryBean = new DictionaryBean();
                        dictionaryBean.setDictionary_value_key(getAreaListResponse.dataList.get(i).area_id);
                        dictionaryBean.setDictionary_value_name(getAreaListResponse.dataList.get(i).area_name);
                        dictionaryBeanList.add(dictionaryBean);
                    }
                    DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(ApplicationActivity.this, dictionaryBeanList);
                    lv_choose.setAdapter(dictionaryAdapter);
                    dictonaryWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.ll_main),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    loadingDialog.dismiss();

                    int index;
                    switch (choose_type) {
                        case 1:
                            if (province_id != -1) {
                                DictionaryBean dictionaryBean = new DictionaryBean();
                                dictionaryBean.setDictionary_value_key(province_id);
                                index = dictionaryBeanList.indexOf(dictionaryBean);
                                if (index != -1) {
                                    lv_choose.setSelection(oldPosition);
                                    dictionaryAdapter.setSelectItem(index);
                                }
                            }
                            break;
                        case 2:
                            if (city_id != -1) {
                                DictionaryBean dictionaryBean = new DictionaryBean();
                                dictionaryBean.setDictionary_value_key(city_id);
                                index = dictionaryBeanList.indexOf(dictionaryBean);
                                if (index != -1) {
                                    lv_choose.setSelection(oldPosition);
                                    dictionaryAdapter.setSelectItem(index);
                                }
                            }
                            break;
                        case 3:
                            if (area_id != -1) {
                                DictionaryBean dictionaryBean = new DictionaryBean();
                                dictionaryBean.setDictionary_value_key(area_id);
                                index = dictionaryBeanList.indexOf(dictionaryBean);
                                if (index != -1) {
                                    lv_choose.setSelection(oldPosition);
                                    dictionaryAdapter.setSelectItem(index);
                                }
                            }
                            break;

                    }
                } else if(getAreaListResponse.code == 2){
                    Intent intent = new Intent(ApplicationActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {
                    loadingDialog.dismiss();
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(ApplicationActivity.this, error);
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
        paramMap.put("usrid", SharedPrefrenceUtils.getString(ApplicationActivity.this, "usrid"));
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
                        shop_image_str=imageResponse.return_param.pic_path;
                        iv_shop_image.setImageBitmap(bt_shop_image);
                    } else if (type == 1) {
                        business_license_str=imageResponse.return_param.pic_path;
                        iv_business_license.setImageBitmap(bt_business_license);
                    } else if (type == 2) {
                        business_image_str=imageResponse.return_param.pic_path;
                        iv_business_image.setImageBitmap(bt_business_image);
                    } else if (type == 3) {
                        id_card_front_str = imageResponse.return_param.pic_path;
                        iv_ic_card_front.setImageBitmap(bt_id_card_front);
                    } else {
                        id_card_back_str = imageResponse.return_param.pic_path;
                        iv_ic_card_back.setImageBitmap(bt_id_card_back);
                    }
                } else if (imageResponse.code == 2) {
                    Intent intent = new Intent(ApplicationActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                } else {
                    if (type == 0) {
                        shop_image_str=null;
                    } else if (type == 1) {
                        business_license_str=null;
                    } else if (type == 2) {
                        business_image_str=null;
                    } else if (type == 3) {
                        id_card_front_str = null;
                    } else {
                        id_card_back_str = null;
                    }

                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(ApplicationActivity.this,
                                imageResponse.msg);
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {

                loadingDialog.dismiss();
                if (type == 0) {
                    shop_image_str=null;
                } else if (type == 1) {
                    business_license_str=null;
                } else if (type == 2) {
                    business_image_str=null;
                } else if (type == 3) {
                    id_card_front_str = null;
                } else {
                    id_card_back_str = null;
                }
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(ApplicationActivity.this,
                            error);
                }
            }
        });

    }

    @Override
    public void ImageType(int type) {
        this.type = type;
        LogUtils.e("type:" + type);
    }

    //申请合作

    /**
     * 创建用户订单并发标
     */
    private void UpdateStoreInfo() {
        loadingDialog.show();
        UpdateStoreInfoProtocol updateStoreInfoProtocol = new UpdateStoreInfoProtocol();
        UpdateStoreInfoRequest updateStoreInfoRequest = new UpdateStoreInfoRequest();
        url = Constants.XINYONGSERVER_URL + updateStoreInfoProtocol.getApiFun();
        updateStoreInfoRequest.map.put("usr_id", SharedPrefrenceUtils.getString(ApplicationActivity.this, "usrid"));
        updateStoreInfoRequest.map.put("store_pic", shop_image_str);
        updateStoreInfoRequest.map.put("store_name", store_name);
        updateStoreInfoRequest.map.put("store_type", String.valueOf(store_type));
        if (province_id > -1) {
            updateStoreInfoRequest.map.put("province_id", String.valueOf(province_id));
        }
        if (city_id > -1) {
            updateStoreInfoRequest.map.put("city_id", String.valueOf(city_id));
        }
        if (area_id > -1) {
            updateStoreInfoRequest.map.put("area_id", String.valueOf(area_id));
        }
        updateStoreInfoRequest.map.put("address", address);
        updateStoreInfoRequest.map.put("contract_name", contract_name);
        updateStoreInfoRequest.map.put("licence_pic", business_license_str);
        updateStoreInfoRequest.map.put("businesssite_pic", business_image_str);
        updateStoreInfoRequest.map.put("id_front_pic", id_card_front_str);
        updateStoreInfoRequest.map.put("id_back_pic", id_card_back_str);
        updateStoreInfoRequest.map.put("contract_telephone", contract_telphone);
        updateStoreInfoRequest.map.put("description", description);
        updateStoreInfoRequest.map.put("area_info", province + city + area);
        updateStoreInfoRequest.map.put("reg_phone", SharedPrefrenceUtils.getString(this, "mobile_phone"));

        LogUtils.e("申请门店参数:" + updateStoreInfoRequest.map.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, updateStoreInfoRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                LogUtils.e("申请结果:" + json.toString());
                UpdateStoreInfoResponse updateStoreInfoResponse = gson.fromJson(json, UpdateStoreInfoResponse.class);
                if (updateStoreInfoResponse != null) {
                    if (updateStoreInfoResponse.code == 0) {
                        ll_commit.setVisibility(View.GONE);
                        rl_commit_success.setVisibility(View.VISIBLE);
                    } else if(updateStoreInfoResponse.code == 2){
                        Intent intent = new Intent(ApplicationActivity.this,LoginActivity.class);
                        UIUtils.startActivityNextAnim(intent);
                    }
                    else {
                        if(!isFinishing()){
                            DialogUtils.showAlertDialog(ApplicationActivity.this,
                                    updateStoreInfoResponse.msg);
                        }
                    }
                }
                btn_commit.setClickable(true);
            }

            @Override
            public void dealWithError(String address, String error) {
                btn_commit.setClickable(true);
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(ApplicationActivity.this,
                            error);
                }
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if ((v.getId() == R.id.et_description && canVerticalScroll(et_description))) {
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
