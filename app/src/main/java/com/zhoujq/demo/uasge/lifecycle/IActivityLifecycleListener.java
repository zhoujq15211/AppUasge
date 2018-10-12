package com.zhoujq.demo.uasge.lifecycle;

import android.app.Application;

public interface IActivityLifecycleListener extends Application.ActivityLifecycleCallbacks {
    void updateData();

    void clearUasges();

    void setValidTime(int validTime);

    boolean isBackground();
}
