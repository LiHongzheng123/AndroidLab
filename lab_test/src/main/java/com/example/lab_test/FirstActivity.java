package com.example.lab_test;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static android.content.ContentValues.TAG;

public class FirstActivity extends Activity {

    private boolean mBound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        /*
        **注意数据库的相关操作必须放在主线程里面，即放在oncreate里面，如果单独定义其他的函数可能会造成线程错误。
         */
        //第一：默认初始化
        Bmob.initialize(this, "7d20eb998da39af4af6b4f0247e19d4f");

        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));//设置权限

        //通过Binder类来绑定服务
        Intent intent = new Intent(FirstActivity.this, LocalService.class);
        // 绑定服务
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        if (mBound){
            unbindService(mConnection);
            mBound=false;
        }
        //广告页跳转
        final Intent localIntent = new Intent(this, MainActivity.class);
        Timer timer = new Timer();
        TimerTask tast = new TimerTask() {
            @Override
            public void run() {
                startActivity(localIntent);
                finish();
            }
        };
        timer.schedule(tast, 5000);
    }
    protected void onDestroy() {
        // TODO 自动生成的方法存根
        super.onDestroy();
        unbindService(mConnection);
    }
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
             mBound = true;
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service; // 取得对service实例的引用
            LocalService localService=binder.getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };
}


