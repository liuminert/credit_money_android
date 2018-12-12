package com.tesu.creditgold.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;


import com.tesu.creditgold.R;

import java.util.Calendar;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * 退出对话框(系统类型)
 *
 * @author C's
 * @version V1.0
 * @date 2014-4-1 下午5:59:10
 */
public class DialogUtils {

    /**
     * 自定义单按钮对话框
     */
    public static void showAlertDialog(Context ctx, String alertContent) {
        String temp;
        temp = alertContent;
        final AlertDialog alertDialog = new Builder(ctx).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        // *** 主要就是在这里实现这种效果的
        // 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
        View dialogView = View.inflate(ctx, R.layout.alert_dialog_main_title,
                null);
        window.setContentView(dialogView);
        // 设置对应提示框内容
        TextView alert_dialog_content = (TextView) dialogView
                .findViewById(R.id.alert_dialog_content);
        alert_dialog_content.setText(temp);
        // 点击空白处不消失
        alertDialog.setCanceledOnTouchOutside(false);


        // 为知道了按钮添加事件，执行退出应用操作
        TextView tv_roger = (TextView) dialogView.findViewById(R.id.tv_roger);
        tv_roger.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
    }

    /**
     * 自定义还款说明对话框
     * +点击事件
     */
    public static Dialog showRepayInstructionsAlertDialog(Context ctx, OnClickListener listener) {
        Builder builder = new Builder(ctx);

        final AlertDialog alertDialog = builder.create();
//		final AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        // *** 主要就是在这里实现这种效果的
        // 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
        View dialogView = View.inflate(ctx, R.layout.alert_dialog_show_repay_instruction,
                null);
        window.setContentView(dialogView);

        // 关闭
        ImageView alert_dialog_content = (ImageView) dialogView
                .findViewById(R.id.iv_close);
        TextView tv_message1 = (TextView) dialogView.findViewById(R.id.tv_message1);
        tv_message1.setText(Utils.setFountColor(tv_message1, 65, 69, UIUtils.getColor(R.color.text_red_one)));
        // 点击空白处不消失
        alertDialog.setCanceledOnTouchOutside(false);
        // 为知道了图片添加事件，执行退出应用操作
        alert_dialog_content.setOnClickListener(listener);
        return alertDialog;
    }

    /**
     * 自定义单按钮对话框
     */
    public static void showSingleAlertDialog(Context ctx, String title, String alertContent) {
        final AlertDialog alertDialog = new Builder(ctx).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        // *** 主要就是在这里实现这种效果的
        // 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
        View dialogView = View.inflate(ctx, R.layout.alert_dialog_single,
                null);
        window.setContentView(dialogView);
        // 设置对应提示框内容
        TextView alert_dialog_content = (TextView) dialogView
                .findViewById(R.id.alert_dialog_content);
        if (!TextUtils.isEmpty(alertContent)) {
            alert_dialog_content.setText(alertContent);
        }

        TextView alert_dialog_title = (TextView) dialogView
                .findViewById(R.id.alert_dialog_title);
        if (!TextUtils.isEmpty(title)) {
            alert_dialog_title.setText(title);
        }
        // 点击空白处不消失
        alertDialog.setCanceledOnTouchOutside(false);


        // 为知道了按钮添加事件，执行退出应用操作
        TextView tv_roger = (TextView) dialogView.findViewById(R.id.tv_roger);
        tv_roger.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
    }

    /**
     * 意见反馈对话框
     */
    public static AlertDialog showFeedBackDialog(Context ctx, OnClickListener onClickListener) {
        final AlertDialog alertDialog = new Builder(ctx).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        // *** 主要就是在这里实现这种效果的
        // 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
        View dialogView = View.inflate(ctx, R.layout.alert_dialog_show_feed_back,
                null);
        window.setContentView(dialogView);
        // 设置对应提示框内容
        TextView alert_dialog_content = (TextView) dialogView
                .findViewById(R.id.alert_dialog_content);
        // 点击空白处不消失
        alertDialog.setCanceledOnTouchOutside(false);


        // 为知道了按钮添加事件，执行退出应用操作
        TextView tv_roger = (TextView) dialogView.findViewById(R.id.tv_roger);
        tv_roger.setOnClickListener(onClickListener);

        return alertDialog;
    }

