package com.tesu.creditgold.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.AreaAdapter;
import com.tesu.creditgold.adapter.ShopAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.AreaBean;
import com.tesu.creditgold.bean.City;
import com.tesu.creditgold.bean.DictionaryBean;
import com.tesu.creditgold.bean.GetAreaListResponse;
import com.tesu.creditgold.bean.GetStoreListResponse;
import com.tesu.creditgold.bean.MyRegion;
import com.tesu.creditgold.bean.ShopBean;
import com.tesu.creditgold.support.PercentLinearLayout;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.StringUtils;
import com.tesu.creditgold.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.tongdun.android.shell.FMAgent;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 搜索页面
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener, AbsListView.OnScrollListener {

    private TextView tv_top_back;
    private View rootView;
    private String url;
    private Dialog loadingDialog;
    private String keyword;
    private EditText et_search;
    private ImageView iv_search;

    private int current;
    private PullToRefreshListView pr_shop;
    private ShopAdapter shopAdapter;
    private Drawable drawable1;

    private TextView tv_province;
    private TextView tv_city;
    private TextView tv_district;

    private GetAreaListResponse getAreaListResponse;
    private ArrayList<AreaBean> areaBeanArrayList;
    private Gson gson;
    private LayoutInflater mInflater;
    private PopupWindow dictonaryWindow;
    private View dictonaryRoot;
    private ListView lv_choose;
    private Button btn_cancel;
    private ArrayList<DictionaryBean> dictionaryBeanList;
    private AreaBean provinceBean;
    private AreaBean cityBean;
    private AreaBean areaBean;
    private String store_type;
    private GetStoreListResponse getStoreListResponse;
    private List<ShopBean> shopBeanList;
    private PercentRelativeLayout pr_search;
    private PercentLinearLayout ll_show_message;
    private int searchType;   //0 获取分类店铺列表 ，   1 根据条件模糊搜索店铺列表
    private ImageView iv_goto_top;
    private int oldPostion;
    private int pageNo = 1;
    private ImageView serch_colose;
    private TextView tv_title;
    private TextView tv_complete;
    private String dictionaryTitle;
    private PercentRelativeLayout rl_title;

    private AnimatorSet backAnimatorSet;//这是显示头尾元素使用的动画
    private AnimatorSet hideAnimatorSet;//这是隐藏头尾元素使用的动画
    private ListView lv;

    private int lvIndext = 0;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_search, null);
        setContentView(rootView);
        et_search = (EditText) rootView.findViewById(R.id.et_search);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        iv_search = (ImageView) rootView.findViewById(R.id.iv_search);
        pr_shop = (PullToRefreshListView) rootView.findViewById(R.id.pr_shop);
        tv_province = (TextView) rootView.findViewById(R.id.tv_province);
        tv_city = (TextView) rootView.findViewById(R.id.tv_city);
        tv_district = (TextView) rootView.findViewById(R.id.tv_district);
        pr_search = (PercentRelativeLayout) rootView.findViewById(R.id.pr_search);
        ll_show_message = (PercentLinearLayout) rootView.findViewById(R.id.ll_show_message);
        iv_goto_top = (ImageView) rootView.findViewById(R.id.iv_goto_top);
        serch_colose = (ImageView) rootView.findViewById(R.id.serch_colose);
        rl_title = (PercentRelativeLayout) rootView.findViewById(R.id.rl_title);

        drawable1 = getResources().getDrawable(R.mipmap.company_list_down);
        drawable1.setBounds(0, 0, 40, 30);//第一0是距左边距离，第二0是距上边距离，40分别是长宽

