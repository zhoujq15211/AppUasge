package com.zhoujq.demo.uasge.lifecycle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.zhoujq.demo.uasge.MyApp;
import com.zhoujq.demo.uasge.entity.Uasge;
import com.zhoujq.demo.uasge.entity.UasgeDao;


/**
 * Activity生命周期监听，可以记录整个app使用情况
 * Application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback)
 * Application.unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback)
 */
public class ActivityLifecycleListener implements IActivityLifecycleListener {
    private int refCount = 0;
    private int validTime = 5000;
    private Uasge foregroundTime = new Uasge();
    private final String TAG = "ActivityLifecycle";
    private UasgeDao dao;


    public ActivityLifecycleListener() {
        dao = MyApp.getInstances().getDaoSession().getUasgeDao();
    }

    @Override
    public void updateData() {
        Log.i(TAG, "需要上传的app使用情况数据：" + dao.loadAll().toString());
        dao.deleteAll();
    }

    @Override
    public void clearUasges() {

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
        if (refCount == 1) {
            //app回到前台
            if (System.currentTimeMillis() - foregroundTime.getBackgroundTime() > validTime) {
                //app切换后台时间大于validTime时候判断为切换到后台,将id重置
                foregroundTime.setId(null);
                foregroundTime.setBackgroundTime(0);
                foregroundTime.setForegroundTime(System.currentTimeMillis());
            }
        }
        Log.i(TAG, refCount + "==onActivityStarted:" + activity.getClass().getSimpleName());
    }


    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        refCount--;
        Log.i(TAG, refCount + "==onActivityStopped:" + activity.getClass().getSimpleName());
        if (isBackground() && foregroundTime != null) {
            foregroundTime.setBackgroundTime(System.currentTimeMillis());
            //保存到数据库
            dao.insertOrReplace(foregroundTime);
            Log.i(TAG, dao.loadAll().size() + "==保存上次在前台时间:" + foregroundTime.getDate() + "==" + foregroundTime.getUseTime());
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

}
