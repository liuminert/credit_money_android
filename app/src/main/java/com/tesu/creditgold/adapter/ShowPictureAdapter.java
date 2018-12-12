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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tesu.creditgold.R;
import com.tesu.creditgold.bean.PictureItemBean;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.BitmapUtils;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.PictureOption;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class ShowPictureAdapter extends BaseAdapter {

    private String TAG = "ShowPictureAdapter";
    private List<PictureItemBean> pictureItemBeans;
    private Context mContext;
    private ImageLoader imageLoader;
//    private ImageCache manager;

    public ShowPictureAdapter(Context context, List<PictureItemBean> pictureItemBeans) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.show_picture_item, null);
            vh = new ViewHolder();
            vh.iv_picture = (ImageView) view.findViewById(R.id.iv_picture);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        String pictureurl = pictureItemBeans.get(pos).getPicWholeUrl();
            if (!TextUtils.isEmpty(pictureurl)) {
                imageLoader.displayImage(pictureurl, vh.iv_picture, PictureOption.getSimpleOptions());
            }

        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return pictureItemBeans.size();
    }

    class ViewHolder {
        public ImageView iv_picture;
    }

}
