package com.tesu.creditgold.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tesu.creditgold.R;
import com.tesu.creditgold.activity.SearchActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.protocol.GetAdCarouselFigureProtocol;
import com.tesu.creditgold.request.GetAdCarouselFigureRequest;
import com.tesu.creditgold.response.GetAdCarouselFigureResponse;
import com.tesu.creditgold.response.GetStoreTypeResponse;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.PictureOption;
import com.tesu.creditgold.util.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class ConsumptionAdapter extends BaseAdapter {

    private String TAG = "ClientListAdapter";
    private List<GetStoreTypeResponse.StoreType> sucDealGoodList;
    private ImageView imageView;
    private Context mContext;
    //布局类型
    private final int TYPE_ONE = 0, TYPE_TWO = 1, TYPE_COUNT = 2;
    private List<View> dots; // 图片标题正文的那些点
    private int oldPosition = 0;
    private ScheduledExecutorService scheduledExecutorService;
    private int currentItem = 0; // 当前图片的索引号
    private List<ImageView> imageViews; // 滑动的图片集合
    boolean isloaded;
    private Dialog loadingDialog;
    private String url;
    private boolean isopen = false;
    private GetAdCarouselFigureResponse getAdCarouselFigureResponse;
    private GridView gv;
    public ConsumptionAdapter(Context context, List<GetStoreTypeResponse.StoreType> sucDealGoodList, Dialog loadingDialog,GridView gv) {
        mContext = context;
        this.gv=gv;
        this.sucDealGoodList = sucDealGoodList;
        this.loadingDialog = loadingDialog;
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance()
                    .init(ImageLoaderConfiguration.createDefault(UIUtils
                            .getContext()));
        }
    }

    public void setLoading(boolean isloaded) {
        this.isloaded = isloaded;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return sucDealGoodList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

//    @Override
//    public int getItemViewType(int position) {
//        LogUtils.e("position:" + position);
//        if (position == 0) {
//            return TYPE_ONE;
//        } else {
//            return TYPE_TWO;
//        }
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return TYPE_COUNT;
//    }

    ConsumptionHeadHolder consumptionHeadHolder;
    ConsumptionFirstHolder consumptionFirstHolder;
    ViewHolder vh;


    public class ConsumptionFirstHolder {
        ImageView iv_one;
        ImageView iv_two;
        ImageView iv_three;
    }

    public class ConsumptionHeadHolder {
        ViewPager vp;
        LinearLayout ll_pager;
        View v_dot0;
        View v_dot1;
        View v_dot2;
        View v_dot3;
        View v_dot4;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        // 获取集合内的对象
        int type = getItemViewType(pos);
        if (view == null) {
//            switch (type) {
//                case TYPE_ONE:
//                    view = View.inflate(UIUtils.getContext(),
//                            R.layout.consumption_head_layout, null);
//                    consumptionHeadHolder = new ConsumptionHeadHolder();
//                    consumptionHeadHolder.vp = (ViewPager) view.findViewById(R.id.vp);
//                    consumptionHeadHolder.v_dot0 = (View) view.findViewById(R.id.v_dot0);
//                    consumptionHeadHolder.v_dot1 = (View) view.findViewById(R.id.v_dot1);
//                    consumptionHeadHolder.v_dot2 = (View) view.findViewById(R.id.v_dot2);
//                    consumptionHeadHolder.v_dot3 = (View) view.findViewById(R.id.v_dot3);
//                    consumptionHeadHolder.v_dot4 = (View) view.findViewById(R.id.v_dot4);
//                    consumptionHeadHolder.ll_pager = (LinearLayout) view.findViewById(R.id.ll_pager);
//                    view.setTag(consumptionHeadHolder);
//                    break;

//                case TYPE_TWO:
                    view = LayoutInflater.from(mContext).inflate(R.layout.consumption_two_item, null);
                    consumptionFirstHolder = new ConsumptionFirstHolder();
                    consumptionFirstHolder.iv_one = (ImageView) view.findViewById(R.id.iv_one);
//                    consumptionFirstHolder.iv_two = (ImageView) view.findViewById(R.id.iv_two);
//                    consumptionFirstHolder.iv_three = (ImageView) view.findViewById(R.id.iv_three);
                    view.setTag(consumptionFirstHolder);
//                    break;
//            }

        } else {
//            switch (type) {
//                case TYPE_ONE:
//                    consumptionHeadHolder = (ConsumptionHeadHolder) view.getTag();
//                    break;
//                case TYPE_TWO:
                    consumptionFirstHolder = (ConsumptionFirstHolder) view.getTag();
//                    break;
//            }
        }
//        switch (type) {
//            case TYPE_ONE:
//                gv.setNumColumns(1);
//                if (!isloaded) {
//                    runAsyncTask();
//                }
//                break;
//            case TYPE_TWO:
//                gv.setNumColumns(3);
                ClickListener clickListener = new ClickListener(pos);
                consumptionFirstHolder.iv_one.setOnClickListener(clickListener);
                ImageLoader.getInstance().displayImage(sucDealGoodList.get(pos).pic, consumptionFirstHolder.iv_one, PictureOption.getSimpleOptions());
//                switch (sucDealGoodList.get(1).size()){
//                    case 1: {
//                        ImageLoader.getInstance().displayImage(sucDealGoodList.get(1).get(0).pic, consumptionFirstHolder.iv_one, PictureOption.getSimpleOptions());
//                        consumptionFirstHolder.iv_one.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                LogUtils.e("点击:" + sucDealGoodList.get(1).get(0).name);
//                                Intent intent = new Intent(mContext, SearchActivity.class);
//                                intent.putExtra("store_type", sucDealGoodList.get(1).get(0).store_type);
//                                UIUtils.startActivityNextAnim(intent);
//                            }
//                        });
//                        consumptionFirstHolder.iv_two.setVisibility(View.INVISIBLE);
//                        consumptionFirstHolder.iv_three.setVisibility(View.INVISIBLE);
//                        break;
//                    }
//                    case 2:{
//                        consumptionFirstHolder.iv_three.setVisibility(View.INVISIBLE);
//                        ImageLoader.getInstance().displayImage(sucDealGoodList.get(1).get(0).pic, consumptionFirstHolder.iv_one, PictureOption.getSimpleOptions());
//                        ImageLoader.getInstance().displayImage(sucDealGoodList.get(1).get(1).pic, consumptionFirstHolder.iv_two, PictureOption.getSimpleOptions());
//                        consumptionFirstHolder.iv_one.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                LogUtils.e("点击:" + sucDealGoodList.get(1).get(0).name);
//                                Intent intent = new Intent(mContext, SearchActivity.class);
//                                intent.putExtra("store_type", sucDealGoodList.get(1).get(0).store_type);
//                                UIUtils.startActivityNextAnim(intent);
//                            }
//                        });
//                        consumptionFirstHolder.iv_two.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                LogUtils.e("点击:" + sucDealGoodList.get(1).get(1).name);
//                                Intent intent = new Intent(mContext, SearchActivity.class);
//                                intent.putExtra("store_type", sucDealGoodList.get(1).get(1).store_type);
//                                UIUtils.startActivityNextAnim(intent);
//                            }
//                        });
//                        break;
//                    }
//                    case 3: {
//                        ImageLoader.getInstance().displayImage(sucDealGoodList.get(1).get(0).pic, consumptionFirstHolder.iv_one, PictureOption.getSimpleOptions());
//                        ImageLoader.getInstance().displayImage(sucDealGoodList.get(1).get(1).pic, consumptionFirstHolder.iv_two, PictureOption.getSimpleOptions());
//                        ImageLoader.getInstance().displayImage(sucDealGoodList.get(1).get(2).pic, consumptionFirstHolder.iv_three, PictureOption.getSimpleOptions());
//                        consumptionFirstHolder.iv_one.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                LogUtils.e("点击:" + sucDealGoodList.get(1).get(0).name);
//                                Intent intent = new Intent(mContext, SearchActivity.class);
//                                intent.putExtra("store_type", sucDealGoodList.get(1).get(0).store_type);
//                                UIUtils.startActivityNextAnim(intent);
//                            }
//                        });
//                        consumptionFirstHolder.iv_two.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                LogUtils.e("点击:" + sucDealGoodList.get(1).get(1).name);
//                                Intent intent = new Intent(mContext, SearchActivity.class);
//                                intent.putExtra("store_type", sucDealGoodList.get(1).get(1).store_type);
//                                UIUtils.startActivityNextAnim(intent);
//                            }
//                        });
//                        consumptionFirstHolder.iv_three.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                LogUtils.e("点击:" + sucDealGoodList.get(1).get(2).name);
//                                Intent intent = new Intent(mContext, SearchActivity.class);
//                                intent.putExtra("store_type", sucDealGoodList.get(1).get(2).store_type);
//                                UIUtils.startActivityNextAnim(intent);
//                            }
//                        });
//                        break;
//                    }
//                }
//                if ((sucDealGoodList.size() - 1) % 3 == 0) {
//                    ClickListener clickListener = new ClickListener((pos - 1) * 3 + 1);
//                    ClickListener clickListener1 = new ClickListener((pos - 1) * 3 + 2);
//                    ClickListener clickListener2 = new ClickListener((pos - 1) * 3 + 3);
//                    consumptionFirstHolder.iv_one.setOnClickListener(clickListener);
//                    consumptionFirstHolder.iv_two.setOnClickListener(clickListener1);
//                    consumptionFirstHolder.iv_three.setOnClickListener(clickListener2);
//                    consumptionFirstHolder.iv_one.setVisibility(View.VISIBLE);
//                    consumptionFirstHolder.iv_two.setVisibility(View.VISIBLE);
//                    consumptionFirstHolder.iv_three.setVisibility(View.VISIBLE);
//                    ImageLoader.getInstance().displayImage(sucDealGoodList.get((pos - 1) * 3 + 1).pic, consumptionFirstHolder.iv_one, PictureOption.getSimpleOptions());
//                    ImageLoader.getInstance().displayImage(sucDealGoodList.get((pos - 1) * 3 + 2).pic, consumptionFirstHolder.iv_two, PictureOption.getSimpleOptions());
//                    ImageLoader.getInstance().displayImage(sucDealGoodList.get((pos - 1) * 3 + 3).pic, consumptionFirstHolder.iv_three, PictureOption.getSimpleOptions());
//                } else {
//                    if(getCount()-1==pos){
//                        LogUtils.e("pos:上"+(sucDealGoodList.size()-1)+"  "+pos*3);
//                        if (pos* 3- (sucDealGoodList.size()-1)==1) {
//                            ClickListener clickListener = new ClickListener((pos - 1) * 3 + 1);
//                            ClickListener clickListener1 = new ClickListener((pos - 1) * 3 + 2);
//                            consumptionFirstHolder.iv_one.setOnClickListener(clickListener);
//                            consumptionFirstHolder.iv_two.setOnClickListener(clickListener1);
//                            consumptionFirstHolder.iv_two.setVisibility(View.VISIBLE);
//                            consumptionFirstHolder.iv_three.setVisibility(View.INVISIBLE);
//                            ImageLoader.getInstance().displayImage(sucDealGoodList.get((pos - 1) * 3 + 1).pic, consumptionFirstHolder.iv_one, PictureOption.getSimpleOptions());
//                            ImageLoader.getInstance().displayImage(sucDealGoodList.get((pos - 1) * 3 + 2).pic, consumptionFirstHolder.iv_two, PictureOption.getSimpleOptions());
//                        }
//                        else{
//                            ClickListener clickListener = new ClickListener((pos - 1) * 3 + 1);
//                            consumptionFirstHolder.iv_one.setOnClickListener(clickListener);
//                            consumptionFirstHolder.iv_two.setVisibility(View.INVISIBLE);
//                            consumptionFirstHolder.iv_three.setVisibility(View.INVISIBLE);
//                            ImageLoader.getInstance().displayImage(sucDealGoodList.get((pos - 1) * 3 + 1).pic, consumptionFirstHolder.iv_one, PictureOption.getSimpleOptions());
//                        }
//                    }else{
//                        LogUtils.e("pos:"+pos+"  "+(sucDealGoodList.size()-1)+"     "+getCount());
//                        ClickListener clickListener = new ClickListener((pos - 1) * 3 + 1);
//                        ClickListener clickListener1 = new ClickListener((pos - 1) * 3 + 2);
//                        ClickListener clickListener2 = new ClickListener((pos - 1) * 3 + 3);
//                        consumptionFirstHolder.iv_one.setOnClickListener(clickListener);
//                        consumptionFirstHolder.iv_two.setOnClickListener(clickListener1);
//                        consumptionFirstHolder.iv_three.setOnClickListener(clickListener2);
//                        consumptionFirstHolder.iv_one.setVisibility(View.VISIBLE);
//                        consumptionFirstHolder.iv_two.setVisibility(View.VISIBLE);
//                        consumptionFirstHolder.iv_three.setVisibility(View.VISIBLE);
//                        ImageLoader.getInstance().displayImage(sucDealGoodList.get((pos - 1) * 3 + 1).pic, consumptionFirstHolder.iv_one, PictureOption.getSimpleOptions());
//                        ImageLoader.getInstance().displayImage(sucDealGoodList.get((pos - 1) * 3 + 2).pic, consumptionFirstHolder.iv_two, PictureOption.getSimpleOptions());
//                        ImageLoader.getInstance().displayImage(sucDealGoodList.get((pos - 1) * 3 + 3).pic, consumptionFirstHolder.iv_three, PictureOption.getSimpleOptions());
//                    }
//                }
//                else if ((sucDealGoodList.size() - 1) % 3 == 1) {
//                    ClickListener clickListener = new ClickListener(pos  + 0);
//                    consumptionFirstHolder.iv_one.setOnClickListener(clickListener);
//                    consumptionFirstHolder.iv_two.setVisibility(View.INVISIBLE);
//                    consumptionFirstHolder.iv_three.setVisibility(View.INVISIBLE);
//                    ImageLoader.getInstance().displayImage(sucDealGoodList.get((pos-1)*3 + 1).pic, consumptionFirstHolder.iv_one, PictureOption.getSimpleOptions());
//                } else if ((sucDealGoodList.size() - 1) % 3 == 2) {
//                    ClickListener clickListener = new ClickListener((pos-1)*3 + 1);
//                    ClickListener clickListener1 = new ClickListener((pos-1)*3 + 2);
//                    consumptionFirstHolder.iv_one.setOnClickListener(clickListener);
//                    consumptionFirstHolder.iv_two.setOnClickListener(clickListener1);
//                    consumptionFirstHolder.iv_two.setVisibility(View.VISIBLE);
//                    consumptionFirstHolder.iv_three.setVisibility(View.INVISIBLE);
//                    ImageLoader.getInstance().displayImage(sucDealGoodList.get((pos-1)*3 + 1).pic, consumptionFirstHolder.iv_one, PictureOption.getSimpleOptions());
//                    ImageLoader.getInstance().displayImage(sucDealGoodList.get((pos-1)*3 + 2).pic, consumptionFirstHolder.iv_two, PictureOption.getSimpleOptions());
//                }
//                break;
//        }

        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return sucDealGoodList.size();
    }


    class ViewHolder {
        ImageView iv_four;
    }

    public void setheadDate(List<GetAdCarouselFigureResponse.AdCarouselFigureBean> adsBeanList) {
        isloaded = true;
        imageViews = new ArrayList<ImageView>();

        // 初始化图片资源
        for (int i = 0; i < adsBeanList.size(); i++) {
            imageView = new ImageView(mContext);
            ImageLoader.getInstance().displayImage(adsBeanList.get(i).img, imageView, PictureOption.getSimpleOptions());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(imageView);
            LogUtils.e("加图" + adsBeanList.size() + "imageViews:" + imageViews.size());
        }


        dots = new ArrayList<View>();
        switch (adsBeanList.size()) {
            case 1:
                consumptionHeadHolder.v_dot0.setVisibility(View.GONE);
                consumptionHeadHolder.v_dot1.setVisibility(View.GONE);
                consumptionHeadHolder.v_dot2.setVisibility(View.GONE);
                consumptionHeadHolder.v_dot3.setVisibility(View.GONE);
                consumptionHeadHolder.v_dot4.setVisibility(View.GONE);
                break;
            case 2:
                dots.add(consumptionHeadHolder.v_dot0);
                dots.add(consumptionHeadHolder.v_dot1);
                consumptionHeadHolder.v_dot2.setVisibility(View.GONE);
                consumptionHeadHolder.v_dot3.setVisibility(View.GONE);
                consumptionHeadHolder.v_dot4.setVisibility(View.GONE);
                break;
            case 3:
                dots.add(consumptionHeadHolder.v_dot0);
                dots.add(consumptionHeadHolder.v_dot1);
                dots.add(consumptionHeadHolder.v_dot2);
                consumptionHeadHolder.v_dot3.setVisibility(View.GONE);
                consumptionHeadHolder.v_dot4.setVisibility(View.GONE);
                break;
            case 4:
                dots.add(consumptionHeadHolder.v_dot0);
                dots.add(consumptionHeadHolder.v_dot1);
                dots.add(consumptionHeadHolder.v_dot2);
                dots.add(consumptionHeadHolder.v_dot3);
                consumptionHeadHolder.v_dot4.setVisibility(View.GONE);
                break;
            case 5:
                dots.add(consumptionHeadHolder.v_dot0);
                dots.add(consumptionHeadHolder.v_dot1);
                dots.add(consumptionHeadHolder.v_dot2);
                dots.add(consumptionHeadHolder.v_dot3);
                dots.add(consumptionHeadHolder.v_dot4);
                break;
            default:
                break;
        }


        consumptionHeadHolder.vp.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
        // 设置一个监听器，当ViewPager中的页面改变时调用
        consumptionHeadHolder.vp.setOnPageChangeListener(new MyPageChangeListener());
        //设置初始选择的页面，要在setadapter之后
        consumptionHeadHolder.vp.setCurrentItem(currentItem);
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

                Gson gson = new Gson();
                getAdCarouselFigureResponse = gson.fromJson(json, GetAdCarouselFigureResponse.class);
                LogUtils.e("loginResponse:" + getAdCarouselFigureResponse.toString());
                if (getAdCarouselFigureResponse.code.equals("0")) {
                    loadingDialog.dismiss();
                    setheadDate(getAdCarouselFigureResponse.dataList);
                } else {
                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(mContext,
                            getAdCarouselFigureResponse.msg);

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
            synchronized (consumptionHeadHolder.vp) {
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
            }
        }

    }

    // 切换当前显示的图片
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            consumptionHeadHolder.vp.setCurrentItem(currentItem);// 切换当前显示的图片
        }
    };

    public void startScrollTask() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 4, TimeUnit.SECONDS);
    }

    /**
     * 点击事件
     */
    @SuppressLint("NewApi")
    private class ClickListener implements View.OnClickListener {

        /**
         * 选择的某项
         */
        public int position = 0;

        public ClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, SearchActivity.class);
            intent.putExtra("store_type", sucDealGoodList.get(position).store_type);
            UIUtils.startActivityNextAnim(intent);
        }
    }
}
