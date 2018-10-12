package com.zhoujq.demo.uasge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zhoujq.demo.uasge.test.Main2Activity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //将保存的app使用数据上传，如果有需要
        MyApp.getInstances().getLifecycleListener().updateData();
        MyApp.getInstances().registerActivityLifecycleCallbacks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApp.getInstances().unregisterActivityLifecycleCallbacks();
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, Main2Activity.class));
    }
}
