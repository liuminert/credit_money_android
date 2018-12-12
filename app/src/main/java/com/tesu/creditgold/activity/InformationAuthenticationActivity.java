package com.tesu.creditgold.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.AreaAdapter;
import com.tesu.creditgold.adapter.DictionaryAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.AreaBean;
import com.tesu.creditgold.bean.BondsmaninfBean;
import com.tesu.creditgold.bean.DictionaryBean;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.GetAreaListResponse;
import com.tesu.creditgold.bean.InterestsBean;
import com.tesu.creditgold.fragment.ControlTabFragment;
import com.tesu.creditgold.response.GetSupportBankListResponse;
import com.tesu.creditgold.response.GetUsrBankListResponse;
import com.tesu.creditgold.support.PercentLinearLayout;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LimitInputTextWatcher;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.StringUtils;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.util.Utils;
import com.tesu.creditgold.widget.LimitEditText;
import com.tesu.creditgold.widget.SupportBankListDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InformationAuthenticationActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = InformationAuthenticationActivity.class.getSimpleName();
    private View rootView;
    private TextView tv_top_back;
    private ControlTabFragment ctf;
    private Dialog loadingDialog;

    private EditText et_phone_number;  //手机
    private TextView tv_is_married;   //婚姻状况
    //    private TextView tv_province;   //省
//    private TextView tv_city;        //市
    private TextView tv_area;        //区
    private TextView tv_job_industry;  //现工作单位行业
    private TextView tv_job;         //职业
    private TextView tv_job_years;   //现单位工作年限
    private TextView tv_year_income;   //年收入
    private EditText et_bank_id;  //银行卡
    private TextView tv_contacts_one; //联系人1关系
    private LimitEditText et_name_one;    //联系人1姓名
    private EditText et_phone_number_one;  //联系人1电话
    private TextView tv_contacts_two; //联系人2关系
    private LimitEditText et_name_two;    //联系人2姓名
    private EditText et_phone_number_two;  //联系人2电话
    private TextView tv_contacts_three; //联系人3关系
    private LimitEditText et_name_three;    //联系人3姓名
    private EditText et_phone_number_three;  //联系人3电话
    private Button btn_next;

    private String phoneNumber;
    private String nameOne;
    private String phoneNumberOne;
    private String nameTwo;
    private String phoneNumberTwo;
    private String nameThree;
    private String phoneNumberThree;
    private String bankId;

    private FkBaseResponse fkbaseResponse;
    private String typeName;  //数据字典类型
    private Gson gson;
    private LayoutInflater mInflater;
    private PopupWindow dictonaryWindow;
    private View dictonaryRoot;
    private ListView lv_choose;
    private Button btn_cancel;
    private ArrayList<DictionaryBean> dictionaryBeanList;
    private int chooseType;  //选择类型  0,婚姻状况   1,现工作单位行业  2,职业  3,现单位工作年限  4,年收入
    //5,联系人1关系  6,联系人2关系  7,联系人3关系
    private DictionaryBean marriedDictonaryBean;  //婚姻状况
    private DictionaryBean jobIndustryDictonaryBean;  //现工作单位行业
    private DictionaryBean jobDictonaryBean;  //职业
    private DictionaryBean jobYearsDictonaryBean;  //现单位工作年限
    private DictionaryBean yearIncomeDictonaryBean;  //年收入
    private DictionaryBean contactsOneDictonaryBean;  //联系人1关系
    private DictionaryBean contactsTwoDictonaryBean;  //联系人2关系
    private DictionaryBean contactsThreeDictonaryBean;  //联系人3关系
    private DictionaryBean diplomaDictonaryBean;  //学历
    private AreaBean provinceBean;
    private AreaBean cityBean;
    private AreaBean areaBean;
    private AreaBean houseProvinceBean;
    private AreaBean houseCityBean;
    private AreaBean houseAreaBean;
    private GetAreaListResponse getAreaListResponse;
    private ArrayList<AreaBean> areaBeanArrayList;
    private String userId;
    private String name;
    private String idNumber;
    private List<BondsmaninfBean> bondsmaninfBeanList; //担保人信息列表(联系人)
    private InterestsBean interestsBean;
    private String store_name;
    private int store_id;
    private int store_type;  //店铺类型(1.家具 2.装修 3.旅游 100.其它)
    private String store_contract;
    private String store_tel;
    private String amount;
    private int store_uid;
    private int is_tiexi;
    private int gather_model_id;
    private TextView tv_diploma;
    private String usr_order_id;

    private TextView tv_phone;
    private TextView tv_diploma_show;
    private TextView tv_is_married_show;
    private TextView tv_area_show;
    private TextView tv_job_industry_show;
    private TextView tv_job_show;
    private TextView tv_job_years_show;
    private TextView tv_year_income_show;
    private TextView tv_bank_id_show;
    private TextView tv_contacts_one_show;
    private TextView tv_contacts_two_show;
    private TextView tv_contacts_three_show;

    private TextView tv_house_address;
    //    private TextView tv_house_province;
//    private TextView tv_house_city;
    private TextView tv_house_area;
    private EditText et_address;
    private TextView tv_job_name_show;
    private EditText et_job_name;
    private String detailedAddress;
    private String houseAddress;
    private String jobName;
    //    private boolean isCardCorrect;   //银行卡号是否正确
    private TextView tv_support_id_card;
    private GetUsrBankListResponse getUsrBankListResponse;
    private GetSupportBankListResponse getSupportBankListResponse;

    private int oldPosition;
    private boolean isFromRecord;

    private String dictionaryTitle;
    private TextView tv_title;
    private TextView tv_complete;

    private Drawable nav_up;
    private String areaStr;
    private String houseAreaStr;
    private String amortization_money;
    private int order_from; //订单来源 0：买家下单，1：卖家下单

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_information_authentication, null);
        setContentView(rootView);

        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        et_phone_number = (EditText) rootView.findViewById(R.id.et_phone_number);
        tv_is_married = (TextView) rootView.findViewById(R.id.tv_is_married);
//        tv_province = (TextView) rootView.findViewById(R.id.tv_province);
//        tv_city = (TextView) rootView.findViewById(R.id.tv_city);
        tv_area = (TextView) rootView.findViewById(R.id.tv_area);
        tv_job_industry = (TextView) rootView.findViewById(R.id.tv_job_industry);
        tv_job = (TextView) rootView.findViewById(R.id.tv_job);
        tv_job_years = (TextView) rootView.findViewById(R.id.tv_job_years);
        tv_year_income = (TextView) rootView.findViewById(R.id.tv_year_income);
        et_bank_id = (EditText) rootView.findViewById(R.id.et_bank_id);
        tv_contacts_one = (TextView) rootView.findViewById(R.id.tv_contacts_one);
        et_name_one = (LimitEditText) rootView.findViewById(R.id.et_name_one);
        et_phone_number_one = (EditText) rootView.findViewById(R.id.et_phone_number_one);
        tv_contacts_two = (TextView) rootView.findViewById(R.id.tv_contacts_two);
        et_name_two = (LimitEditText) rootView.findViewById(R.id.et_name_two);
        et_phone_number_two = (EditText) rootView.findViewById(R.id.et_phone_number_two);
        tv_contacts_three = (TextView) rootView.findViewById(R.id.tv_contacts_three);
        et_name_three = (LimitEditText) rootView.findViewById(R.id.et_name_three);
        et_phone_number_three = (EditText) rootView.findViewById(R.id.et_phone_number_three);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        tv_diploma = (TextView) rootView.findViewById(R.id.tv_diploma);

        tv_phone = (TextView) rootView.findViewById(R.id.tv_phone);
        tv_diploma_show = (TextView) rootView.findViewById(R.id.tv_diploma_show);
        tv_is_married_show = (TextView) rootView.findViewById(R.id.tv_is_married_show);
        tv_area_show = (TextView) rootView.findViewById(R.id.tv_area_show);
        tv_job_industry_show = (TextView) rootView.findViewById(R.id.tv_job_industry_show);
        tv_job_show = (TextView) rootView.findViewById(R.id.tv_job_show);
        tv_job_years_show = (TextView) rootView.findViewById(R.id.tv_job_years_show);
        tv_year_income_show = (TextView) rootView.findViewById(R.id.tv_year_income_show);
        tv_bank_id_show = (TextView) rootView.findViewById(R.id.tv_bank_id_show);
        tv_contacts_one_show = (TextView) rootView.findViewById(R.id.tv_contacts_one_show);
        tv_contacts_two_show = (TextView) rootView.findViewById(R.id.tv_contacts_two_show);
        tv_contacts_three_show = (TextView) rootView.findViewById(R.id.tv_contacts_three_show);
        tv_house_address = (TextView) rootView.findViewById(R.id.tv_house_address);
