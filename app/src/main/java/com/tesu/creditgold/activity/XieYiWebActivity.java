package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.response.GetArticleByIdResponse;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;

import java.util.HashMap;
import java.util.Map;

public class XieYiWebActivity extends BaseActivity {
    private static final String TAG = XieYiWebActivity.class.getSimpleName();
    private View rootView;
    private WebView webView;
    //css显示图片样式
    private String CSS_STYPE = "<head><style>img{max-width:340px !important;}</style></head>";
    private TextView tv_contract_title;
    private TextView tv_top_back;
    private RelativeLayout rl_head;
    private int article_id;
    private Dialog loadingDialog;
    private GetArticleByIdResponse getArticleByIdResponse;
    private Gson gson;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_xina_web, null);
        setContentView(rootView);
        rl_head=(RelativeLayout)rootView.findViewById(R.id.rl_head);
        webView = (WebView) rootView.findViewById(R.id.webview);
        tv_top_back=(TextView)rootView.findViewById(R.id.tv_top_back);
        tv_contract_title=(TextView)rootView.findViewById(R.id.tv_contract_title);
        Intent intent = getIntent();
        article_id = intent.getIntExtra("article_id", 0);
        loadingDialog = DialogUtils.createLoadDialog(XieYiWebActivity.this, true);
        gson = new Gson();

        getArticleById();

        tv_top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });
        return null;
    }

    /**
     * 根据id获取合同
     */
    private void getArticleById() {
        loadingDialog.show();
        String url= Constants.XINYONGSERVER_URL+"credit_money_background/user/getArticleById.do";
        Map<String,String> params = new HashMap<String,String>();
        params.put("article_id", article_id+"");
        LogUtils.e("params:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Log.e(TAG, "获取合同:" + json);
                getArticleByIdResponse = gson.fromJson(json, GetArticleByIdResponse.class);
                if (getArticleByIdResponse.getCode() == 0) {
                    tv_contract_title.setText(getArticleByIdResponse.getData().getArticle_title());
                    webView.loadDataWithBaseURL(null, CSS_STYPE + getArticleByIdResponse.getData().getArticle_content(), "text/html", "utf-8", null);

                } else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(XieYiWebActivity.this, getArticleByIdResponse.getMsg());
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(XieYiWebActivity.this, error);
                }
            }
        });
    }


}
