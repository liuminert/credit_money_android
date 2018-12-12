package com.tesu.creditgold.activity;

import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.util.Utils;
import com.tesu.creditgold.widget.StartCustomTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 关于我们页面
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;
    private TextView tv_version;
    private TextView tv_content;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_about, null);
        setContentView(rootView);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_version = (TextView) rootView.findViewById(R.id.tv_version);
        tv_content=(TextView)rootView.findViewById(R.id.tv_content);
        initData();
        return null;
    }


    public void initData() {
        tv_version.setText("秒赊 • 版本号 v" + Utils.getVersionName(this));
        tv_top_back.setOnClickListener(this);
        tv_content.setText("\u3000\u3000全木行是为传统家具行业提供辅助性销售的综合服务平台，是深圳特速集团践行“融汇财富、产业帮扶”经营理念的一个子品牌，是传统木材与木制品流通产业在进行产业升级中诞生的优秀模式。 全木行平台依托集团优势，立足家具垂直细分领域消费分期服务，围绕产业链上下游，拓展到泛家居消费金融市场。顺势而发，适时推出秒赊APP，聚力互联网和消费金融市场，全方位融入线下消费场景，以扩大消费者需求为切入点，带动消费潜在市场，促进交易增长，实现对合作商家的帮扶。");
        tv_content.setTextColor(UIUtils.getColor(R.color.text_color_gray));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
        }
    }
}
