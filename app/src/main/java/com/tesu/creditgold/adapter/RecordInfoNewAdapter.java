package com.tesu.creditgold.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.activity.PrepaymentActivity;
import com.tesu.creditgold.response.GetRepaymentListResponse;
import com.tesu.creditgold.support.PercentLinearLayout;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.MathUtils;
import com.tesu.creditgold.util.UIUtils;

import java.util.HashMap;
import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class RecordInfoNewAdapter extends BaseAdapter{

    private HashMap<String,Integer> grouphashMap=new HashMap<String,Integer>();
    private String TAG="RecordInfoNewAdapter";
    private List<GetRepaymentListResponse.RepaymentBean> dataLists;
    private Context mContext;
    private CallBack callBack;
    public RecordInfoNewAdapter(Context context, List<GetRepaymentListResponse.RepaymentBean> dataLists){
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
            view= LayoutInflater.from(mContext).inflate(R.layout.record_info_new_item, null);
            vh=new ViewHolder();
            vh.rl_periods= (PercentRelativeLayout) view.findViewById(R.id.rl_periods);
            vh.tv_periods=(TextView)view.findViewById(R.id.tv_periods);
            vh.iv_repayment= (ImageView) view.findViewById(R.id.iv_repayment);
            vh.iv_periods= (ImageView) view.findViewById(R.id.iv_periods);
            vh.ll_repayment_details= (PercentLinearLayout) view.findViewById(R.id.ll_repayment_details);
            vh.tv_amount= (TextView) view.findViewById(R.id.tv_amount);
            vh.tv_is_repay= (TextView) view.findViewById(R.id.tv_is_repay);
            vh.tv_overdue_sale= (TextView) view.findViewById(R.id.tv_overdue_sale);
            vh.tv_return_date= (TextView) view.findViewById(R.id.tv_return_date);
            vh.rl_prepayment= (PercentRelativeLayout) view.findViewById(R.id.rl_prepayment);
            vh.tv_prepayment= (TextView) view.findViewById(R.id.tv_prepayment);
            vh.tv_already_applied= (TextView) view.findViewById(R.id.tv_already_applied);
            vh.iv_prepayment= (ImageView) view.findViewById(R.id.iv_prepayment);
            vh.tv_repay_money= (TextView) view.findViewById(R.id.tv_repay_money);
            vh.tv_repay_date= (TextView) view.findViewById(R.id.tv_repay_date);
            view.setTag(vh);
        }else{
            vh=(ViewHolder)view.getTag();
        }
        final GetRepaymentListResponse.RepaymentBean repaymentBean = dataLists.get(pos);
        String period = MathUtils.numberToChinese(repaymentBean.cur_period);

        if(!TextUtils.isEmpty(period)){
            vh.tv_periods.setText("第"+period+"期");
        }
        vh.tv_amount.setText(repaymentBean.repay_money);
        int status = repaymentBean.repay_status;
        switch (status){
            case 0:
                vh.tv_is_repay.setText("待还款");
                vh.tv_repay_date.setText("应还日期");
                vh.tv_repay_money.setText("本期应还金额");
                vh.iv_repayment.setVisibility(View.GONE);
                vh.tv_already_applied.setVisibility(View.GONE);
                vh.iv_prepayment.setVisibility(View.VISIBLE);
                if(repaymentBean.order_allot==2){
                    vh.rl_prepayment.setVisibility(View.VISIBLE);
                    vh.tv_prepayment.setTextColor(mContext.getResources().getColor(R.color.default_button_color));
                }else {
                    vh.rl_prepayment.setVisibility(View.GONE);
                }
                break;
            case 1:
            case 3:
                vh.tv_is_repay.setText("已还款");
                vh.iv_repayment.setVisibility(View.VISIBLE);
                vh.rl_prepayment.setVisibility(View.GONE);
                vh.tv_repay_money.setText("本期应还金额");
                vh.tv_repay_date.setText("还款日期");
                break;
            case 2:
                vh.tv_is_repay.setText("待还款");
                vh.iv_repayment.setVisibility(View.GONE);
                vh.rl_prepayment.setVisibility(View.VISIBLE);
                vh.tv_prepayment.setTextColor(mContext.getResources().getColor(R.color.text_color_gray));
                vh.tv_repay_money.setText("本期应还金额");
                vh.tv_already_applied.setVisibility(View.VISIBLE);
                vh.iv_prepayment.setVisibility(View.GONE);
                vh.tv_repay_date.setText("应还日期");
                break;
        }
        vh.tv_overdue_sale.setText("￥"+repaymentBean.expiredMoney);

        if(repaymentBean.isShowing){
            vh.ll_repayment_details.setVisibility(View.VISIBLE);
        }else {
            vh.ll_repayment_details.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(repaymentBean.repay_time)){
            vh.tv_return_date.setText(repaymentBean.repay_time);
        }

        vh.rl_periods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!repaymentBean.isShowing){
                    vh.iv_periods.setImageResource(R.mipmap.arrow_up);
                    changeShowing(pos, true);
                }else {
                    vh.iv_periods.setImageResource(R.mipmap.arrow_down);
                    changeShowing(pos, false);
                }
                notifyDataSetChanged();
                callBack.callback(pos);
            }
        });

        vh.rl_prepayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repaymentBean.repay_status==0){
                    Intent intent = new Intent(mContext, PrepaymentActivity.class);
                    intent.putExtra("bank_id",repaymentBean.bank_id);
                    intent.putExtra("cur_period",repaymentBean.cur_period);
                    intent.putExtra("repay_money",repaymentBean.repay_money);
                    intent.putExtra("yxt_repayment_id", repaymentBean.yxt_repayment_id);
                    UIUtils.startActivityForResult(intent,100);
                }
            }
        });
        return view;
    }

    private void changeShowing(int pos,boolean isTrue) {
        for(int i=0 ;i<dataLists.size();i++){
            if(i == pos){
                dataLists.get(i).isShowing=isTrue;
            }else {
                dataLists.get(i).isShowing=false;
            }
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataLists.size();
    }

    class ViewHolder{
        PercentRelativeLayout  rl_periods;
        TextView tv_periods;
        ImageView iv_repayment;
        ImageView iv_periods;
        PercentLinearLayout ll_repayment_details;
        TextView tv_amount;
        TextView tv_is_repay;
        TextView tv_overdue_sale;
        TextView tv_return_date;
        PercentRelativeLayout rl_prepayment;
        TextView tv_prepayment;
        TextView tv_already_applied;
        ImageView iv_prepayment;
        TextView tv_repay_money;
        TextView tv_repay_date;
    }

    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }

    public interface CallBack{
        void callback(int position);
    }
}
