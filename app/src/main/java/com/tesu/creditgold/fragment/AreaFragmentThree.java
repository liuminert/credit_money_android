package com.tesu.creditgold.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.AreaAdapter;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.AreaBean;
import com.tesu.creditgold.bean.GetAreaListResponse;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/5/17 0017.
 */
public class AreaFragmentThree extends Fragment {
    private Dialog loadingDialog;
    private GetAreaListResponse getAreaListResponse;
    private ArrayList<AreaBean> areaBeanArrayList;
    private Gson gson;
    private ListView lv_area_three;
    private AreaAdapter mAreaAdapter;
    private AreaBean areaBean;
    private AreaBean cityBean;
    private AreaBean provinceBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_area_three, null);

        lv_area_three = (ListView) view.findViewById(R.id.lv_area_three);
        loadingDialog = DialogUtils.createLoadDialog(getActivity(), false);
        gson = new Gson();

        if(provinceBean != null && cityBean != null){
            getAreaList(provinceBean,cityBean);
        }

        return view;

    }

    /**
     *获取地区列表
     */
    public void getAreaList(final AreaBean provinceBean,final AreaBean cityBean) {
        loadingDialog.show();
        String url = Constants.XINYONGSERVER_URL+"credit_money_background/user/getAreaList.do";
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("area_id",cityBean.getArea_id()+"");
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("获得地区列表:" + json.toString());
                getAreaListResponse = gson.fromJson(json, GetAreaListResponse.class);
                if (getAreaListResponse.getCode() == 0) {
                    areaBeanArrayList = getAreaListResponse.getDataList();
                    if (areaBeanArrayList != null && areaBeanArrayList.size() > 0) {
                        mAreaAdapter = new AreaAdapter(getActivity(), areaBeanArrayList);
                        if(areaBean != null){
                            int pos = areaBeanArrayList.indexOf(areaBean);
                            if(pos != -1){
                                mAreaAdapter.setSelectItem(pos);
                            }
                        }
                        lv_area_three.setAdapter(mAreaAdapter);

                        lv_area_three.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                AreaBean areaBean = areaBeanArrayList.get(position);
                                if(areaBean != null){
                                    Intent intent = getActivity().getIntent();
                                    intent.putExtra("provinceBean",provinceBean);
                                    intent.putExtra("cityBean",cityBean);
                                    intent.putExtra("areaBean",areaBean);
                                    intent.putExtra("areaStr",provinceBean.getArea_name()+" "+cityBean.getArea_name()+" "+areaBean.getArea_name());
                                    getActivity().setResult(200, intent);
                                    getActivity().finish();
                                }
                            }
                        });

                    }

                } else {
                    DialogUtils.showAlertDialog(getActivity(),
                            getAreaListResponse.getResultText());
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(getActivity(),
                        error);
            }
        });
    }

    public void setChooseArea(AreaBean provinceBean,AreaBean cityBaen,AreaBean areaBean){
        if(areaBean != null) {
            this.areaBean = areaBean;
        }
        if(cityBaen != null){
            this.cityBean = cityBaen;
        }
        if(provinceBean != null){
            this.provinceBean = provinceBean;
        }
    }
}
