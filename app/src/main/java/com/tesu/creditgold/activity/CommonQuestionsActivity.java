package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.CommonQuestionsAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.CommonQuestionBean;
import com.tesu.creditgold.response.GetCommonQuetionsResponse;
import com.tesu.creditgold.response.GetUnReadCountResponse;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 账户管理
 */
public class CommonQuestionsActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;

    private ListView lv_questions;
    private Dialog loadingDialog;
    private GetCommonQuetionsResponse getCommonQuetionsResponse;
    private List<CommonQuestionBean> commonQuestionBeanList;
    private CommonQuestionsAdapter commonQuestionsAdapter;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_common_questions, null);
        setContentView(rootView);
        lv_questions= (ListView) rootView.findViewById(R.id.lv_questions);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        initData();
        return null;
    }


    public void initData() {
        loadingDialog = DialogUtils.createLoadDialog(this, true);

        tv_top_back.setOnClickListener(this);

        lv_questions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonQuestionBean commonQuestionBean = commonQuestionBeanList.get(position);
                Intent intent = new Intent(CommonQuestionsActivity.this,CommonQuestionInfoActivity.class);
                intent.putExtra("commonQuestionBean",commonQuestionBean);
                UIUtils.startActivityNextAnim(intent);
            }
        });

        getCommonQuestion();
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

    /**
     * 查找所有的常见问题数据
     */
    public void getCommonQuestion() {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/question/selectAll.do";
        Map<String, String> params = new HashMap<String, String>();
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                LogUtils.e("查找所有的常见问题数据:" + json);
                getCommonQuetionsResponse = gson.fromJson(json, GetCommonQuetionsResponse.class);
                if (getCommonQuetionsResponse.getCode() == 0) {
                    commonQuestionBeanList = getCommonQuetionsResponse.getData();
                    commonQuestionsAdapter = new CommonQuestionsAdapter(CommonQuestionsActivity.this,commonQuestionBeanList);
                    lv_questions.setAdapter(commonQuestionsAdapter);

                } else {
                    DialogUtils.showAlertDialog(CommonQuestionsActivity.this, getCommonQuetionsResponse.getMsg());
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(CommonQuestionsActivity.this, error);
            }
        });
    }
}
