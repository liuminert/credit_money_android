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
import com.tesu.creditgold.response.FindTraRecordListResponse;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.UIUtils;

import java.util.HashMap;
import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class DetailsAdapter extends BaseAdapter{

    private HashMap<String,Integer> grouphashMap=new HashMap<String,Integer>();
    private String TAG="ClientListAdapter";
    private List<FindTraRecordListResponse.TraRecord> dataLists;
    private Context mContext;
    public DetailsAdapter(Context context, List<FindTraRecordListResponse.TraRecord> dataLists){
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
            view= LayoutInflater.from(mContext).inflate(R.layout.details_item, null);
            vh=new ViewHolder();
            vh.tv_time=(TextView)view.findViewById(R.id.tv_time);
            vh.tv_title=(TextView)view.findViewById(R.id.tv_title);
            vh.tv_money=(TextView)view.findViewById(R.id.tv_money);
            view.setTag(vh);
        }else{
            vh=(ViewHolder)view.getTag();
        }
        vh.tv_time.setText("" + dataLists.get(pos).time);
        switch (dataLists.get(pos).type){
            case 1:
                vh.tv_title.setText("交易收入");
                break;
            case 2:
                vh.tv_title.setText("提现");
                break;
            case 3:
                vh.tv_title.setText("交易服务费");
                break;
            case 100:
                vh.tv_title.setText("其它");
                break;
        }
        String amountStr = dataLists.get(pos).amount;
       if(amountStr.indexOf("-")>=0){
           vh.tv_money.setTextColor(UIUtils.getResources().getColor(R.color.text_color_black));
           vh.tv_money.setText("- "+ amountStr.substring(1));
       }
        else{
           vh.tv_money.setTextColor(UIUtils.getResources().getColor(R.color.default_button_color));
           vh.tv_money.setText("+ " + dataLists.get(pos).amount);
       }

        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataLists.size();
    }

    class ViewHolder{
        public TextView tv_time;
        public TextView tv_title;
        public TextView tv_money;
    }
}