//        tv_province.setCompoundDrawables(null, null, drawable1, null);
//        tv_city.setCompoundDrawables(null, null, drawable1, null);
//        tv_district.setCompoundDrawables(null, null, drawable1, null);
        gson = new Gson();


        pr_shop.setMode(PullToRefreshBase.Mode.BOTH);
        pr_shop.setOnRefreshListener(this);

        if (mInflater == null) {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        dictonaryRoot = mInflater.inflate(R.layout.choose_dialog, null);
        dictonaryWindow = new PopupWindow(dictonaryRoot, ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        dictonaryWindow.setFocusable(true);
        lv_choose = (ListView) dictonaryRoot.findViewById(R.id.lv_choose);
        btn_cancel = (Button) dictonaryRoot.findViewById(R.id.btn_cancel);
        tv_title = (TextView) dictonaryRoot.findViewById(R.id.tv_title);
        tv_complete = (TextView) dictonaryRoot.findViewById(R.id.tv_complete);
        lv_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.e("点击了:" + position);
                switch (current) {
                    case 0:
                        provinceBean = areaBeanArrayList.get(position);
                        tv_province.setText(provinceBean.getArea_name());
                        pageNo = 1;
                        getStoreListByKeyword();
                        oldPostion = lv_choose.getFirstVisiblePosition();
                        break;
                    case 1:
                        cityBean = areaBeanArrayList.get(position);
                        tv_city.setText(cityBean.getArea_name());
                        pageNo = 1;
                        getStoreListByKeyword();
                        oldPostion = lv_choose.getFirstVisiblePosition();
                        break;
                    case 2:
                        areaBean = areaBeanArrayList.get(position);
                        tv_district.setText(areaBean.getArea_name());
                        pageNo = 1;
                        getStoreListByKeyword();
                        oldPostion = lv_choose.getFirstVisiblePosition();
                        break;
                }
                dictonaryWindow.dismiss();
            }
        });

        shopBeanList = new ArrayList<ShopBean>();
        shopAdapter = new ShopAdapter(this, shopBeanList);
        pr_shop.setAdapter(shopAdapter);

        //为ListView添加一个Header，这个Header与ToolBar一样高。这样我们可以正确的看到列表中的第一个元素而不被遮住。
        View header = new View(SearchActivity.this);
