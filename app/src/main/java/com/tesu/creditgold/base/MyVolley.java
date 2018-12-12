package com.tesu.creditgold.base;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.cache.SimpleImageLoader;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.ExceptionUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.Utils;

import java.util.ArrayList;
import java.util.Map;


/**
 * Helper class that is used to provide references to initialized RequestQueue(s) and ImageLoader(s)
 *
 * @author Ognyan Bankov
 */
public class MyVolley {
    private static RequestQueue mRequestQueue;
    private static RequestQueue mRequestBeaconQueue;
    private static SimpleImageLoader mImageLoader;
    private static String TAG = MyVolley.class.getSimpleName();
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_NOMAL = 2;
    public static final int PRIORITY_HIGH = 3;
    public static final int PRIORITY_IMMEDIATE = 4;
    public static Context mContext;


    public static void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mRequestBeaconQueue = Volley.newRequestQueue(context);
        //mImageLoader = new SimpleImageLoader(mRequestQueue, BitmapImageCache.getInstance(null));
        mContext = context;
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    /**
     * Returns instance of ImageLoader initialized with {@see FakeImageCache} which effectively means
     * that no memory caching is used. This is useful for images that you know that will be show
     * only once.
     *
     * @return
     */
    public static SimpleImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }


    public static final int POST = 1;
    public static final int GET = 0;
    //private String uri=Constants.SERVER_URL;

    /**
     * 返回时回调接口
     * dealWithJson 处理返回json
     * dealWithError 处理返回错误
     */

    public interface VolleyCallback {
        public void dealWithJson(String address, String json);

        public void dealWithError(String address, String error);

    }

//    public static void uploadNoFile(int type, String url, Map<String, String> map, final VolleyCallback callback, Request.Priority priority) {
//        uploadNoFile(type, url, map, callback, -1, priority);
//    }

