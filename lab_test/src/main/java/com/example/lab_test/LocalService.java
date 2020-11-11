package com.example.lab_test;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by lihongzheng on 2020/11/10.
 */

public class LocalService extends Service {
    @Nullable

    private static final String TAG = "LocalService";
    private final IBinder mBinder = new LocalBinder();//相当于注册来进行数据的绑定
    private final Random mGenerator = new Random();

    /**
     * 供客户端使用的类，因为我们知道服务通常是和客户端运行在一个线程， * 所以我们不需要处理IPC，这里这个Binder类很像MVP里的中介者或者是Service的代理
     */
    public class LocalBinder extends Binder {
        LocalService getService() {
            //执行相关的操作的代码，此处即为存储数据的代码



            //插入数据库数据，此处直接插入云数据库
            PackageManager pm=getPackageManager();
            int j=0;
            List<String> ls1=new ArrayList<>();

            /*
            *使用不同的而组件来判断运行的时间
            */
            Calendar beginCal = Calendar.getInstance();
            beginCal.add(Calendar.DAY_OF_MONTH, -5);//相当于把天数减去5，即为得到的天数
            Calendar endCal = Calendar.getInstance();
            UsageStatsManager manager=(UsageStatsManager)getApplicationContext().getSystemService(USAGE_STATS_SERVICE);
            /**
             * 最近两周启动过所用app的List
             * queryUsageStats第一个参数是根据后面的参数获取合适数据的来源，有按天，按星期，按月，按年等。
             *  UsageStatsManager.INTERVAL_BEST 最好的匹配，不推荐使用
             *   UsageStatsManager.INTERVAL_DAILY 按天
             *   UsageStatsManager.INTERVAL_WEEKLY 按星期
             *   UsageStatsManager.INTERVAL_MONTHLY 按月
             *   UsageStatsManager.INTERVAL_YEARLY 按年
             */
            //即可按照天数来显示
            List<UsageStats> stats=manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,beginCal.getTimeInMillis(),endCal.getTimeInMillis());
            final int i=0;
            for(UsageStats us:stats){
                try {
                    StringBuilder sb=new StringBuilder();
                    pm=getApplicationContext().getPackageManager();
                    final ApplicationInfo applicationInfo=pm.getApplicationInfo(us.getPackageName(),PackageManager.GET_META_DATA);
                    if((applicationInfo.flags&applicationInfo.FLAG_SYSTEM)<=0){
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String t1= null;
                        t1 = format.format(new Date(us.getLastTimeUsed()));//最后使用的时间
                        //t1 = format.format(new Date(us.getLastTimeStamp()));//获取时间戳
                        String t2=null;
                        t2=format.format(new Date(us.getFirstTimeStamp()));//第一次使用的时间
                        //sb.append(pm.getApplicationLabel(applicationInfo)+"--\t"+t2+"--\t"+t1+"--\t"+us.getTotalTimeInForeground()+"\n");
                        ls1.add(sb.toString());
                        //将其封装进入自定义的类，然后装入数据库之中
                        PhoneMsg psg=new PhoneMsg(new String((String) pm.getApplicationLabel(applicationInfo)),t2,t1,us.getTotalTimeInForeground()/1000);
                        //直接装入Bmob数据库即可
                        final PackageManager finalPm = pm;
                        psg.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e==null){
                                    Toast.makeText(getApplicationContext(),"添加成功"+new String((String) finalPm.getApplicationLabel(applicationInfo)),Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(),"添加失败"+new String((String) finalPm.getApplicationLabel(applicationInfo)),Toast.LENGTH_LONG).show();
                                    Log.d("原因原因", String.valueOf(e));
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return LocalService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate: !!!!!!!!!!");
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: !!!!!!!");
        return mBinder;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: !!!!!!!!!!");
    }
}
