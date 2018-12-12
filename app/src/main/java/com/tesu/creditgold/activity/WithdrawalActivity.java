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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.AddPictureAdapter;
import com.tesu.creditgold.adapter.ArticleListAdapter;
import com.tesu.creditgold.adapter.PictureAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.PhotoUpImageItem;
import com.tesu.creditgold.bean.PictureItemBean;
import com.tesu.creditgold.protocol.GetArticleListProtocol;
import com.tesu.creditgold.protocol.UserWithdrawalsProtocol;
import com.tesu.creditgold.request.GetArticleListRequest;
import com.tesu.creditgold.request.UserWithdrawalsRequest;
import com.tesu.creditgold.response.GetArticleListResponse;
import com.tesu.creditgold.response.UserWithdrawalsResponse;
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
import com.tesu.creditgold.widget.NoScrollGridView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
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
 * 提现
 */
public class WithdrawalActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;
    private Button btn_withrawal;
    private double store_account;
    private String store_id;
    private TextView tv;
    //提现金额
    private double amount;
    private EditText et_money;
    private String bank_name;
    private String bank_username;
    private String bank_card_number;
    private String branch_name;
    private TextView tv_card;
    private TextView tv_card_change;
    private TextView tv1;
    private int is_set_withdraw_pwd;
    private String mobile_phone;

    private PopupWindow pWindow;
    private View root;
    private LayoutInflater mInflater;
    private String timepath;
    private static final int PHOTO_GRAPH = 1;// 拍照
    private static final int PHOTO_ZOOM = 2; // 缩放
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private File mFile;
    private String path;

    private int position;

    private NoScrollGridView gv_add_pic;
    private List<PictureItemBean> pictureItemBeanList;
    private AddPictureAdapter pictureAdapter;
    private Dialog loadingDialog;
    private FkBaseResponse fkbaseResponse;
    private Gson gson;
    private String userId;

    private static final int SHOWIMAGE = 101;

    // 图片储存成功后
    protected static final int INTERCEPT = 3;

    private ImageView iv_bank;
    private TextView tv_bank_name;

    private ArrayList<PhotoUpImageItem> selectImages;

    private String suoPath;

    private ScrollView sc_one;
    private PercentRelativeLayout rl_two;

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
        rootView = View.inflate(this, R.layout.activity_withdrawal, null);
        setContentView(rootView);
        tv_card = (TextView) rootView.findViewById(R.id.tv_card);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        btn_withrawal = (Button) rootView.findViewById(R.id.btn_withrawal);
        tv = (TextView) rootView.findViewById(R.id.tv);
        tv_card_change = (TextView) rootView.findViewById(R.id.tv_card_change);
        et_money = (EditText) rootView.findViewById(R.id.et_money);
        tv1 = (TextView) rootView.findViewById(R.id.tv1);
        gv_add_pic = (NoScrollGridView) rootView.findViewById(R.id.gv_add_pic);
        iv_bank = (ImageView) rootView.findViewById(R.id.iv_bank);
        tv_bank_name = (TextView) rootView.findViewById(R.id.tv_bank_name);
        sc_one = (ScrollView) rootView.findViewById(R.id.sc_one);
        rl_two = (PercentRelativeLayout) rootView.findViewById(R.id.rl_two);

        userId = SharedPrefrenceUtils.getString(this, "usrid");

        pictureItemBeanList = new ArrayList<PictureItemBean>();
        PictureItemBean pictureItemBean = new PictureItemBean();
        pictureItemBeanList.add(pictureItemBean);
        pictureAdapter = new AddPictureAdapter(this, pictureItemBeanList);
        gv_add_pic.setAdapter(pictureAdapter);

        initData();
        return null;
    }


    public void initData() {
        Intent intent = getIntent();
        mobile_phone = intent.getStringExtra("mobile_phone");
        store_account = intent.getDoubleExtra("store_account", 0);
        is_set_withdraw_pwd = intent.getIntExtra("is_set_withdraw_pwd", 0);
        store_id = intent.getStringExtra("store_id");
        bank_card_number = intent.getStringExtra("bank_card_number");
        bank_name = intent.getStringExtra("bank_name");
        bank_username = intent.getStringExtra("bank_username");
        branch_name = intent.getStringExtra("branch_name");
        tv_top_back.setOnClickListener(this);
        btn_withrawal.setOnClickListener(this);
        tv_card_change.setOnClickListener(this);
        tv1.setOnClickListener(this);

        UIUtils.setPricePoint(et_money);
//        if(TextUtils.isEmpty(bank_card_number)&&TextUtils.isEmpty(bank_name)&&TextUtils.isEmpty(bank_username)){
//            tv_card_change.setText("添加账号");
//        }
//        else{
        if (!TextUtils.isEmpty(bank_name)) {
            tv_bank_name.setText(bank_name);

            if (bank_name.contains("工商")) {
                iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.icbc));
            } else if (bank_name.contains("广发")) {
                iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.cgfb));
            } else if (bank_name.contains("建设")) {
                iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.bfec));
            } else if (bank_name.contains("交通")) {
                iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.cboc));
            } else if (bank_name.contains("农商")) {
                iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.rcb));
            } else if (bank_name.contains("农业")) {
                iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.alb));
            } else if (bank_name.contains("平安")) {
                iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.pab));
            } else if (bank_name.contains("邮政储蓄")) {
                iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.psb));
            } else if (bank_name.contains("招商")) {
                iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.cmb));
            } else if (bank_name.contains("光大")) {
                iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.eboc));
            } else if (bank_name.contains("中国银行")) {
                iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.boc));
            }
        }
        if(!TextUtils.isEmpty(bank_card_number)){
            if (bank_card_number.length() > 4) {
                tv_card.setText(bank_card_number.substring(0, 4) + "********" + bank_card_number.substring(bank_card_number.length() - 4, bank_card_number.length()));
            } else {
                tv_card.setText(bank_card_number);
            }
        }
