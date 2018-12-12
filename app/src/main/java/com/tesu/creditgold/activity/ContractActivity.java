package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.ArticleListAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.fragment.ControlTabFragment;
import com.tesu.creditgold.protocol.CreateUsrProtocol;
import com.tesu.creditgold.protocol.GetArticleListProtocol;
import com.tesu.creditgold.request.CreateUsrRequest;
import com.tesu.creditgold.request.GetArticleListRequest;
import com.tesu.creditgold.response.CreateUsrResponse;
import com.tesu.creditgold.response.GetArticleListResponse;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.UIUtils;

import java.util.HashMap;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 相关合同
 */
public class ContractActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;

    private String url;
    //接口请求菊花
    private Dialog loadingDialog;
    private ListView lv_article;
    private ArticleListAdapter articleListAdapter;
    private  GetArticleListResponse getArticleListResponse;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_contract, null);
        setContentView(rootView);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        lv_article=(ListView)rootView.findViewById(R.id.lv_article);
        initData();
        return null;
    }


    public void initData() {
        loadingDialog = DialogUtils.createLoadDialog(ContractActivity.this, true);
        tv_top_back.setOnClickListener(this);
        runAsyncTask();
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



    public void runAsyncTask() {
        loadingDialog.show();
        GetArticleListProtocol getArticleListProtocol = new GetArticleListProtocol();
        GetArticleListRequest getArticleListRequest = new GetArticleListRequest();
        url = Constants.XINYONGSERVER_URL+getArticleListProtocol.getApiFun();
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, getArticleListRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                getArticleListResponse = gson.fromJson(json, GetArticleListResponse.class);
                LogUtils.e("sendVerificationCodeResponse:" + json.toString());
                if (getArticleListResponse.code == 0) {
                    loadingDialog.dismiss();
                    if (articleListAdapter == null) {
                        articleListAdapter = new ArticleListAdapter(ContractActivity.this, getArticleListResponse.dataList);
                        lv_article.setAdapter(articleListAdapter);
                    }
                    lv_article.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(ContractActivity.this, XinaWebActivity.class);
                            intent.putExtra("article_title", getArticleListResponse.dataList.get(position).article_title);
                            intent.putExtra("logistics", getArticleListResponse.dataList.get(position).article_content);
                            UIUtils.startActivityNextAnim(intent);
                        }
                    });
                } else if(getArticleListResponse.code == 2){
                    Intent intent = new Intent(ContractActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }
                else {
                    loadingDialog.dismiss();
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(ContractActivity.this,
                                getArticleListResponse.msg);
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(ContractActivity.this, error);
                }
            }
        });
    }


}
