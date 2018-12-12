package com.tesu.creditgold.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tesu.creditgold.R;
import com.tesu.creditgold.activity.LoginActivity;
import com.tesu.creditgold.base.BaseFragment;
import com.tesu.creditgold.base.TabBasePager;
import com.tesu.creditgold.base.ViewTabBasePager;
import com.tesu.creditgold.tabpager.TabConsumptionPager;
import com.tesu.creditgold.tabpager.TabMyselfPager;
import com.tesu.creditgold.tabpager.TabScanPager;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.LoginUtils;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.zxing.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: 许明达
 * @创建时间: 2015年3月31日下午3:14:18
 * @版权: 微位科技版权所有
 * @描述: 控制侧滑菜单以及 附近 找店 发现 我的四个页面
 */
public class ControlTabFragment extends BaseFragment implements
        OnCheckedChangeListener {

//    @ViewInject(R.id.rg_content)
    private RadioGroup mRadioGroup;

//    @ViewInject(R.id.rb_consumption_installment)
    private RadioButton rb_consumption_installment;
//    @ViewInject(R.id.rb_scan_installment)
    private RadioButton rb_scan_installment;
//    @ViewInject(R.id.rb_my_installment)
    private RadioButton rb_my_installment;

    // 内容区域
//    @ViewInject(R.id.fl_content_fragment)
    private FrameLayout mFrameLayout;
    // 底部区域
//    @ViewInject(R.id.fl_bottom)
    private FrameLayout bFrameLayout;
//    @ViewInject(R.id.dl)
    private FrameLayout mDragLayout;

    // 处理事件分发的自定义LinearLayout
//    @ViewInject(R.id.my_ll)
    private LinearLayout mLinearLayout;
//    @ViewInject(R.id.lv)
    private ListView lv;

    // 默认选中第0页面
    public static int mCurrentIndex = 0;
    // 默认选中第0页面
    public static int beforeIndex = 0;
    // 中间变量
    public static int temp = 0;
    // 底部页面的集合
    private List<ViewTabBasePager> mPagerList;
    private TabConsumptionPager tabConsumptionPager;
    private TabScanPager tabScanPager;
    private TabMyselfPager tabMyselfPager;
    List<RadioButton> radioButtonList;
    public static boolean isLogin = false;
    private final int SCANER_CODE = 1;

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.control_tab, null);
        // Viewutil工具的注入
//        ViewUtils.inject(this, view);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.rg_content);
        rb_consumption_installment = (RadioButton) view.findViewById(R.id.rb_consumption_installment);
        rb_scan_installment = (RadioButton) view.findViewById(R.id.rb_scan_installment);
        rb_my_installment = (RadioButton) view.findViewById(R.id.rb_my_installment);
        mFrameLayout = (FrameLayout) view.findViewById(R.id.fl_content_fragment);
        bFrameLayout = (FrameLayout) view.findViewById(R.id.fl_bottom);
        mDragLayout = (FrameLayout) view.findViewById(R.id.dl);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.my_ll);
        lv = (ListView) view.findViewById(R.id.lv);
        return view;
    }

    @Override
    protected void initData() {

        // 添加实际的页面
        mPagerList = new ArrayList<ViewTabBasePager>();
        tabConsumptionPager = new TabConsumptionPager(mActivity);
        mPagerList.add(tabConsumptionPager);
        //商店
        tabScanPager = new TabScanPager(mActivity);
        mPagerList.add(tabScanPager);
        // 晒美甲
        tabMyselfPager = new TabMyselfPager(mActivity);
        mPagerList.add(tabMyselfPager);
        // 给RadioGroup 设置监听
        getmRadioGroup().setOnCheckedChangeListener(this);
        if (radioButtonList == null) {
            radioButtonList = new ArrayList<RadioButton>();
            radioButtonList.add(rb_consumption_installment);
            radioButtonList.add(rb_scan_installment);
            radioButtonList.add(rb_my_installment);
        }
        switchCurrentPage();

    }

    public void setChecked(int item) {
        switch (item) {
            case 0:
                if(rb_consumption_installment!=null){
                    rb_consumption_installment.setChecked(true);
                }
                break;
            case 1:
                if(rb_scan_installment != null){
                    rb_scan_installment.setChecked(true);
                }
                break;
            case 2:
                if(rb_my_installment != null){
                    rb_my_installment.setChecked(true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // 根据选中的RadioButton的id切换页面
        switch (checkedId) {
            case R.id.rb_consumption_installment:
                beforeIndex = 0;
                mCurrentIndex = 0;
                break;
            case R.id.rb_scan_installment:
                beforeIndex = 1;
                mCurrentIndex = 1;
                break;
            case R.id.rb_my_installment:
                beforeIndex = 2;
                mCurrentIndex = 2;
                break;
            default:
                break;
        }
        switchCurrentPage();
    }

    /**
     * 切换RadioGroup对应的页面
     */
    public void switchCurrentPage() {
        if(mFrameLayout != null){
            mFrameLayout.removeAllViews();
            ViewTabBasePager tabBasePager = mPagerList.get(mCurrentIndex);
            // 获得每个页面对应的布局
            View rootView = tabBasePager.getRootView();

            // 填充数据
            if (mCurrentIndex == 1) {
                tabBasePager.initData();
                mFrameLayout.addView(rootView);
                Intent openCameraIntent = new Intent(UIUtils.getContext(),
                        CaptureActivity.class);
                UIUtils.startActivityForResult(openCameraIntent, SCANER_CODE);

            }
            else if(mCurrentIndex == 2){
                // 没有登录 登录页面
                TabBasePager tabLoginPager;

                if (LoginUtils.isLogin()) {
                    View myselfView = tabBasePager.getRootView();
                    tabBasePager.initData();
                    mFrameLayout.addView(myselfView);
                    tabMyselfPager.Update();
                } else {
                    // 进入登录注册页面
                    Intent inent=new Intent(UIUtils.getContext(), LoginActivity.class);
                    UIUtils.startActivityNextAnim(inent);
                }
            }
            else {
                tabBasePager.initData();
                mFrameLayout.addView(rootView);
            }
            setSelectedColor(mCurrentIndex);
        }
    }

    public RadioGroup getmRadioGroup() {
        return mRadioGroup;
    }

    public TabConsumptionPager getTabConsumptionPagerr() {
        return tabConsumptionPager;
    }

    public TabScanPager getTabScanPager() {
        return tabScanPager;
    }

    public TabMyselfPager getTabMyselfbyPager() {
        return tabMyselfPager;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void setSelectedColor(int item) {
        for (int i = 0; i < radioButtonList.size(); i++) {
            RadioButton radioButton = radioButtonList.get(i);
            if (i == item) {
                //渐变色
                Shader shader = new LinearGradient(0, 0, 0, 60,UIUtils.getColor(R.color.default_button_color), UIUtils.getColor(R.color.default_button_color), Shader.TileMode.CLAMP);
                radioButton.getPaint().setShader(shader);
            } else {
                Shader shader = new LinearGradient(0, 0, 0, 0, Color.BLACK, Color.BLACK, Shader.TileMode.CLAMP);
                radioButton.getPaint().setShader(shader);
            }
            radioButton.postInvalidate();
        }
    }

}