    // 提示对话框
    public static AlertDialog showUpdateDialog(Context context, String alertContent1, OnClickListener clickListener, boolean isForceUpdate) {
        final Context mContext = context;
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .create();
        // Alertdialog对话框，设置点击其他位置不消失 ,必须先AlertDialog.Builder.create()之后才能调用
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
//				设置对话框宽度
        WindowManager manager = alertDialog.getWindow().getWindowManager();
        Display d = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = (int) (d.getWidth() * 0.80);
//				params.height=(int) (d.getHeight()*0.28);
        alertDialog.getWindow().setAttributes(params);
        Window window = alertDialog.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
        View dialogView = View.inflate(context,
                R.layout.alert_dialog_update, null);
        window.setContentView(dialogView);
        // 设置对应提示框内容
//			TextView alert_dialog_title = (TextView) dialogView
//					.findViewById(R.id.alert_dialog_title);
//			if(!TextUtils.isEmpty(alert_title)){
//				alert_dialog_title.setText(alert_title);
//			}
        TextView alert_dialog_content1 = (TextView) dialogView
                .findViewById(R.id.alert_dialog_content1);
        if (!TextUtils.isEmpty(alertContent1)) {
            alert_dialog_content1.setText(Html.fromHtml(alertContent1));
        }
//			TextView alert_dialog_content2 = (TextView) dialogView
//					.findViewById(R.id.alert_dialog_content2);
//			if(!TextUtils.isEmpty(alertContent2)){
//				alert_dialog_content2.setText(alertContent2);
//			}
        // 为知道了按钮添加事件，执行退出应用操作
        TextView tv_roger = (TextView) dialogView
                .findViewById(R.id.tv_cancel);
        tv_roger.setOnClickListener(clickListener);
        if (!isForceUpdate) {
            tv_roger.setText("稍后再说");
        }

        TextView tv_update = (TextView) dialogView
                .findViewById(R.id.tv_update);
        tv_update.setOnClickListener(clickListener);

        return alertDialog;

    }

    /**
     * 自定义单按钮对话框
     * +点击事件
     */
    public static Dialog showAlertDialog(Context ctx, String alertContent, OnClickListener listener) {
        Builder builder = new Builder(ctx);

        final AlertDialog alertDialog = builder.create();
//		final AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        // *** 主要就是在这里实现这种效果的
        // 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
        View dialogView = View.inflate(ctx, R.layout.alert_dialog_main_title,
                null);
        window.setContentView(dialogView);

        // 设置对应提示框内容
        TextView alert_dialog_content = (TextView) dialogView
                .findViewById(R.id.alert_dialog_content);
        alert_dialog_content.setText(alertContent);
        // 点击空白处不消失
        alertDialog.setCanceledOnTouchOutside(false);
        // 为知道了按钮添加事件，执行退出应用操作
        Button tv_roger = (Button) dialogView.findViewById(R.id.tv_roger);
        tv_roger.setOnClickListener(listener);
        return alertDialog;
    }

    /**
     * 自定义单按钮对话框
     * +点击事件
     */
    public static Dialog showInputAlertDialog(Context ctx, OnClickListener listener) {
        Builder builder = new Builder(ctx);

        final AlertDialog alertDialog = builder.create();
//		final AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();

        Window window = alertDialog.getWindow();
        // *** 主要就是在这里实现这种效果的
        // 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
        View dialogView = View.inflate(ctx, R.layout.alert_dialog_show_input,
                null);
        alertDialog.setView(((Activity) ctx).getLayoutInflater().inflate(R.layout.alert_dialog_show_input, null));
        alertDialog.show();
        window.setContentView(dialogView);

//		// 设置对应提示框内容
//		EditText alert_dialog_content = (EditText) dialogView
//				.findViewById(R.id.alert_dialog_content);
        // 点击空白处不消失
        alertDialog.setCanceledOnTouchOutside(false);

        // 为知道了按钮添加事件，执行退出应用操作
        Button tv_roger = (Button) dialogView.findViewById(R.id.tv_roger);
        tv_roger.setOnClickListener(listener);
        return alertDialog;
    }