//        tv_house_province = (TextView) rootView.findViewById(R.id.tv_house_province);
//        tv_house_city = (TextView) rootView.findViewById(R.id.tv_house_city);
        tv_house_area = (TextView) rootView.findViewById(R.id.tv_house_area);
        et_address = (EditText) rootView.findViewById(R.id.et_address);
        tv_job_name_show = (TextView) rootView.findViewById(R.id.tv_job_name_show);
        et_job_name = (EditText) rootView.findViewById(R.id.et_job_name);
        tv_support_id_card = (TextView) rootView.findViewById(R.id.tv_support_id_card);

//        tv_phone.setText(Utils.setFountColor(tv_phone));
//        tv_diploma_show.setText(Utils.setFountColor(tv_diploma_show));
//        tv_is_married_show.setText(Utils.setFountColor(tv_is_married_show));
//        tv_area_show.setText(Utils.setFountColor(tv_area_show));
//        tv_job_industry_show.setText(Utils.setFountColor(tv_job_industry_show));
//        tv_job_show.setText(Utils.setFountColor(tv_job_show));
//        tv_job_years_show.setText(Utils.setFountColor(tv_job_years_show));
//        tv_year_income_show.setText(Utils.setFountColor(tv_year_income_show));
//        tv_bank_id_show.setText(Utils.setFountColor(tv_bank_id_show));
//        tv_contacts_one_show.setText(Utils.setFountColor(tv_contacts_one_show));
//        tv_contacts_two_show.setText(Utils.setFountColor(tv_contacts_two_show));
//        tv_contacts_three_show.setText(Utils.setFountColor(tv_contacts_three_show));
//        tv_house_address.setText(Utils.setFountColor(tv_house_address));
//        tv_job_name_show.setText(Utils.setFountColor(tv_job_name_show));

//        et_name_one.addTextChangedListener(new LimitInputTextWatcher(et_name_one));
//        et_name_two.addTextChangedListener(new LimitInputTextWatcher(et_name_two));
//        et_name_three.addTextChangedListener(new LimitInputTextWatcher(et_name_three));

        loadingDialog = DialogUtils.createLoadDialog(InformationAuthenticationActivity.this, false);
        if (ctf == null) {
            ctf = MainActivity.getCtf();
        }

        gson = new Gson();
        userId = SharedPrefrenceUtils.getString(this, "usrid");
        bondsmaninfBeanList = new ArrayList<BondsmaninfBean>();
        Drawable drawable1 = getResources().getDrawable(R.mipmap.right_arrow);
        drawable1.setBounds(0, 0, 40, 30);//第一0是距左边距离，第二0是距上边距离，40分别是长宽

        nav_up = getResources().getDrawable(R.mipmap.arrow_up);
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());

