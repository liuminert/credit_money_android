package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.MyFragmentAdapter;
import com.tesu.creditgold.adapter.RecordAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.fragment.FragmentClosed;
import com.tesu.creditgold.fragment.FragmentCompleted;
import com.tesu.creditgold.fragment.FragmentOther;
import com.tesu.creditgold.fragment.FragmentWaiteExamine;
import com.tesu.creditgold.fragment.FragmentWaiteRepayment;
import com.tesu.creditgold.protocol.GetUsrOrderListProtocol;
import com.tesu.creditgold.request.GetUsrOrderListRequest;
import com.tesu.creditgold.response.GetUsrOrderListResponse;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 分期记录
 */
public class InstallmentRecordActivity extends BaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener, RecordAdapter.AddToActivities, ViewPager.OnPageChangeListener, FragmentWaiteExamine.OnFragmentInteractionListener {

    //判断是否刷新
    private boolean isRefresh = false;
    private TextView tv_top_back;
    private View rootView;
    private PullToRefreshListView lv_record;
    private TextView tv_empty;
    private RecordAdapter recordAdapter;
    private String url;
    //接口请求菊花
    private Dialog loadingDialog;
    private GetUsrOrderListResponse getUsrOrderListResponse;
    private int order_manager;
    private String usrid;
    private TextView tv_title;
    private int pageNo = 1;
    private List<GetUsrOrderListResponse.OrderBean> orderBeanList;

