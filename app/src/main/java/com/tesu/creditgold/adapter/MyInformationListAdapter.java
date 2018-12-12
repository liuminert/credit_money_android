package com.tesu.creditgold.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.bean.AreaBean;
import com.tesu.creditgold.bean.MyInformationBean;
import com.tesu.creditgold.support.PercentRelativeLayout;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class MyInformationListAdapter extends BaseAdapter{

    private String TAG="MyInformationListAdapter";
    private List<MyInformationBean> myInformationBeanList;
    private Context mContext;
    private int  selectItem=-1;
    public MyInformationListAdapter(Context context, List<MyInformationBean> myInformationBeanList){
        mContext=context;
        this.myInformationBeanList=myInformationBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return myInformationBeanList.get(arg0);
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
            view= LayoutInflater.from(mContext).inflate(R.layout.my_information_list_item, null);
            vh=new ViewHolder();
            vh.iv_unread= (ImageView) view.findViewById(R.id.iv_unread);
            vh.tv_information_title= (TextView) view.findViewById(R.id.tv_information_title);
            vh.tv_send_time= (TextView) view.findViewById(R.id.tv_send_time);
            vh.tv_information_content= (TextView) view.findViewById(R.id.tv_information_content);
            vh.rl_total= (PercentRelativeLayout) view.findViewById(R.id.rl_total);
            view.setTag(vh);
        }else{
            vh=(ViewHolder)view.getTag();
        }

//        int ps = pos%2;
//        if(ps==0){
//            vh.rl_total.setBackgroundResource(R.color.tab_background);
//        }else{
//            vh.rl_total.setBackgroundResource(R.color.GrayBackground);
//        }

        MyInformationBean myInformationBean = myInformationBeanList.get(pos);
        if(myInformationBean.getRead_flag() == 0){
            vh.iv_unread.setVisibility(View.VISIBLE);
            vh.rl_total.setBackgroundResource(R.color.tab_background);
        }else {
            vh.iv_unread.setVisibility(View.GONE);
            vh.rl_total.setBackgroundResource(R.color.GrayBackground);
        }
        if(!TextUtils.isEmpty(myInformationBean.getMsg_title())){
            vh.tv_information_title.setText(myInformationBean.getMsg_title());
        }
        if(!TextUtils.isEmpty(myInformationBean.getSend_msg())){
            vh.tv_information_content.setText(myInformationBean.getSend_msg());
        }
        if(!TextUtils.isEmpty(myInformationBean.getSend_time_string())){
            vh.tv_send_time.setText(myInformationBean.getSend_time_string());
        }

        return view;
    }

    public  void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return myInformationBeanList.size();
    }

    class ViewHolder{
        private ImageView iv_unread;
        private TextView tv_information_title;
        private TextView tv_send_time;
        private TextView tv_information_content;
        private PercentRelativeLayout rl_total;
    }
}
