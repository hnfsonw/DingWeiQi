package com.hojy.bracelet.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;


import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by LoyBin on 18/2/27.
 */

public class MyApplication extends Application{

    public static Context sInstance;
    private static Handler mHandler;
    public static String sToken;
    public static long sAcountId;
    public static int sDeviceId;
    public static int sDeivceNumber = 0;
    public static String sAcountName;


    //定义缓存的存储结构
    private static Map<String, Object> mCacheMap = new HashMap<>();

    public static Map<String, Object> getCacheMap() {
        return mCacheMap;
    }

    /**
     * 得到上下文
     *
     * @return
     */
    public static Context getContext() {
        return sInstance;
    }

    /**
     * 得到主线程的handler
     *
     * @return
     */
    public static Handler getHandler() {
        return mHandler;
    }


    @Override
    public void onCreate() {//程序的入口方法
        super.onCreate();
        JPushInterface.init(this);
        JPushInterface.setDebugMode(false);
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
//        CrashReport.initCrashReport(getApplicationContext(), "c5ef65d3af", true);
//        strategy.setAppChannel("text");  //设置渠道
//        strategy.setAppVersion("v1.0.0");      //App的版本
//        strategy.setAppPackageName("com.hojy.bracelet");  //App的包名
        //上下文
        sInstance = getApplicationContext();

        //创建一个主线程的handler
        mHandler = new Handler();

    }
}
