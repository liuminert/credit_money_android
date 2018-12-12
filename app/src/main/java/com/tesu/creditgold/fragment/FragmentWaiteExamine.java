package com.tesu.creditgold.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tesu.creditgold.R;
import com.tesu.creditgold.activity.InstallmentRecordActivity;
import com.tesu.creditgold.adapter.RecordAdapter;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.protocol.GetUsrOrderListProtocol;
import com.tesu.creditgold.request.GetUsrOrderListRequest;
import com.tesu.creditgold.response.GetUsrOrderListResponse;
import com.tesu.creditgold.support.PercentLinearLayout;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentWaiteExamine.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentWaiteExamine#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentWaiteExamine extends Fragment implements PullToRefreshBase.OnRefreshListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //判断是否刷新
    private boolean isRefresh = false;
    private PullToRefreshListView lv_record;
    private RecordAdapter recordAdapter;
    private String url;
    //接口请求菊花
    private Dialog loadingDialog;
    private GetUsrOrderListResponse getUsrOrderListResponse;
    private String usrid;
    private int pageNo = 1;
    private List<GetUsrOrderListResponse.OrderBean> orderBeanList;
    private PercentLinearLayout ll_no_order;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentWaiteExamine.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentWaiteExamine newInstance(String param1, String param2) {
        FragmentWaiteExamine fragment = new FragmentWaiteExamine();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentWaiteExamine() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_waite_examine, container, false);
        lv_record = (PullToRefreshListView) view.findViewById(R.id.lv_record);
        ll_no_order = (PercentLinearLayout) view.findViewById(R.id.ll_no_order);

        initData();
        return view;
    }

    private void initData() {
        orderBeanList = new ArrayList<>();
        loadingDialog = DialogUtils.createLoadDialog(getActivity(), true);
        usrid = SharedPrefrenceUtils.getString(getActivity(), "usrid");
        lv_record.setMode(PullToRefreshBase.Mode.BOTH);
        lv_record.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        lv_record.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
        lv_record.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以刷新");
        lv_record.setOnRefreshListener(this);
        getUsrOrderList();
    }

    public void getUsrOrderList() {
//        loadingDialog.show();
//        GetUsrOrderListProtocol getUsrOrderListProtocol = new GetUsrOrderListProtocol();
        GetUsrOrderListRequest getUsrOrderListRequest = new GetUsrOrderListRequest();
        url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/getUsrOrderListByStatus.do";
        getUsrOrderListRequest.map.put("page_no", String.valueOf(pageNo));
        getUsrOrderListRequest.map.put("page_number", "10");
        getUsrOrderListRequest.map.put("usrid", usrid);
        getUsrOrderListRequest.map.put("is_show_flag", "1");
        getUsrOrderListRequest.map.put("statusType", "1");
//        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, getUsrOrderListRequest.map, new MyVolley.VolleyCallback() {
//            @Override
//            public void dealWithJson(String address, String json) {
//
//                Gson gson = new Gson();
//                LogUtils.e("获取商户信息:" + json);
//                getUsrOrderListResponse = gson.fromJson(json, GetUsrOrderListResponse.class);
//                if (getUsrOrderListResponse.code.equals("0")) {
//                    loadingDialog.dismiss();
//                    List<GetUsrOrderListResponse.OrderBean> orderBeans = getUsrOrderListResponse.return_param.order_list;
//                    if (orderBeans.size() > 0) {
////                        for(int i=0;i<getUsrOrderListResponse.return_param.order_list.size();i++){
////                            if(getUsrOrderListResponse.return_param.order_list.get(i).is_show_flag==1){
////                                orderBeanList.add(getUsrOrderListResponse.return_param.order_list.get(i));
////                            }
////                        }
////                        for(GetUsrOrderListResponse.OrderBean orderBean : orderBeans){
////                            if(orderBean.order_status == 1){
////                                orderBeanList.add(orderBean);
////                            }
////                        }
//                        orderBeanList.addAll(getUsrOrderListResponse.return_param.order_list);
//                        if (recordAdapter == null) {
//                            recordAdapter = new RecordAdapter(getActivity(), orderBeanList, loadingDialog);
//                            recordAdapter.setAddToActivities((InstallmentRecordActivity) getActivity());
//                            lv_record.setAdapter(recordAdapter);
//                        } else {
//                            recordAdapter.notifyDataSetChanged();
//                        }
//                    }
//
//                    if(orderBeanList.size()>0){
//                        ll_no_order.setVisibility(View.GONE);
//                    }else{
//                        ll_no_order.setVisibility(View.VISIBLE);
//                    }
//
//                } else {
//                    loadingDialog.dismiss();
//                    DialogUtils.showAlertDialog(getActivity(),
//                            getUsrOrderListResponse.msg);
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
//                DialogUtils.showAlertDialog(getActivity(), error);
//            }
//        });
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (!isRefresh) {
            isRefresh = true;
            if (lv_record.isHeaderShown()) {
                pageNo = 1;
                orderBeanList.clear();
                getUsrOrderList();
            } else if (lv_record.isFooterShown()) {
                pageNo++;
                getUsrOrderList();
            }
        }
    }

    public void setPageNo(int pageNo){
        this.pageNo = pageNo;
        if(pageNo==1){
            if(orderBeanList != null){
                orderBeanList.clear();
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