//        tv_is_married.setCompoundDrawables(null, null, drawable1, null);
//        tv_province.setCompoundDrawables(null, null, drawable1, null);
//        tv_city.setCompoundDrawables(null, null, drawable1, null);
//        tv_area.setCompoundDrawables(null,null,drawable1,null);
//        tv_job_industry.setCompoundDrawables(null,null,drawable1,null);
//        tv_job.setCompoundDrawables(null,null,drawable1,null);
//        tv_job_years.setCompoundDrawables(null,null,drawable1,null);
//        tv_year_income.setCompoundDrawables(null,null,drawable1,null);
//        tv_contacts_one.setCompoundDrawables(null,null,drawable1,null);
//        tv_contacts_two.setCompoundDrawables(null,null,drawable1,null);
//        tv_contacts_three.setCompoundDrawables(null,null,drawable1,null);
//        tv_diploma.setCompoundDrawables(null,null,drawable1,null);
//        tv_house_province.setCompoundDrawables(null,null,drawable1,null);
//        tv_house_city.setCompoundDrawables(null,null,drawable1,null);
//        tv_house_area.setCompoundDrawables(null,null,drawable1,null);

        initData();

        return null;
    }

    /**
     * 易行通银行卡验证
     */
    private void checkBankId() {
//        phoneNumber = et_phone_number.getText().toString();
//        if(TextUtils.isEmpty(phoneNumber)){
//            return;
//        }
//        bankId = et_bank_id.getText().toString();
//        if(TextUtils.isEmpty(bankId)){
//            return;
//        }
        btn_next.setEnabled(false);
        btn_next.setBackgroundResource(R.mipmap.login_no);

        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/verifyBankCard.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("bank_id", bankId);
        params.put("borrower_usrname", name);
        params.put("borrower_mobile_phone", phoneNumber);
        params.put("borrower_id_card", idNumber);
        params.put("usr_order_id", usr_order_id);
        LogUtils.e("params:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Log.e(TAG, "易行通银行卡验证返回:" + json);
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkbaseResponse.getCode() == 0) {
                    setCreditInf();

                } else if (fkbaseResponse.getCode() == 2) {
                    Intent intent = new Intent(InformationAuthenticationActivity.this, LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                } else {
                    btn_next.setEnabled(true);
                    btn_next.setBackgroundResource(R.mipmap.btn_login);
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(InformationAuthenticationActivity.this, fkbaseResponse.getMsg());
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                btn_next.setEnabled(true);
                btn_next.setBackgroundResource(R.mipmap.btn_login);
                loadingDialog.dismiss();
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(InformationAuthenticationActivity.this, error);
                }
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        idNumber = intent.getStringExtra("idNumber");
        interestsBean = (InterestsBean) intent.getSerializableExtra("interestsBean");
        usr_order_id = intent.getStringExtra("usr_order_id");
        store_name = intent.getStringExtra("store_name");
        store_id = intent.getIntExtra("store_id", 0);
        store_type = intent.getIntExtra("store_type", 0);
        store_contract = intent.getStringExtra("store_contract");
        store_tel = intent.getStringExtra("store_tel");
        amount = intent.getStringExtra("amount");
        store_uid = intent.getIntExtra("store_uid", 0);
        is_tiexi = intent.getIntExtra("is_tiexi", 0);
        gather_model_id = intent.getIntExtra("gather_model_id", 0);
        isFromRecord = intent.getBooleanExtra("isFromRecord", false);
        amortization_money = intent.getStringExtra("amortization_money");
        order_from = intent.getIntExtra("order_from", 0);

        if (!TextUtils.isEmpty(userId)) {
            setMessage();
        }
        if (mInflater == null) {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        dictonaryRoot = mInflater.inflate(R.layout.choose_dialog, null);
        dictonaryWindow = new PopupWindow(dictonaryRoot, ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.FILL_PARENT);
        dictonaryWindow.setFocusable(true);
        lv_choose = (ListView) dictonaryRoot.findViewById(R.id.lv_choose);
        btn_cancel = (Button) dictonaryRoot.findViewById(R.id.btn_cancel);
        tv_title = (TextView) dictonaryRoot.findViewById(R.id.tv_title);
        tv_complete = (TextView) dictonaryRoot.findViewById(R.id.tv_complete);

        lv_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (chooseType) {
                    case 0: //婚姻状况
                        marriedDictonaryBean = dictionaryBeanList.get(position);
                        tv_is_married.setText(marriedDictonaryBean.getDictionary_value_name());
                        tv_is_married.setTextColor(UIUtils.getColor(R.color.text_color_black));
                        oldPosition = lv_choose.getFirstVisiblePosition();
                        break;
                    case 1://现工作单位行业
                        jobIndustryDictonaryBean = dictionaryBeanList.get(position);
                        tv_job_industry.setText(jobIndustryDictonaryBean.getDictionary_value_name());
                        tv_job_industry.setTextColor(UIUtils.getColor(R.color.text_color_black));
                        oldPosition = lv_choose.getFirstVisiblePosition();
                        break;
                    case 2: //职业
                        jobDictonaryBean = dictionaryBeanList.get(position);
                        tv_job.setText(jobDictonaryBean.getDictionary_value_name());
                        tv_job.setTextColor(UIUtils.getColor(R.color.text_color_black));
                        oldPosition = lv_choose.getFirstVisiblePosition();
                        break;
                    case 3: //现单位工作年限
                        jobYearsDictonaryBean = dictionaryBeanList.get(position);
                        tv_job_years.setText(jobYearsDictonaryBean.getDictionary_value_name());
                        tv_job_years.setTextColor(UIUtils.getColor(R.color.text_color_black));
                        oldPosition = lv_choose.getFirstVisiblePosition();
                        break;
                    case 4: //年收入
                        yearIncomeDictonaryBean = dictionaryBeanList.get(position);
                        tv_year_income.setText(yearIncomeDictonaryBean.getDictionary_value_name());
                        tv_year_income.setTextColor(UIUtils.getColor(R.color.text_color_black));
                        oldPosition = lv_choose.getFirstVisiblePosition();
                        break;
                    case 5:  //联系人1关系
                        contactsOneDictonaryBean = dictionaryBeanList.get(position);
                        tv_contacts_one.setText(contactsOneDictonaryBean.getDictionary_value_name());
                        tv_contacts_one.setTextColor(UIUtils.getColor(R.color.text_color_black));

                        tv_contacts_one.setCompoundDrawables(null, null, nav_up, null);

                        oldPosition = lv_choose.getFirstVisiblePosition();
                        break;
                    case 6: //联系人2关系
                        contactsTwoDictonaryBean = dictionaryBeanList.get(position);
                        tv_contacts_two.setText(contactsTwoDictonaryBean.getDictionary_value_name());
                        tv_contacts_two.setTextColor(UIUtils.getColor(R.color.text_color_black));

                        tv_contacts_two.setCompoundDrawables(null, null, nav_up, null);

                        oldPosition = lv_choose.getFirstVisiblePosition();
                        break;
                    case 7: //联系人3关系
                        contactsThreeDictonaryBean = dictionaryBeanList.get(position);
                        tv_contacts_three.setText(contactsThreeDictonaryBean.getDictionary_value_name());
                        tv_contacts_three.setTextColor(UIUtils.getColor(R.color.text_color_black));

                        tv_contacts_three.setCompoundDrawables(null, null, nav_up, null);

                        oldPosition = lv_choose.getFirstVisiblePosition();
                        break;
                    case 11: //学历
                        diplomaDictonaryBean = dictionaryBeanList.get(position);
                        tv_diploma.setText(diplomaDictonaryBean.getDictionary_value_name());
                        tv_diploma.setTextColor(UIUtils.getColor(R.color.text_color_black));
                        oldPosition = lv_choose.getFirstVisiblePosition();
                        break;
                }
                dictonaryWindow.dismiss();
            }
        });

        tv_top_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        tv_is_married.setOnClickListener(this);
        tv_job_industry.setOnClickListener(this);
        tv_job.setOnClickListener(this);
        tv_job_years.setOnClickListener(this);
        tv_year_income.setOnClickListener(this);
        tv_contacts_one.setOnClickListener(this);
        tv_contacts_two.setOnClickListener(this);
        tv_contacts_three.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
//        tv_province.setOnClickListener(this);
//        tv_city.setOnClickListener(this);
        tv_area.setOnClickListener(this);
        tv_diploma.setOnClickListener(this);
//        tv_house_province.setOnClickListener(this);
//        tv_house_city.setOnClickListener(this);
        tv_house_area.setOnClickListener(this);
        tv_support_id_card.setOnClickListener(this);
        tv_complete.setOnClickListener(this);