//    public static void uploadNoFile(int type, String url, Map<String, String> map, final VolleyCallback callback, int timeout, Request.Priority priority) {
//        DefaultRetryPolicy retry = null;
//        if (timeout != -1) {
//            retry = new DefaultRetryPolicy(timeout, 3, 1);
//        }
//        else{
//            retry=( new DefaultRetryPolicy( 500000,//默认超时时间，应设置一个稍微大点儿的，例如本处的500000
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ) );
//        }
//        final String method = url;
//        RequestQueue queue = null;
//
//        queue = MyVolley.getRequestQueue();
//        Log.e(TAG, "OtherQueue" + priority);
//
//        //queue.cancelAll(method);
//        StringRequest request = null;
//        if (type == GET) {
//            Uri.Builder builder = Uri.parse(Constants.SERVER_URL + method).buildUpon();
//            for (String key : map.keySet()) {
//                builder.appendQueryParameter(key, map.get(key));
//            }
//            request = new StringRequest(builder.toString(), new Listener<String>() {
//                @Override
//                public void onResponse(String arg0) {
//
////					Log.i(TAG,"onresponse:"+arg0);
//                    callback.dealWithJson(method, arg0);
//                }
//            }, new ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError arg0) {
//
//                    Log.i(TAG, "volleyerror: " + arg0.getMessage());
//                    callback.dealWithError(method, ExceptionUtils.getExceptionMsg(arg0));
//                }
//            });
//
//        } else if (type == POST) {
//            request = new StringRequest(Method.POST, Constants.SERVER_URL + method, new Listener<String>() {
//                @Override
//                public void onResponse(String arg0) {
//                    //Log.i(TAG,"onresponse:"+arg0);
//                    callback.dealWithJson(method, arg0);
//                }
//            }, new ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError arg0) {
//
//                    Log.i(TAG, "volleyerror: " + arg0.getMessage());
//                    callback.dealWithError(method, ExceptionUtils.getExceptionMsg(arg0));
//                }
//
//            });
//            request.setParams(map);
//        }
//        if (retry != null) request.setRetryPolicy(retry);
//        request.setPriority(priority);
//        request.setShouldCache(false);
//        request.setTag(method);
//        queue.add(request);
//    }

    public static void uploadNoFileWholeUrl(int type, String url, Map<String, String> map, final VolleyCallback callback) {

        String token = SharedPrefrenceUtils.getString(mContext, "token");
        if(!TextUtils.isEmpty(token)){
            map.put("token",token);
        }

        String timetemp = SharedPrefrenceUtils.getString(mContext, "timetemp");
        if(TextUtils.isEmpty(timetemp)){
            timetemp = "0";
        }
        long cuTime = System.currentTimeMillis()+Long.valueOf(timetemp);

        if(url.contains("credit_money_background")){
            map.put("app_id",Constants.app_id);
            map.put("sign", Utils.sign(Constants.app_id, Constants.app_secret, String.valueOf(cuTime)));
        }else {
            map.put("app_id",Constants.fk_app_id);
            map.put("sign", Utils.sign(Constants.fk_app_id, Constants.fk_app_secret, String.valueOf(cuTime)));
        }

        map.put("timetemp",String.valueOf(cuTime));


        DefaultRetryPolicy retry = null;
        //10秒(重复请求次数为1)
        retry = new DefaultRetryPolicy(15000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        final String method = url;
        RequestQueue queue = null;
        queue = MyVolley.getRequestQueue();
        Log.e(TAG, "OtherQueue" + Request.Priority.NORMAL);

        //queue.cancelAll(method);
        StringRequest request = null;
        if (type == GET) {
            Uri.Builder builder = Uri.parse(method).buildUpon();
            for (String key : map.keySet()) {
                builder.appendQueryParameter(key, map.get(key));
            }
            SsX509TrustManager.allowAllSSL();
            request = new StringRequest(builder.toString(), new Listener<String>() {
                @Override
                public void onResponse(String arg0) {

//					Log.i(TAG,"onresponse:"+arg0);
                    callback.dealWithJson(method, arg0);
                }
            }, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {

                    Log.i(TAG, "volleyerror: " + arg0.getMessage());
                    callback.dealWithError(method, ExceptionUtils.getExceptionMsg(arg0));
                }
            });

        } else if (type == POST) {
            SsX509TrustManager.allowAllSSL();
            request = new StringRequest(Method.POST, method, new Listener<String>() {
                @Override
                public void onResponse(String arg0) {
                    //Log.i(TAG,"onresponse:"+arg0);
                    callback.dealWithJson(method, arg0);
                }
            }, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {

                    Log.i(TAG, "volleyerror: " + arg0.getMessage());
                    callback.dealWithError(method, ExceptionUtils.getExceptionMsg(arg0));
                }

            });
            request.setParams(map);
        }
        if (retry != null) {
            request.setRetryPolicy(retry);
        }
        request.setPriority(Request.Priority.NORMAL);
        request.setShouldCache(false);
        request.setTag(method);
        queue.add(request);
    }

    public static void uploadNoFileWholeUrl1(int type, String url, Map<String, String> map, final VolleyCallback callback) {
        DefaultRetryPolicy retry = null;
        //12秒(4的3倍时间)
        retry = new DefaultRetryPolicy(15000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        final String method = url;
        RequestQueue queue = null;
        queue = MyVolley.getRequestQueue();
        Log.e(TAG, "OtherQueue" + Request.Priority.NORMAL);

        //queue.cancelAll(method);
        StringRequest request = null;
        if (type == GET) {
            Uri.Builder builder = Uri.parse(method).buildUpon();
            for (String key : map.keySet()) {
                builder.appendQueryParameter(key, map.get(key));
            }
            SsX509TrustManager.allowAllSSL();
            request = new StringRequest(builder.toString(), new Listener<String>() {
                @Override
                public void onResponse(String arg0) {

//					Log.i(TAG,"onresponse:"+arg0);
                    callback.dealWithJson(method, arg0);
                }
            }, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {

                    Log.i(TAG, "volleyerror: " + arg0.getMessage());
                    callback.dealWithError(method, ExceptionUtils.getExceptionMsg(arg0));
                }
            });

        } else if (type == POST) {
            SsX509TrustManager.allowAllSSL();
            request = new StringRequest(Method.POST, method, new Listener<String>() {
                @Override
                public void onResponse(String arg0) {
                    //Log.i(TAG,"onresponse:"+arg0);
                    callback.dealWithJson(method, arg0);
                }
            }, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {

                    Log.i(TAG, "volleyerror: " + arg0.getMessage());
                    callback.dealWithError(method, ExceptionUtils.getExceptionMsg(arg0));
                }

            });
            request.setParams(map);
        }
        if (retry != null) {
            request.setRetryPolicy(retry);
        }
        request.setPriority(Request.Priority.NORMAL);
        request.setShouldCache(false);
        request.setTag(method);
        queue.add(request);
    }

    /**
     * 此方法默认get请求
     *
     * @param url      调用接口方法
     * @param map      上传参数
     * @param callback 服务器端返回参数回调接口
     */
//    public static void uploadNoFile(String url, Map<String, String> map, final VolleyCallback callback) {
//        uploadNoFile(GET, url, map, callback);
//    }


    /**
     * @param type     请求类型（POST，GET）
     * @param url      调用接口方法
     * @param map      上传参数
     * @param callback 服务器端返回参数回调接口
     * @param timeout  设置连接超时
     */
//    public static void uploadNoFile(int type, String url, Map<String, String> map, final VolleyCallback callback, int timeout) {
//        uploadNoFile(type, url, map, callback, timeout, Request.Priority.NORMAL);
//    }

    /**
     * @param type     请求类型（POST，GET）
     * @param url      调用接口方法
     * @param map      上传参数
     * @param callback 服务器端返回参数回调接口
     */
