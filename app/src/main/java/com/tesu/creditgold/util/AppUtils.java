package com.tesu.creditgold.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tesu.creditgold.bean.CallHistoryBean;
import com.tesu.creditgold.bean.MyAppInfo;
import com.tesu.creditgold.bean.PhoneInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/1/11 0011.
 */
public class AppUtils {
    static  String TAG = "ApkTool";
    public static List<MyAppInfo> mLocalInstallApps = null;
    /**联系人显示名称**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**电话号码**/
    private static final int PHONES_NUMBER_INDEX = 1;
    private static final String[] PHONES_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID };

    //获取应用名称
    public static List<MyAppInfo> scanLocalInstallAppList(PackageManager packageManager) {
        List<MyAppInfo> myAppInfos = new ArrayList<MyAppInfo>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤掉系统app
//            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
//                continue;
//            }
                MyAppInfo myAppInfo = new MyAppInfo();
                myAppInfo.setAppName(packageInfo.applicationInfo.loadLabel(packageManager).toString());
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                    continue;
                }
                myAppInfo.setImage(packageInfo.applicationInfo.loadIcon(packageManager));
                myAppInfos.add(myAppInfo);
            }
        }catch (Exception e){
            Log.e(TAG, "===============获取应用包信息失败");
        }
        return myAppInfos;
    }

    //获取通话记录
    //    ContentResolver cr;
    //    cr=getContentResolver();
    //    String callHistoryListStr=getCallHistoryList(null, cr);
    public static List<CallHistoryBean> getCallHistoryList(Context context, ContentResolver cr){
        List<CallHistoryBean> callHistoryBeanList = new ArrayList<CallHistoryBean>();

        Cursor cs;
        cs=cr.query(CallLog.Calls.CONTENT_URI, //系统方式获取通讯录存储地址
                new String[]{
                        CallLog.Calls.CACHED_NAME,  //姓名
                        CallLog.Calls.NUMBER,    //号码
                        CallLog.Calls.TYPE,  //呼入/呼出(2)/未接
                        CallLog.Calls.DATE,  //拨打时间
                        CallLog.Calls.DURATION   //通话时长
                },null,null,CallLog.Calls.DEFAULT_SORT_ORDER);
        String callHistoryListStr="";
        int i=0;
        if(cs!=null &&cs.getCount()>0){
            for(cs.moveToFirst();!cs.isAfterLast() & i<300; cs.moveToNext()){
                String callName=cs.getString(0);
                String callNumber=cs.getString(1);
                //通话类型
                int callType=Integer.parseInt(cs.getString(2));
                String callTypeStr="";
                switch (callType) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callTypeStr="来电";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callTypeStr="去电";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callTypeStr="未接电话";
                        break;
                }
                //拨打时间
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date callDate=new Date(Long.parseLong(cs.getString(3)));
                String callDateStr=sdf.format(callDate);
                //通话时长
                int callDuration=Integer.parseInt(cs.getString(4));
//                int min=callDuration/60;
//                int sec=callDuration%60;
//                String callDurationStr=min+"分"+sec+"秒";
//                String callOne="类型：" + callTypeStr + ", 称呼：" + callName + ", 号码："
//                        +callNumber + ", 通话时长：" + callDurationStr + ", 时间:" + callDateStr
//                        +"\n---------------------\n";

                CallHistoryBean callHistoryBean = new CallHistoryBean();
                if(!TextUtils.isEmpty(callTypeStr)){
                    callHistoryBean.setDial_mode(callTypeStr);
                }
                if(!TextUtils.isEmpty(callNumber)){
                    callHistoryBean.setMobile_phone(callNumber);
                }
                if(!TextUtils.isEmpty(callName)){
                    callHistoryBean.setName(callName);
                }
                if(callDuration==-1){
                    continue;
                }

                callHistoryBean.setCall_time(callDuration);
                callHistoryBean.setCall_when(callDateStr);
                callHistoryBeanList.add(callHistoryBean);

//                callHistoryListStr+=callOne;
                i++;
            }
        }

//        return callHistoryListStr;
        return callHistoryBeanList;
    }
