package com.tesu.creditgold.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tesu.creditgold.R;
import com.tesu.creditgold.bean.PictureItemBean;
import com.tesu.creditgold.imagechche.ImageCache;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.BitmapUtils;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.PictureOption;

import java.util.HashMap;
import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class PictureAdapter extends BaseAdapter {

    private String TAG = "PictureAdapter";
    private List<PictureItemBean> pictureItemBeans;
    private Context mContext;
    private ICallBack callBack;
    private ImageLoader imageLoader;
//    private ImageCache manager;

    public PictureAdapter(Context context, List<PictureItemBean> pictureItemBeans) {
        mContext = context;
        this.pictureItemBeans = pictureItemBeans;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(mContext)));
//        manager = ImageCache.getInstanse(context);
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return pictureItemBeans.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int pos, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        final ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.picture_item, null);
            vh = new ViewHolder();
            vh.rl_image = (PercentRelativeLayout) view.findViewById(R.id.rl_image);
            vh.iv_picture = (ImageView) view.findViewById(R.id.iv_picture);
            vh.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
            vh.rl_add_image = (PercentRelativeLayout) view.findViewById(R.id.rl_add_image);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        if (pos == pictureItemBeans.size() - 1) {
            vh.rl_image.setVisibility(View.GONE);
            vh.rl_add_image.setVisibility(View.VISIBLE);
        } else {
            vh.rl_image.setVisibility(View.VISIBLE);
            vh.rl_add_image.setVisibility(View.GONE);
        }

        String picturePath = pictureItemBeans.get(pos).getPicturePath();
        String pictureurl = pictureItemBeans.get(pos).getPictureUrl();
            if (!TextUtils.isEmpty(pictureurl)) {
                imageLoader.displayImage(Constants.BaseImageUrl + pictureurl, vh.iv_picture, PictureOption.getSimpleOptions());
//                vh.iv_picture.setTag(pictureurl);
//                vh.iv_picture.setImageResource(R.mipmap.loadding);
//                manager.loadImages(vh.iv_picture, Constants.BaseImageUrl+pictureurl, false);
            }
            else {
                if(!TextUtils.isEmpty(picturePath)){
                    Bitmap bitmap = BitmapUtils.getSmallBitmap(picturePath);
                    vh.iv_picture.setImageBitmap(bitmap);
                }
            }

        vh.rl_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage(pos);
            }
        });

        vh.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage(pos);
            }
        });

        vh.iv_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage(pos);
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return pictureItemBeans.size();
    }

    class ViewHolder {
        public PercentRelativeLayout rl_image;
        public ImageView iv_picture;
        public ImageView iv_delete;
        public PercentRelativeLayout rl_add_image;
    }

    /**
     * 添加图片
     **/
    private void addImage(int pos) {
        callBack.addImage(pos);
    }

    /**
     * 删除图片
     **/
    private void deleteImage(int pos) {
        callBack.deleteImage(pos);
    }

    /**
     * 删除图片
     **/
    private void showImage(int pos) {
        callBack.showImage(pos);
    }

    public interface ICallBack {
        void addImage(int pos);

        void deleteImage(int pos);

        void showImage(int pos);
    }

    public void setCallBack(ICallBack callBack) {
        this.callBack = callBack;
    }
}