    /**
     * 自定义双按钮对话框
     */
    public static AlertDialog showAlertDoubleBtnDialog(Context ctx, String alertContent, OnClickListener listener) {
        String temp;
        temp = alertContent;
        final AlertDialog alertDialog = new Builder(ctx).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        // *** 主要就是在这里实现这种效果的
        // 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
        View dialogView = View.inflate(ctx, R.layout.alert_dialog_double_btn,
                null);
        window.setContentView(dialogView);
        // 设置对应提示框内容
        TextView alert_dialog_content = (TextView) dialogView
                .findViewById(R.id.alert_dialog_content);
        alert_dialog_content.setText(temp);
        //取消
        TextView tv_cancle = (TextView) dialogView
                .findViewById(R.id.tv_cancle);
        //确定
        TextView tv_ensure = (TextView) dialogView
                .findViewById(R.id.tv_ensure);
        tv_cancle.setOnClickListener(listener);
        tv_ensure.setOnClickListener(listener);
        // 点击空白处不消失
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    /**
     * 自定义拨打电话对话框
     */
    public static AlertDialog showTellCallDialog(Context ctx, OnClickListener listener) {
        final AlertDialog alertDialog = new Builder(ctx).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        // *** 主要就是在这里实现这种效果的
        // 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
        View dialogView = View.inflate(ctx, R.layout.alert_dialog_tell_call,
                null);
        window.setContentView(dialogView);
        // 设置对应提示框内容
        //取消
        TextView tv_cancle = (TextView) dialogView
                .findViewById(R.id.tv_cancle);
        //确定
        TextView tv_ensure = (TextView) dialogView
                .findViewById(R.id.tv_ensure);
        tv_cancle.setOnClickListener(listener);
        tv_ensure.setOnClickListener(listener);
        // 点击空白处不消失
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    /**
     * 自定义删除订单对话框
     */
    public static AlertDialog showDelOrderDialog(Context ctx, OnClickListener listener) {
        final AlertDialog alertDialog = new Builder(ctx).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        // *** 主要就是在这里实现这种效果的
        // 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
        View dialogView = View.inflate(ctx, R.layout.alert_dialog_del_order,
                null);
        window.setContentView(dialogView);
        // 设置对应提示框内容
        //取消
        TextView tv_cancle = (TextView) dialogView
                .findViewById(R.id.tv_cancle);
        //确定
        TextView tv_ensure = (TextView) dialogView
                .findViewById(R.id.tv_ensure);
        tv_cancle.setOnClickListener(listener);
        tv_ensure.setOnClickListener(listener);
        // 点击空白处不消失
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    /**
     * 自定义确认订单信息对话框
     */
    public static AlertDialog showOrderCheckDialog(Context ctx, String shopName, String amount, String period, String money, OnClickListener listener) {
        final AlertDialog alertDialog = new Builder(ctx).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        // *** 主要就是在这里实现这种效果的
        // 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
        View dialogView = View.inflate(ctx, R.layout.alert_dialog_order_check,
                null);
        window.setContentView(dialogView);
        // 设置对应提示框内容
        TextView tv_shop_name = (TextView) dialogView
                .findViewById(R.id.tv_shop_name);
        TextView tv_amount = (TextView) dialogView
                .findViewById(R.id.tv_amount);
        TextView tv_period = (TextView) dialogView
                .findViewById(R.id.tv_period);
        TextView tv_money = (TextView) dialogView
                .findViewById(R.id.tv_money);
        tv_shop_name.setText(shopName);
        tv_amount.setText(amount + "元");
        tv_period.setText(period + "个月");
        tv_money.setText(money);
        //继续提交
        Button btn_continue = (Button) dialogView
                .findViewById(R.id.btn_continue);
        //重新提交
        Button btn_reset = (Button) dialogView
                .findViewById(R.id.btn_reset);
        btn_continue.setOnClickListener(listener);
        btn_reset.setOnClickListener(listener);
        // 点击空白处不消失
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    /**
     * 显示合同弹框
     *
     * @param ctx
     * @param title
     * @param url
     * @param listener
     * @return
     */
    public static AlertDialog showArticleDialog(Context ctx, String title, String url, OnClickListener listener) {
        final AlertDialog alertDialog = new Builder(ctx).create();
        String CSS_STYPE = "<head><style>img{max-width:340px !important;}</style></head>";
        alertDialog.show();
        Window window = alertDialog.getWindow();
        // *** 主要就是在这里实现这种效果的
        // 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
        View dialogView = View.inflate(ctx, R.layout.article_dialog,
                null);
        window.setContentView(dialogView);

        TextView tv_contract_title = (TextView) dialogView.findViewById(R.id.tv_contract_title);
        WebView webView = (WebView) dialogView.findViewById(R.id.webview);
        Button btn_agree = (Button) dialogView.findViewById(R.id.btn_agree);

        if (!TextUtils.isEmpty(title)) {
            tv_contract_title.setText(title);
        }
        if (!TextUtils.isEmpty(url)) {
            webView.loadDataWithBaseURL(null, CSS_STYPE + url, "text/html", "utf-8", null);
        }

        btn_agree.setOnClickListener(listener);
        // 点击空白处不消失
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    /**
     * 显示合同信息提示
     *
     * @param ctx
     * @param title
     * @param listener
     * @return
     */
    public static AlertDialog showArticleMessageDialog(Context ctx, String title, OnClickListener listener) {
        final AlertDialog alertDialog = new Builder(ctx).create();

        alertDialog.show();
        Window window = alertDialog.getWindow();
        // *** 主要就是在这里实现这种效果的
        // 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
        View dialogView = View.inflate(ctx, R.layout.article_message_dialog,
                null);
        window.setContentView(dialogView);

        TextView tv_article = (TextView) dialogView.findViewById(R.id.tv_article);
        Button btn_agree = (Button) dialogView.findViewById(R.id.btn_agree);

        tv_article.setOnClickListener(listener);
        btn_agree.setOnClickListener(listener);

        // 点击空白处不消失
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        return alertDialog;
    }

    /**
     * 显示个人信息使用授权书提示
     *
     * @param ctx
     * @param title
     * @param listener
     * @return
     */
    public static AlertDialog showPersonalInformationDialog(Context ctx, String title, OnClickListener listener) {
        final AlertDialog alertDialog = new Builder(ctx).create();

        alertDialog.show();
        Window window = alertDialog.getWindow();
        // *** 主要就是在这里实现这种效果的
        // 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
        View dialogView = View.inflate(ctx, R.layout.personal_information_dialog,
                null);
        window.setContentView(dialogView);

        TextView tv_article = (TextView) dialogView.findViewById(R.id.tv_article);
        TextView tv_article1 = (TextView) dialogView.findViewById(R.id.tv_article1);
        Button btn_agree = (Button) dialogView.findViewById(R.id.btn_agree);

        tv_article.setOnClickListener(listener);
        tv_article1.setOnClickListener(listener);
        btn_agree.setOnClickListener(listener);

        // 点击空白处不消失
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        return alertDialog;
    }

    /**
     * 选择交易类型
     *
     * @param ctx
     * @param listener
     * @return
     */
    public static AlertDialog showTransactionTypeDialog(Context ctx, OnClickListener listener,int type) {
        final AlertDialog alertDialog = new AlertDialog.Builder(ctx)
                .create();
        // Alertdialog对话框，设置点击其他位置不消失 ,必须先AlertDialog.Builder.create()之后才能调用
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
//				设置对话框宽度
        WindowManager manager = alertDialog.getWindow().getWindowManager();
        Display d = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = (int) (d.getWidth() * 1);
//				params.height=(int) (d.getHeight()*0.28);
        alertDialog.getWindow().setAttributes(params);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);

        // *** 主要就是在这里实现这种效果的
        // 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
        View dialogView = View.inflate(ctx, R.layout.transaction_type_dialog,
                null);
        window.setContentView(dialogView);

        TextView tv_cancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
        TextView tv_all = (TextView) dialogView.findViewById(R.id.tv_all);
        TextView tv_credit_amount = (TextView) dialogView.findViewById(R.id.tv_credit_amount);
        TextView tv_transaction_expenditure = (TextView) dialogView.findViewById(R.id.tv_transaction_expenditure);
        TextView tv_repayment_amount = (TextView) dialogView.findViewById(R.id.tv_repayment_amount);

        tv_cancel.setOnClickListener(listener);
        tv_all.setOnClickListener(listener);
        tv_credit_amount.setOnClickListener(listener);
        tv_transaction_expenditure.setOnClickListener(listener);
        tv_repayment_amount.setOnClickListener(listener);

        switch (type){
            case 0:
                tv_all.setBackgroundResource(R.mipmap.button_yellow_rec);
                tv_all.setTextColor(UIUtils.getResources().getColor(R.color.tab_background));
                tv_credit_amount.setBackgroundResource(R.mipmap.button_white_rec);
                tv_credit_amount.setTextColor(UIUtils.getResources().getColor(R.color.TextGrayColor));
                tv_transaction_expenditure.setBackgroundResource(R.mipmap.button_white_rec);
                tv_transaction_expenditure.setTextColor(UIUtils.getResources().getColor(R.color.TextGrayColor));
                tv_repayment_amount.setBackgroundResource(R.mipmap.button_white_rec);
                tv_repayment_amount.setTextColor(UIUtils.getResources().getColor(R.color.TextGrayColor));
                break;
            case 1:
                tv_all.setBackgroundResource(R.mipmap.button_white_rec);
                tv_all.setTextColor(UIUtils.getResources().getColor(R.color.TextGrayColor));
                tv_credit_amount.setBackgroundResource(R.mipmap.button_yellow_rec);
                tv_credit_amount.setTextColor(UIUtils.getResources().getColor(R.color.tab_background));
                tv_transaction_expenditure.setBackgroundResource(R.mipmap.button_white_rec);
                tv_transaction_expenditure.setTextColor(UIUtils.getResources().getColor(R.color.TextGrayColor));
                tv_repayment_amount.setBackgroundResource(R.mipmap.button_white_rec);
                tv_repayment_amount.setTextColor(UIUtils.getResources().getColor(R.color.TextGrayColor));
                break;
            case 2:
                tv_all.setBackgroundResource(R.mipmap.button_white_rec);
                tv_all.setTextColor(UIUtils.getResources().getColor(R.color.TextGrayColor));
                tv_credit_amount.setBackgroundResource(R.mipmap.button_white_rec);
                tv_credit_amount.setTextColor(UIUtils.getResources().getColor(R.color.TextGrayColor));
                tv_transaction_expenditure.setBackgroundResource(R.mipmap.button_yellow_rec);
                tv_transaction_expenditure.setTextColor(UIUtils.getResources().getColor(R.color.tab_background));
                tv_repayment_amount.setBackgroundResource(R.mipmap.button_white_rec);
                tv_repayment_amount.setTextColor(UIUtils.getResources().getColor(R.color.TextGrayColor));
                break;
            case 3:
                tv_all.setBackgroundResource(R.mipmap.button_white_rec);
                tv_all.setTextColor(UIUtils.getResources().getColor(R.color.TextGrayColor));
                tv_credit_amount.setBackgroundResource(R.mipmap.button_white_rec);
                tv_credit_amount.setTextColor(UIUtils.getResources().getColor(R.color.TextGrayColor));
                tv_transaction_expenditure.setBackgroundResource(R.mipmap.button_white_rec);
                tv_transaction_expenditure.setTextColor(UIUtils.getResources().getColor(R.color.TextGrayColor));
                tv_repayment_amount.setBackgroundResource(R.mipmap.button_yellow_rec);
                tv_repayment_amount.setTextColor(UIUtils.getResources().getColor(R.color.tab_background));
                break;
        }

        // 点击空白处不消失
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        return alertDialog;
    }

    /**
     * 显示日历选择对话框
     *
     * @param ctx
     * @param listener
     * @return
     */
    public static AlertDialog showDatePickerDialog(Context ctx, OnClickListener listener, DatePicker.OnDateChangedListener onDateChangedListener, int year, int monthOfYear, int dayOfMonth, boolean isDayVisible) {
        final AlertDialog alertDialog = new AlertDialog.Builder(ctx)
                .create();
        // Alertdialog对话框，设置点击其他位置不消失 ,必须先AlertDialog.Builder.create()之后才能调用
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
//				设置对话框宽度
        WindowManager manager = alertDialog.getWindow().getWindowManager();
        Display d = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = (int) (d.getWidth() * 1);
//				params.height=(int) (d.getHeight()*0.28);
        alertDialog.getWindow().setAttributes(params);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);

        // *** 主要就是在这里实现这种效果的
        // 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
        View dialogView = View.inflate(ctx, R.layout.date_picker_dialog,
                null);
        window.setContentView(dialogView);

        TextView tv_cancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
        TextView tv_submit = (TextView) dialogView.findViewById(R.id.tv_submit);
        DatePicker datePickerStart = (DatePicker) dialogView.findViewById(R.id.datePickerStart);

        tv_cancel.setOnClickListener(listener);
        tv_submit.setOnClickListener(listener);

        datePickerStart.init(year, monthOfYear, dayOfMonth, onDateChangedListener);

        setMyStyle(ctx,datePickerStart);
        // 如果要隐藏当前日期，则使用下面方法。
        if (!isDayVisible) {
            hidDay(datePickerStart);
        }
        if (Integer.valueOf(Build.VERSION.SDK)>=11) {
            datePickerStart.setMaxDate(new Date().getTime());
        }

        // 点击空白处不消失
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        return alertDialog;
    }

    /**
     * 隐藏DatePicker中的日期显示
     *
     * @param mDatePicker
     */
    public static void hidDay(DatePicker mDatePicker) {
        try {
            /* 处理android5.0以上的特殊情况 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                if (daySpinnerId != 0) {
                    View daySpinner = mDatePicker.findViewById(daySpinnerId);
                    if (daySpinner != null) {
                        daySpinner.setVisibility(View.GONE);
                    }
                }
            } else {
                Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerfFields) {
                    if ("mDaySpinner".equals(datePickerField.getName()) || ("mDayPicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try {
                            dayPicker = datePickerField.get(mDatePicker);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        ((View) dayPicker).setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setMyStyle(Context mContext,DatePicker mDatePicker) {
        LinearLayout llFirst = (LinearLayout) mDatePicker.getChildAt(0);
        LinearLayout llSecond = (LinearLayout) llFirst.getChildAt(0);
        llSecond.setPadding(0, 0,0, 0);
        llSecond.setBackgroundResource(R.color.transparent);//设置datepickerdialog的背景

        for (int i = 0; i < llSecond.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) llSecond.getChildAt(i); // Numberpickers in llSecond
            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                //更改分割线的颜色
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, ContextCompat.getDrawable(mContext, R.color.LineGrayColor));
                        setPickerMargin(mContext, picker);
                        setNumberPickerDivider(picker);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    /** * 设置picker之间的间距 */
    public static void setPickerMargin(Context mContext,NumberPicker picker) {
//        LinearLayout.LayoutParams p= (LinearLayout.LayoutParams) picker.getLayoutParams();
//        p.setMargins(0,0,0,0);
//        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.JELLY_BEAN_MR1){
//            p.setMarginStart(0);
//            p.setMarginEnd(0);
//        }

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width/2, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.JELLY_BEAN_MR1){
            params.setMarginStart(0);
            params.setMarginEnd(0);
        }
        picker.setLayoutParams(params);
    }

    /** * 设置picker分割线的宽度 */
    public static void setNumberPickerDivider(NumberPicker picker) {
        Field[] fields=NumberPicker.class.getDeclaredFields();
        for(Field f:fields){
            try{
                if(f.getName().equals("mSelectionDividerHeight")){
                    f.setAccessible(true);
                    f.set(picker,2);
                    break;
                }
            }catch (Exception e){
             e.printStackTrace();
            }

        }
    }




    /**
     * 创建列表对话框
     *
     * @param ctx      上下文 必填
     * @param iconId   图标，如：R.drawable.icon 必填
     * @param title    标题 必填
     * @param itemsId  字符串数组资源id 必填
     * @param listener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
    public static Dialog createListDialog(Context ctx, int iconId,
                                          String title, int itemsId,
                                          DialogInterface.OnClickListener listener) {
        Dialog dialog = null;
        Builder builder = new Builder(
                ctx);
        // 设置对话框的图标
        builder.setIcon(iconId);
        // 设置对话框的标题
        builder.setTitle(title);
        // 添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
        builder.setItems(itemsId, listener);
        // 创建一个列表对话框
        dialog = builder.create();
        return dialog;
    }

    /**
     * 创建单选按钮对话框
     *
     * @param ctx       上下文 必填
     * @param iconId    图标，如：R.drawable.icon 必填
     * @param title     标题 必填
     * @param itemsId   字符串数组资源id 必填
     * @param listener  单选按钮项监听器，需实现android.content.DialogInterface.OnClickListener接口
     *                  必填
     * @param btnName   按钮名称 必填
     * @param listener2 按钮监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
    public static Dialog createRadioDialog(Context ctx, int iconId,
                                           String title, int itemsId,
                                           DialogInterface.OnClickListener listener,
                                           String btnName,
                                           DialogInterface.OnClickListener listener2) {
        Dialog dialog = null;
        Builder builder = new Builder(
                ctx);
        // 设置对话框的图标
        builder.setIcon(iconId);
        // 设置对话框的标题
        builder.setTitle(title);
        // 0: 默认第一个单选按钮被选中
        builder.setSingleChoiceItems(itemsId, 0, listener);
        // 添加一个按钮
        builder.setPositiveButton(btnName, listener2);
        // 创建一个单选按钮对话框
        dialog = builder.create();
        return dialog;
    }

    /**
     * 创建复选对话框
     *
     * @param ctx       上下文 必填
     * @param iconId    图标，如：R.drawable.icon 必填
     * @param title     标题 必填
     * @param itemsId   字符串数组资源id 必填
     * @param flags     初始复选情况 必填
     * @param listener  单选按钮项监听器，需实现android.content.DialogInterface.
     *                  OnMultiChoiceClickListener接口 必填
     * @param btnName   按钮名称 必填
     * @param listener2 按钮监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
    public static Dialog createCheckBoxDialog(
            Context ctx,
            int iconId,
            String title,
            int itemsId,
            boolean[] flags,
            DialogInterface.OnMultiChoiceClickListener listener,
            String btnName,
            DialogInterface.OnClickListener listener2) {
        Dialog dialog = null;
        Builder builder = new Builder(
                ctx);
        // 设置对话框的图标
        builder.setIcon(iconId);
        // 设置对话框的标题
        builder.setTitle(title);
        builder.setMultiChoiceItems(itemsId, flags, listener);
        // 添加一个按钮
        builder.setPositiveButton(btnName, listener2);
        // 创建一个复选对话框
        dialog = builder.create();
        return dialog;
    }

    /**
     * 日期对话框
     *
     * @param context 上下文
     * @param v       带.setText的控件
     * @return 对话框实例
     */
    public static Dialog createDateDialog(Context context, final View v) {
        Dialog dialog = null;
        Calendar calender = Calendar.getInstance();
        dialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        if (v instanceof TextView) {

                            ((TextView) v).setText(year + "年"
                                    + (monthOfYear + 1) + "月" + dayOfMonth
                                    + "日");
                        }
                        if (v instanceof EditText) {
                            ((EditText) v).setText(year + "年"
                                    + (monthOfYear + 1) + "月" + dayOfMonth
                                    + "日");
                        }

                    }
                }, calender.get(calender.YEAR), calender.get(calender.MONTH),
                calender.get(calender.DAY_OF_MONTH));

        return dialog;
    }

    // /**
    // * 加载对话框
    // *
    // * @param context
    // * 上下文
    // * @param msg
    // * 内容
    // * @return
    // */
    // public static ProgressDialog createLoadDialog(Context context, String
    // msg) {
    //
    // android.app.ProgressDialog dialog = new ProgressDialog(context);
    // // 设置风格为圆形进度条
    // dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    // // 设置内容
    // dialog.setMessage(msg);
    // // 设置进度条是否为不明确
    // dialog.setIndeterminate(false);
    // // 设置空白出不关闭
    // dialog.setCanceledOnTouchOutside(false);
    // // 设置进度条是否可以按退回键取消
    // dialog.setCancelable(true);
    // return dialog;
    // }

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @return
     */
    public static Dialog createLoadDialog2(Context context, boolean isClickable) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        layout.getBackground().setAlpha(100);
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(isClickable);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        return loadingDialog;
    }

    public static Dialog createLoadDialog(Context context, boolean isClickable) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_layout, null);// 得到加载view
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(isClickable);// 不可以用“返回键”取消
        loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;
    }

    /**
     * @return 创建一个提示网络没有打开的dialog
     */
    public static void createNetWorkNotOpenDialog(Context ctx) {
        showAlertDialog(ctx, "网络连接已经断开,请稍后重试");
    }

    /**
     * @return 创建带进度条的对话框
     */
    public static ProgressDialog createProgressDialog(Context context, DialogInterface.OnClickListener listener) {


        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "取消", listener);
        return dialog;
    }


}
