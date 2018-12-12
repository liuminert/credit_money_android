package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tesu.creditgold.R;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.PictureOption;
import com.tesu.creditgold.util.UIUtils;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 店铺二维码
 */
public class CodeActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;
    private String qr_pic;
    private String store_name;
    private TextView tv_shop_name;
    private ImageView iv_code;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_code, null);
        setContentView(rootView);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_shop_name= (TextView) rootView.findViewById(R.id.tv_shop_name);
        iv_code=(ImageView)rootView.findViewById(R.id.iv_code);
        initData();
        return null;
    }


    public void initData() {
        Intent intent=getIntent();
        qr_pic=intent.getStringExtra("qr_pic");
        store_name=intent.getStringExtra("store_name");
        LogUtils.e("qr_pic:" + qr_pic);
        if(!TextUtils.isEmpty(store_name)){
            tv_shop_name.setText(store_name);
        }
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance()
                    .init(ImageLoaderConfiguration.createDefault(UIUtils
                            .getContext()));
        }
        tv_top_back.setOnClickListener(this);
        ImageLoader.getInstance().displayImage(qr_pic, iv_code, PictureOption.getSimpleOptions());
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



//    public void runAsyncTask() {
//        loadingDialog.show();
//        CreateUsrProtocol createUsrProtocol = new CreateUsrProtocol();
//        CreateUsrRequest createUsrRequest = new CreateUsrRequest();
//        url = createUsrProtocol.getApiFun();
//        createUsrRequest.map.put("usrid", SharedPrefrenceUtils.getString(ContractActivity.this,"usrid"));
//        createUsrRequest.map.put("usrname", usrname);
//        createUsrRequest.map.put("id_card", id_card);
//        MyVolley.uploadNoFile(MyVolley.POST, url, createUsrRequest.map, new MyVolley.VolleyCallback() {
//            @Override
//            public void dealWithJson(String address, String json) {
//
//                Gson gson = new Gson();
//                CreateUsrResponse createUsrResponse = gson.fromJson(json, CreateUsrResponse.class);
//                LogUtils.e("sendVerificationCodeResponse:" + json.toString());
//                if (createUsrResponse.code.equals("0")) {
//                    loadingDialog.dismiss();
//                    Intent intent = getIntent();
//                    intent.putExtra("sina_whithhold_url",createUsrResponse.sina_whithhold_url);
//                    setResult(200,intent);
//                    finish();
//                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
//                } else {
//                    loadingDialog.dismiss();
//                    DialogUtils.showAlertDialog(ContractActivity.this,
//                            createUsrResponse.msg);
//                }
//
//            }
//
//            @Override
//            public void dealWithError(String address, String error) {
//                loadingDialog.dismiss();
//                DialogUtils.showAlertDialog(ContractActivity.this, error);
//            }
//        });
//    }


}
