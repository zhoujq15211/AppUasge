package com.zhoujq.demo.uasge;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.zhoujq.demo.uasge.entity.DaoMaster;
import com.zhoujq.demo.uasge.entity.DaoSession;
import com.zhoujq.demo.uasge.lifecycle.ActivitysLifecycleListener;
import com.zhoujq.demo.uasge.lifecycle.IActivityLifecycleListener;

public class MyApp extends Application {
    private IActivityLifecycleListener lifecycleListener;

    private static MyApp instances;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        setDatabase();
        lifecycleListener = new ActivitysLifecycleListener();
    }

    public IActivityLifecycleListener getLifecycleListener() {
        return lifecycleListener;
    }


    public static MyApp getInstances() {
        return instances;
    }

    /**
     * 一般在第一个启动的activity中注册
     */
    public void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(lifecycleListener);
    }

    /**
     * 在最后一个activity destroy时候取消注册
     */
    public void unregisterActivityLifecycleCallbacks() {
        unregisterActivityLifecycleCallbacks(lifecycleListener);
        lifecycleListener.clearUasges();
    }

    /**
     *  * 设置greenDao
     *  
     */
    private void setDatabase() {
        mHelper = new DaoMaster.DevOpenHelper(this, "uasge", null);
        db = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
