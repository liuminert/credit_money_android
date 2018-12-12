package com.tesu.creditgold.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.AmountDetailAdapter;
import com.tesu.creditgold.adapter.CommonExpandableListAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.AmountDetailBean;
import com.tesu.creditgold.response.GetAmountTranscationDetailsResponse;
import com.tesu.creditgold.response.GetArticleByIdResponse;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 账户管理
 */
public class AmountTranscationDetalsActivity extends BaseActivity implements View.OnClickListener, DatePicker.OnDateChangedListener {

    private TextView tv_top_back;
    private View rootView;

    private TextView tv_change;  //筛选
    private TextView tv_time1;  //日期
    private ExpandableListView lv_details;
    private Dialog loadingDialog;
    private String userId;
    private GetAmountTranscationDetailsResponse getAmountTranscationDetailsResponse;
    private DecimalFormat df = new DecimalFormat("###0.00");
    private List<AmountDetailBean> amountDetailBeanList;
    private AmountDetailAdapter amountDetailAdapter;
    private AlertDialog dateDialog;
    private AlertDialog tracnsactionTypeDialog;
    private int year;
    private int monthOfYear;
    private int dayOfMonth;
    private String startTime;
    private RelativeLayout rl_bank_management;
    private TextView tv_title;
    private int transaction_type;
    private boolean isfromDetals;  //是否是点击交易详情跳过来
    private List<GetAmountTranscationDetailsResponse.AmountBean> resultTest;
    private CommonExpandableListAdapter<AmountDetailBean, GetAmountTranscationDetailsResponse.AmountBean> commonExpandableListAdapter;
    private ImageView iv_choose;
    private TextView tv_total_amount1;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_amount_transcation_details, null);
        setContentView(rootView);
        tv_change = (TextView) rootView.findViewById(R.id.tv_change);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_time1 = (TextView) rootView.findViewById(R.id.tv_time1);
        lv_details = (ExpandableListView) rootView.findViewById(R.id.lv_details);
        rl_bank_management = (RelativeLayout) rootView.findViewById(R.id.rl_bank_management);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        iv_choose = (ImageView) rootView.findViewById(R.id.iv_choose);
        tv_total_amount1 = (TextView) rootView.findViewById(R.id.tv_total_amount1);
        initData();
        return null;
    }


    public void initData() {
        loadingDialog = DialogUtils.createLoadDialog(this, true);
        userId = SharedPrefrenceUtils.getString(this, "usrid");

        Intent intent = getIntent();
        transaction_type = intent.getIntExtra("transaction_type", 0);
        isfromDetals = intent.getBooleanExtra("isfromDetals", false);

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        monthOfYear = c.get(Calendar.MONTH);
        dayOfMonth = c.get(Calendar.DATE);

        if(monthOfYear<9){
            tv_time1.setText(year + "年0" + (monthOfYear + 1) + "月");
        }else {
            tv_time1.setText(year + "年" + (monthOfYear + 1) + "月");
        }
        startTime = year + "-" + (monthOfYear + 1) + "-01 00:00:00";

        getTransactinInfo(startTime);

        tv_top_back.setOnClickListener(this);
        tv_change.setOnClickListener(this);
        iv_choose.setOnClickListener(this);

        lv_details.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AmountDetailBean amountDetailBean = amountDetailBeanList.get(position);
                Intent intent = new Intent(AmountTranscationDetalsActivity.this, AmountTranscationDetailInfoActivity.class);
                intent.putExtra("amountDetailBean", amountDetailBean);
                UIUtils.startActivityNextAnim(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_change:  //筛选
                tracnsactionTypeDialog = DialogUtils.showTransactionTypeDialog(this, this, transaction_type);
                break;
            case R.id.iv_choose:  //选择日期
                dateDialog = DialogUtils.showDatePickerDialog(this, this, AmountTranscationDetalsActivity.this, year, monthOfYear, dayOfMonth, false);
                break;
            case R.id.tv_top_back:
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.tv_cancel:
                if (dateDialog != null && dateDialog.isShowing()) {
                    dateDialog.dismiss();
                }
                if (tracnsactionTypeDialog != null && tracnsactionTypeDialog.isShowing()) {
                    tracnsactionTypeDialog.dismiss();
                }
                break;
            case R.id.tv_submit:
                dateDialog.dismiss();
                startTime = year + "-" + (monthOfYear + 1) + "-01 00:00:00";
//                transaction_type = 0;
                getTransactinInfo(startTime);
                break;
            case R.id.tv_all:  //全部
                tracnsactionTypeDialog.dismiss();
                transaction_type = 0;
                getTransactinInfo(startTime);
                break;
            case R.id.tv_credit_amount: //授信金额
                tracnsactionTypeDialog.dismiss();
                transaction_type = 1;
                getTransactinInfo(startTime);
                break;
            case R.id.tv_transaction_expenditure: //交易支出
                tracnsactionTypeDialog.dismiss();
                transaction_type = 2;
                getTransactinInfo(startTime);
                break;
            case R.id.tv_repayment_amount: //还款金额
                tracnsactionTypeDialog.dismiss();
                transaction_type = 3;
                getTransactinInfo(startTime);
                break;
        }
    }

    /**
     * 根据用户id和交易类型查找交易流水
     */
    private void getTransactinInfo(final String startTime1) {

        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/selectTransactionInfo.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
        if (transaction_type != 0) {
            params.put("transaction_type", String.valueOf(transaction_type));
        }
        if (!TextUtils.isEmpty(startTime1)) {
            params.put("transaction_time", startTime1);
        }
        LogUtils.e("params:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("查找交易流水:" + json);
                getAmountTranscationDetailsResponse = new Gson().fromJson(json, GetAmountTranscationDetailsResponse.class);
                if (getAmountTranscationDetailsResponse != null) {

                    switch (transaction_type) {
                        case 0:
                            tv_title.setText("交易明细");
                            break;
                        case 1:
                            tv_title.setText("累计授信金额明细");
                            break;
                        case 2:
                            tv_title.setText("已用额度明细");
                            break;
                        case 3:
                            tv_title.setText("已还金额明细");
                            break;
                    }

                    if (isfromDetals) {
                        tv_change.setVisibility(View.VISIBLE);
                    } else {
                        tv_change.setVisibility(View.GONE);
                    }

                    //设置适配器
                    commonExpandableListAdapter = new CommonExpandableListAdapter<AmountDetailBean, GetAmountTranscationDetailsResponse.AmountBean>(AmountTranscationDetalsActivity.this, R.layout.amount_detail_item, R.layout.amount_group_detail_item) {
                        @Override
                        protected void getChildView(ViewHolder holder, int groupPositon, int childPositon, boolean isLastChild, AmountDetailBean amountDetailBean) {
                            TextView tv_type = holder.getView(R.id.tv_type);
                            TextView tv_time = holder.getView(R.id.tv_time);
                            TextView tv_amount = holder.getView(R.id.tv_amount);
                            TextView tv_freeze_mode = holder.getView(R.id.tv_freeze_mode);

                            int freezeMode = amountDetailBean.getFreeze_mode();
                            String amount = df.format(amountDetailBean.getTransaction_amount());
                            switch (amountDetailBean.getTransaction_type()) {
                                case 1:
                                    tv_type.setText("授信金额");
                                    tv_amount.setText("+ " + amount);
                                    tv_amount.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                                    break;
                                case 2:
                                    tv_type.setText("交易支出");
                                    if (freezeMode == 2) {
                                        tv_amount.setText("+ " + amount);
                                        tv_amount.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                                    } else {
                                        tv_amount.setText("- " + amount);
                                        tv_amount.setTextColor(UIUtils.getColor(R.color.text_color_black));
                                    }
                                    break;
                                case 3:
                                    tv_type.setText("还款金额");
                                    tv_amount.setText("+ " + amount);
                                    tv_amount.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                                    break;
                                case 4:
                                    tv_type.setText("额度过期");
                                    tv_amount.setText("- " + amount);
                                    tv_amount.setTextColor(UIUtils.getColor(R.color.text_color_black));
                                    break;
                            }

                            if (!TextUtils.isEmpty(amountDetailBean.getTransaction_time_format())) {
                                tv_time.setText(amountDetailBean.getTransaction_time_format());
                            }
                            switch (freezeMode) {
                                case 0:
                                    tv_freeze_mode.setVisibility(View.GONE);
                                    break;
                                case 1:
                                    tv_freeze_mode.setVisibility(View.VISIBLE);
                                    tv_freeze_mode.setText("冻结");
                                    break;
                                case 2:
                                    tv_freeze_mode.setVisibility(View.VISIBLE);
                                    tv_freeze_mode.setText("解冻");
                                    break;
                                default:
                                    tv_freeze_mode.setVisibility(View.GONE);
                                    break;
                            }

                        }

                        @Override
                        protected void getGroupView(ViewHolder holder, int groupPositon, boolean isExpanded, GetAmountTranscationDetailsResponse.AmountBean data) {
                            TextView tv_time = holder.getView(R.id.tv_time);//时间
                            TextView tv_total_amount = holder.getView(R.id.tv_total_amount);//总数
                            ImageView iv_choose = holder.getView(R.id.iv_choose);//选择日历
                            if (!TextUtils.isEmpty(data.getMonth_key())) {
                                tv_time.setText(data.getMonth_key());
                            }
                            iv_choose.setOnClickListener(AmountTranscationDetalsActivity.this);
                            if(groupPositon == 0){
                                iv_choose.setVisibility(View.VISIBLE);
                            }else {
                                iv_choose.setVisibility(View.GONE);
                            }

                            switch (transaction_type) {
                                case 0:
                                    tv_total_amount.setText("支出：￥"+df.format(data.getTotal_pay_amount())+"  收入：￥"+df.format(data.getTotal_income_amount()));
                                    break;
                                case 1:
                                    tv_total_amount.setText("累计授信：￥"+df.format(data.getTotal_income_amount()));
                                    break;
                                case 2:
                                    tv_total_amount.setText("累计支出：￥"+df.format(data.getTotal_pay_amount()));
                                    break;
                                case 3:
                                    tv_total_amount.setText("累计还款：￥"+df.format(data.getTotal_return_amount()));
                                    break;
                            }

                        }
                    };
                    lv_details.setAdapter(commonExpandableListAdapter);
                    lv_details.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v,
                                                    int groupPosition, long id) {
                            // TODO Auto-generated method stub
                            return true;
                        }
                    });
                    lv_details.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                            AmountDetailBean amountDetailBean = (AmountDetailBean) commonExpandableListAdapter.getChild(groupPosition,childPosition);
                            Intent intent = new Intent(AmountTranscationDetalsActivity.this, AmountTranscationDetailInfoActivity.class);
                            intent.putExtra("amountDetailBean", amountDetailBean);
                            UIUtils.startActivityNextAnim(intent);
                            return false;
                        }
                    });
                    lv_details.setGroupIndicator(null);
                    resultTest = getAmountTranscationDetailsResponse.getResultText();
                    commonExpandableListAdapter.getGroupData().clear();
                    commonExpandableListAdapter.getChildrenData().clear();

                    if (resultTest != null && resultTest.size()>0) {
                        for (int i = 0; i < resultTest.size(); i++) {
                            commonExpandableListAdapter.getGroupData().add(resultTest.get(i));
                            commonExpandableListAdapter.getChildrenData().add(resultTest.get(i).getData());
                        }
                        if(commonExpandableListAdapter.getGroupData().size()>0){
                            for (int i = 0; i < commonExpandableListAdapter.getGroupData().size(); i++) {
                                if(commonExpandableListAdapter.getChildrenData().get(i) != null){
                                    lv_details.expandGroup(i);
                                }
                            }
                        }

                        rl_bank_management.setVisibility(View.GONE);
                    }else {
                        rl_bank_management.setVisibility(View.VISIBLE);
                        if(monthOfYear<9){
                            tv_time1.setText(year + "年0" + (monthOfYear + 1) + "月");
                        }else {
                            tv_time1.setText(year + "年" + (monthOfYear + 1) + "月");
                        }

                        switch (transaction_type) {
                            case 0:
                                tv_total_amount1.setText("支出：￥0"+"  收入：￥0");
                                break;
                            case 1:
                                tv_total_amount1.setText("累计授信：￥0");
                                break;
                            case 2:
                                tv_total_amount1.setText("累计支出：￥0");
                                break;
                            case 3:
                                tv_total_amount1.setText("累计还款：￥0");
                                break;
                        }
                    }
                    commonExpandableListAdapter.notifyDataSetChanged();


                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(AmountTranscationDetalsActivity.this, error);
                }
            }
        });
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;

    }
}
