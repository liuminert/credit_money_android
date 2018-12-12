package com.tesu.creditgold.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.bean.PictureItemBean;
import com.tesu.creditgold.support.PercentLinearLayout;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.BitmapUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.PictureOption;

public class ShowImageActivity extends BaseActivity implements View.OnClickListener{
    private View rootView;
    private TextView tv_top_back;
    private PictureItemBean pictureItemBean;
    private ImageView iv_show;
    private PopupWindow pWindow;
    private View root;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    private boolean isFromInfo;
    private ImageView iv_watermark;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_show_image, null);
        setContentView(rootView);

        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        iv_show = (ImageView) rootView.findViewById(R.id.iv_show);
        iv_watermark = (ImageView) rootView.findViewById(R.id.iv_watermark);

        if (mInflater == null) {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        root = mInflater.inflate(R.layout.delete_image_dialog, null);
        pWindow = new PopupWindow(root, ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.FILL_PARENT);

        root.findViewById(R.id.btn_delete).setOnClickListener(this);
        root.findViewById(R.id.btn_cancel)
                .setOnClickListener(this);
        tv_top_back.setOnClickListener(this);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));

        Intent intent = getIntent();
        pictureItemBean = (PictureItemBean) intent.getSerializableExtra("pictureItemBean");
        LogUtils.e("pictureItemBean:"+pictureItemBean.toString());
        if(pictureItemBean != null){
            Bitmap bitmap = null;
            if(!TextUtils.isEmpty(pictureItemBean.getPicturePath())){
                bitmap = BitmapUtils.getSmallBitmap(pictureItemBean.getPicturePath());
                iv_show.setImageBitmap(bitmap);
            }else if(!TextUtils.isEmpty(pictureItemBean.getPicWholeUrl())){
                imageLoader.displayImage(pictureItemBean.getPicWholeUrl(), iv_show, PictureOption.getSimpleOptions());
            }
        }

        isFromInfo = intent.getBooleanExtra("isFromShopInof",false);

        if(!isFromInfo){
            iv_show.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    pWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.relative),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                    return false;
                }
            });
            iv_watermark.setVisibility(View.GONE);
        }else {
            iv_watermark.setVisibility(View.VISIBLE);
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_top_back:
                finish();
                break;
            case R.id.btn_delete:
                Intent intent = getIntent();
                setResult(200,intent);
                finish();
                break;
            case R.id.btn_cancel:
                pWindow.dismiss();
                break;
        }

    }
}
