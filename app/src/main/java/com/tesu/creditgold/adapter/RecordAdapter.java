package com.tesu.creditgold.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
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
import com.tesu.creditgold.activity.ContinueOrderActivity;
import com.tesu.creditgold.activity.DigitalPackagingActivity;
import com.tesu.creditgold.activity.EducationPackagingActivity;
import com.tesu.creditgold.activity.FurniturePackagingActivity;
import com.tesu.creditgold.activity.HomePackagingActivity;
import com.tesu.creditgold.activity.IdentityAuthenticationActivity;
import com.tesu.creditgold.activity.InformationAuthenticationActivity;
import com.tesu.creditgold.activity.InstallmentPaymentActivity;
import com.tesu.creditgold.activity.InstallmentRecordActivity;
import com.tesu.creditgold.activity.LifePackagingActivity;
import com.tesu.creditgold.activity.MedicalBeautyPackagingActivity;
import com.tesu.creditgold.activity.OnlinePackagingActivity;
import com.tesu.creditgold.activity.OtherPackagingActivity;
import com.tesu.creditgold.activity.PackagingAgreementActivity;
import com.tesu.creditgold.activity.RecodeDetailActivity;
import com.tesu.creditgold.activity.ShowLiveActivity;
import com.tesu.creditgold.activity.TourismPackagingActivity;
import com.tesu.creditgold.activity.UseQuotaFillInfomationActivity;
import com.tesu.creditgold.activity.WoodPackagingActivity;
import com.tesu.creditgold.activity.XieYiWebActivity;
import com.tesu.creditgold.activity.XinaWebActivity;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.GetStoreDescByUsridResponse;
import com.tesu.creditgold.bean.GetStoreInfoResponse;
import com.tesu.creditgold.bean.ShopBean;
import com.tesu.creditgold.protocol.GetRepaymentListProtocol;
import com.tesu.creditgold.protocol.UpdateOrderInfoProtocol;
import com.tesu.creditgold.request.GetRepaymentListRequest;
import com.tesu.creditgold.response.GetRepaymentListResponse;
import com.tesu.creditgold.response.GetUsrInfResponse;
import com.tesu.creditgold.response.GetUsrOrderListResponse;
import com.tesu.creditgold.response.UpdateOrderInfoResponse;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DateUtils;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.widget.RecordInfoDialog;

import java.io.Serializable;
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
public class RecordAdapter extends BaseAdapter {

    private HashMap<String, Integer> grouphashMap = new HashMap<String, Integer>();
    private String TAG = "ClientListAdapter";
    private List<GetUsrOrderListResponse.OrderBean> dataLists;
    private Context mContext;
    private String order_id;
    private Dialog loadingDialog;
    private FkBaseResponse fkbaseResponse;
    private Gson gson;
    private GetStoreDescByUsridResponse getStoreDescByUsridResponse;
    private ShopBean shopBean;
    private AlertDialog  showOrderCheckDialog;
    private AlertDialog isDeleteDialog;
    private Dialog showRepayInstructionDialog;

