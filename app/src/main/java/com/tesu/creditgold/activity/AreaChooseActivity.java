package com.tesu.creditgold.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.MyFragmentAdapter;
import com.tesu.creditgold.adapter.MyPagerAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.bean.AreaBean;
import com.tesu.creditgold.fragment.AreaFragmentOne;
import com.tesu.creditgold.fragment.AreaFragmentThree;
import com.tesu.creditgold.fragment.AreaFragmentTwo;
import com.tesu.creditgold.util.UIUtils;

import android.view.ViewGroup.LayoutParams;


import java.util.ArrayList;
import java.util.List;

public class AreaChooseActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener,AreaFragmentTwo.ChooseArea{
    private View rootView;
    private TextView tv_top_back;
    private ViewPager myViewPager; // 要使用的ViewPager
    private View page1, page2, page3; // ViewPager包含的页面
    private List<View> pageList; // ViewPager包含的页面列表，一般给adapter传的是一个list
    private MyPagerAdapter myPagerAdapter; // 适配器
    private TextView tv_tab0, tv_tab1, tv_tab2; // 3个选项卡
    private ImageView line_tab; // tab选项卡的下划线
    private int moveOne = 0; // 下划线移动一个选项卡
    private boolean isScrolling = false; // 手指是否在滑动
    private boolean isBackScrolling = false; // 手指离开后的回弹
    private long startTime = 0;
    private long currentTime = 0;
    private AreaFragmentOne areaFragmentOne;
    private AreaFragmentTwo areaFragmentTwo;
    private AreaFragmentThree areaFragmentThree;
    private AreaBean provinceBean;
    private AreaBean cityBean;
    private AreaBean areaBean;


    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_area_choose, null);
        setContentView(rootView);

        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);

        myViewPager = (ViewPager) findViewById(R.id.myViewPager);

        areaFragmentOne = new AreaFragmentOne();
        areaFragmentTwo = new AreaFragmentTwo();
        areaFragmentThree = new AreaFragmentThree();

        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(areaFragmentOne);
        fragmentList.add(areaFragmentTwo);
        fragmentList.add(areaFragmentThree);

        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragmentList);
        myViewPager.setAdapter(myFragmentAdapter);

        tv_tab0 = (TextView) findViewById(R.id.tv_tab0);
        tv_tab1 = (TextView) findViewById(R.id.tv_tab1);
        tv_tab2 = (TextView) findViewById(R.id.tv_tab2);
        myViewPager.setCurrentItem(0);
        tv_tab0.setTextColor(UIUtils.getColor(R.color.default_button_color));
        tv_tab1.setTextColor(Color.BLACK);
        tv_tab2.setTextColor(Color.BLACK);
        tv_tab0.setOnClickListener(this);
        tv_tab1.setOnClickListener(this);
        tv_tab2.setOnClickListener(this);
        myViewPager.setOnPageChangeListener(this);
        line_tab = (ImageView) findViewById(R.id.line_tab);

        tv_top_back.setOnClickListener(this);
        initData();

        return null;
    }

    private void initData() {
        /** * 获取屏幕的宽度 */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;

        /** * 重新设置下划线的宽度 */
        LayoutParams lp = line_tab.getLayoutParams();
        lp.width = screenW / 3;
        line_tab.setLayoutParams(lp);
        moveOne = lp.width; // 滑动一个页面的距离

        Intent intent = getIntent();
        provinceBean = (AreaBean) intent.getSerializableExtra("provinceBean");
        cityBean = (AreaBean) intent.getSerializableExtra("cityBean");
        areaBean = (AreaBean) intent.getSerializableExtra("areaBean");

        if(provinceBean != null && cityBean != null && areaBean != null){
            areaFragmentOne.setChooseArea(provinceBean);
            tv_tab0.setText(provinceBean.getArea_name());

            areaFragmentTwo.setChooseArea(provinceBean,cityBean);
            tv_tab1.setText(cityBean.getArea_name());
            tv_tab1.setVisibility(View.VISIBLE);

            areaFragmentThree.setChooseArea(provinceBean,cityBean,areaBean);
            tv_tab2.setText(areaBean.getArea_name());
            tv_tab2.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_back:
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.tv_tab0:
                myViewPager.setCurrentItem(0);
                areaFragmentOne.setOldPostion();
                break;
            case R.id.tv_tab1:
                myViewPager.setCurrentItem(1);
                areaFragmentTwo.setOldPostion();
                break;
            case R.id.tv_tab2:
                myViewPager.setCurrentItem(2);
                break;
            default:
                break;

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        currentTime = System.currentTimeMillis();
        if (isScrolling && (currentTime - startTime > 200)) {
            movePositionX(position, moveOne * positionOffset);
            startTime = currentTime;
        }
        if (isBackScrolling) {
            movePositionX(position);
        }

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                tv_tab0.setTextColor(UIUtils.getColor(R.color.default_button_color));
                tv_tab1.setTextColor(Color.BLACK);
                tv_tab2.setTextColor(Color.BLACK);
                areaFragmentOne.setOldPostion();
                movePositionX(0);
                break;
            case 1:
                tv_tab0.setTextColor(Color.BLACK);
                tv_tab1.setTextColor(UIUtils.getColor(R.color.default_button_color));
                tv_tab2.setTextColor(Color.BLACK);
                areaFragmentTwo.setOldPostion();
                movePositionX(1);
                break;
            case 2:
                tv_tab0.setTextColor(Color.BLACK);
                tv_tab1.setTextColor(Color.BLACK);
                tv_tab2.setTextColor(UIUtils.getColor(R.color.default_button_color));
                movePositionX(2);
                break;
            default:
                break;
        }

    }

    /**
     * 下划线跟随手指的滑动而移动
     * * @param toPosition
     * * @param positionOffsetPixels
     */
    private void movePositionX(int toPosition, float positionOffsetPixels) {
        // TODO Auto-generated method stub
        float curTranslationX = line_tab.getTranslationX();
        float toPositionX = moveOne * toPosition + positionOffsetPixels;
        ObjectAnimator animator = ObjectAnimator.ofFloat(line_tab, "translationX", curTranslationX, toPositionX);
        animator.setDuration(500);
        animator.start();
    }

    /**
     * 下划线滑动到新的选项卡中
     * * @param toPosition
     */
    private void movePositionX(int toPosition) {
        // TODO Auto-generated method stub
        movePositionX(toPosition, 0);
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case 1:
                isScrolling = true;
                isBackScrolling = false;
                break;
            case 2:
                isScrolling = false;
                isBackScrolling = true;
                break;
            default:
                isScrolling = false;
                isBackScrolling = false;
                break;
        }

    }

    @Override
    public void setArea(AreaBean areaBean,int type) {
       switch (type){
           case 0:
//               tv_tab0.setText("省");
               break;
           case 1:
               provinceBean = areaBean;
               tv_tab0.setText(areaBean.getArea_name());
               tv_tab1.setText("请选择");
               tv_tab1.setVisibility(View.VISIBLE);
               tv_tab2.setVisibility(View.GONE);
               areaFragmentTwo.getAreaList(provinceBean.getArea_id());

               break;
           case 2:
               cityBean = areaBean;
               tv_tab1.setText(cityBean.getArea_name());
               tv_tab2.setText("请选择");
               tv_tab2.setVisibility(View.VISIBLE);

               areaFragmentThree.getAreaList(provinceBean,cityBean);
               break;
       }

        myViewPager.setCurrentItem(type);
    }
}
