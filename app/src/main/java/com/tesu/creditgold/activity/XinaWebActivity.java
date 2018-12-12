package com.tesu.creditgold.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.util.LogUtils;

public class XinaWebActivity extends BaseActivity {
    private View rootView;
    private WebView webView;
    private String sina_whithhold_url;
    //css显示图片样式
    private String CSS_STYPE = "<head><style>img{max-width:340px !important;}</style></head>";
    private String logistics;
    private TextView tv_contract_title;
    private TextView tv_top_back;
    private RelativeLayout rl_head;
    private String article_title;
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
        sina_whithhold_url = intent.getStringExtra("sina_whithhold_url");
        logistics=intent.getStringExtra("logistics");
        article_title=intent.getStringExtra("article_title");
        LogUtils.e("logistics:"+logistics);
        LogUtils.e("article_title:"+article_title);
        if(TextUtils.isEmpty(logistics)) {
            tv_contract_title.setText("设置还款账户");
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient() { // 通过webView打开链接，不调用系统浏览器

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onLoadResource(WebView view, String url) {
                    if (url.startsWith("http://www.baidu.com")) {
                        Intent intent = getIntent();
                        setResult(201, intent);
                        XinaWebActivity.this.finish();
                    }
                    super.onLoadResource(view, url);
                }
            });
            webView.loadUrl(sina_whithhold_url);//需要做成apk的web网址
        }
        else{
            tv_contract_title.setText(article_title);
            rl_head.setVisibility(View.VISIBLE);
            if(logistics.startsWith("http://")){
                webView.loadUrl(logistics);
            }else {
                webView.loadDataWithBaseURL(null, CSS_STYPE + logistics, "text/html", "utf-8", null);
            }
        }
        tv_top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });
        return null;
    }

}
