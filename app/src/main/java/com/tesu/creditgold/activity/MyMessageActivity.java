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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.fragment.ControlTabFragment;
import com.tesu.creditgold.response.ImageResponse;
import com.tesu.creditgold.support.PercentLinearLayout;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.BitmapUtils;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.MobileUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.widget.CircleImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 账户管理
 */
public class MyMessageActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;

    private PercentRelativeLayout rl_head;
    private PercentRelativeLayout rl_nick_name;
    private CircleImageView iv_head;
    private TextView tv_nick_name;
    private TextView tv_pnone;
    private PercentLinearLayout ll_main;

    private PopupWindow pWindow;
    private View root;
    private LayoutInflater mInflater;
    private String timepath;
    /**
     * 照片参数
     */
    private static final int PHOTO_GRAPH = 4;// 拍照
    private static final int PHOTO_ZOOM = 5; // 缩放
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private String path;

    private String head_portrait_pic;
    private String url;
    private Dialog loadingDialog;
    public static ControlTabFragment ctf;
    private String nick_name;

    // 图片储存成功后
    protected static final int INTERCEPT = 6;
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
        rootView = View.inflate(this, R.layout.activity_my_message, null);
        setContentView(rootView);
        rl_head= (PercentRelativeLayout) rootView.findViewById(R.id.rl_head);
        rl_nick_name= (PercentRelativeLayout) rootView.findViewById(R.id.rl_nick_name);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        iv_head = (CircleImageView) rootView.findViewById(R.id.iv_head);
        tv_nick_name = (TextView) rootView.findViewById(R.id.tv_nick_name);
        tv_pnone = (TextView) rootView.findViewById(R.id.tv_pnone);
        ll_main = (PercentLinearLayout) rootView.findViewById(R.id.ll_main);

        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }
        root = mInflater.inflate(R.layout.alert_dialog, null);
        pWindow = new PopupWindow(root, ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.FILL_PARENT);

        initData();
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_GRAPH) {
            // 设置文件保存路径
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            path = dir + "/" + timepath + ".jpg";
            File file = new File(path);
            if (file.exists()) {
                try {
                    Bitmap photo = BitmapUtils.getSmallBitmap(path);
                    if(photo != null){
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        Bitmap new_photo = BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));
                        String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                .format(new Date());
                        path = BitmapUtils.saveMyBitmap(suoName, new_photo);
                        handler.sendEmptyMessage(INTERCEPT);

                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            //通知相册刷新
            Uri uriData = Uri.parse("file://" + path);
            UIUtils.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uriData));
        }

        // 读取相册缩放图片
        if (requestCode == PHOTO_ZOOM && data != null) {
            if(data.getData()!=null){
                // 图片信息需包含在返回数据中
                String[] proj = {MediaStore.Images.Media.DATA};
                // 获取包含所需数据的Cursor对象
                @SuppressWarnings("deprecation")
                Uri uri = data.getData();
                // 获取包含所需数据的Cursor对象
                @SuppressWarnings("deprecation")
                Cursor cursor = null;
                try {
                    cursor = managedQuery(uri, proj, null, null, null);
                    if (cursor == null) {
                        uri = BitmapUtils.getPictureUri(data, MyMessageActivity.this);
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

        if(requestCode == 100 && resultCode ==200){
            nick_name = data.getStringExtra("nickname");
            if(!TextUtils.isEmpty(nick_name)){
                tv_nick_name.setText(nick_name);
                ctf.getTabMyselfbyPager().runAsyncTask();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void initData() {
        loadingDialog = DialogUtils.createLoadDialog(MyMessageActivity.this, true);

        if (ctf == null) {
            ctf = MainActivity.getCtf();
        }

        tv_pnone.setText(SharedPrefrenceUtils.getString(this, "mobile_phone"));
        Intent intent = getIntent();
        head_portrait_pic = intent.getStringExtra("head_portrait_pic");
        nick_name = intent.getStringExtra("nick_name");

        if (!TextUtils.isEmpty(head_portrait_pic)) {
            ImageLoader.getInstance().displayImage(head_portrait_pic, iv_head, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    LogUtils.e("错误空");
                    iv_head.setImageResource(R.mipmap.personal_portrait);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        } else {
            iv_head.setImageResource(R.mipmap.personal_portrait);
        }

        if(!TextUtils.isEmpty(nick_name)){
            tv_nick_name.setText(nick_name);
        }


        tv_top_back.setOnClickListener(this);
        rl_head.setOnClickListener(this);
        rl_nick_name.setOnClickListener(this);

        root.findViewById(R.id.btn_Phone).setOnClickListener(itemsOnClick);
        root.findViewById(R.id.btn_TakePicture)
                .setOnClickListener(itemsOnClick);
        root.findViewById(R.id.bg_photo).getBackground().setAlpha(100);
        root.findViewById(R.id.btn_cancel).setOnClickListener(itemsOnClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_head:
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(ll_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.rl_nick_name:
                Intent intent = new Intent(MyMessageActivity.this,NicknameActivity.class);
                UIUtils.startActivityForResult(intent,100);
                break;
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
                    timepath = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    MainActivity.timepath = timepath;
                    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                        File file = new File(dir, timepath + ".jpg");
                        UIUtils.startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                                MediaStore.EXTRA_OUTPUT, Uri.fromFile(file)), PHOTO_GRAPH);
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

    /**
     * 上传图片
     */
    private void uploadImage(final String suoPath) {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/uploadPicture.do";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("usrid", SharedPrefrenceUtils.getString(MyMessageActivity.this, "usrid"));
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
                    head_portrait_pic = imageResponse.return_param.pic_path;
                    setCreditInf();
                } else {
                    if (!TextUtils.isEmpty(imageResponse.msg)) {
                        if (!isFinishing()) {
                            DialogUtils.showAlertDialog(MyMessageActivity.this,
                                    imageResponse.msg);
                        }
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (!TextUtils.isEmpty(error)) {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(MyMessageActivity.this,
                                error);
                    }
                }
            }
        });

    }

    /**
     * 修改头像
     */
    private void setCreditInf() {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/setCreditInf_2.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("usrid", SharedPrefrenceUtils.getString(MyMessageActivity.this, "usrid"));
        params.put("head_portrait_pic", head_portrait_pic);
        LogUtils.e("params:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                FkBaseResponse fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkbaseResponse.getCode() == 0) {
//                    ctf.getTabMyselfbyPager().initData();
                    if (!TextUtils.isEmpty(head_portrait_pic)) {
                        ImageLoader.getInstance().displayImage(Constants.BaseImageUrl+head_portrait_pic, iv_head, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {
                                LogUtils.e("错误空");
                                iv_head.setImageResource(R.mipmap.personal_portrait);
                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }
                        });
                    } else {
                        iv_head.setImageResource(R.mipmap.personal_portrait);
                    }

                    ctf.getTabMyselfbyPager().runAsyncTask();
                } else {
                    if (!TextUtils.isEmpty(fkbaseResponse.getMsg())) {
                        if (!isFinishing()) {
                            DialogUtils.showAlertDialog(MyMessageActivity.this, fkbaseResponse.getMsg());
                        }
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (!TextUtils.isEmpty(error)) {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(MyMessageActivity.this, error);
                    }
                }

            }
        });
    }

}
