package com.tesu.creditgold.fragment;

import android.app.Dialog;
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
public class AreaFragmentTwo extends Fragment {
    private Dialog loadingDialog;
    private GetAreaListResponse getAreaListResponse;
    private ArrayList<AreaBean> areaBeanArrayList;
    private Gson gson;
    private ListView lv_area_two;
    private AreaFragmentTwo.ChooseArea chooseArea;
    private int oldPostion=-1;
    private AreaAdapter mAreaAdapter;
    private AreaBean cityBean;
    private AreaBean provinceBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_area_two, null);

        lv_area_two = (ListView) view.findViewById(R.id.lv_area_two);
        loadingDialog = DialogUtils.createLoadDialog(getActivity(), false);
        gson = new Gson();

        chooseArea = (AreaFragmentTwo.ChooseArea) getActivity();
        if(provinceBean != null){
            getAreaList(provinceBean.getArea_id());
        }
        return view;

    }

    /**
     *获取地区列表
     * @param areaId
     */
    public void getAreaList(int areaId) {
        loadingDialog.show();
        String url = Constants.XINYONGSERVER_URL+"credit_money_background/user/getAreaList.do";
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("area_id",areaId+"");
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
                        if(cityBean != null){
                            int pos = areaBeanArrayList.indexOf(cityBean);
                            if(pos != -1){
                                mAreaAdapter.setSelectItem(pos);
                            }
                        }
                        lv_area_two.setAdapter(mAreaAdapter);

                        lv_area_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                AreaBean areaBean = areaBeanArrayList.get(position);
                                oldPostion = position;
                                if (areaBean != null) {
                                    chooseArea.setArea(areaBean, 2);
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

    public interface ChooseArea{
        public void setArea(AreaBean areaBean,int type);
    }

    public void setOldPostion(){
        if(oldPostion != -1){
            mAreaAdapter.setSelectItem(oldPostion);
            mAreaAdapter.notifyDataSetChanged();
        }
    }

    public void setChooseArea(AreaBean provinceBean,AreaBean cityBean){
        if(provinceBean != null){
            this.provinceBean = provinceBean;
        }
        if(cityBean != null){
            this.cityBean = cityBean;
        }
    }
}
