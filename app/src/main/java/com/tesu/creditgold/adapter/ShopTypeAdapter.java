package com.tesu.creditgold.adapter;

import android.annotation.TargetApi;
import android.content.Context;
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
import com.tesu.creditgold.bean.ShopBean;
import com.tesu.creditgold.response.GetStoreTypeResponse;
import com.tesu.creditgold.util.PictureOption;

import java.util.HashMap;
import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class ShopTypeAdapter extends BaseAdapter{

    private String TAG="ShopTypeAdapter";
    private List<GetStoreTypeResponse.StoreType> dataLists;
    private Context mContext;
    private ImageLoader imageLoader;
    public ShopTypeAdapter(Context context, List<GetStoreTypeResponse.StoreType> dataLists){
        mContext=context;
        this.dataLists=dataLists;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(mContext)));
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return dataLists.get(arg0);
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
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.shop_type_item, null);
            vh=new ViewHolder();
            vh.iv_shop_type= (ImageView) view.findViewById(R.id.iv_shop_type);
            view.setTag(vh);
        }else{
            vh=(ViewHolder)view.getTag();
        }
        GetStoreTypeResponse.StoreType shopType = dataLists.get(pos);
        if(!TextUtils.isEmpty(shopType.pic)){
            imageLoader.displayImage(shopType.pic,vh.iv_shop_type,PictureOption.getFirstOptions());
        }
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataLists.size();
    }

    class ViewHolder{
        public ImageView iv_shop_type;
    }
}
