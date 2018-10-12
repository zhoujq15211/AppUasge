package com.zhoujq.demo.uasge.lifecycle;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.zhoujq.demo.uasge.MyApp;
import com.zhoujq.demo.uasge.entity.Uasge;
import com.zhoujq.demo.uasge.entity.UasgeDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Activity生命周期监听,可以记录activity使用情况
 * Application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback)
 * Application.unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback)
 */
public class ActivitysLifecycleListener implements IActivityLifecycleListener {
    private int refCount = 0;
    private int validTime = 5000;
    private final String TAG = "ActivityLifecycle";
    private UasgeDao dao;
    private Map<String, Uasge> uasgeMap = new HashMap<>(0);

    public ActivitysLifecycleListener() {
        dao = MyApp.getInstances().getDaoSession().getUasgeDao();
    }

    @Override
    public void updateData() {
        List<Uasge> list = dao.queryBuilder().list();
        Log.i(TAG, "需要上传的app使用情况数据");
        for (Uasge uasge : list) {
            Log.i(TAG, "保存上次在前台时间:" + uasge.getDate() + "==" + uasge.getUseTime() + "==" + uasge.getClassName());
        }
        dao.deleteAll();
        uasgeMap.clear();
    }

    @Override
    public void clearUasges() {
        uasgeMap.clear();
    }

    @Override
    public void setValidTime(int validTime) {
        this.validTime = validTime * 1000;
    }

    @Override
    public boolean isBackground() {
        return refCount < 1;
    }
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        refCount++;
        //app回到前台
        Uasge uasge = uasgeMap.get(activity.getClass().getSimpleName());
        if (uasge == null) {
            //activity不存在时候,即此activity第一次打开
            uasge = new Uasge();
            uasge.setClassName(activity.getClass().getSimpleName());
            uasge.setForegroundTime(System.currentTimeMillis());
            uasgeMap.put(activity.getClass().getSimpleName(), uasge);
        } else {
            if (System.currentTimeMillis() - uasge.getBackgroundTime() > validTime) {
                //app切换后台时间大于validTime时候判断为切换到后台,将id重置
                uasge.setId(null);
                uasge.setBackgroundTime(0);
                uasge.setForegroundTime(System.currentTimeMillis());
            }
        }
        Log.i(TAG, "onActivityStarted:" + activity.getClass().getSimpleName());
    }


    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        //activity不可见,需要将数据保存到数据库
        refCount--;
        Log.i(TAG, "onActivityStopped:" + activity.getClass().getSimpleName());
        Uasge uasge = uasgeMap.get(activity.getClass().getSimpleName());
        if (uasge != null) {
            uasge.setBackgroundTime(System.currentTimeMillis());
            //保存到数据库
            dao.insertOrReplace(uasge);
            Log.i(TAG, dao.loadAll().size() + "==保存上次在前台时间:" + uasge.getDate() + "==" + uasge.getUseTime() + "==" + uasge.getClassName());
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