//        header.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.abc_action_bar_default_height_material)));
        rl_title.measure(0, 0);
        header.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rl_title.getMeasuredHeight() - 5));
        header.setBackgroundColor(Color.parseColor("#00000000"));
        lv = pr_shop.getRefreshableView();
        lv.addHeaderView(header);

        tv_top_back.setOnClickListener(this);
        tv_province.setOnClickListener(this);
        tv_city.setOnClickListener(this);
        tv_district.setOnClickListener(this);
        pr_search.setOnClickListener(this);
        iv_goto_top.setOnClickListener(this);
        serch_colose.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        tv_complete.setOnClickListener(this);

        pr_shop.setOnScrollListener(this);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = et_search.getText().toString();
                if (!TextUtils.isEmpty(str)) {
                    serch_colose.setVisibility(View.VISIBLE);
                } else {
                    serch_colose.setVisibility(View.GONE);
                }

            }
        });

        initData();
        return null;
    }


    public void initData() {
        Intent intent = getIntent();
        store_type = intent.getStringExtra("store_type");

        loadingDialog = DialogUtils.createLoadDialog(SearchActivity.this, true);

        getStoreListByType(store_type);
        pr_shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 2 >= 0 && position - 2 < shopBeanList.size()) {
                    ShopBean shopBean = shopBeanList.get(position - 2);
                    Intent intent = new Intent(SearchActivity.this, ShopInfoActivity.class);
                    intent.putExtra("store_id", shopBean.getStore_id());
                    setFinish();
                    UIUtils.startActivityNextAnim(intent);

//                    Intent intent1 = new Intent(SearchActivity.this,SmokePackagingActivity.class);
//                    UIUtils.startActivityNextAnim(intent1);
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_back:
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.btn_cancel:
            case R.id.tv_complete:
                dictonaryWindow.dismiss();
                break;
            case R.id.tv_province:
                dictionaryTitle = "请选择省份";
                current = 0;
                getAreaList(0);
                break;
            case R.id.tv_city:
                dictionaryTitle = "请选择市";
                if (provinceBean == null) {
                    UIUtils.showToastCenter(SearchActivity.this, "请先选择省份");
                    return;
                }
                current = 1;
                getAreaList(provinceBean.getArea_id());
                break;
            case R.id.tv_district:
                dictionaryTitle = "请选择区";
                if (provinceBean == null) {
                    UIUtils.showToastCenter(SearchActivity.this, "请先选择省份");
                    return;
                }
                if (cityBean == null) {
                    UIUtils.showToastCenter(SearchActivity.this, "请先选择市");
                    return;
                }
                current = 2;
                getAreaList(cityBean.getArea_id());
                break;
            case R.id.pr_search:
                pageNo = 1;
                getStoreListByKeyword();
                break;
            case R.id.iv_goto_top:
                pr_shop.setSelection(0);
                iv_goto_top.setVisibility(View.GONE);
                animateBack();
                break;
            case R.id.serch_colose:
                et_search.setText("");
                break;
        }
    }

    /**
     * 根据条件模糊搜索店铺列表
     */
    private void getStoreListByKeyword() {
        searchType = 1;
        loadingDialog.show();
        keyword = et_search.getText().toString();
//        String url =Constants.XINYONGSERVER_URL+"credit_money_background/user/searchStoreListByKeyword.do";
        String url = Constants.XINYONGSERVER_URL + "credit_money_background/user/searchStorePageListByKeyword.do";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key_word", keyword);
        params.put("store_type", store_type);
        LogUtils.e("store_type:" + store_type);
        if (provinceBean != null) {
            params.put("province_id", provinceBean.getArea_id() + "");
        }
        if (cityBean != null) {
            params.put("city_id", cityBean.getArea_id() + "");
        }
        if (areaBean != null) {
            params.put("area_id", areaBean.getArea_id() + "");
        }
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(10));
        LogUtils.e("params:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("获得模糊店铺列表:" + json.toString());
                getStoreListResponse = gson.fromJson(json, GetStoreListResponse.class);
                if (getStoreListResponse.getCode() == 0) {
                    if (pageNo == 1) {
                        shopBeanList.clear();
                    }
                    shopBeanList.addAll(getStoreListResponse.getDataList());
                    if (shopBeanList.size() == 0) {
                        ll_show_message.setVisibility(View.VISIBLE);
//                        iv_goto_top.setVisibility(View.GONE);
                    } else {
                        ll_show_message.setVisibility(View.GONE);
//                        if(shopBeanList.size()>6){
//                            iv_goto_top.setVisibility(View.VISIBLE);
//                        }else {
//                            iv_goto_top.setVisibility(View.GONE);
//                        }
                    }
                    shopAdapter.notifyDataSetChanged();

                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(SearchActivity.this,
                                getStoreListResponse.getMsg());
                    }
                }

                pr_shop.onRefreshComplete();
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                pr_shop.onRefreshComplete();
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(SearchActivity.this,
                            error);
                }
            }
        });
    }

    /**
     * 获取分类店铺列表
     *
     * @param storeType
     */
    private void getStoreListByType(String storeType) {
        searchType = 0;
        loadingDialog.show();
//        String url = Constants.XINYONGSERVER_URL+"credit_money_background/user/getStoreListByType.do";
        String url = Constants.XINYONGSERVER_URL + "credit_money_background/user/getStorePagerListByType.do";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("store_type", storeType);
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(10));
        LogUtils.e("params:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("获得店铺列表:" + json.toString());
                getStoreListResponse = gson.fromJson(json, GetStoreListResponse.class);
                if (getStoreListResponse.getCode() == 0) {
                    if (pageNo == 1) {
                        shopBeanList.clear();
                    }
                    shopBeanList.addAll(getStoreListResponse.getDataList());
                    if (shopBeanList.size() == 0) {
                        ll_show_message.setVisibility(View.VISIBLE);
//                        iv_goto_top.setVisibility(View.GONE);
                    } else {
                        ll_show_message.setVisibility(View.GONE);
//                        if(shopBeanList.size()>6){
//                            iv_goto_top.setVisibility(View.VISIBLE);
//                        }else {
//                            iv_goto_top.setVisibility(View.GONE);
//                        }
                    }
                    shopAdapter.notifyDataSetChanged();

                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(SearchActivity.this,
                                getStoreListResponse.getMsg());
                    }
                }

                pr_shop.onRefreshComplete();
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                pr_shop.onRefreshComplete();
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(SearchActivity.this,
                            error);
                }
            }
        });
    }


    /**
     * 获取地区列表
     *
     * @param areaId
     */
    private void getAreaList(int areaId) {
        loadingDialog.show();
        String url = Constants.XINYONGSERVER_URL + "credit_money_background/user/getAreaList.do";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("area_id", areaId + "");
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("获得地区列表:" + json.toString());
                getAreaListResponse = gson.fromJson(json, GetAreaListResponse.class);
                if (getAreaListResponse.getCode() == 0) {

                    areaBeanArrayList = getAreaListResponse.getDataList();
                    if (areaBeanArrayList != null && areaBeanArrayList.size() > 0) {
                        AreaAdapter mAreaAdapter = new AreaAdapter(SearchActivity.this, areaBeanArrayList);
                        lv_choose.setAdapter(mAreaAdapter);
                        if (!TextUtils.isEmpty(dictionaryTitle)) {
                            tv_title.setText(dictionaryTitle);
                        }
//                        dictonaryWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
//                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        dictonaryWindow.showAsDropDown((LinearLayout) findViewById(R.id.rg));


                        int index;

                        if (current == 0) {  //当重新选择省份后，市和区的数据清空
                            cityBean = null;
                            tv_city.setText("市");
                            areaBean = null;
                            tv_district.setText("区/县");
                            if (provinceBean != null) {
                                index = areaBeanArrayList.indexOf(provinceBean);
                                if (index != -1) {
                                    lv_choose.setSelection(oldPostion);
                                    mAreaAdapter.setSelectItem(index);
                                }
                            }
                        } else if (current == 1) { //当重新选择市后，地区数据清空
                            areaBean = null;
                            tv_district.setText("区/县");
                            if (cityBean != null) {
                                index = areaBeanArrayList.indexOf(cityBean);
                                if (index != -1) {
                                    lv_choose.setSelection(oldPostion);
                                    mAreaAdapter.setSelectItem(index);
                                }
                            }
                        } else {
                            if (areaBean != null) {
                                index = areaBeanArrayList.indexOf(areaBean);
                                if (index != -1) {
                                    lv_choose.setSelection(oldPostion);
                                    mAreaAdapter.setSelectItem(index);
                                }
                            }
                        }
                    }


                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(SearchActivity.this,
                                getAreaListResponse.getResultText());
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(SearchActivity.this,
                            error);
                }
            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {

        if (pr_shop.isHeaderShown()) {
            pageNo = 1;
//            iv_goto_top.setVisibility(View.GONE);

        } else if (pr_shop.isFooterShown()) {
            pageNo++;
//            iv_goto_top.setVisibility(View.VISIBLE);

        }
        if (searchType == 0) {
            getStoreListByType(store_type);
        } else {
            getStoreListByKeyword();
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {

            // 滚动之前,手还在屏幕上  记录滚动前的下标
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                //view.getLastVisiblePosition()得到当前屏幕可见的第一个item在整个listview中的下标
                lvIndext = view.getLastVisiblePosition();
                break;

            //滚动停止
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                //记录滚动停止后 记录当前item的位置
                int scrolled = view.getLastVisiblePosition();
                //滚动后下标大于滚动前 向下滚动了

                if (scrolled > lvIndext) {
                    //scroll = false;
                    iv_goto_top.setVisibility(View.VISIBLE);
                    if (lv.getFirstVisiblePosition() > 1) {
                        animateHide();
                    }
                }
                //向上滚动了
                else {
                    iv_goto_top.setVisibility(View.GONE);
                    //scroll = true;
                    animateBack();
                }
                break;

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    private void animateBack() {
        //先清除其他动画
        if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
            hideAnimatorSet.cancel();
        }
        if (backAnimatorSet != null && backAnimatorSet.isRunning()) {
            //如果这个动画已经在运行了，就不管它
        } else {
            backAnimatorSet = new AnimatorSet();
            //下面两句是将头尾元素放回初始位置。
            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(rl_title, "translationY", rl_title.getTranslationY(), 0f);
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(headerAnimator);
            backAnimatorSet.setDuration(300);
            backAnimatorSet.playTogether(animators);
            backAnimatorSet.start();
        }
    }

    private void animateHide() {
        //先清除其他动画
        if (backAnimatorSet != null && backAnimatorSet.isRunning()) {
            backAnimatorSet.cancel();
        }
        if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
            //如果这个动画已经在运行了，就不管它
        } else {
            hideAnimatorSet = new AnimatorSet();
            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(rl_title, "translationY", rl_title.getTranslationY(), -rl_title.getHeight());//将rl_title隐藏到上面
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(headerAnimator);
            hideAnimatorSet.setDuration(200);
            hideAnimatorSet.playTogether(animators);
            hideAnimatorSet.start();
        }
    }
}
