package com.tesu.creditgold.adapter;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.activity.CarPackagingActivity;
import com.tesu.creditgold.activity.DigitalPackagingActivity;
import com.tesu.creditgold.activity.EducationPackagingActivity;
import com.tesu.creditgold.activity.FurniturePackagingActivity;
import com.tesu.creditgold.activity.HomePackagingActivity;
import com.tesu.creditgold.activity.IdentityAuthenticationActivity;
import com.tesu.creditgold.activity.InformationAuthenticationActivity;
import com.tesu.creditgold.activity.InstallmentPaymentActivity;
import com.tesu.creditgold.activity.LifePackagingActivity;
import com.tesu.creditgold.activity.MedicalBeautyPackagingActivity;
import com.tesu.creditgold.activity.OnlinePackagingActivity;
import com.tesu.creditgold.activity.OtherPackagingActivity;
import com.tesu.creditgold.activity.PackagingAgreementActivity;
import com.tesu.creditgold.activity.ShowLiveActivity;
import com.tesu.creditgold.activity.TourismPackagingActivity;
import com.tesu.creditgold.activity.WoodPackagingActivity;
import com.tesu.creditgold.activity.XinaWebActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.GetStoreDescByUsridResponse;
import com.tesu.creditgold.bean.ShopBean;
import com.tesu.creditgold.protocol.GetRepaymentListProtocol;
import com.tesu.creditgold.protocol.UpdateOrderInfoProtocol;
import com.tesu.creditgold.request.GetRepaymentListRequest;
import com.tesu.creditgold.response.GetRepaymentListResponse;
import com.tesu.creditgold.response.GetSupportBankListResponse;
import com.tesu.creditgold.response.GetUsrOrderListResponse;
import com.tesu.creditgold.response.UpdateOrderInfoResponse;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DateUtils;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.widget.RecordInfoDialog;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class SupportBankAdapter extends BaseAdapter {

    private String TAG = "SupportBankAdapter";
    private List<GetSupportBankListResponse.SupportBankBean> dataLists;
    private Context mContext;

    public SupportBankAdapter(Context context, List<GetSupportBankListResponse.SupportBankBean> dataLists) {
        mContext = context;
        this.dataLists = dataLists;
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
        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.support_bank_item, null);
            vh = new ViewHolder();
            vh.tv_bank_name = (TextView) view.findViewById(R.id.tv_bank_name);
            vh.tv_each_limit = (TextView) view.findViewById(R.id.tv_each_limit);
            vh.tv_daily_limit = (TextView) view.findViewById(R.id.tv_daily_limit);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        GetSupportBankListResponse.SupportBankBean supportBankBean = dataLists.get(pos);
        if(!TextUtils.isEmpty(supportBankBean.getBname())){
            vh.tv_bank_name.setText(supportBankBean.getBname());
        }
        String unit="";
        if(!TextUtils.isEmpty(supportBankBean.getUnit())){
            if(supportBankBean.getUnit().equals("1")){
                unit = "￥";
            }else if(supportBankBean.getUnit().equals("2")){
                unit = "$";
            }else {
                unit = "$";
            }
        }
        vh.tv_daily_limit.setText(unit+supportBankBean.getDailyLimit());
        vh.tv_each_limit.setText(unit+supportBankBean.getEachLimit());

        return view;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataLists.size();
    }

    class ViewHolder {
        TextView tv_bank_name;
        TextView tv_each_limit;
        TextView tv_daily_limit;
    }

}