//        }
        tv.setText(store_account + "元");

        if(store_account == 0){
            rl_two.setVisibility(View.VISIBLE);
            sc_one.setVisibility(View.GONE);
        }else {
            rl_two.setVisibility(View.GONE);
            sc_one.setVisibility(View.VISIBLE);
        }

        gson = new Gson();
        loadingDialog = DialogUtils.createLoadDialog(WithdrawalActivity.this, false);

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

        pictureAdapter.setCallBack(new AddPictureAdapter.ICallBack() {
            @Override
            public void addImage(int pos) {
                if (pictureItemBeanList.size() >= 11) {
                    UIUtils.showToastCenter(WithdrawalActivity.this, "最多只能添加10张交易凭证");
                    return;
                }
                position = pos;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }

            @Override
            public void deleteImage(int pos) {
                pictureItemBeanList.remove(pos);
                pictureAdapter.notifyDataSetChanged();
            }

            @Override
            public void showImage(int pos) {
                position = pos;
                PictureItemBean pictureItemBean2 = pictureItemBeanList.get(pos);
                Intent intent1 = new Intent(WithdrawalActivity.this, ShowImageActivity.class);
                intent1.putExtra("pictureItemBean", pictureItemBean2);
                UIUtils.startActivityForResult(intent1, SHOWIMAGE);
            }
        });

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
//            getSinglePhoto(data);
            getMultiplePhoto(data);
        } else if (requestCode == 10) {
            if (resultCode == 1) {
                bank_card_number = data.getStringExtra("bank_card_number");
                bank_name = data.getStringExtra("bank_name");
                bank_username = data.getStringExtra("bank_username");
                tv_card.setText(bank_name + "（" + bank_card_number.substring(bank_card_number.length() - 4, bank_card_number.length()) + ")");
            }
        } else if (requestCode == 20) {
            if (resultCode == 1) {
                Intent intent = new Intent(WithdrawalActivity.this, MakeSureWithdrawalActivity.class);
                intent.putExtra("store_id", store_id);
                intent.putExtra("mobile_phone", mobile_phone);
                intent.putExtra("amount", String.valueOf(amount));
                UIUtils.startActivityNextAnim(intent);
                finish();
            }
        }

        if (requestCode == SHOWIMAGE && data != null) {
            pictureItemBeanList.remove(position);
            pictureAdapter.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, data);
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


