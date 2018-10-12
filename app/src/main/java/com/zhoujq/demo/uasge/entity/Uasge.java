package com.zhoujq.demo.uasge.entity;

import android.annotation.SuppressLint;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.text.Format;
import java.text.SimpleDateFormat;

@Entity
public class Uasge {
    @Id(autoincrement = true)
    private Long id;
    private long foregroundTime;
    private long backgroundTime;
    private String className;
    @SuppressLint("SimpleDateFormat")
    @Transient
    private Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Generated(hash = 858880982)
    public Uasge(Long id, long foregroundTime, long backgroundTime,
            String className) {
        this.id = id;
        this.foregroundTime = foregroundTime;
        this.backgroundTime = backgroundTime;
        this.className = className;
    }

    @Generated(hash = 1109213620)
    public Uasge() {
    }


    public long getUseTime() {
        return (backgroundTime - foregroundTime) / 1000;
    }

    public long getForegroundTime() {
        return foregroundTime;
    }

    public void setForegroundTime(long foregroundTime) {
        this.foregroundTime = foregroundTime;
    }

    public long getBackgroundTime() {
        return backgroundTime;
    }

    public void setBackgroundTime(long backgroundTime) {
        this.backgroundTime = backgroundTime;
    }

    public String getDate() {
        return format.format(foregroundTime);
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ForegroundTime{" +
                "foregroundTime=" + foregroundTime +
                ", backgroundTime=" + backgroundTime +
                ", className='" + className + '\'' +
                '}';
    }
}
