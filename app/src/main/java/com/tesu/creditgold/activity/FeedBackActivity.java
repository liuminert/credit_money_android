package com.tesu.creditgold.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.tesu.creditgold.adapter.PictureAdapter;
import com.tesu.creditgold.adapter.ProblemPictureAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.PhotoUpImageItem;
import com.tesu.creditgold.bean.PictureItemBean;
import com.tesu.creditgold.support.PercentLinearLayout;
import com.tesu.creditgold.util.BitmapUtils;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.PictureOption;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.widget.NoScrollGridView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;
    private TextView tv_business_complaint; //业务投诉
    private TextView tv_problem_feedback; //问题反馈
    private TextView tv_product_suggestion; //产品建议
    private TextView tv_other; //其他
    private EditText et_content;  //反馈内容
    private TextView tv_submit;

    private PopupWindow pWindow;
    private View root;
    private LayoutInflater mInflater;
    private String timepath;
    private static final int PHOTO_GRAPH = 1;// 拍照
    private static final int PHOTO_ZOOM = 2; // 缩放
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private File mFile;
    private String path;
    private NoScrollGridView gv_phote;
    private List<PictureItemBean> photePictureItemBeanList;  //照片list
    private ProblemPictureAdapter mPhotePictureAdapter;
    private int position;
    private Gson gson;
    private Dialog loadingDialog;
    private static final int SHOWIMAGE = 101;
    private ArrayList<PhotoUpImageItem> selectImages;
    private String userId;
    private FkBaseResponse fkbaseResponse;
    private int advice_type_id = 2;
    private String advice_text;
    private AlertDialog alertDialog;

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
        rootView = View.inflate(this, R.layout.activity_feed_back, null);
        setContentView(rootView);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_business_complaint = (TextView) rootView.findViewById(R.id.tv_business_complaint);
        tv_problem_feedback = (TextView) rootView.findViewById(R.id.tv_problem_feedback);
        tv_product_suggestion = (TextView) rootView.findViewById(R.id.tv_product_suggestion);
        tv_other = (TextView) rootView.findViewById(R.id.tv_other);
        et_content = (EditText) rootView.findViewById(R.id.et_content);
        gv_phote = (NoScrollGridView) rootView.findViewById(R.id.gv_phote);
        tv_submit = (TextView) rootView.findViewById(R.id.tv_submit);
        gv_phote = (NoScrollGridView) rootView.findViewById(R.id.gv_phote);

        initData();
        return null;
    }


    public void initData() {
        gson = new Gson();
        loadingDialog = DialogUtils.createLoadDialog(FeedBackActivity.this, false);
        userId = SharedPrefrenceUtils.getString(this, "usrid");

        photePictureItemBeanList = new ArrayList<PictureItemBean>();
        PictureItemBean pictureItemBean = new PictureItemBean();
        photePictureItemBeanList.add(pictureItemBean);
        mPhotePictureAdapter = new ProblemPictureAdapter(this, photePictureItemBeanList);
        gv_phote.setAdapter(mPhotePictureAdapter);

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

        mPhotePictureAdapter.setCallBack(new ProblemPictureAdapter.ICallBack() {
            @Override
            public void addImage(int pos) {
                position = pos;
                pWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }

            @Override
            public void deleteImage(int pos) {
                photePictureItemBeanList.remove(pos);
                mPhotePictureAdapter.notifyDataSetChanged();
            }

            @Override
            public void showImage(int pos) {
                position = pos;
                PictureItemBean pictureItemBean2 = photePictureItemBeanList.get(pos);
                Intent intent1 = new Intent(FeedBackActivity.this, ShowImageActivity.class);
                intent1.putExtra("pictureItemBean", pictureItemBean2);
                UIUtils.startActivityForResult(intent1, SHOWIMAGE);
            }
        });


        tv_top_back.setOnClickListener(this);
        tv_business_complaint.setOnClickListener(this);
        tv_problem_feedback.setOnClickListener(this);
        tv_product_suggestion.setOnClickListener(this);
        tv_other.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_GRAPH) {
            // 设置文件保存路径
            File dir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            path = dir + "/" + timepath + ".jpg";

            et_content.setFocusable(false);

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
            getMultiplePhoto(data);
        }

        if (requestCode == SHOWIMAGE && data != null) {
            photePictureItemBeanList.remove(position);
            mPhotePictureAdapter.notifyDataSetChanged();
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
                        et_content.setFocusable(true);
                        et_content.setFocusableInTouchMode(true);

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

                            photePictureItemBeanList.add(0, pictureItemBean);
                            mPhotePictureAdapter.notifyDataSetChanged();
                        } else {
                            if (!isFinishing()) {
                                DialogUtils.showAlertDialog(FeedBackActivity.this,
                                        fkbaseResponse.getMsg());
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        et_content.setFocusable(true);
                        et_content.setFocusableInTouchMode(true);

                        if (isLast) {
                            loadingDialog.dismiss();
                        }
                        if (!isFinishing()) {
                            DialogUtils.showAlertDialog(FeedBackActivity.this,
                                    msg);
                        }
                    }
                });


    }

    /**
     * 意见反馈
     */
    private void addAdviceFeedback() {
        String tell = SharedPrefrenceUtils.getString(this, "mobile_phone");
        String nick_name = SharedPrefrenceUtils.getString(this, "nick_name");
        loadingDialog.show();
        String url = Constants.XINYONGSERVER_URL + "credit_money_background/user/addAdviceFeedback.do";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("advice_usr_id", userId);
        paramMap.put("advice_phone", tell);
        if(!TextUtils.isEmpty(nick_name)){
            paramMap.put("advice_usrname", nick_name);
        }
        paramMap.put("advice_type_id", String.valueOf(advice_type_id));
        paramMap.put("advice_text", advice_text);
        List<String> pics = new ArrayList<>();
        for(int i=0;i<photePictureItemBeanList.size()-1;i++){
            PictureItemBean pictureItemBean = photePictureItemBeanList.get(i);
            pics.add(pictureItemBean.getPictureUrl());
        }
        paramMap.put("advice_pic", gson.toJson(pics));
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, paramMap, new MyVolley.VolleyCallback() {
           @Override
           public void dealWithJson(String address, String json) {
               LogUtils.e("意见反馈结果:" + json);
               loadingDialog.dismiss();
               fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
               if (fkbaseResponse.getCode() == 0) {
                   alertDialog = DialogUtils.showFeedBackDialog(FeedBackActivity.this, new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           switch (v.getId()){
                               case R.id.tv_roger:
                                   alertDialog.dismiss();
                                   et_content.setText("");
                                   photePictureItemBeanList.clear();
                                   PictureItemBean pictureItemBean = new PictureItemBean();
                                   photePictureItemBeanList.add(pictureItemBean);
                                   mPhotePictureAdapter.notifyDataSetChanged();
                                   break;
                           }
                       }
                   });
               } else {
                   if (!isFinishing()) {
                       DialogUtils.showAlertDialog(FeedBackActivity.this,
                               fkbaseResponse.getMsg());
                   }
               }

           }

           @Override
           public void dealWithError(String address, String error) {
               et_content.setFocusable(true);
               et_content.setFocusableInTouchMode(true);

               loadingDialog.dismiss();
               if (!isFinishing()) {
                   DialogUtils.showAlertDialog(FeedBackActivity.this,
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
                et_content.setFocusable(true);
                et_content.setFocusableInTouchMode(true);

                loadingDialog.dismiss();
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                LogUtils.e("baseResponse:" + json.toString());
                HashMap<String, String> hashMap = gson.fromJson(gson.toJson(fkbaseResponse.getReturn_param()), HashMap.class);
                String picUrl = hashMap.get("pic_path");
                if (fkbaseResponse.getCode() == 0) {
                    PictureItemBean pictureItemBean = new PictureItemBean();
                    pictureItemBean.setPicturePath(suoPath);
                    pictureItemBean.setPictureUrl(picUrl);

                    photePictureItemBeanList.add(0, pictureItemBean);
                    mPhotePictureAdapter.notifyDataSetChanged();
                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(FeedBackActivity.this,
                                fkbaseResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                et_content.setFocusable(true);
                et_content.setFocusableInTouchMode(true);

                loadingDialog.dismiss();
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(FeedBackActivity.this,
                            error);
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_back:
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.tv_business_complaint: //业务投诉
                tv_business_complaint.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                tv_business_complaint.setBackgroundResource(R.mipmap.opinion_frame_select);
                tv_problem_feedback.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_problem_feedback.setBackgroundResource(R.mipmap.opinion_frame_default);
                tv_product_suggestion.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_product_suggestion.setBackgroundResource(R.mipmap.opinion_frame_default);
                tv_other.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_other.setBackgroundResource(R.mipmap.opinion_frame_default);
                advice_type_id = 1;
                break;
            case R.id.tv_problem_feedback: //问题反馈
                tv_business_complaint.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_business_complaint.setBackgroundResource(R.mipmap.opinion_frame_default);
                tv_problem_feedback.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                tv_problem_feedback.setBackgroundResource(R.mipmap.opinion_frame_select);
                tv_product_suggestion.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_product_suggestion.setBackgroundResource(R.mipmap.opinion_frame_default);
                tv_other.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_other.setBackgroundResource(R.mipmap.opinion_frame_default);
                advice_type_id = 2;
                break;
            case R.id.tv_product_suggestion: //产品建议
                tv_business_complaint.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_business_complaint.setBackgroundResource(R.mipmap.opinion_frame_default);
                tv_problem_feedback.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_problem_feedback.setBackgroundResource(R.mipmap.opinion_frame_default);
                tv_product_suggestion.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                tv_product_suggestion.setBackgroundResource(R.mipmap.opinion_frame_select);
                tv_other.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_other.setBackgroundResource(R.mipmap.opinion_frame_default);
                advice_type_id = 3;
                break;
            case R.id.tv_other: //其他
                tv_business_complaint.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_business_complaint.setBackgroundResource(R.mipmap.opinion_frame_default);
                tv_problem_feedback.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_problem_feedback.setBackgroundResource(R.mipmap.opinion_frame_default);
                tv_product_suggestion.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_product_suggestion.setBackgroundResource(R.mipmap.opinion_frame_default);
                tv_other.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                tv_other.setBackgroundResource(R.mipmap.opinion_frame_select);
                advice_type_id = 4;
                break;
            case R.id.tv_submit: //提交
                advice_text = et_content.getText().toString();
                if(TextUtils.isEmpty(advice_text)){
                    UIUtils.showToastCenter(FeedBackActivity.this,"反馈内容不能为空");
                    return;
                }

                addAdviceFeedback();
                break;
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
                    Intent intent = new Intent(FeedBackActivity.this, ChoosePhotoFolderActivity.class);
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
