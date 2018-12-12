package com.tesu.creditgold.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tesu.creditgold.R;
import com.tesu.creditgold.bean.PhotoUpImageBucket;
import com.tesu.creditgold.util.PictureOption;

import java.util.ArrayList;
import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class PhoteFolderAdapter extends BaseAdapter{

    private List<PhotoUpImageBucket> arrayList;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private String TAG = PhoteFolderAdapter.class.getSimpleName();
    public PhoteFolderAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
        arrayList = new ArrayList<PhotoUpImageBucket>();//初始化集合
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCacheExtraOptions(96, 120)
                .build();
        // Initialize ImageLoader with configuration.
        imageLoader.init(config);


    };

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder vh;
        if(view==null){
            view = layoutInflater.inflate(R.layout.item_photo_folder, parent, false);
            vh=new ViewHolder();
            vh.photo_folder_name_tv = (TextView) view.findViewById(R.id.photo_folder_name_tv);
            vh.image_iv = (ImageView) view.findViewById(R.id.image_iv);
            view.setTag(vh);
        }else{
            vh=(ViewHolder)view.getTag();
        }

        imageLoader.displayImage("file://"+arrayList.get(pos).getImageList().get(0).getImagePath(),
                vh.image_iv, PictureOption.getRectangleOptions());
        vh.photo_folder_name_tv.setText(arrayList.get(pos).getBucketName()+"("+arrayList.get(pos).getImageList().size()+")");

        return view;

    }


    class ViewHolder{
        TextView photo_folder_name_tv;
        ImageView image_iv;
    }

    public void setArrayList(List<PhotoUpImageBucket> arrayList) {
        this.arrayList = arrayList;
    }
}
