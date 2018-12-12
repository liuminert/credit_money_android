package com.tesu.creditgold.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tesu.creditgold.R;
import com.tesu.creditgold.bean.PhotoUpImageItem;
import com.tesu.creditgold.util.PictureOption;

import java.util.List;
import android.os.Handler;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class PhoteItemAdapter extends BaseAdapter{

    private List<PhotoUpImageItem> list;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;
    private GridView myGridView;
    public PhoteItemAdapter(List<PhotoUpImageItem> list, Context context){
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
    }

    /**
     * 设置gridview对象
     *
     * @param gridView
     */
    public void setGridView(GridView gridView)
    {
        this.myGridView = gridView;
    }

    /**
     * update gridview 单条数据
     *
     * @param item 新数据对象
     */
    public void updateItemData(PhotoUpImageItem item)
    {
        Message msg = Message.obtain();
        // 进行数据对比获取对应数据在list中的位置
        int ids = list.indexOf(item);

        msg.arg1 = ids;
        // 更新mDataList对应位置的数据
        list.set(ids, item);
        // handle刷新界面
        han.sendMessage(msg);
    }

    @SuppressLint("HandlerLeak")
    private Handler han = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            updateItem(msg.arg1);
        };
    };

    /**
     * 刷新指定item
     *
     * @param index item在listview中的位置
     */
    private void updateItem(int index)
    {
        if (myGridView == null)
        {
            return;
        }

        // 获取当前可以看到的item位置
        int visiblePosition = myGridView.getFirstVisiblePosition();
        // 如添加headerview后 firstview就是hearderview
        // 所有索引+1 取第一个view
        // View view = listview.getChildAt(index - visiblePosition + 1);
        // 获取点击的view
        View view = myGridView.getChildAt(index-visiblePosition);
//        ImageView imageView = (ImageView) view.findViewById(R.id.image_item);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.check);
        // 获取mDataList.set(ids, item);更新的数据
        PhotoUpImageItem data = (PhotoUpImageItem) getItem(index);
        // 重新设置界面显示数据
//        imageLoader.displayImage("file://" + data.getImagePath(), imageView, PictureOption.getRectangleOptions());
        checkBox.setChecked(data.isSelected());
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_photo_item, parent, false);
            holder = new Holder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_item);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.check);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        //图片加载器的使用代码，就这一句代码即可实现图片的加载。请注意
        //这里的uri地址，因为我们现在实现的是获取本地图片，所以使
        //用"file://"+图片的存储地址。如果要获取网络图片，
        //这里的uri就是图片的网络地址。
        imageLoader.displayImage("file://" + list.get(position).getImagePath(), holder.imageView, PictureOption.getRectangleOptions());
        holder.checkBox.setChecked(list.get(position).isSelected());
        return convertView;
    }

    class Holder{
        ImageView imageView;
        CheckBox checkBox;
    }
}
