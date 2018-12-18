package com.mils.whisper.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.mils.whisper.bean.User;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2018/5/31.
 */

public class MyApplicant extends Application {

    private static Context context;
    public static User user;
    public static Boolean OUT_BY_HOME = true;
    private String TAG="myApplicant";
    private int mActivityCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initBomb();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d(TAG,"onActivityCreated");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d(TAG,"onActivityStarted");
                mActivityCount++;
                if (mActivityCount > 0){
                    OUT_BY_HOME = true;
                    Log.d(TAG,"OUT_BY_HOME:"+OUT_BY_HOME);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.d(TAG,"onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.d(TAG,"onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.d(TAG,"onActivityStopped");
                mActivityCount--;
                if (mActivityCount == 0){
                    OUT_BY_HOME = false;
                    Log.d(TAG,"OUT_BY_HOME:"+OUT_BY_HOME);
                }else{
                    OUT_BY_HOME = true;
                    Log.d(TAG,"OUT_BY_HOME:"+OUT_BY_HOME);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.d(TAG,"onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d(TAG,"onActivityDestroyed");
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.d(TAG, "onTerminate");
        super.onTerminate();
    }
    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        Log.d(TAG, "onLowMemory");
        super.onLowMemory();
    }
    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        Log.d(TAG, "onTrimMemory");
        super.onTrimMemory(level);
        OUT_BY_HOME = false;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    /*初始化Bmob*/
    public void initBomb() {
        //默认设置
        Log.d(TAG,"initBomb");
        Bmob.initialize(this, "9284ad889d2a6385632e43c0f92d7102");

        //自定义设置
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
    }

    public static Context getContext() {
        return context;
    }
}