//    public static void uploadNoFile(int type, String url, Map<String, String> map, final VolleyCallback callback) {
//        uploadNoFile(type, url, map, callback, -1);
//    }

    /**
     * @param url          调用接口方法
     * @param paramMap     上传参数（不包括文件）
     * @param filesMap     上传文件
     * @param fileArrayMap 上传文件数组
     * @param callback     服务器端返回参数回调接口
     */
//    public static void uploadWithFile(String url, Map<String, String> paramMap, Map<String, String> filesMap, Map<String, ArrayList<String>> fileArrayMap, final VolleyCallback callback) {
//        final String method = url;
//        RequestQueue queue = MyVolley.getRequestQueue();
//        queue.cancelAll(method);
//        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Constants.SERVER_URL + method, new Listener<String>() {
//            @Override
//            public void onResponse(String arg0) {
//                //Log.i(TAG,"onresponse:"+arg0);
//                if (callback != null) callback.dealWithJson(method, arg0);
//            }
//        }, new ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError arg0) {
//
//                Log.i(TAG, "volleyerror: " + arg0.getMessage());
//                arg0.printStackTrace();
//                if (callback != null)
//                    callback.dealWithError(method, ExceptionUtils.getExceptionMsg(arg0));
//            }
//
//        });
//        if (filesMap != null && filesMap.size() > 0) {
//            for (String fileKey : filesMap.keySet()) {
//                request.addFile(fileKey, filesMap.get(fileKey));
//            }
//        }
//        if (fileArrayMap != null && fileArrayMap.size() > 0) {
//            for (String fileArrayKey : fileArrayMap.keySet()) {
//                request.addFileArray(fileArrayKey, fileArrayMap.get(fileArrayKey));
//            }
//        }
//        request.addMultipartParam(paramMap);
//        request.setTag(method);
//        queue.add(request);
//    }

    /**
     * @param url          调用接口方法
     * @param paramMap     上传参数（不包括文件）
     * @param filesMap     上传文件
     * @param fileArrayMap 上传文件数组
     * @param callback     服务器端返回参数回调接口
     */
    public static void uploadWithFileWholeUrl(String url, Map<String, String> paramMap, Map<String, String> filesMap, Map<String, ArrayList<String>> fileArrayMap, final VolleyCallback callback) {

        String token = SharedPrefrenceUtils.getString(mContext, "token");
        if(!TextUtils.isEmpty(token)){
            paramMap.put("token",token);
        }

        String timetemp = SharedPrefrenceUtils.getString(mContext, "timetemp");
        if(TextUtils.isEmpty(timetemp)){
            timetemp = "0";
        }
        long cuTime = System.currentTimeMillis()+Long.valueOf(timetemp);

        if(url.contains("credit_money_background")){
            paramMap.put("app_id",Constants.app_id);
            paramMap.put("sign", Utils.sign(Constants.app_id, Constants.app_secret, String.valueOf(cuTime)));
        }else {
            paramMap.put("app_id",Constants.fk_app_id);
            paramMap.put("sign", Utils.sign(Constants.fk_app_id, Constants.fk_app_secret, String.valueOf(cuTime)));
        }
        paramMap.put("timetemp",String.valueOf(cuTime));

        DefaultRetryPolicy retry = null;
        //12秒(4的3倍时间)
        retry = new DefaultRetryPolicy(120000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        final String method = url;
        RequestQueue queue = MyVolley.getRequestQueue();
        queue.cancelAll(method);
        SimpleMultiPartRequest request = new SimpleMultiPartRequest(method, new Listener<String>() {
            @Override
            public void onResponse(String arg0) {
                //Log.i(TAG,"onresponse:"+arg0);
                if (callback != null) callback.dealWithJson(method, arg0);
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

                Log.i(TAG, "volleyerror: " + arg0.getMessage());
                arg0.printStackTrace();
                if (callback != null)
                    callback.dealWithError(method, ExceptionUtils.getExceptionMsg(arg0));
            }

        });
        if (filesMap != null && filesMap.size() > 0) {
            for (String fileKey : filesMap.keySet()) {
                request.addFile(fileKey, filesMap.get(fileKey));
            }
        }
        if (fileArrayMap != null && fileArrayMap.size() > 0) {
            for (String fileArrayKey : fileArrayMap.keySet()) {
                request.addFileArray(fileArrayKey, fileArrayMap.get(fileArrayKey));
            }
        }
        if (retry != null) {
            request.setRetryPolicy(retry);
        }
        request.addMultipartParam(paramMap);
        request.setTag(method);
        queue.add(request);
    }

}