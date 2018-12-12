package com.tesu.creditgold.adapter;

import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tesu.creditgold.R;
import com.tesu.creditgold.activity.ChoosePhotoActivity;
import com.tesu.creditgold.bean.PhotoUpImageItem;
import com.tesu.creditgold.util.PictureOption;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class SelectPhoteItemAdapter extends BaseAdapter{

    private List<PhotoUpImageItem> list;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;
    private ChoosePhotoActivity mContext;
    public SelectPhoteItemAdapter(List<PhotoUpImageItem> list, ChoosePhotoActivity context){
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
        // 使用DisplayImageOption.Builder()创建DisplayImageOptions

        this.mContext = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_select_photo_item, parent, false);
            holder = new Holder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_item);
            holder.delete_iv = (ImageView) convertView.findViewById(R.id.delete_iv);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        //图片加载器的使用代码，就这一句代码即可实现图片的加载。请注意
        //这里的uri地址，因为我们现在实现的是获取本地图片，所以使
        //用"file://"+图片的存储地址。如果要获取网络图片，
        //这里的uri就是图片的网络地址。
        imageLoader.displayImage("file://" + list.get(position).getImagePath(), holder.imageView, PictureOption.getRectangleOptions());

        holder.delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.deletePhoto(position);
            }
        });

        return convertView;
    }


    class Holder{
        ImageView imageView;
        ImageView delete_iv;
    }
}