//        getUsrBankList();
        getLatestBankId();
    }

    /**
     * 设置上次未提交的数据
     */
    private void setMessage() {
        String messageStr = SharedPrefrenceUtils.getString(this, userId+"informationAuthentication");
        if (!TextUtils.isEmpty(messageStr)) {
            HashMap<String, String> hs = gson.fromJson(messageStr, HashMap.class);
            LogUtils.e("未提交数据:" + hs.toString());

            phoneNumber = hs.get("phoneNumber");
            if (!TextUtils.isEmpty(phoneNumber)) {
                et_phone_number.setText(phoneNumber);
            }

            diplomaDictonaryBean = gson.fromJson(hs.get("diplomaDictonaryBean"), DictionaryBean.class);
            if (diplomaDictonaryBean != null) {
                tv_diploma.setText(diplomaDictonaryBean.getDictionary_value_name());
                tv_diploma.setTextColor(UIUtils.getColor(R.color.text_color_black));
            }

            marriedDictonaryBean = gson.fromJson(hs.get("marriedDictonaryBean"), DictionaryBean.class);
            if (marriedDictonaryBean != null) {
                tv_is_married.setText(marriedDictonaryBean.getDictionary_value_name());
                tv_is_married.setTextColor(UIUtils.getColor(R.color.text_color_black));
            }

            provinceBean = gson.fromJson(hs.get("provinceBean"),AreaBean.class);
            cityBean = gson.fromJson(hs.get("cityBean"),AreaBean.class);
            areaBean = gson.fromJson(hs.get("areaBean"), AreaBean.class);

            areaStr = hs.get("areaStr");
            if (!TextUtils.isEmpty(areaStr) && !areaStr.startsWith("选择")) {
                tv_area.setText(areaStr);
                tv_area.setTextColor(UIUtils.getColor(R.color.text_color_black));
            }



            houseProvinceBean = gson.fromJson(hs.get("houseProvinceBean"),AreaBean.class);
            houseCityBean = gson.fromJson(hs.get("houseCityBean"),AreaBean.class);
            houseAreaBean = gson.fromJson(hs.get("houseAreaBean"), AreaBean.class);

            houseAreaStr = hs.get("houseAreaStr");
            if (!TextUtils.isEmpty(houseAreaStr) &&!houseAreaStr.startsWith("选择")) {
                tv_house_area.setText(houseAreaStr);
                tv_house_area.setTextColor(UIUtils.getColor(R.color.text_color_black));
            }


            detailedAddress = hs.get("detailedAddress");
            if (!TextUtils.isEmpty(detailedAddress)) {
                et_address.setText(detailedAddress);
            }

            jobName = hs.get("jobName");
            if (!TextUtils.isEmpty(jobName)) {
                et_job_name.setText(jobName);
            }

            jobIndustryDictonaryBean = gson.fromJson(hs.get("jobIndustryDictonaryBean"), DictionaryBean.class);
            if (jobIndustryDictonaryBean != null) {
                tv_job_industry.setText(jobIndustryDictonaryBean.getDictionary_value_name());
                tv_job_industry.setTextColor(UIUtils.getColor(R.color.text_color_black));
            }

            jobDictonaryBean = gson.fromJson(hs.get("jobDictonaryBean"), DictionaryBean.class);
            if (jobDictonaryBean != null) {
                tv_job.setText(jobDictonaryBean.getDictionary_value_name());
                tv_job.setTextColor(UIUtils.getColor(R.color.text_color_black));
            }

            jobYearsDictonaryBean = gson.fromJson(hs.get("jobYearsDictonaryBean"), DictionaryBean.class);
            if (jobYearsDictonaryBean != null) {
                tv_job_years.setText(jobYearsDictonaryBean.getDictionary_value_name());
                tv_job_years.setTextColor(UIUtils.getColor(R.color.text_color_black));
            }

            yearIncomeDictonaryBean = gson.fromJson(hs.get("yearIncomeDictonaryBean"), DictionaryBean.class);
            if (yearIncomeDictonaryBean != null) {
                tv_year_income.setText(yearIncomeDictonaryBean.getDictionary_value_name());
                tv_year_income.setTextColor(UIUtils.getColor(R.color.text_color_black));
            }


            bankId = hs.get("bankId");
            if (!TextUtils.isEmpty(bankId)) {
                et_bank_id.setText(bankId);
            }

            contactsOneDictonaryBean = gson.fromJson(hs.get("contactsOneDictonaryBean"), DictionaryBean.class);
            if (contactsOneDictonaryBean != null) {
                tv_contacts_one.setText(contactsOneDictonaryBean.getDictionary_value_name());
                tv_contacts_one.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_contacts_one.setCompoundDrawables(null, null, nav_up, null);
            }

            contactsTwoDictonaryBean = gson.fromJson(hs.get("contactsTwoDictonaryBean"), DictionaryBean.class);
            if (contactsTwoDictonaryBean != null) {
                tv_contacts_two.setText(contactsTwoDictonaryBean.getDictionary_value_name());
                tv_contacts_two.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_contacts_two.setCompoundDrawables(null, null, nav_up, null);
            }

            contactsThreeDictonaryBean = gson.fromJson(hs.get("contactsThreeDictonaryBean"), DictionaryBean.class);
            if (contactsThreeDictonaryBean != null) {
                tv_contacts_three.setText(contactsThreeDictonaryBean.getDictionary_value_name());
                tv_contacts_three.setTextColor(UIUtils.getColor(R.color.text_color_black));
                tv_contacts_three.setCompoundDrawables(null, null, nav_up, null);
            }

            nameOne = hs.get("nameOne");
            if (!TextUtils.isEmpty(nameOne)) {
                et_name_one.setText(nameOne);
            }
            nameTwo = hs.get("nameTwo");
            if (!TextUtils.isEmpty(nameTwo)) {
                et_name_two.setText(nameTwo);
            }
            nameThree = hs.get("nameThree");
            if (!TextUtils.isEmpty(nameThree)) {
                et_name_three.setText(nameThree);
            }
            phoneNumberOne = hs.get("phoneNumberOne");
            if (!TextUtils.isEmpty(phoneNumberOne)) {
                et_phone_number_one.setText(phoneNumberOne);
            }
            phoneNumberTwo = hs.get("phoneNumberTwo");
            if (!TextUtils.isEmpty(phoneNumberTwo)) {
                et_phone_number_two.setText(phoneNumberTwo);
            }
            phoneNumberThree = hs.get("phoneNumberThree");
            if (!TextUtils.isEmpty(phoneNumberThree)) {
                et_phone_number_three.setText(phoneNumberThree);
            }

        }else {
            phoneNumber = SharedPrefrenceUtils.getString(this,"mobile_phone");
            if(!TextUtils.isEmpty(phoneNumber)){
                et_phone_number.setText(phoneNumber);
            }

            diplomaDictonaryBean = new DictionaryBean();
            diplomaDictonaryBean.setDictionary_value_key(4);
            diplomaDictonaryBean.setDictionary_value_name("本科");
            tv_diploma.setText(diplomaDictonaryBean.getDictionary_value_name());
            tv_diploma.setTextColor(UIUtils.getColor(R.color.text_color_black));

            marriedDictonaryBean = new DictionaryBean();
            marriedDictonaryBean.setDictionary_value_key(2);
            marriedDictonaryBean.setDictionary_value_name("已婚已育");
            tv_is_married.setText(marriedDictonaryBean.getDictionary_value_name());
            tv_is_married.setTextColor(UIUtils.getColor(R.color.text_color_black));

            jobDictonaryBean = new DictionaryBean();
            jobDictonaryBean.setDictionary_value_key(6);
            jobDictonaryBean.setDictionary_value_name("高级管理人员、总公司总经理");
            tv_job.setText(jobDictonaryBean.getDictionary_value_name());
            tv_job.setTextColor(UIUtils.getColor(R.color.text_color_black));

            jobYearsDictonaryBean = new DictionaryBean();
            jobYearsDictonaryBean.setDictionary_value_key(5);
            jobYearsDictonaryBean.setDictionary_value_name("5年以上");
            tv_job_years.setText(jobYearsDictonaryBean.getDictionary_value_name());
            tv_job_years.setTextColor(UIUtils.getColor(R.color.text_color_black));

            yearIncomeDictonaryBean = new DictionaryBean();
            yearIncomeDictonaryBean.setDictionary_value_key(6);
            yearIncomeDictonaryBean.setDictionary_value_name("30万元以上");
            tv_year_income.setText(yearIncomeDictonaryBean.getDictionary_value_name());
            tv_year_income.setTextColor(UIUtils.getColor(R.color.text_color_black));

            contactsOneDictonaryBean = new DictionaryBean();
            contactsOneDictonaryBean.setDictionary_value_key(1);
            contactsOneDictonaryBean.setDictionary_value_name("父母");
            tv_contacts_one.setText(contactsOneDictonaryBean.getDictionary_value_name());
            tv_contacts_one.setTextColor(UIUtils.getColor(R.color.text_color_black));
            tv_contacts_one.setCompoundDrawables(null, null, nav_up, null);

            contactsTwoDictonaryBean = new DictionaryBean();
            contactsTwoDictonaryBean.setDictionary_value_key(0);
            contactsTwoDictonaryBean.setDictionary_value_name("配偶");
            tv_contacts_two.setText(contactsTwoDictonaryBean.getDictionary_value_name());
            tv_contacts_two.setTextColor(UIUtils.getColor(R.color.text_color_black));
            tv_contacts_two.setCompoundDrawables(null, null, nav_up, null);

            contactsThreeDictonaryBean = new DictionaryBean();
            contactsThreeDictonaryBean.setDictionary_value_key(3);
            contactsThreeDictonaryBean.setDictionary_value_name("兄弟姐妹");
            tv_contacts_three.setText(contactsThreeDictonaryBean.getDictionary_value_name());
            tv_contacts_three.setTextColor(UIUtils.getColor(R.color.text_color_black));
            tv_contacts_three.setCompoundDrawables(null, null, nav_up, null);


        }

    }

    private void getLatestBankId() {
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/getLatestBankIdByUsrid.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("usrid", userId);
        LogUtils.e("获得用户最新银行卡号参数:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Log.e(TAG, "获得用户最新银行卡号:" + json);
                FkBaseResponse baseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (baseResponse.getCode() == 0) {
                    HashMap<String, String> hs = gson.fromJson(gson.toJson(baseResponse.getReturn_param()), HashMap.class);
                    String bankId = hs.get("bank_id");
                    if (!TextUtils.isEmpty(bankId)) {
                        et_bank_id.setText(bankId);
                    }
                } else if (baseResponse.getCode() == 2) {
                    Intent intent = new Intent(InformationAuthenticationActivity.this, LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(InformationAuthenticationActivity.this, baseResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                Log.e(TAG, "获得用户最新银行卡号:" + error);
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(InformationAuthenticationActivity.this, error);
                }

            }
        });
    }

    private void getUsrBankList() {
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/getUsrBankList.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("usrid", userId);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Log.e(TAG, "获得用户银行列表:" + json);
                getUsrBankListResponse = gson.fromJson(json, GetUsrBankListResponse.class);
                if (getUsrBankListResponse.getCode() == 0) {
                    List<GetUsrBankListResponse.BankBean> bankBeanList = getUsrBankListResponse.getReturn_param().getBank_list();
                    if (bankBeanList != null && bankBeanList.size() > 0) {
                        et_bank_id.setText(bankBeanList.get(bankBeanList.size() - 1).getBank_no());
                    }
                } else if (getUsrBankListResponse.getCode() == 2) {
                    Intent intent = new Intent(InformationAuthenticationActivity.this, LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(InformationAuthenticationActivity.this, getUsrBankListResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                Log.e(TAG, "获得用户银行列表:" + error);
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(InformationAuthenticationActivity.this, error);
                }

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            saveMessage();
            ctf.setChecked(ctf.beforeIndex);
            ctf.mCurrentIndex = ctf.beforeIndex;
            finish();
            overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 保存未提交的数据
     */
    private void saveMessage() {
        HashMap<String, String> hs = new HashMap<>();
        hs.put("usr_order_id", usr_order_id);

        phoneNumber = et_phone_number.getText().toString();
        if (!TextUtils.isEmpty(phoneNumber)) {
            hs.put("phoneNumber", phoneNumber);
        }

        if (diplomaDictonaryBean != null) {
            hs.put("diplomaDictonaryBean", gson.toJson(diplomaDictonaryBean));
        }
        if (marriedDictonaryBean != null) {
            hs.put("marriedDictonaryBean", gson.toJson(marriedDictonaryBean));
        }
        if(provinceBean != null){
            hs.put("provinceBean", gson.toJson(provinceBean));
        }
        if(cityBean != null){
            hs.put("cityBean", gson.toJson(cityBean));
        }
        areaStr = tv_area.getText().toString();
        if (!TextUtils.isEmpty(areaStr)) {
            hs.put("areaStr", areaStr);
        }

        if (areaBean != null) {
            hs.put("areaBean", gson.toJson(areaBean));
        }
        if(houseProvinceBean != null){
            hs.put("houseProvinceBean", gson.toJson(houseProvinceBean));
        }
        if(houseCityBean != null){
            hs.put("houseCityBean", gson.toJson(houseCityBean));
        }

        houseAreaStr = tv_house_area.getText().toString();
        if (!TextUtils.isEmpty(houseAreaStr)) {
            hs.put("houseAreaStr", houseAreaStr);
        }
        if (houseAreaBean != null) {
            hs.put("houseAreaBean", gson.toJson(houseAreaBean));
        }

        detailedAddress = et_address.getText().toString();
        if (!TextUtils.isEmpty(detailedAddress)) {
            hs.put("detailedAddress", detailedAddress);
        }

        jobName = et_job_name.getText().toString();
        if (!TextUtils.isEmpty(jobName)) {
            hs.put("jobName", jobName);
        }

        if (jobIndustryDictonaryBean != null) {
            hs.put("jobIndustryDictonaryBean", gson.toJson(jobIndustryDictonaryBean));
        }
        if (jobDictonaryBean != null) {
            hs.put("jobDictonaryBean", gson.toJson(jobDictonaryBean));
        }
        if (jobYearsDictonaryBean != null) {
            hs.put("jobYearsDictonaryBean", gson.toJson(jobYearsDictonaryBean));
        }
        if (yearIncomeDictonaryBean != null) {
            hs.put("yearIncomeDictonaryBean", gson.toJson(yearIncomeDictonaryBean));
        }

        bankId = et_bank_id.getText().toString();
        if (!TextUtils.isEmpty(bankId)) {
            hs.put("bankId", bankId);
        }

        if (contactsOneDictonaryBean != null) {
            hs.put("contactsOneDictonaryBean", gson.toJson(contactsOneDictonaryBean));
        }
        if (contactsTwoDictonaryBean != null) {
            hs.put("contactsTwoDictonaryBean", gson.toJson(contactsTwoDictonaryBean));
        }
        if (contactsThreeDictonaryBean != null) {
            hs.put("contactsThreeDictonaryBean", gson.toJson(contactsThreeDictonaryBean));
        }

        nameOne = et_name_one.getText().toString();
        if (!TextUtils.isEmpty(nameOne)) {
            hs.put("nameOne", nameOne);
        }
        nameTwo = et_name_two.getText().toString();
        if (!TextUtils.isEmpty(nameTwo)) {
            hs.put("nameTwo", nameTwo);
        }
        nameThree = et_name_three.getText().toString();
        if (!TextUtils.isEmpty(nameThree)) {
            hs.put("nameThree", nameThree);
        }
        phoneNumberOne = et_phone_number_one.getText().toString();
        if (!TextUtils.isEmpty(phoneNumberOne)) {
            hs.put("phoneNumberOne", phoneNumberOne);
        }
        phoneNumberTwo = et_phone_number_two.getText().toString();
        if (!TextUtils.isEmpty(phoneNumberTwo)) {
            hs.put("phoneNumberTwo", phoneNumberTwo);
        }
        phoneNumberThree = et_phone_number_three.getText().toString();
        if (!TextUtils.isEmpty(phoneNumberThree)) {
            hs.put("phoneNumberThree", phoneNumberThree);
        }

        SharedPrefrenceUtils.setString(this, userId+"informationAuthentication", gson.toJson(hs));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 110) {
            if (resultCode == 200) {
                provinceBean = (AreaBean) data.getSerializableExtra("provinceBean");
                cityBean = (AreaBean) data.getSerializableExtra("cityBean");
                areaBean = (AreaBean) data.getSerializableExtra("areaBean");
                areaStr = data.getStringExtra("areaStr");
                if (!TextUtils.isEmpty(areaStr)) {
                    tv_area.setText(areaStr);
                    tv_area.setTextColor(UIUtils.getColor(R.color.text_color_black));
                }
            }
        } else if (requestCode == 111) {
            if (resultCode == 200) {
                houseProvinceBean = (AreaBean) data.getSerializableExtra("provinceBean");
                houseCityBean = (AreaBean) data.getSerializableExtra("cityBean");
                houseAreaBean = (AreaBean) data.getSerializableExtra("areaBean");
                houseAreaStr = data.getStringExtra("areaStr");
                if (!TextUtils.isEmpty(houseAreaStr)) {
                    tv_house_area.setText(houseAreaStr);
                    tv_house_area.setTextColor(UIUtils.getColor(R.color.text_color_black));
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
//                Intent intent = new Intent(InformationAuthenticationActivity.this,TourismPackagingActivity.class);
//                setFinish();
//                UIUtils.startActivityNextAnim(intent);
                writeCreditInf();
                break;
            case R.id.tv_top_back:
                saveMessage();
                UIUtils.hideInputWindow(this);
                ctf.setChecked(ctf.beforeIndex);
                ctf.mCurrentIndex = ctf.beforeIndex;
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.tv_is_married://婚姻状况
                dictionaryTitle = "婚姻状况";
                chooseType = 0;
                typeName = "tszd_2";
                getDictonary();
                break;
            case R.id.tv_job_industry://现工作单位行业
                dictionaryTitle = "单位行业";
                chooseType = 1;
                typeName = "tszd_7";
                getDictonary();
                break;
            case R.id.tv_job://职业
                dictionaryTitle = "职业";
                chooseType = 2;
                typeName = "tszd_8";
                getDictonary();
                break;
            case R.id.tv_job_years://现单位工作年限
                dictionaryTitle = "工作年限";
                chooseType = 3;
                typeName = "tszd_9";
                getDictonary();
                break;
            case R.id.tv_year_income://年收入
                dictionaryTitle = "年收入";
                chooseType = 4;
                typeName = "tszd_10";
                getDictonary();
                break;
            case R.id.tv_contacts_one://联系人1关系
                dictionaryTitle = "联系人1";
                chooseType = 5;
                typeName = "tszd_35";
                getDictonary();
                break;
            case R.id.tv_contacts_two://联系人2关系
                dictionaryTitle = "联系人2";
                chooseType = 6;
                typeName = "tszd_35";
                getDictonary();
                break;
            case R.id.tv_contacts_three://联系人3关系
                dictionaryTitle = "联系人3";
                chooseType = 7;
                typeName = "tszd_35";
                getDictonary();
                break;
            case R.id.btn_cancel: //选择取消
            case R.id.tv_complete: //选择完成
                dictonaryWindow.dismiss();
                break;
            case R.id.tv_area: //区
                Intent intent = new Intent(InformationAuthenticationActivity.this, AreaChooseActivity.class);
                intent.putExtra("provinceBean",provinceBean);
                intent.putExtra("cityBean",cityBean);
                intent.putExtra("areaBean",areaBean);
                UIUtils.startActivityForResult(intent, 110);
                break;
            case R.id.tv_diploma:  //学历
                dictionaryTitle = "学历";
                chooseType = 11;
                typeName = "tszd_1";
                getDictonary();
                break;
            case R.id.tv_house_area:
                Intent intent1 = new Intent(InformationAuthenticationActivity.this, AreaChooseActivity.class);
                intent1.putExtra("provinceBean",houseProvinceBean);
                intent1.putExtra("cityBean",houseCityBean);
                intent1.putExtra("areaBean",houseAreaBean);
                UIUtils.startActivityForResult(intent1, 111);
                break;
            case R.id.tv_support_id_card://支持的银行卡
                getSupportBankList();
                break;
        }
    }

    private void getSupportBankList() {
        loadingDialog.show();
        String url = Constants.XINYONGSERVER_URL + "credit_money_background/user/getSupportBankList.do";
        HashMap<String, String> params = new HashMap<String, String>();
        if(!TextUtils.isEmpty(amortization_money)){
            params.put("amount",amortization_money);
        }
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("获得银行列表:" + json.toString());
                getSupportBankListResponse = gson.fromJson(json, GetSupportBankListResponse.class);
                if (getSupportBankListResponse.getCode() == 0) {
                    List<GetSupportBankListResponse.SupportBankBean> supportBankBeanList = getSupportBankListResponse.getDataList();
                    SupportBankListDialog supportBankListDialog = new SupportBankListDialog(InformationAuthenticationActivity.this, supportBankBeanList);
                    supportBankListDialog.show();

                } else if (getSupportBankListResponse.getCode() == 2) {
                    Intent intent = new Intent(InformationAuthenticationActivity.this, LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(InformationAuthenticationActivity.this,
                                getSupportBankListResponse.getResultText());
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(InformationAuthenticationActivity.this,
                            error);
                }
            }
        });
    }

    private void writeCreditInf() {
        phoneNumber = et_phone_number.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请输入手机号码");
            return;
        }
        if (!StringUtils.isMobileNO(phoneNumber)) {
            UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请输入正确的手机号码");
            return;
        }
        if (diplomaDictonaryBean == null) {
            UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请选择学历");
            return;
        }
        if (marriedDictonaryBean == null) {
            UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请选择婚姻状况");
            return;
        }
        if (areaBean == null) {
            UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请选择户籍地");
            return;
        }
        if (houseAreaBean == null) {
            UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请选择现住址");
            return;
        }
        detailedAddress = et_address.getText().toString();
        if (TextUtils.isEmpty(detailedAddress)) {
            UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请输入详细地址");
            return;
        }
        houseAddress = houseProvinceBean.getArea_name() + houseCityBean.getArea_name() + houseAreaBean.getArea_name() + detailedAddress;
        jobName = et_job_name.getText().toString();
        if (TextUtils.isEmpty(jobName)) {
            UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请输入单位名称");
            return;
        }
        if (jobIndustryDictonaryBean == null) {
            UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请选择现工作单位行业");
            return;
        }
        if (jobDictonaryBean == null) {
            UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请选择职位");
            return;
        }
        if (jobYearsDictonaryBean == null) {
            UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请选择单位工作年限");
            return;
        }
        if (yearIncomeDictonaryBean == null) {
            UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请选择年收入");
            return;
        }
        bankId = et_bank_id.getText().toString();
        if (TextUtils.isEmpty(bankId)) {
            UIUtils.showToastCenter(InformationAuthenticationActivity.this, "银行卡号不能为空");
            return;
        }

        if (contactsOneDictonaryBean == null) {
            UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请选择联系人1");
            return;
        }

        nameOne = et_name_one.getText().toString();
        nameTwo = et_name_two.getText().toString();
        nameThree = et_name_three.getText().toString();
        phoneNumberOne = et_phone_number_one.getText().toString();
        phoneNumberTwo = et_phone_number_two.getText().toString();
        phoneNumberThree = et_phone_number_three.getText().toString();

        bondsmaninfBeanList.clear();
        if (contactsOneDictonaryBean != null) {
            if (TextUtils.isEmpty(nameOne) || TextUtils.isEmpty(phoneNumberOne)) {
                UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请填写完整联系人1信息");
                return;
            }
            if (!StringUtils.isMobileNO(phoneNumberOne)) {
                UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请填写正确的联系人1手机号");
                return;
            }
            BondsmaninfBean bondsmaninfBean = new BondsmaninfBean();
            bondsmaninfBean.setRel_mobile_phone(phoneNumberOne);
            bondsmaninfBean.setRel_usrname(nameOne);
            bondsmaninfBean.setRelation(contactsOneDictonaryBean.getDictionary_value_key());
            bondsmaninfBeanList.add(bondsmaninfBean);
        }

        if (contactsTwoDictonaryBean == null) {
            UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请选择联系人2");
            return;
        }

        if (contactsTwoDictonaryBean != null) {
            if (TextUtils.isEmpty(nameTwo) || TextUtils.isEmpty(phoneNumberTwo)) {
                UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请填写完整联系人2信息");
                return;
            }
            if (!StringUtils.isMobileNO(phoneNumberTwo)) {
                UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请填写正确的联系人2手机号");
                return;
            }
            BondsmaninfBean bondsmaninfBean = new BondsmaninfBean();
            bondsmaninfBean.setRel_mobile_phone(phoneNumberTwo);
            bondsmaninfBean.setRel_usrname(nameTwo);
            bondsmaninfBean.setRelation(contactsTwoDictonaryBean.getDictionary_value_key());
            bondsmaninfBeanList.add(bondsmaninfBean);
        }
        if (contactsThreeDictonaryBean == null) {
            UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请选择联系人3");
            return;
        }
        if (contactsThreeDictonaryBean != null) {
            if (TextUtils.isEmpty(nameThree) || TextUtils.isEmpty(phoneNumberThree)) {
                UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请填写完整联系人3信息");
                return;
            }
            if (!StringUtils.isMobileNO(phoneNumberThree)) {
                UIUtils.showToastCenter(InformationAuthenticationActivity.this, "请填写正确的联系人3手机号");
                return;
            }
            BondsmaninfBean bondsmaninfBean = new BondsmaninfBean();
            bondsmaninfBean.setRel_mobile_phone(phoneNumberThree);
            bondsmaninfBean.setRel_usrname(nameThree);
            bondsmaninfBean.setRelation(contactsThreeDictonaryBean.getDictionary_value_key());
            bondsmaninfBeanList.add(bondsmaninfBean);
        }

        int value1 = contactsOneDictonaryBean.getDictionary_value_key();
        int value2 = contactsTwoDictonaryBean.getDictionary_value_key();
        int value3 = contactsThreeDictonaryBean.getDictionary_value_key();
        if ((value1 != 0 && value1 !=1 && value1 != 2 && value1 != 3) && ((value2 != 0 && value2 !=1 && value2 != 2 && value2 != 3) && (value3 != 0 && value3 !=1 && value3 != 2 && value3 != 3))) {
            UIUtils.showToastCenter(InformationAuthenticationActivity.this, "联系人中至少要有一个直系亲属");
            return;
        }
        if ((contactsOneDictonaryBean.getDictionary_value_key() == 0 && contactsTwoDictonaryBean.getDictionary_value_key() == 0) || (contactsOneDictonaryBean.getDictionary_value_key() == 0 && contactsThreeDictonaryBean.getDictionary_value_key() == 0) || (contactsTwoDictonaryBean.getDictionary_value_key() == 0 && contactsThreeDictonaryBean.getDictionary_value_key() == 0)) {
            UIUtils.showToastCenter(InformationAuthenticationActivity.this, "联系人中最多只能有一个配偶");
            return;
        }

        setCreditInf();
//        checkBankId();
    }

    /**
     * 写入授信资料
     */
    private void setCreditInf() {
        String phoneIp = Utils.getPhoneIp();
        String imei = Utils.getIEMI(this);
        loadingDialog.show();
//        String url= Constants.FENGKONGSERVER_URL+"tsfkxt/user/setCreditInf_2.do";
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/setOrdersInf.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("usrid", userId);
        params.put("borrow_uid", userId);
        params.put("submit_step", "3");
        params.put("scene_id", gather_model_id + "");
        params.put("usr_order_id", usr_order_id);
        params.put("borrower_mobile_phone", phoneNumber);
        params.put("mobile_imei", imei);
        params.put("ip", phoneIp);
        params.put("diploma", diplomaDictonaryBean.getDictionary_value_key() + "");
        params.put("marital", marriedDictonaryBean.getDictionary_value_key() + "");
        params.put("register_place", areaBean.getArea_id() + "");
        params.put("industry", jobIndustryDictonaryBean.getDictionary_value_key() + "");
        params.put("profession_level", jobDictonaryBean.getDictionary_value_key() + "");
        params.put("cur_hire_duration", jobYearsDictonaryBean.getDictionary_value_key() + "");
        params.put("income", yearIncomeDictonaryBean.getDictionary_value_key() + "");
        params.put("bank_id", bankId);
        params.put("bondsmaninf_list", gson.toJson(bondsmaninfBeanList));
        params.put("com_name", jobName);
        params.put("present_address", houseAddress);
        LogUtils.e("params:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                saveMessage();

                loadingDialog.dismiss();
                btn_next.setEnabled(true);
                btn_next.setBackgroundResource(R.mipmap.btn_login);
                Log.e(TAG, "写入征信资料返回:" + json);
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkbaseResponse.getCode() == 0) {
                    setFinish();
                    Intent intent = new Intent();
                    if(order_from == 1){
                        intent.setClass(InformationAuthenticationActivity.this, PackagingAgreementActivity.class);
                        intent.putExtra("usr_order_id", usr_order_id);
                    }else {
                        switch (gather_model_id) {//1 旅游，  2 家具，  3 生活，  4 装修，  5 3C数码，  6 教育，  7 医美，  8 电商，  9 车类，  10 烟酒补品，  11 名品，  12 其他,  13 木材分期
                            case 1:
                                intent.setClass(InformationAuthenticationActivity.this, TourismPackagingActivity.class);
                                break;
                            case 2:
                                intent.setClass(InformationAuthenticationActivity.this, FurniturePackagingActivity.class);
                                break;
                            case 3:
                                intent.setClass(InformationAuthenticationActivity.this, LifePackagingActivity.class);
                                break;
                            case 4:
                                intent.setClass(InformationAuthenticationActivity.this, HomePackagingActivity.class);
                                break;
                            case 5:
                                intent.setClass(InformationAuthenticationActivity.this, DigitalPackagingActivity.class);
                                break;
                            case 6:
                                intent.setClass(InformationAuthenticationActivity.this, EducationPackagingActivity.class);
                                break;
                            case 7:
                                intent.setClass(InformationAuthenticationActivity.this, MedicalBeautyPackagingActivity.class);
                                break;
                            case 8:
                                intent.setClass(InformationAuthenticationActivity.this, OnlinePackagingActivity.class);
                                break;
                            case 9:
                                intent.setClass(InformationAuthenticationActivity.this, CarPackagingActivity.class);
                                break;
                            case 10:
                            case 11:
                            case 12:
                                intent.setClass(InformationAuthenticationActivity.this, OtherPackagingActivity.class);
                                break;
                            case 13:
                                intent.setClass(InformationAuthenticationActivity.this, WoodPackagingActivity.class);
                                break;
                            default:
                                intent.setClass(InformationAuthenticationActivity.this, OtherPackagingActivity.class);
                                break;
                        }
                        intent.putExtra("interestsBean", interestsBean);
                        intent.putExtra("store_name", store_name);
                        intent.putExtra("store_id", store_id);
                        intent.putExtra("store_contract", store_contract);
                        intent.putExtra("store_tel", store_tel);
                        intent.putExtra("amount", amount);
                        intent.putExtra("store_uid", store_uid);
                        intent.putExtra("is_tiexi", is_tiexi);
                        intent.putExtra("store_type", store_type);
                        intent.putExtra("usr_order_id", usr_order_id);
                    }
                    UIUtils.startActivityNextAnim(intent);
                } else if (fkbaseResponse.getCode() == 2) {
                    Intent intent = new Intent(InformationAuthenticationActivity.this, LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(InformationAuthenticationActivity.this, fkbaseResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                btn_next.setEnabled(true);
                btn_next.setBackgroundResource(R.mipmap.btn_login);
                Log.e(TAG, "写入征信资料返回错误:" + error);
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(InformationAuthenticationActivity.this, error);
                }

            }
        });
    }

    /**
     * 获取地区列表
     *
     * @param areaId
     */
    private void getAreaList(int areaId) {
        loadingDialog.show();
        String url = Constants.XINYONGSERVER_URL + "credit_money_background/user/getAreaList.do";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("area_id", areaId + "");
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("获得地区列表:" + json.toString());
                getAreaListResponse = gson.fromJson(json, GetAreaListResponse.class);


                if (getAreaListResponse.getCode() == 0) {
                    int index;

                    areaBeanArrayList = getAreaListResponse.getDataList();
                    if (areaBeanArrayList != null && areaBeanArrayList.size() > 0) {
                        AreaAdapter mAreaAdapter = new AreaAdapter(InformationAuthenticationActivity.this, areaBeanArrayList);
                        lv_choose.setAdapter(mAreaAdapter);
                        dictonaryWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                        switch (chooseType) {
                            case 10:
                                if (areaBean != null) {
                                    index = areaBeanArrayList.indexOf(areaBean);
                                    if (index != -1) {
                                        lv_choose.setSelection(oldPosition);
                                        mAreaAdapter.setSelectItem(index);
                                    }
                                }
                                break;
                            case 13:
                                houseAreaBean = null;
                                tv_house_area.setText("区");
                                tv_house_area.setTextColor(UIUtils.getColor(R.color.text_color_gray));
                                if (houseCityBean != null) {
                                    index = areaBeanArrayList.indexOf(houseCityBean);
                                    if (index != -1) {
                                        lv_choose.setSelection(oldPosition);
                                        mAreaAdapter.setSelectItem(index);
                                    }
                                }
                                break;
                            case 14:
                                if (houseAreaBean != null) {
                                    index = areaBeanArrayList.indexOf(houseAreaBean);
                                    if (index != -1) {
                                        lv_choose.setSelection(oldPosition);
                                        mAreaAdapter.setSelectItem(index);
                                    }
                                }
                                break;
                        }

                    }

                } else if (getAreaListResponse.getCode() == 2) {
                    Intent intent = new Intent(InformationAuthenticationActivity.this, LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(InformationAuthenticationActivity.this,
                                getAreaListResponse.getResultText());
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(InformationAuthenticationActivity.this,
                            error);
                }
            }
        });
    }

    /**
     * 获取数据字典数据源
     */
    private void getDictonary() {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/getDictionaryDatasource.do";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("type_code", typeName);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                LogUtils.e("数据字典数据源参数:" + typeName);
                LogUtils.e("数据字典数据源:" + json.toString());
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkbaseResponse.getCode() == 0) {
                    HashMap<String, Objects> hashMap = gson.fromJson(gson.toJson(fkbaseResponse.getReturn_param()), HashMap.class);
                    dictionaryBeanList = StringUtils.fromJsonList(gson.toJson(hashMap.get("data_list")), DictionaryBean.class);
                    LogUtils.e("dictionaryBeanList:" + dictionaryBeanList.toString());
                    DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(InformationAuthenticationActivity.this, dictionaryBeanList);
                    lv_choose.setAdapter(dictionaryAdapter);
                    if (!TextUtils.isEmpty(dictionaryTitle)) {
                        tv_title.setText(dictionaryTitle);
                    }
                    dictonaryWindow.showAtLocation((PercentLinearLayout) findViewById(R.id.relative),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);


                    int index;
                    switch (chooseType) {  //选择类型  0,婚姻状况   1,现工作单位行业  2,职业  3,现单位工作年限  4,年收入
                        //5,联系人1关系  6,联系人2关系  7,联系人3关系  11,学历
                        case 0:
                            if (marriedDictonaryBean != null) {
                                index = dictionaryBeanList.indexOf(marriedDictonaryBean);
                                if (index != -1) {
                                    lv_choose.setSelection(oldPosition);
                                    dictionaryAdapter.setSelectItem(index);
                                }
                            }
                            break;
                        case 1:
                            if (jobIndustryDictonaryBean != null) {
                                index = dictionaryBeanList.indexOf(jobIndustryDictonaryBean);
                                if (index != -1) {
                                    lv_choose.setSelection(oldPosition);
                                    dictionaryAdapter.setSelectItem(index);
                                }
                            }
                            break;
                        case 2:
                            if (jobDictonaryBean != null) {
                                index = dictionaryBeanList.indexOf(jobDictonaryBean);
                                if (index != -1) {
                                    lv_choose.setSelection(oldPosition);
                                    dictionaryAdapter.setSelectItem(index);
                                }
                            }
                            break;
                        case 3:
                            if (jobYearsDictonaryBean != null) {
                                index = dictionaryBeanList.indexOf(jobYearsDictonaryBean);
                                if (index != -1) {
                                    lv_choose.setSelection(oldPosition);
                                    dictionaryAdapter.setSelectItem(index);
                                }
                            }
                            break;
                        case 4:
                            if (yearIncomeDictonaryBean != null) {
                                index = dictionaryBeanList.indexOf(yearIncomeDictonaryBean);
                                if (index != -1) {
                                    lv_choose.setSelection(oldPosition);
                                    dictionaryAdapter.setSelectItem(index);
                                }
                            }
                            break;
                        case 5:
                            if (contactsOneDictonaryBean != null) {
                                index = dictionaryBeanList.indexOf(contactsOneDictonaryBean);
                                if (index != -1) {
                                    lv_choose.setSelection(oldPosition);
                                    dictionaryAdapter.setSelectItem(index);
                                }
                            }
                            break;
                        case 6:
                            if (contactsTwoDictonaryBean != null) {
                                index = dictionaryBeanList.indexOf(contactsTwoDictonaryBean);
                                if (index != -1) {
                                    lv_choose.setSelection(oldPosition);
                                    dictionaryAdapter.setSelectItem(index);
                                }
                            }
                            break;
                        case 7:
                            if (contactsThreeDictonaryBean != null) {
                                index = dictionaryBeanList.indexOf(contactsThreeDictonaryBean);
                                if (index != -1) {
                                    lv_choose.setSelection(oldPosition);
                                    dictionaryAdapter.setSelectItem(index);
                                }
                            }
                            break;
                        case 11:
                            if (diplomaDictonaryBean != null) {
                                index = dictionaryBeanList.indexOf(diplomaDictonaryBean);
                                if (index != -1) {
                                    lv_choose.setSelection(oldPosition);
                                    dictionaryAdapter.setSelectItem(index);
                                }
                            }
                            break;
                    }


                } else if (fkbaseResponse.getCode() == 2) {
                    Intent intent = new Intent(InformationAuthenticationActivity.this, LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(InformationAuthenticationActivity.this,
                                fkbaseResponse.getMsg());
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(InformationAuthenticationActivity.this,
                            error);
                }
            }
        });
    }
}
