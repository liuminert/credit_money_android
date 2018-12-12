package com.tesu.creditgold.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.protocol.GetAppVersionInfoProtocol;
import com.tesu.creditgold.request.GetAppVersionInfoRequest;
import com.tesu.creditgold.response.GetAppVersionInfoResponse;
import com.tesu.creditgold.response.GetArticleByIdResponse;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.util.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 账户管理
 */
public class AboutCreditActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;

    private RelativeLayout rl_update;
    private RelativeLayout rl_about;
    private ImageView iv_update_point;
    private GetAppVersionInfoResponse getAppVersionInfoResponse;
    private boolean hasNewVersion;
    private AlertDialog alertDialog;
    private Dialog loadingDialog;
    private GetArticleByIdResponse getArticleByIdResponse;
    private String updateStr;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_about_credit, null);
        setContentView(rootView);
        rl_update= (RelativeLayout) rootView.findViewById(R.id.rl_update);
        rl_about= (RelativeLayout) rootView.findViewById(R.id.rl_about);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        iv_update_point = (ImageView) rootView.findViewById(R.id.iv_update_point);
        initData();
        return null;
    }


    public void initData() {
        loadingDialog = DialogUtils.createLoadDialog(this, true);

        getAppVersionInfo();

        tv_top_back.setOnClickListener(this);
        rl_update.setOnClickListener(this);
        rl_about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_update:
                if(hasNewVersion){
                    getArticleById(Constants.updateStrId);
                }else {
                    UIUtils.showToastCenter(AboutCreditActivity.this,"当前已是最新版本");
                }
                break;
            case R.id.tv_top_back:
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.rl_about:
                Intent intent = new Intent(AboutCreditActivity.this,AboutActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
        }
    }

    public void getAppVersionInfo() {
        GetAppVersionInfoProtocol getAppVersionInfoProtocol = new GetAppVersionInfoProtocol();
        GetAppVersionInfoRequest getAppVersionInfoRequest = new GetAppVersionInfoRequest();
        getAppVersionInfoRequest.map.put("platform", "android");
        String url = Constants.XINYONGSERVER_URL +  getAppVersionInfoProtocol.getApiFun();
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, getAppVersionInfoRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                try {
                    Gson gson = new Gson();
                    getAppVersionInfoResponse = gson.fromJson(json, GetAppVersionInfoResponse.class);
                    LogUtils.e("更新" + json.toString());
                    if (getAppVersionInfoResponse.code == 0) {
                        if (getAppVersionInfoResponse.data.newest_version > Double.parseDouble(Utils.getVersionName(AboutCreditActivity.this))) {
                            hasNewVersion = true;
                            iv_update_point.setVisibility(View.VISIBLE);
                        }else {
                            hasNewVersion = false;
                            iv_update_point.setVisibility(View.GONE);
                        }
                    } else {
                        if (!TextUtils.isEmpty(getAppVersionInfoResponse.msg)) {
                            if (!isFinishing()) {
                                DialogUtils.showAlertDialog(AboutCreditActivity.this, getAppVersionInfoResponse.msg);
                            }
                        }
                    }
                } catch (JsonSyntaxException e) {

                }


            }

            @Override
            public void dealWithError(String address, String error) {
            }
        });
    }


    /**
     * 根据id获取合同
     */
    private void getArticleById(int article_id) {
        loadingDialog.show();
        String url= Constants.XINYONGSERVER_URL+"credit_money_background/user/getArticleById.do";
        Map<String,String> params = new HashMap<String,String>();
        params.put("article_id", article_id+"");
        LogUtils.e("params:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("获取合同:" + json);
                getArticleByIdResponse = new Gson().fromJson(json, GetArticleByIdResponse.class);
                if (getArticleByIdResponse.getCode() == 0) {
                    updateStr = getArticleByIdResponse.getData().getArticle_content();
                    if(!isFinishing()){
                        if(getAppVersionInfoResponse.data.force_update==0){
                            alertDialog = DialogUtils.showUpdateDialog(AboutCreditActivity.this,updateStr, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    switch (v.getId()){
                                        case R.id.tv_cancel:
                                            alertDialog.dismiss();
                                            break;
                                        case R.id.tv_update:
                                            Uri uri = Uri.parse(getAppVersionInfoResponse.data.download_url);
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            startActivity(intent);
                                            break;
                                    }
                                }
                            },false);
                        }else {
                            alertDialog = DialogUtils.showUpdateDialog(AboutCreditActivity.this, updateStr, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    switch (v.getId()){
                                        case R.id.tv_cancel:
                                            alertDialog.dismiss();
//                                            finish();
                                            finishAll();
                                            break;
                                        case R.id.tv_update:
                                            Uri uri = Uri.parse(getAppVersionInfoResponse.data.download_url);
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            startActivity(intent);
                                            break;
                                    }
                                }
                            }, true);
                        }
                    }
                } else {
                    if(!isFinishing()){
                        DialogUtils.showAlertDialog(AboutCreditActivity.this, getArticleByIdResponse.getMsg());
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if(!isFinishing()){
                    DialogUtils.showAlertDialog(AboutCreditActivity.this, error);
                }
            }
        });
    }

}