//    //获取短信
//    public static String getSmsInPhone() {
//        final String SMS_URI_ALL = "content://sms/";
//        final String SMS_URI_INBOX = "content://sms/inbox";
//        final String SMS_URI_SEND = "content://sms/sent";
//        final String SMS_URI_DRAFT = "content://sms/draft";
//        final String SMS_URI_OUTBOX = "content://sms/outbox";
//        final String SMS_URI_FAILED = "content://sms/failed";
//        final String SMS_URI_QUEUED = "content://sms/queued";
//
//        StringBuilder smsBuilder = new StringBuilder();
//
//        try {
//            Uri uri = Uri.parse(SMS_URI_ALL);
//            String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
//            Cursor cur = UIUtils.getContext().getContentResolver().query(uri, projection, null, null, "date desc");      // 获取手机内部短信
//            int j = 0;
//
//            if (cur.moveToFirst()) {
//                int index_Address = cur.getColumnIndex("address");
//                int index_Person = cur.getColumnIndex("person");
//                int index_Body = cur.getColumnIndex("body");
//                int index_Date = cur.getColumnIndex("date");
//                int index_Type = cur.getColumnIndex("type");
//
//                do {
//
//                    j++;
//
//                    String strAddress = cur.getString(index_Address);
//                    int intPerson = cur.getInt(index_Person);
//                    String strbody = cur.getString(index_Body);
//                    long longDate = cur.getLong(index_Date);
//                    int intType = cur.getInt(index_Type);
//
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                    Date d = new Date(longDate);
//                    String strDate = dateFormat.format(d);
//
//                    String strType = "";
//                    if (intType == 1) {
//                        strType = "接收";
//                    } else if (intType == 2) {
//                        strType = "发送";
//                    } else {
//                        strType = "null";
//                    }
//
//                    smsBuilder.append("[ ");
//                    smsBuilder.append(strAddress + ", ");
//                    smsBuilder.append(intPerson + ", ");
//                    smsBuilder.append(strbody + ", ");
//                    smsBuilder.append(strDate + ", ");
//                    smsBuilder.append(strType);
//                    smsBuilder.append(" ]\n\n");
//                } while (cur.moveToNext() && j<300);
//
//                if (!cur.isClosed()) {
//                    cur.close();
//                    cur = null;
//                }
//            } else {
//                smsBuilder.append("no result!");
//            } // end if
//
//            smsBuilder.append("getSmsInPhone has executed!");
//
//        } catch (SQLiteException ex) {
//        }
//
//        return smsBuilder.toString();
//    }

    /**
     * 获取手机通讯录
     * @param context
     * @return
     */
    public static List getPhoneNumberFromMobile(Context context) {
        // TODO Auto-generated constructor stub
        List list = new ArrayList();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        //moveToNext方法返回的是一个boolean类型的数据
        int i=0;
        while (cursor.moveToNext()) {

            i++;
            if(i>=300){
                break;
            }

            //读取通讯录的姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            PhoneInfo phoneInfo = new PhoneInfo(name, number);
            list.add(phoneInfo);
        }
        return list;
    }

    /**
     * 获取sim卡上的通讯录
     * @param context
     * @return
     */
    public static List getSIMContacts(Context context) {
        // TODO Auto-generated constructor stub
        List list = new ArrayList();
        Uri uri = Uri.parse("content://icc/adn");
        Cursor cursor = context.getContentResolver().query(uri, PHONES_PROJECTION, null, null,
                null);
        //moveToNext方法返回的是一个boolean类型的数据
        int i=0;
        while (cursor.moveToNext()) {

            i++;
            if(i>=300){
                break;
            }

            //读取通讯录的号码
            String number = cursor.getString(PHONES_NUMBER_INDEX);
            if(TextUtils.isEmpty(number)){
                continue;
            }
            //读取通讯录的姓名
            String name = cursor.getString(PHONES_DISPLAY_NAME_INDEX);
            PhoneInfo phoneInfo = new PhoneInfo(name, number);
            list.add(phoneInfo);
        }
        return list;
    }

    public static String getPhoneNuame(){
        return android.os.Build.MODEL;
    }


//    public static boolean isAppInstalled(Context context, String uri) {
//        PackageManager pm = context.getPackageManager();
//        boolean installed = false;
//        try {
//            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
//            installed = true;
//        } catch (PackageManager.NameNotFoundException e) {
//            installed = false;
//        }
//        return installed;
//    }

    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }
}
