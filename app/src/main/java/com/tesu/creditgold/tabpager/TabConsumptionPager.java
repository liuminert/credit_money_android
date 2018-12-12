package com.tesu.creditgold.tabpager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tesu.creditgold.R;
import com.tesu.creditgold.activity.RealNameActivity;
import com.tesu.creditgold.activity.SearchActivity;
import com.tesu.creditgold.adapter.ConsumptionAdapter;
import com.tesu.creditgold.adapter.DictionaryAdapter;
import com.tesu.creditgold.adapter.ShopTypeAdapter;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.base.ViewTabBasePager;
import com.tesu.creditgold.bean.DictionaryBean;
import com.tesu.creditgold.protocol.GetAdCarouselFigureProtocol;
import com.tesu.creditgold.protocol.GetStoreTypeProtocol;
import com.tesu.creditgold.protocol.LoginProtocol;
import com.tesu.creditgold.request.GetAdCarouselFigureRequest;
import com.tesu.creditgold.request.GetStoreTypeRequest;
import com.tesu.creditgold.request.LoginRequest;
import com.tesu.creditgold.response.GetAdCarouselFigureResponse;
import com.tesu.creditgold.response.GetStoreTypeResponse;
import com.tesu.creditgold.response.LoginResponse;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.PictureOption;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.util.Utils;
import com.tesu.creditgold.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @作者: 许明达
 * @创建时间: 2016-4-27下午5:24:30
 * @版权: 特速版权所有
 * @描述: 详情页面
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class TabConsumptionPager extends ViewTabBasePager implements View.OnClickListener{

    private String goods_desc;
    private ConsumptionAdapter consumptionAdapter;
    private Dialog loadingDialog;
    private String url;
    private boolean isLoaded = false;
    private boolean isRefresh = false;
    private List<GetStoreTypeResponse.StoreType> datalist;

    private List<View> dots; // 图片标题正文的那些点
    private int oldPosition = 0;
    private ScheduledExecutorService scheduledExecutorService;
    private int currentItem = 0; // 当前图片的索引号
    private List<ImageView> imageViews; // 滑动的图片集合
    private ImageView imageView;
    boolean isloaded;
    private boolean isopen = false;
    ViewPager vp;
    LinearLayout ll_pager;
    View v_dot0;
    View v_dot1;
    View v_dot2;
    View v_dot3;
    View v_dot4;
    private GetAdCarouselFigureResponse getAdCarouselFigureResponse;
    private ImageLoader imageLoader;

    private ListView lv_shop_type;
    private ShopTypeAdapter shopTypeAdapter;

    public TabConsumptionPager(Context context) {
        super(context);
        this.goods_desc = goods_desc;
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext,
                R.layout.consumption_pager, null);
        ViewUtils.inject(this, view);
        vp = (ViewPager) view.findViewById(R.id.vp);
        v_dot0 = (View) view.findViewById(R.id.v_dot0);
        v_dot1 = (View) view.findViewById(R.id.v_dot1);
        v_dot2 = (View) view.findViewById(R.id.v_dot2);
        v_dot3 = (View) view.findViewById(R.id.v_dot3);
        v_dot4 = (View) view.findViewById(R.id.v_dot4);
        ll_pager = (LinearLayout) view.findViewById(R.id.ll_pager);
        lv_shop_type = (ListView) view.findViewById(R.id.lv_shop_type);

        lv_shop_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position < datalist.size()){
                    if(datalist.get(position) != null){
                        Intent intent = new Intent(mContext, SearchActivity.class);
                        intent.putExtra("store_type", datalist.get(position).store_type);
                        UIUtils.startActivityNextAnim(intent);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void initData() {

        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance()
                    .init(ImageLoaderConfiguration.createDefault(UIUtils
                            .getContext()));
        }

        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(mContext)));

        loadingDialog = DialogUtils.createLoadDialog(mContext, true);
        if (!isLoaded) {
            GetStoreType();
        }
        if (!isloaded) {
            runAsyncTask();
        }
    }

    //获取门店类型
    public void GetStoreType() {

        loadingDialog.show();
        GetStoreTypeProtocol getStoreTypeProtocol = new GetStoreTypeProtocol();
        GetStoreTypeRequest getStoreTypeRequest = new GetStoreTypeRequest();
//        url = Constants.XINYONGSERVER_URL + getStoreTypeProtocol.getApiFun();
        url = Constants.XINYONGSERVER_URL + "credit_money_background/user/getStoreType/V1.do";
        getStoreTypeRequest.map.put("size","3");
        LogUtils.e("getStoreTypeResponse:params"+getStoreTypeRequest.map.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, getStoreTypeRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();

                Gson gson = new Gson();
                GetStoreTypeResponse getStoreTypeResponse = gson.fromJson(json, GetStoreTypeResponse.class);
                LogUtils.e("getStoreTypeResponse:" + getStoreTypeResponse.toString());
                if (getStoreTypeResponse.code == 0) {
                    isLoaded = true;
                    datalist = getStoreTypeResponse.dataList;
                    if(datalist != null && datalist.size()>=0){
                        shopTypeAdapter = new ShopTypeAdapter(mContext,datalist);
                        lv_shop_type.setAdapter(shopTypeAdapter);
                    }
                } else {
                    if(!TextUtils.isEmpty(getStoreTypeResponse.msg)){
                        DialogUtils.showAlertDialog(mContext, getStoreTypeResponse.msg);
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!TextUtils.isEmpty(error)){
                    DialogUtils.showAlertDialog(mContext, error);
                }
            }
        });
    }

    public void setheadDate(List<GetAdCarouselFigureResponse.AdCarouselFigureBean> adsBeanList) {
        isloaded = true;
        imageViews = new ArrayList<ImageView>();

        // 初始化图片资源
        for (int i = 0; i < adsBeanList.size(); i++) {
            imageView = new ImageView(mContext);
//            ImageLoader.getInstance().displayImage(adsBeanList.get(i).img, imageView);
            imageLoader.displayImage(adsBeanList.get(i).img, imageView, PictureOption.getBannerOptions());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(imageView);
            LogUtils.e("加图" + adsBeanList.size() + "imageViews:" + imageViews.size());
        }


        dots = new ArrayList<View>();
        switch (adsBeanList.size()) {
            case 1:
                v_dot0.setVisibility(View.GONE);
                v_dot1.setVisibility(View.GONE);
                v_dot2.setVisibility(View.GONE);
                v_dot3.setVisibility(View.GONE);
                v_dot4.setVisibility(View.GONE);
                break;
            case 2:
                dots.add(v_dot0);
                dots.add(v_dot1);
                v_dot2.setVisibility(View.GONE);
                v_dot3.setVisibility(View.GONE);
                v_dot4.setVisibility(View.GONE);
                break;
            case 3:
                dots.add(v_dot0);
                dots.add(v_dot1);
                dots.add(v_dot2);
                v_dot3.setVisibility(View.GONE);
                v_dot4.setVisibility(View.GONE);
                break;
            case 4:
                dots.add(v_dot0);
                dots.add(v_dot1);
                dots.add(v_dot2);
                dots.add(v_dot3);
                v_dot4.setVisibility(View.GONE);
                break;
            case 5:
                dots.add(v_dot0);
                dots.add(v_dot1);
                dots.add(v_dot2);
                dots.add(v_dot3);
                dots.add(v_dot4);
                break;
            default:
                break;
        }


        vp.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
        // 设置一个监听器，当ViewPager中的页面改变时调用
        vp.setOnPageChangeListener(new MyPageChangeListener());
        //设置初始选择的页面，要在setadapter之后
        vp.setCurrentItem(currentItem);
        if (!isopen) {
            LogUtils.e("开");
            startScrollTask();
            isopen = true;
        }
    }

    public void runAsyncTask() {
        loadingDialog.show();
        GetAdCarouselFigureProtocol getAdCarouselFigureProtocol = new GetAdCarouselFigureProtocol();
        GetAdCarouselFigureRequest getAdCarouselFigureRequest = new GetAdCarouselFigureRequest();
        url = Constants.XINYONGSERVER_URL + getAdCarouselFigureProtocol.getApiFun();
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, getAdCarouselFigureRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                getAdCarouselFigureResponse = gson.fromJson(json, GetAdCarouselFigureResponse.class);
                LogUtils.e("getStoreTypeResponse:" + getAdCarouselFigureResponse.toString());
                if (getAdCarouselFigureResponse.code.equals("0")) {
                    setheadDate(getAdCarouselFigureResponse.dataList);
                } else {
                    if(!TextUtils.isEmpty(getAdCarouselFigureResponse.msg)){
                        DialogUtils.showAlertDialog(mContext,
                                getAdCarouselFigureResponse.msg);
                    }

                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!TextUtils.isEmpty(error)){
                    DialogUtils.showAlertDialog(mContext, error);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 填充ViewPager页面的适配器
     *
     * @author Administrator
     */
    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return getAdCarouselFigureResponse.dataList.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(imageViews.get(arg1));
            return imageViews.get(arg1);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {
//            currentItem = position;
//            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
//            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
//            oldPosition = position;

            if (dots.size() > 0) {
                currentItem = position % imageViews.size();
                dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
                dots.get(position % imageViews.size()).setBackgroundResource(R.drawable.dot_focused);
                oldPosition = position % imageViews.size();
            }

        }

        public void onPageScrollStateChanged(int arg0) {

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    /**
     * 换行切换任务
     */
    private class ScrollTask implements Runnable {

        public void run() {
            synchronized (vp) {
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
            }
        }

    }

    // 切换当前显示的图片
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            vp.setCurrentItem(currentItem);// 切换当前显示的图片
        }
    };

    public void startScrollTask() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 4, TimeUnit.SECONDS);
    }
}