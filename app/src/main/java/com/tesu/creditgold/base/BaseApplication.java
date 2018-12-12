package com.tesu.creditgold.base;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.tesu.creditgold.util.MyExceptionHandler;

import cn.jpush.android.api.JPushInterface;
import cn.tongdun.android.shell.FMAgent;


/**
 * @作者: 周岐同
 * @创建时间: 2015年3月31日下午4:16:08
 * @版权: 微位科技版权所有 
 * @描述: 记录全局变量	
 */
public class BaseApplication extends Application {
	/** 全局Context，原理是因为Application类是应用最先运行的，所以在我们的代码调用时，该值已经被赋值过了 */
	private static BaseApplication mInstance;
	/** 主线程ID */
	private static int mMainThreadId = -1;
	/** 主线程 */
	private static Thread mMainThread;
	/** 主线程Handler */
	private static Handler mMainThreadHandler;
	/** 主线程Looper */
	private static Looper mMainLooper;




	@Override
	public void onCreate() {
//		SDKInitializer.initialize(this);
		//android.os.Process.myTid()  获取调用进程的id
		//android.os.Process.myUid() 获取 该进程的用户id
		//android.os.Process.myPid() 获取进程的id
		mMainThreadId = android.os.Process.myTid();
		mMainThread = Thread.currentThread();
		mMainThreadHandler = new Handler();
		mMainLooper = getMainLooper();
		mInstance = this;

		/*初始化volley*/
		MyVolley.init(this);
		super.onCreate();

//		//极光推送初始化
		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
		JPushInterface.init(this);     		// 初始化 JPush

//		SQLiteDatabase db = Connector.getDatabase();

		try {
//			FMAgent.openLog();
//			FMAgent.init(this, FMAgent.ENV_SANDBOX);    //测试服
			FMAgent.init(this, FMAgent.ENV_PRODUCTION);  //正式服
		}catch (Exception e){
			e.printStackTrace();
		}

		MyExceptionHandler.create(this);

	}


	public static BaseApplication getApplication() {
		return mInstance;
	}

	/** 获取主线程ID */
	public static int getMainThreadId() {
		return mMainThreadId;
	}

	/** 获取主线程 */
	public static Thread getMainThread() {
		return mMainThread;
	}

	/** 获取主线程的handler */
	public static Handler getMainThreadHandler() {
		return mMainThreadHandler;
	}

	/** 获取主线程的looper */
	public static Looper getMainThreadLooper() {
		return mMainLooper;
	}
}