//                            if (!photo.isRecycled()) {
//                                photo.recycle();
//                            }
//                            if (!new_photo.isRecycled()) {
//                                new_photo.recycle();
//                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private void xUtilsUpload(final int i, final boolean isLast) {
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
                            pictureItemBeanList.add(0, pictureItemBean);
                            pictureAdapter.notifyDataSetChanged();
                        } else {
                            if (!isFinishing()) {
                                DialogUtils.showAlertDialog(WithdrawalActivity.this,
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
                            DialogUtils.showAlertDialog(WithdrawalActivity.this,
                                    msg);
                        }
                    }
                });
    }

    /**
     * 从相册获取一张照片
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
                    uri = BitmapUtils.getPictureUri(data, WithdrawalActivity.this);
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


        }
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
                    pictureItemBeanList.add(0, pictureItemBean);
                    pictureAdapter.notifyDataSetChanged();
                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(WithdrawalActivity.this,
                                fkbaseResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(WithdrawalActivity.this,
                            error);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv1: {
                et_money.setText(String.valueOf(store_account));
                break;
            }
            case R.id.tv_roger: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.tv_card_change: {
                Intent intent = new Intent(WithdrawalActivity.this, AccountManagementActivity.class);
                intent.putExtra("is_set_withdraw_pwd", is_set_withdraw_pwd);
                intent.putExtra("bank_username", bank_username);
                intent.putExtra("bank_card_number", bank_card_number);
                intent.putExtra("bank_name", bank_name);
                intent.putExtra("branch_name", branch_name);
                UIUtils.startActivityForResultNextAnim(intent, 10);
                break;
            }
            case R.id.btn_withrawal: {
                if (!TextUtils.isEmpty(et_money.getText().toString())) {
                    amount = Double.parseDouble(et_money.getText().toString());
                    if (amount <= store_account) {
                        if (store_account > 0) {
                            if (amount > 0) {
                                if (is_set_withdraw_pwd == 1) {
                                    Intent intent = new Intent(WithdrawalActivity.this, MakeSureWithdrawalActivity.class);
                                    intent.putExtra("store_id", store_id);
                                    intent.putExtra("mobile_phone", mobile_phone);
                                    intent.putExtra("is_set_withdraw_pwd", is_set_withdraw_pwd);
                                    intent.putExtra("amount", String.valueOf(amount));
                                    if (pictureItemBeanList.size() <= 1) {
                                        UIUtils.showToastCenter(WithdrawalActivity.this, "至少需要添加一张交易凭证");
                                        return;
                                    }
//                                    intent.putExtra("trans_certificate_pic", gson.toJson(pictureItemBeanList));
                                    intent.putExtra("pictureItemBeanList", (Serializable) pictureItemBeanList);
                                    UIUtils.startActivityNextAnim(intent);
                                    setFinish();
                                } else {
                                    Intent intent = new Intent(WithdrawalActivity.this, SetWithdrawalPasswordActivity.class);
                                    UIUtils.startActivityForResultNextAnim(intent, 20);
                                }
                            } else {
                                DialogUtils.showAlertDialog(WithdrawalActivity.this,
                                        "提现金额不能为0！");
                            }
                        } else {
                            DialogUtils.showAlertDialog(WithdrawalActivity.this,
                                    "没有可提现金额！");
                        }
                    } else {
                        DialogUtils.showAlertDialog(WithdrawalActivity.this,
                                "超出可提现金额！");
                    }
                }
                break;
            }
            case R.id.tv_top_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
        }
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

                    Intent intent = new Intent(WithdrawalActivity.this, ChoosePhotoFolderActivity.class);
                    intent.putExtra("size",pictureItemBeanList.size()-1);
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