    public RecordAdapter(Context context, List<GetUsrOrderListResponse.OrderBean> dataLists,Dialog loadingDialog) {
        mContext = context;
        this.dataLists = dataLists;
        this.loadingDialog=loadingDialog;
        gson = new Gson();
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
            view = LayoutInflater.from(mContext).inflate(R.layout.installment_record_item, null);
            vh = new ViewHolder();
            vh.ll_order_main=(LinearLayout)view.findViewById(R.id.ll_order_main);
//            vh.rl_operation=(RelativeLayout)view.findViewById(R.id.rl_operation);
            vh.tv_del = (TextView) view.findViewById(R.id.tv_del);
            vh.tv_operation = (TextView) view.findViewById(R.id.tv_operation);
//            vh.rl_contract=(RelativeLayout)view.findViewById(R.id.rl_contract);
            vh.tv_info = (TextView) view.findViewById(R.id.tv_info);
            vh.tv_look = (TextView) view.findViewById(R.id.tv_look);
//            vh.rl_repayment=(RelativeLayout)view.findViewById(R.id.rl_repayment);
            vh.tv_state = (TextView) view.findViewById(R.id.tv_state);
            vh.tv_money = (TextView) view.findViewById(R.id.tv_money);
            vh.tv_time = (TextView) view.findViewById(R.id.tv_time);
            vh.tv_type = (TextView) view.findViewById(R.id.tv_type);
            vh.tv_order_num = (TextView) view.findViewById(R.id.tv_order_num);
            vh.tv_repay_instructions = (TextView) view.findViewById(R.id.tv_repay_instructions);
            vh.rl_freeze_money = (RelativeLayout) view.findViewById(R.id.rl_freeze_money);
            vh.tv_freeze_money = (TextView) view.findViewById(R.id.tv_freeze_money);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        LogUtils.e("dataLists.get(pos).is_show_flag:"+dataLists.get(pos).is_show_flag+"  order_sn:"+dataLists.get(pos).order_sn);
        if(dataLists.get(pos).order_from == 1){
            vh.rl_freeze_money.setVisibility(View.VISIBLE);
            vh.tv_freeze_money.setText(dataLists.get(pos).freeze_loan_quota+"");
        }else {
            vh.rl_freeze_money.setVisibility(View.GONE);
        }

             /*订单状态，0：资料提交中
        1：待审核，2：待还款，3：还款中，4：还款完成*/
            switch (dataLists.get(pos).order_status) {
                case 0: {
                    vh.tv_look.setVisibility(View.GONE);
                    vh.tv_info.setVisibility(View.GONE);
                    vh.tv_state.setText("待提交");
//                    vh.rl_operation.setVisibility(View.VISIBLE);
                    vh.tv_del.setVisibility(View.GONE);
                    vh.tv_operation.setVisibility(View.VISIBLE);
                    vh.tv_operation.setText("继续提交");
                    vh.tv_repay_instructions.setVisibility(View.GONE);
                    break;
                }
                case 1: {
                    vh.tv_look.setVisibility(View.VISIBLE);
                    vh.tv_info.setVisibility(View.GONE);
                    vh.tv_state.setText("待审核");
//                    vh.rl_operation.setVisibility(View.GONE);
                    vh.tv_operation.setVisibility(View.GONE);
                    vh.tv_del.setVisibility(View.GONE);
                    vh.tv_repay_instructions.setVisibility(View.GONE);
                    break;
                }
                case 2: {
                    vh.tv_look.setVisibility(View.VISIBLE);
                    vh.tv_info.setVisibility(View.VISIBLE);
                    vh.tv_state.setText("待还款");
//                    vh.rl_operation.setVisibility(View.GONE);
                    vh.tv_operation.setVisibility(View.GONE);
                    vh.tv_del.setVisibility(View.GONE);
                    vh.tv_repay_instructions.setVisibility(View.VISIBLE);
                    break;
                }
                case 3: {
                    vh.tv_look.setVisibility(View.VISIBLE);
                    vh.tv_info.setVisibility(View.VISIBLE);
                    vh.tv_state.setText("还款中");
//                    vh.rl_operation.setVisibility(View.GONE);
                    vh.tv_operation.setVisibility(View.GONE);
                    vh.tv_del.setVisibility(View.GONE);
                    vh.tv_repay_instructions.setVisibility(View.VISIBLE);
                    break;
                }
                case 4: {
                    vh.tv_look.setVisibility(View.VISIBLE);
                    vh.tv_info.setVisibility(View.VISIBLE);
                    vh.tv_state.setText("已还款");
//                    vh.rl_operation.setVisibility(View.VISIBLE);
                    vh.tv_del.setVisibility(View.VISIBLE);
                    vh.tv_operation.setVisibility(View.GONE);
                    vh.tv_repay_instructions.setVisibility(View.GONE);
                    break;
                }
                case 5: {
                    vh.tv_look.setVisibility(View.VISIBLE);
                    vh.tv_info.setVisibility(View.GONE);
                    vh.tv_state.setText("初审不通过");
//                    vh.rl_operation.setVisibility(View.GONE);
                    vh.tv_operation.setVisibility(View.GONE);
                    vh.tv_del.setVisibility(View.GONE);
                    vh.tv_repay_instructions.setVisibility(View.GONE);
                    break;
                }
                case 6: {
                    vh.tv_look.setVisibility(View.VISIBLE);
                    vh.tv_info.setVisibility(View.GONE);
                    vh.tv_state.setText("复审不通过");
//                    vh.rl_operation.setVisibility(View.GONE);
                    vh.tv_operation.setVisibility(View.GONE);
                    vh.tv_del.setVisibility(View.GONE);
                    vh.tv_repay_instructions.setVisibility(View.GONE);
                    break;
                }
                case 7:{
                    vh.tv_look.setVisibility(View.GONE);
                    vh.tv_info.setVisibility(View.GONE);
//                    vh.tv_state.setText("超时关闭");
                    vh.tv_state.setText("订单关闭");
//                    vh.rl_operation.setVisibility(View.VISIBLE);
                    vh.tv_del.setVisibility(View.VISIBLE);
                    vh.tv_operation.setVisibility(View.VISIBLE);
                    vh.tv_operation.setText("重新下单");
                    vh.tv_repay_instructions.setVisibility(View.GONE);

                    String userOrderId = dataLists.get(pos).usr_order_id;
                    deleteMessage(userOrderId);
                    break;
                }
                case 8:{
                    vh.tv_look.setVisibility(View.VISIBLE);
                    vh.tv_info.setVisibility(View.GONE);
                    vh.tv_state.setText("签约失败");
//                    vh.rl_operation.setVisibility(View.VISIBLE);
                    vh.tv_del.setVisibility(View.VISIBLE);
                    vh.tv_operation.setVisibility(View.GONE);
                    vh.tv_repay_instructions.setVisibility(View.GONE);
                    break;
                }
                case 9:{
                    vh.tv_look.setVisibility(View.VISIBLE);
                    vh.tv_info.setVisibility(View.GONE);
                    vh.tv_state.setText("审核失败");
//                    vh.rl_operation.setVisibility(View.VISIBLE);
                    vh.tv_del.setVisibility(View.VISIBLE);
                    vh.tv_operation.setVisibility(View.GONE);
                    vh.tv_repay_instructions.setVisibility(View.GONE);
                    break;
                }
                case 10:{
                    vh.tv_look.setVisibility(View.VISIBLE);
                    vh.tv_info.setVisibility(View.GONE);
                    vh.tv_state.setText("订单关闭");
//                    vh.rl_operation.setVisibility(View.VISIBLE);
                    vh.tv_del.setVisibility(View.VISIBLE);
                    vh.tv_operation.setVisibility(View.GONE);
                    vh.tv_repay_instructions.setVisibility(View.GONE);
                    break;
                }
            }


            vh.tv_time.setText(DateUtils.milliToSimpleDateTime(dataLists.get(pos).create_time*1000));
            String orderMoney = String.valueOf(dataLists.get(pos).order_money);
            BigDecimal db = new BigDecimal(orderMoney);
            vh.tv_money.setText(db.toPlainString());
            //   0:旅游，    1：家具  2：家装
            vh.tv_type.setText(dataLists.get(pos).store_type_name);

            vh.tv_order_num.setText(dataLists.get(pos).order_sn);
            vh.tv_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    order_id = dataLists.get(pos).order_sn;
                    Intent intent = new Intent(mContext, RecodeDetailActivity.class);
                    intent.putExtra("order_id", order_id);
                    UIUtils.startActivityNextAnim(intent);
                }
            });
            vh.tv_operation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dataLists.get(pos).order_status==7){
                        if(dataLists.get(pos).order_from == 1){
                            Intent intent = new Intent(mContext,UseQuotaFillInfomationActivity.class);
                            UIUtils.startActivityNextAnim(intent);
                        }else {
                            findStoreDescById(dataLists.get(pos).store_uid);
                        }
                    }else {
                        Intent intent = new Intent(mContext, ContinueOrderActivity.class);
                        intent.putExtra("orderBean",dataLists.get(pos));
                        UIUtils.startActivityNextAnim(intent);

                    }
                }
            });
            vh.tv_look.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String borrowed_contract = dataLists.get(pos).borrowed_contract;
                    if(!TextUtils.isEmpty(borrowed_contract)){
                        Intent intent = new Intent(mContext, XinaWebActivity.class);
                        intent.putExtra("logistics",borrowed_contract);
                        intent.putExtra("article_title","借款协议");
                        UIUtils.startActivityNextAnim(intent);
                    }else{
                        getOrdersContract(dataLists.get(pos).usr_order_id);
                    }
                }
            });
            vh.tv_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isDeleteDialog = DialogUtils.showDelOrderDialog(mContext, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.tv_cancle:
                                    isDeleteDialog.dismiss();
                                    break;
                                case R.id.tv_ensure:
                                    isDeleteDialog.dismiss();
                                    String order_sn = dataLists.get(pos).order_sn;
                                    updateOrderInfo(order_sn, pos);
                                    break;
                            }
                        }
                    });

                }
            });
        vh.tv_repay_instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRepayInstructionDialog = DialogUtils.showRepayInstructionsAlertDialog(mContext, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.iv_close:
                                showRepayInstructionDialog.dismiss();
                                break;
                        }

                    }
                });

            }
        });

        return view;
    }

    /**
     * 删除已经超时关闭的订单信息
     * @param userOrderId
     */
    private void deleteMessage(String userOrderId) {
        if(!TextUtils.isEmpty(userOrderId)){
            String messageStr = SharedPrefrenceUtils.getString(mContext, userOrderId + "-step4");
            if(!TextUtils.isEmpty(messageStr)){
                SharedPrefrenceUtils.setString(mContext,userOrderId + "-step4","");
            }

            messageStr = SharedPrefrenceUtils.getString(mContext, userOrderId);
            if(!TextUtils.isEmpty(messageStr)){
                SharedPrefrenceUtils.setString(mContext,userOrderId,"");
            }
        }
    }


    /**
     * 将activity加入到队列中
     */
    public void setAddToActivities(AddToActivities addToActivities) {
        addToActivities.addToActivities();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataLists.size();
    }

    class ViewHolder {
        public TextView tv_state;
        public TextView tv_money;
        public TextView tv_type;
        TextView tv_time;
        TextView tv_order_num;
//        RelativeLayout rl_repayment;
//        RelativeLayout rl_contract;
//        RelativeLayout rl_operation;
        TextView tv_operation;
        TextView tv_del;
        TextView tv_info;
        TextView tv_look;
        LinearLayout ll_order_main;
        TextView tv_repay_instructions;
        RelativeLayout rl_freeze_money;
        TextView tv_freeze_money;
    }

    /**
     *根据id店铺详情
     */
    private void findStoreDescById(String usr_id) {
        String url = Constants.XINYONGSERVER_URL+"credit_money_background/user/findStoreDescByUsrid.do";
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("usr_id", usr_id);
        LogUtils.e("usr_id:"+usr_id);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("获得店铺详情:" + json.toString());
                getStoreDescByUsridResponse = gson.fromJson(json, GetStoreDescByUsridResponse.class);
                if (getStoreDescByUsridResponse.getCode() == 0) {
                    shopBean = getStoreDescByUsridResponse.getStore_info();
                    Intent intent=new Intent(mContext,InstallmentPaymentActivity.class);
                    if(shopBean != null){
                        intent.putExtra("store_id",shopBean.getStore_id());
                        intent.putExtra("store_type",shopBean.getStore_type());
                        intent.putExtra("store_name",shopBean.getStore_name());
                        intent.putExtra("store_contract",shopBean.getContract_name());
                        intent.putExtra("store_tel",shopBean.getContract_telephone());
                        intent.putExtra("usr_id",shopBean.getUsr_id());
                        intent.putExtra("is_tiexi",shopBean.getIs_tiexi());
                        intent.putExtra("gather_model_id", shopBean.getGather_model_id());
                    }
                    UIUtils.startActivityNextAnim(intent);

                } else {
                    DialogUtils.showAlertDialog(mContext,
                            getStoreDescByUsridResponse.getMsg());
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext,
                        error);
            }
        });
    }

    /**
     * 获取订单合同接口
     */
    private void getOrdersContract(String usr_order_id) {
        loadingDialog.show();
        String url= Constants.FENGKONGSERVER_URL+"tsfkxt/user/get_orders_contract.do";
        Map<String,String> params = new HashMap<String,String>();
        params.put("usr_order_id", usr_order_id);
        LogUtils.e("params:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Log.e(TAG, "获取订单合同:" + json);
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if(fkbaseResponse.getCode()==0){
                    HashMap<String,String> hs = gson.fromJson(gson.toJson(fkbaseResponse.getReturn_param()),HashMap.class);
                    String borrowed_contract = hs.get("borrowed_contract");
                    if(!TextUtils.isEmpty(borrowed_contract)){
//                        wv_agreement.loadDataWithBaseURL(null, CSS_STYPE + borrowed_contract, "text/html", "utf-8", null);
//                        wv_agreement.loadUrl(borrowed_contract);
                        Intent intent = new Intent(mContext, XinaWebActivity.class);
                        intent.putExtra("logistics",borrowed_contract);
                        intent.putExtra("article_title","借款协议");
                        UIUtils.startActivityNextAnim(intent);
                    }

                }else {
                    DialogUtils.showAlertDialog(mContext, fkbaseResponse.getMsg());
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);
            }
        });
    }



    /**
     * 删除订单
     */
    private void updateOrderInfo(String order_sn, final int possin) {
        loadingDialog.show();
        UpdateOrderInfoProtocol updateOrderInfoProtocol=new UpdateOrderInfoProtocol();
        String url= Constants.FENGKONGSERVER_URL+updateOrderInfoProtocol.getApiFun();
        Map<String,String> params = new HashMap<String,String>();
        params.put("order_sn", order_sn);
        params.put("is_show_flag", "0");
        LogUtils.e("params:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Log.e(TAG, "获取订单合同:" + json);
                Gson gson = new Gson();
                UpdateOrderInfoResponse updateOrderInfoResponse = gson.fromJson(json, UpdateOrderInfoResponse.class);
                if(updateOrderInfoResponse.code==0){
                    dataLists.remove(possin);
                    notifyDataSetChanged();
                    DialogUtils.showAlertDialog(mContext, updateOrderInfoResponse.msg);
                }else {
                    DialogUtils.showAlertDialog(mContext, updateOrderInfoResponse.msg);
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);
            }
        });
    }

    public interface AddToActivities{
        void addToActivities();
    }
}
