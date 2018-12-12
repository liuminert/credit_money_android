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
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.bean.AmountDetailBean;
import com.tesu.creditgold.bean.AreaBean;
import com.tesu.creditgold.util.UIUtils;

import java.text.DecimalFormat;
import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class AmountDetailAdapter extends BaseAdapter{

    private String TAG="AmountDetailAdapter";
    private List<AmountDetailBean> amountDetailBeanList;
    private Context mContext;
    private int  selectItem=-1;
    private DecimalFormat df = new DecimalFormat("###0.00");

    public AmountDetailAdapter(Context context, List<AmountDetailBean> amountDetailBeanList){
        mContext=context;
        this.amountDetailBeanList=amountDetailBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return amountDetailBeanList.get(arg0);
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
            view= LayoutInflater.from(mContext).inflate(R.layout.amount_detail_item, null);
            vh=new ViewHolder();
            vh.tv_type=(TextView)view.findViewById(R.id.tv_type);
            vh.tv_time=(TextView)view.findViewById(R.id.tv_time);
            vh.tv_amount=(TextView)view.findViewById(R.id.tv_amount);
            vh.tv_freeze_mode=(TextView)view.findViewById(R.id.tv_freeze_mode);
            view.setTag(vh);
        }else{
            vh=(ViewHolder)view.getTag();
        }

        AmountDetailBean amountDetailBean = amountDetailBeanList.get(pos);
        int freezeMode =amountDetailBean.getFreeze_mode();
        String amount = df.format(amountDetailBean.getTransaction_amount());
        switch (amountDetailBean.getTransaction_type()){
            case 1:
                vh.tv_type.setText("授信金额");
                vh.tv_amount.setText("+ "+amount);
                vh.tv_amount.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                break;
            case 2:
                vh.tv_type.setText("交易支出");
                if(freezeMode == 2){
                    vh.tv_amount.setText("+ "+amount);
                    vh.tv_amount.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                }else {
                    vh.tv_amount.setText("- "+amount);
                    vh.tv_amount.setTextColor(UIUtils.getColor(R.color.text_color_black));
                }
                break;
            case 3:
                vh.tv_type.setText("还款金额");
                vh.tv_amount.setText("+ "+amount);
                vh.tv_amount.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                break;
            case 4:
                vh.tv_type.setText("额度过期");
                vh.tv_amount.setText("- "+amount);
                vh.tv_amount.setTextColor(UIUtils.getColor(R.color.text_color_black));
                break;
        }

        if(!TextUtils.isEmpty(amountDetailBean.getTransaction_time_format())){
            vh.tv_time.setText(amountDetailBean.getTransaction_time_format());
        }
        switch (freezeMode){
            case 0:
                vh.tv_freeze_mode.setVisibility(View.GONE);
                break;
            case 1:
                vh.tv_freeze_mode.setVisibility(View.VISIBLE);
                vh.tv_freeze_mode.setText("冻结");
                break;
            case 2:
                vh.tv_freeze_mode.setVisibility(View.VISIBLE);
                vh.tv_freeze_mode.setText("解冻");
                break;
            default:
                vh.tv_freeze_mode.setVisibility(View.GONE);
                break;
        }

        return view;
    }

    public  void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return amountDetailBeanList.size();
    }

    class ViewHolder{
        public TextView tv_type;
        public TextView tv_time;
        public TextView tv_amount;
        public TextView tv_freeze_mode;
    }
}
