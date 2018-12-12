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
import com.tesu.creditgold.response.GetRepaymentListResponse;

import java.util.HashMap;
import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class RecordInfoAdapter extends BaseAdapter{

    private HashMap<String,Integer> grouphashMap=new HashMap<String,Integer>();
    private String TAG="ClientListAdapter";
    private List<GetRepaymentListResponse.RepaymentBean> dataLists;
    private Context mContext;
    public RecordInfoAdapter(Context context, List<GetRepaymentListResponse.RepaymentBean> dataLists){
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
            view= LayoutInflater.from(mContext).inflate(R.layout.record_info_item, null);
            vh=new ViewHolder();
            vh.tv_state=(TextView)view.findViewById(R.id.tv_state);
            vh.tv_money=(TextView)view.findViewById(R.id.tv_money);
            vh.tv_time=(TextView)view.findViewById(R.id.tv_time);
            vh.tv_expiredMoney=(TextView)view.findViewById(R.id.tv_expiredMoney);
            vh.tv_cur_period=(TextView)view.findViewById(R.id.tv_cur_period);
            view.setTag(vh);
        }else{
            vh=(ViewHolder)view.getTag();
        }
        int status = dataLists.get(pos).repay_status;
        switch (status){
            case 0:
                vh.tv_state.setText("未还款");
                break;
            case 1:
            case 3:
                vh.tv_state.setText("已还款");
                break;
            case 2:
                vh.tv_state.setText("还款中");
                break;
        }
        vh.tv_expiredMoney.setText(dataLists.get(pos).expiredMoney);
        vh.tv_money.setText(dataLists.get(pos).repay_money);
        vh.tv_time.setText(dataLists.get(pos).repay_time);
        vh.tv_cur_period.setText(String.valueOf(dataLists.get(pos).cur_period));
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataLists.size();
    }

    class ViewHolder{
        public TextView tv_state;
        public TextView tv_money;
        public TextView tv_time;
        public TextView tv_cur_period;
        public TextView tv_expiredMoney;
    }
}