    private PercentRelativeLayout rl_waite_examine;  //待审核
    private PercentRelativeLayout rl_waite_repayment;  //待还款
    private PercentRelativeLayout rl_completed;  //已完成
    private PercentRelativeLayout rl_closed;  //已关闭
    private PercentRelativeLayout rl_other;  //其他
    private View iv_waite_examine;
    private View iv_waite_repayment;
    private View iv_completed;
    private View iv_closed;
    private View iv_other;
    private TextView tv_waite_examine;
    private TextView tv_waite_repayment;
    private TextView tv_completed;
    private TextView tv_closed;
    private TextView tv_other;
    private ViewPager myViewPager;
    private FragmentWaiteRepayment fragmentWaiteRepayment;
    private FragmentWaiteExamine fragmentWaiteExamine;
    private FragmentCompleted fragmentCompleted;
    private FragmentClosed fragmentClosed;
    private FragmentOther fragmentOther;
    private MyFragmentAdapter myFragmentAdapter;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_installment_record, null);
        setContentView(rootView);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_empty = (TextView) rootView.findViewById(R.id.tv_empty);
        lv_record = (PullToRefreshListView) rootView.findViewById(R.id.lv_record);
        rl_waite_examine = (PercentRelativeLayout) rootView.findViewById(R.id.rl_waite_examine);
        rl_waite_repayment = (PercentRelativeLayout) rootView.findViewById(R.id.rl_waite_repayment);
        rl_completed = (PercentRelativeLayout) rootView.findViewById(R.id.rl_completed);
        rl_closed = (PercentRelativeLayout) rootView.findViewById(R.id.rl_closed);
        rl_other = (PercentRelativeLayout) rootView.findViewById(R.id.rl_other);
        iv_waite_examine = rootView.findViewById(R.id.iv_waite_examine);
        iv_waite_repayment = rootView.findViewById(R.id.iv_waite_repayment);
        iv_completed = rootView.findViewById(R.id.iv_completed);
        iv_closed = rootView.findViewById(R.id.iv_closed);
        iv_other = rootView.findViewById(R.id.iv_other);
        myViewPager = (ViewPager) rootView.findViewById(R.id.myViewPager);
        tv_waite_examine = (TextView) rootView.findViewById(R.id.tv_waite_examine);
        tv_waite_repayment = (TextView) rootView.findViewById(R.id.tv_waite_repayment);
        tv_completed = (TextView) rootView.findViewById(R.id.tv_completed);
        tv_closed = (TextView) rootView.findViewById(R.id.tv_closed);
        tv_other = (TextView) rootView.findViewById(R.id.tv_other);
        initData();
        return null;
    }


    public void initData() {
        orderBeanList = new ArrayList<>();
        loadingDialog = DialogUtils.createLoadDialog(InstallmentRecordActivity.this, true);
        tv_top_back.setOnClickListener(this);
        usrid = SharedPrefrenceUtils.getString(InstallmentRecordActivity.this, "usrid");
        order_manager = getIntent().getIntExtra("order_manager", 0);
        tv_title.setText("我的订单");
        lv_record.setMode(PullToRefreshBase.Mode.BOTH);
        lv_record.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        lv_record.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
        lv_record.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以刷新");
        lv_record.setOnRefreshListener(this);
//        GetUsrOrderList();

        fragmentWaiteRepayment = FragmentWaiteRepayment.newInstance("waite_repayment", "");
        fragmentWaiteExamine = FragmentWaiteExamine.newInstance("waite_examine", "");
        fragmentCompleted = FragmentCompleted.newInstance("completed", "");
        fragmentClosed = FragmentClosed.newInstance("closed", "");
        fragmentOther = FragmentOther.newInstance("other", "");
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(fragmentWaiteExamine);
        fragmentList.add(fragmentWaiteRepayment);
        fragmentList.add(fragmentCompleted);
        fragmentList.add(fragmentClosed);
        fragmentList.add(fragmentOther);
        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragmentList);
        myViewPager.setAdapter(myFragmentAdapter);

        myViewPager.setCurrentItem(0);
        myViewPager.setOnPageChangeListener(this);
        setTitleColor(0);

        rl_waite_examine.setOnClickListener(this);
        rl_waite_repayment.setOnClickListener(this);
        rl_completed.setOnClickListener(this);
        rl_closed.setOnClickListener(this);
        rl_other.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ensure: {
                Intent intent = new Intent(InstallmentRecordActivity.this, LoginActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.tv_top_back:
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.rl_waite_examine: //待审核
                setTitleColor(0);
                myViewPager.setCurrentItem(0);
                break;
            case R.id.rl_waite_repayment: //待还款
                setTitleColor(1);
                myViewPager.setCurrentItem(1);
                break;
            case R.id.rl_completed: //已完成
                setTitleColor(2);
                myViewPager.setCurrentItem(2);
                break;
            case R.id.rl_closed: //已关闭
                setTitleColor(3);
                myViewPager.setCurrentItem(3);
                break;
            case R.id.rl_other: //其他
                setTitleColor(4);
                myViewPager.setCurrentItem(4);
                break;
        }
    }

    public void setTitleColor(int i) {
        switch (i) {
            case 0:
                tv_waite_examine.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                tv_waite_repayment.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_completed.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_closed.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_other.setTextColor(UIUtils.getColor(R.color.text_color_black));
                iv_waite_examine.setVisibility(View.VISIBLE);
                iv_waite_repayment.setVisibility(View.INVISIBLE);
                iv_completed.setVisibility(View.INVISIBLE);
                iv_closed.setVisibility(View.INVISIBLE);
                iv_other.setVisibility(View.INVISIBLE);
                break;
            case 1:
                tv_waite_examine.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_waite_repayment.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                tv_completed.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_closed.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_other.setTextColor(UIUtils.getColor(R.color.text_color_black));
                iv_waite_examine.setVisibility(View.INVISIBLE);
                iv_waite_repayment.setVisibility(View.VISIBLE);
                iv_completed.setVisibility(View.INVISIBLE);
                iv_closed.setVisibility(View.INVISIBLE);
                iv_other.setVisibility(View.INVISIBLE);
                break;
            case 2:
                tv_waite_examine.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_waite_repayment.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_completed.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                tv_closed.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_other.setTextColor(UIUtils.getColor(R.color.text_color_black));
                iv_waite_examine.setVisibility(View.INVISIBLE);
                iv_waite_repayment.setVisibility(View.INVISIBLE);
                iv_completed.setVisibility(View.VISIBLE);
                iv_closed.setVisibility(View.INVISIBLE);
                iv_other.setVisibility(View.INVISIBLE);
                break;
            case 3:
                tv_waite_examine.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_waite_repayment.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_completed.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_closed.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                tv_other.setTextColor(UIUtils.getColor(R.color.text_color_black));
                iv_waite_examine.setVisibility(View.INVISIBLE);
                iv_waite_repayment.setVisibility(View.INVISIBLE);
                iv_completed.setVisibility(View.INVISIBLE);
                iv_closed.setVisibility(View.VISIBLE);
                iv_other.setVisibility(View.INVISIBLE);
                break;
            case 4:
                tv_waite_examine.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_waite_repayment.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_completed.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_closed.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_other.setTextColor(UIUtils.getColor(R.color.text_color_yellow));
                iv_waite_examine.setVisibility(View.INVISIBLE);
                iv_waite_repayment.setVisibility(View.INVISIBLE);
                iv_completed.setVisibility(View.INVISIBLE);
                iv_closed.setVisibility(View.INVISIBLE);
                iv_other.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void GetUsrOrderList() {
        loadingDialog.show();
        GetUsrOrderListProtocol getUsrOrderListProtocol = new GetUsrOrderListProtocol();
        GetUsrOrderListRequest getUsrOrderListRequest = new GetUsrOrderListRequest();
        url = Constants.FENGKONGSERVER_URL + getUsrOrderListProtocol.getApiFun();
        getUsrOrderListRequest.map.put("page_no", String.valueOf(pageNo));
        getUsrOrderListRequest.map.put("page_number", "10");
        getUsrOrderListRequest.map.put("usrid", usrid);
        getUsrOrderListRequest.map.put("is_show_flag", "1");
//        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, getUsrOrderListRequest.map, new MyVolley.VolleyCallback() {
//            @Override
//            public void dealWithJson(String address, String json) {
//
//                Gson gson = new Gson();
//                LogUtils.e("获取商户信息:" + json);
//                getUsrOrderListResponse = gson.fromJson(json, GetUsrOrderListResponse.class);
//                if (getUsrOrderListResponse.code.equals("0")) {
//                    loadingDialog.dismiss();
//                    if (getUsrOrderListResponse.return_param.order_list.size() > 0) {
////                        for(int i=0;i<getUsrOrderListResponse.return_param.order_list.size();i++){
////                            if(getUsrOrderListResponse.return_param.order_list.get(i).is_show_flag==1){
////                                orderBeanList.add(getUsrOrderListResponse.return_param.order_list.get(i));
////                            }
////                        }
//                        orderBeanList.addAll(getUsrOrderListResponse.return_param.order_list);
//                        if (recordAdapter == null) {
//                            recordAdapter = new RecordAdapter(InstallmentRecordActivity.this, orderBeanList, loadingDialog);
//                            recordAdapter.setAddToActivities(InstallmentRecordActivity.this);
//                            lv_record.setAdapter(recordAdapter);
//                        } else {
//                            recordAdapter.notifyDataSetChanged();
//                        }
//                        tv_empty.setVisibility(View.GONE);
//                    } else {
//                        if (pageNo == 1) {
//                            tv_empty.setVisibility(View.VISIBLE);
//                        } else {
//                            tv_empty.setVisibility(View.GONE);
//                        }
//                    }
//
//                } else {
//                    loadingDialog.dismiss();
//                    if (!isFinishing()) {
//                        DialogUtils.showAlertDialog(InstallmentRecordActivity.this,
//                                getUsrOrderListResponse.msg);
//                    }
//
//                }
//                if (isRefresh) {
//                    isRefresh = false;
//                    lv_record.onRefreshComplete();
//                }
//
//            }
//
//            @Override
//            public void dealWithError(String address, String error) {
//                loadingDialog.dismiss();
//                if (isRefresh) {
//                    isRefresh = false;
//                    lv_record.onRefreshComplete();
//                }
//                if (!isFinishing()) {
//                    DialogUtils.showAlertDialog(InstallmentRecordActivity.this, error);
//                }
//            }
//        });
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (!isRefresh) {
            isRefresh = true;
            if (lv_record.isHeaderShown()) {
                pageNo = 1;
                orderBeanList.clear();
                GetUsrOrderList();
            } else if (lv_record.isFooterShown()) {
                pageNo++;
                GetUsrOrderList();
            }
        }
    }

    @Override
    public void addToActivities() {
        setFinish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTitleColor(position);
        switch (position){
            case 0:
                fragmentWaiteExamine.setPageNo(1);
                fragmentWaiteExamine.getUsrOrderList();
                break;
            case 1:
                fragmentWaiteRepayment.setPageNo(1);
                fragmentWaiteRepayment.getUsrOrderList();
                break;
            case 2:
                fragmentCompleted.setPageNo(1);
                fragmentCompleted.getUsrOrderList();
                break;
            case 3:
                fragmentClosed.setPageNo(1);
                fragmentClosed.getUsrOrderList();
                break;
            case 4:
                fragmentOther.setPageNo(1);
                fragmentOther.getUsrOrderList();
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
