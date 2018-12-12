package com.tesu.creditgold.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
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
import com.tesu.creditgold.util.PictureOption;
import com.tesu.creditgold.widget.XCRoundRectImageView;

import java.util.HashMap;
import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class ShopAdapter extends BaseAdapter{

    private HashMap<String,Integer> grouphashMap=new HashMap<String,Integer>();
    private String TAG="ClientListAdapter";
    private List<ShopBean> dataLists;
    private Context mContext;
    private ImageLoader imageLoader;
    public ShopAdapter(Context context, List<ShopBean> dataLists){
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
            view= LayoutInflater.from(mContext).inflate(R.layout.shop_item, null);
            vh=new ViewHolder();
            vh.iv_shop= (XCRoundRectImageView) view.findViewById(R.id.iv_shop);
            vh.tv_shop_name=(TextView)view.findViewById(R.id.tv_shop_name);
            vh.tv_address=(TextView)view.findViewById(R.id.tv_address);
            vh.tv_tell=(TextView)view.findViewById(R.id.tv_tell);
            view.setTag(vh);
        }else{
            vh=(ViewHolder)view.getTag();
        }
        ShopBean shopBean = dataLists.get(pos);
        if(!TextUtils.isEmpty(shopBean.getStore_pic())){
            imageLoader.displayImage(shopBean.getStore_pic(), vh.iv_shop, PictureOption.getRectangleOptions());
        }
        if(!TextUtils.isEmpty(shopBean.getAddress())){
            vh.tv_address.setText(shopBean.getArea_info()+shopBean.getAddress());
        }
        if(!TextUtils.isEmpty(shopBean.getContract_telephone())){
            vh.tv_tell.setText("电话:"+shopBean.getContract_telephone());
        }
        if(!TextUtils.isEmpty(shopBean.getStore_name())){
            vh.tv_shop_name.setText(shopBean.getStore_name());
        }
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataLists.size();
    }

    class ViewHolder{
        public XCRoundRectImageView iv_shop;
        public TextView tv_shop_name;
        public TextView tv_address;
        public TextView tv_tell;
    }
}
