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
import com.tesu.creditgold.activity.RepaymentAccountActivity;
import com.tesu.creditgold.bean.AreaBean;
import com.tesu.creditgold.response.GetUsrBankListResponse;
import com.tesu.creditgold.util.UIUtils;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class BankListAdapter extends BaseAdapter{

    private String TAG="BankListAdapter";
    private List<GetUsrBankListResponse.BankBean> bankBeanList;
    private Context mContext;
    private ChangeBank changeBank;
    public BankListAdapter(Context context, List<GetUsrBankListResponse.BankBean> bankBeanList){
        mContext=context;
        this.bankBeanList=bankBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return bankBeanList.get(arg0);
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
            view= LayoutInflater.from(mContext).inflate(R.layout.bank_list_item, null);
            vh=new ViewHolder();
            vh.tv_bank_name=(TextView)view.findViewById(R.id.tv_bank_name);
            vh.tv_bank_card=(TextView)view.findViewById(R.id.tv_bank_card);
            vh.tv_change=(TextView)view.findViewById(R.id.tv_change);
            vh.iv_bank= (ImageView) view.findViewById(R.id.iv_bank);
            view.setTag(vh);
        }else{
            vh=(ViewHolder)view.getTag();
        }
        String bankName = bankBeanList.get(pos).getBank_name();
        if(!TextUtils.isEmpty(bankName)){
            vh.tv_bank_name.setText(bankName);

            if(bankName.contains("工商")){
                vh.iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.icbc));
            }else if(bankName.contains("广发")){
                vh.iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.cgfb));
            }else if(bankName.contains("建设")){
                vh.iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.bfec));
            }else if(bankName.contains("交通")){
                vh.iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.cboc));
            }else if(bankName.contains("农商")){
                vh.iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.rcb));
            }else if(bankName.contains("农业")){
                vh.iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.alb));
            }else if(bankName.contains("平安")){
                vh.iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.pab));
            }else if(bankName.contains("邮政储蓄")){
                vh.iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.psb));
            }else if(bankName.contains("招商")){
                vh.iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.cmb));
            }else if(bankName.contains("光大")){
                vh.iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.eboc));
            }else if(bankName.contains("中国银行")){
                vh.iv_bank.setImageDrawable(UIUtils.getDrawable(R.mipmap.boc));
            }
        }
        String bankNo = bankBeanList.get(pos).getBank_no();
        if(!TextUtils.isEmpty(bankNo)){
            if(bankNo.length()<4){
                vh.tv_bank_card.setText(bankNo);
            }else {
                vh.tv_bank_card.setText(bankNo.substring(0,4)+"**********"+bankNo.substring(bankNo.length()-4));
            }
        }

        int status = bankBeanList.get(pos).getIs_modify();
        if(status == 1){
            vh.tv_change.setVisibility(View.VISIBLE);
//            vh.tv_change.setText("修改银行卡");
            vh.tv_change.setEnabled(true);
            vh.tv_change.setTextColor(mContext.getResources().getColor(R.color.default_button_color));
        }else if(status == 0){
            vh.tv_change.setVisibility(View.GONE);
        }else {
            vh.tv_change.setVisibility(View.GONE);
//            vh.tv_change.setText("处理中");
            vh.tv_change.setEnabled(false);
            vh.tv_change.setTextColor(mContext.getResources().getColor(R.color.text_color_gray));
        }

        vh.tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBank.changeBank(pos);
            }
        });


        return view;
    }

    public void setChangeListener(ChangeBank changeListener){
        changeBank = changeListener;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return bankBeanList.size();
    }

    class ViewHolder{
        public TextView tv_bank_name;
        public TextView tv_bank_card;
        public TextView tv_change;
        public ImageView iv_bank;
    }

    public interface ChangeBank{
        void changeBank(int pos);
    }
}
