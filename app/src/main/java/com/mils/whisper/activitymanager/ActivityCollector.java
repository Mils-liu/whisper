package com.mils.whisper.activitymanager;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */

public class ActivityCollector {

    private static String tag="ActivityTAG";

    public static int ACTIVITY_REGISTER = 1;

    public static List<Activity> activities=new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
        Log.d(tag,activity.toString());
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for(Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }

    /**
     * 从Activity集合查询, 传入的Activity是否存在
     * 如果存在就返回该Activity,不存在就返回null
     * @param activity 需要查询的Activity, 比如MainActivity.class
     * @return
     */
    public static Activity getActivity(Class<?> activity) {
        for (int i = 0; i < activities.size(); i++) {
            // 判断是否是自身或者子类
            if (activities.get(i).getClass().isAssignableFrom(activity)) {
                return activities.get(i);
            }
        }
        return null;
    }

    public static void getActivityList(){
        for(int i=0; i < activities.size(); i++){
            Log.d(tag,activities.get(i).toString());
            if (i==activities.size());
            Log.d(tag,"---------------------------------------------");
        }
    }

}
