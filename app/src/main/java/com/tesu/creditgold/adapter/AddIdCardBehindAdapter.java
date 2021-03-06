package com.tesu.creditgold.adapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.tesu.creditgold.R;
import com.tesu.creditgold.callback.ImageCallBack;
import com.tesu.creditgold.util.DialogUtils;

import java.util.List;


public class AddIdCardBehindAdapter extends BaseAdapter {

    private String TAG = "ClientListAdapter";
    private List<Bitmap> bitmapList;
    private Context mContext;
    private PopupWindow popupWindow;
    private View view;
    private List<String> pics;
    private int num;
    private ImageCallBack imageCallBack;

    public AddIdCardBehindAdapter(Context context, List<Bitmap> bitmapList, PopupWindow popupWindow, View view, List<String> pics, int num, ImageCallBack imageCallBack) {
        mContext = context;
        this.popupWindow=popupWindow;
        this.view=view;
        this.bitmapList = bitmapList;
        this.pics=pics;
        this.num=num;
        this.imageCallBack=imageCallBack;
    }
    public void setDate( List<Bitmap> bitmapList, List<String> pics){
        this.bitmapList = bitmapList;
        this.pics=pics;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return bitmapList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int pos, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.add_image_item, null);
            vh = new ViewHolder();
            vh.iv= (ImageView) view.findViewById(R.id.iv_comment);
            vh.iv_image= (ImageView) view.findViewById(R.id.iv_image);
            vh.rl=(RelativeLayout)view.findViewById(R.id.rl);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        if(bitmapList.size()==1){
            vh.iv.setVisibility(View.VISIBLE);
            vh.iv_image.setVisibility(View.INVISIBLE);
        }
        else{
            if(pos<bitmapList.size()-1){
                vh.iv.setVisibility(View.INVISIBLE);
                vh.iv_image.setVisibility(View.VISIBLE);
                vh.iv_image.setBackgroundColor(Color.TRANSPARENT);
                vh.iv_image.setImageBitmap(bitmapList.get(pos));
                vh.iv_image.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            else{
                vh.iv.setVisibility(View.VISIBLE);
                vh.iv_image.setVisibility(View.INVISIBLE);
            }
        }
        ImageViewListener imageViewListener=new ImageViewListener(pos);
        vh.iv.setOnClickListener(imageViewListener);
        vh.iv_image.setOnClickListener(imageViewListener);
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return bitmapList.size();
    }


    class ViewHolder {
        ImageView iv;
        ImageView iv_image;
        RelativeLayout rl;
    }
    /**
     * 点击事件
     */
    @SuppressLint("NewApi")
    private class ImageViewListener implements View.OnClickListener {

        /**
         * 选择的某项
         */
        public int position = 0;
        public ImageViewListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(bitmapList.size()==1) {
                if (imageCallBack != null) {
                    imageCallBack.ImageType(4);
                }
                popupWindow.setAnimationStyle(R.style.AnimBottom);
                popupWindow.showAtLocation(view,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
            else{
                if(position==bitmapList.size()-1){
                    if (imageCallBack != null) {
                        imageCallBack.ImageType(4);
                    }
                    if (bitmapList.size() < num + 1) {
                        popupWindow.setAnimationStyle(R.style.AnimBottom);
                        popupWindow.showAtLocation(view,
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    } else if (bitmapList.size() == num + 1) {
                        DialogUtils.showAlertDialog(mContext, "最多添加" + num + "张图片！");
                    }
                }
                else{
                    if(pics.size()>0){
                        pics.remove(0);
                    }
                    if(position<bitmapList.size()-1){
                        bitmapList.remove(position);
                    }
                    notifyDataSetChanged();
                }
            }
        }
    }

}
