package com.tesu.creditgold.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.response.FindFrostmoneyListResponse;
import com.tesu.creditgold.response.GetUsrInfResponse;
import com.tesu.creditgold.util.DateUtils;
import com.tesu.creditgold.util.UIUtils;

import java.util.HashMap;
import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class OrderAdapter extends BaseAdapter{

    private HashMap<String,Integer> grouphashMap=new HashMap<String,Integer>();
    private String TAG="ClientListAdapter";
    private List<FindFrostmoneyListResponse.FrostmoneyBean> dataLists;
    private Context mContext;
    public OrderAdapter(Context context, List<FindFrostmoneyListResponse.FrostmoneyBean> dataLists){
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
            view= LayoutInflater.from(mContext).inflate(R.layout.order_item, null);
            vh=new ViewHolder();
            vh.tv_order_num=(TextView)view.findViewById(R.id.tv_order_num);
            vh.tv_time=(TextView)view.findViewById(R.id.tv_time);
            vh.tv_name=(TextView)view.findViewById(R.id.tv_name);
            vh.tv_deal_money=(TextView)view.findViewById(R.id.tv_deal_money);
            vh.tv_entry_money=(TextView)view.findViewById(R.id.tv_entry_money);
            vh.tv=(TextView)view.findViewById(R.id.tv);
            view.setTag(vh);
        }else{
            vh=(ViewHolder)view.getTag();
        }
        vh.tv_time.setText(dataLists.get(pos).add_time);
        vh.tv_order_num.setText("交易单号:"+dataLists.get(pos).order_sn);
        vh.tv_name.setText(dataLists.get(pos).usrname+" "+dataLists.get(pos).borrower_mobile_phone);
        vh.tv_deal_money.setText("￥"+dataLists.get(pos).deal_money);
        vh.tv_entry_money.setText("￥"+dataLists.get(pos).entry_money);
        if(dataLists.get(pos).is_defrost==1){
            vh.tv.setTextColor(UIUtils.getColor(R.color.text_color_black));
            vh.tv.setText("解冻");
            vh.tv.setVisibility(View.GONE);
        }
        else{
            vh.tv.setTextColor(UIUtils.getColor(R.color.orange));
            vh.tv.setText("冻结");
            vh.tv.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataLists.size();
    }

    class ViewHolder{
        public TextView tv_order_num;
        public TextView tv_time;
        public TextView tv_name;
        TextView tv_deal_money;
        TextView tv_entry_money;
        TextView tv;
    }
}
