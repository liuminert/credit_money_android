package com.tesu.creditgold.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.bean.QuotaOrderBean;
import com.tesu.creditgold.response.GetUsrInfResponse;
import com.tesu.creditgold.util.UIUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class QuotaOrderAdapter extends BaseAdapter{

    private String TAG="ClientListAdapter";
    private List<GetUsrInfResponse.OrderBean> dataLists;
    private Context mContext;
    public QuotaOrderAdapter(Context context, List<GetUsrInfResponse.OrderBean> dataLists){
        mContext=context;
        this.dataLists=dataLists;
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
            view= LayoutInflater.from(mContext).inflate(R.layout.quota_order_item, null);
            vh=new ViewHolder();
            vh.tv_order_id=(TextView)view.findViewById(R.id.tv_order_id);
            vh.tv_product_name=(TextView)view.findViewById(R.id.tv_product_name);
            vh.tv_money=(TextView)view.findViewById(R.id.tv_money);
            vh.tv_create_time=(TextView)view.findViewById(R.id.tv_create_time);
            vh.tv_order_state=(TextView)view.findViewById(R.id.tv_order_state);
            view.setTag(vh);
        }else{
            vh=(ViewHolder)view.getTag();
        }

        GetUsrInfResponse.OrderBean quotaOrderBean= dataLists.get(pos);


        if(!TextUtils.isEmpty(quotaOrderBean.order_sn)){
            vh.tv_order_id.setText("订单号 : "+quotaOrderBean.order_sn);
        }
        if(!TextUtils.isEmpty(quotaOrderBean.wood_type_name)){
            vh.tv_product_name.setText("品名 : "+quotaOrderBean.wood_type_name);
        }
        vh.tv_money.setText("订单金额 : "+quotaOrderBean.order_money+"元");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        vh.tv_create_time.setText(format.format(new Date(quotaOrderBean.create_time)));
//        vh.tv_order_state.setText(quotaOrderBean.order_status_ch);
        if(quotaOrderBean.order_status==0){
            vh.tv_order_state.setBackgroundResource(R.mipmap.notcompleted);
            vh.tv_order_state.setText("待交易");
            vh.tv_order_state.setTextColor(UIUtils.getResources().getColor(R.color.text_color_yellow));
        }else if(quotaOrderBean.order_status == 1){
            vh.tv_order_state.setBackgroundResource(R.mipmap.unsettled);
            vh.tv_order_state.setText("待审核");
            vh.tv_order_state.setTextColor(UIUtils.getResources().getColor(R.color.text_color_blue));
        }else if(quotaOrderBean.order_status == 4){
            vh.tv_order_state.setBackgroundResource(R.color.tab_background);
            vh.tv_order_state.setText("已完成");
            vh.tv_order_state.setTextColor(UIUtils.getResources().getColor(R.color.text_red_one));
        }else if(quotaOrderBean.order_status == 2 || quotaOrderBean.order_status == 3){
            vh.tv_order_state.setBackgroundResource(R.mipmap.unsettled);
            vh.tv_order_state.setText("待结算");
            vh.tv_order_state.setTextColor(UIUtils.getResources().getColor(R.color.text_color_blue));
        }else {
            vh.tv_order_state.setBackgroundResource(R.mipmap.notcompleted);
            vh.tv_order_state.setText("审核不通过");
            vh.tv_order_state.setTextColor(UIUtils.getResources().getColor(R.color.text_color_yellow));
        }

        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataLists.size();
    }

    class ViewHolder{
        public TextView tv_order_id;
        public TextView tv_product_name;
        public TextView tv_money;
        public TextView tv_create_time;
        public TextView tv_order_state;
    }
}
