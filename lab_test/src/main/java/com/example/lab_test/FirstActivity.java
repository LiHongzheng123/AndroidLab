package com.example.lab_test;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
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

public class FirstActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        /*
        **注意数据库的相关操作必须放在主线程里面，即放在oncreate里面，如果单独定义其他的函数可能会造成线程错误。
         */
        //第一：默认初始化
        Bmob.initialize(this, "7d20eb998da39af4af6b4f0247e19d4f");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));//设置权限

        //插入数据库数据，此处直接插入云数据库
        PackageManager pm=getPackageManager();
       // List<PackageInfo> list2=pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
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
                    PhoneMsg psg=new PhoneMsg(new String((String) pm.getApplicationLabel(applicationInfo)),t2,t1,us.getTotalTimeInForeground());
                    //直接装入Bmob数据库即可
                    final PackageManager finalPm = pm;
                    psg.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                Toast.makeText(getApplicationContext(),"添加成功"+new String((String) finalPm.getApplicationLabel(applicationInfo)),Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"添加失败"+new String((String) finalPm.getApplicationLabel(applicationInfo)),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //广告页跳转到其他页面
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
}
