package com.example.lab3;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends Activity {

    private static int DB_VERSION=1;//定义数据可以的版本号

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)//安卓5.0以上的功能
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*
        *
        * *使用sqlite较为麻烦，暂定使用Bmob数据库
        * *
        * *
        * *
         */

//        //创建辅助类对象
//        DBHelper helper = new DBHelper(getApplicationContext(), "test.db", null,DB_VERSION);
//        //调用getWritableDatabase()或getReadableDatabase()才会真正创建或打开
//        SQLiteDatabase db=helper.getWritableDatabase();




        //第一：默认初始化
        Bmob.initialize(this, "7d20eb998da39af4af6b4f0247e19d4f");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置数据库显示跳转页面
        TextView tv1=findViewById(R.id.tv1);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ShowActivity.class));
            }
        });




        PackageManager pm=getPackageManager();
        List<PackageInfo> list2=pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        int j=0;
        ListView li=findViewById(R.id.listview);
        List<String> ls1=new ArrayList<>();
//        for (PackageInfo packageInfo : list2) {
//            //得到手机上已经安装的应用的名字,即在AndriodMainfest.xml中的app_name。
//            String appName=packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
//            //得到手机上已经安装的应用的图标,即在AndriodMainfest.xml中的icon,即获取安装的应用的图标。
//            Drawable drawable = packageInfo.applicationInfo.loadIcon(getPackageManager());
//            //得到应用所在包的名字,即在AndriodMainfest.xml中的package的值。
//            String packageName=packageInfo.packageName;
//            //获取手机的应用的初次安装时间
//            String firsttime=packageInfo.firstInstallTime+"";
//            //判断是否是用户的应用程序或者系统的应用程序：
////            if(!isSystemApp(packageInfo) && !isSystemUpdateApp(packageInfo)){
////
////                appInfos.add(appInfo);
////
////                Log.i("pxj", "yuanshi packageName is:::"+packageInfoString);
////
////            }
////
////            public boolean isSystemApp(PackageInfo pInfo) {
////
////                return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
////
////            }
////
////
////
////            public boolean isSystemUpdateApp(PackageInfo pInfo) {
////
////                return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
////
////            }
//            Log.e("=======aaa", "应用的名字:"+appName);
//            Log.e("=======bbbb", "应用的包名字:"+packageName);
//             //ls1.add("应用名称："+appName+"==第一次安装时间"+firsttime);
//            j++;
//
//        }

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
                   // sb.append(pm.getApplicationLabel(applicationInfo)+"--第一次使用\t"+t2+"--最后一次使用\t"+t1+"--总时间\t"+us.getTotalTimeInForeground()+"\n");
                    sb.append(pm.getApplicationLabel(applicationInfo)+"--\t"+t2+"--\t"+t1+"--\t"+us.getTotalTimeInForeground()+"\n");
                    ls1.add(sb.toString());



                    //将其封装进入自定义的类，然后装入数据库之中
                    PhoneMsg psg=new PhoneMsg(new String((String) pm.getApplicationLabel(applicationInfo)),t2,t1,us.getTotalTimeInForeground());
                    //insert(psg);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_single_choice,
                ls1 );//
        li.setAdapter(adapter);
        li.setChoiceMode(ListView.CHOICE_MODE_SINGLE); //单选
        //
        // sqlite数据库遗留操作。
        // db.close(); //操作完成后关闭数据库连接
    }

    //封装数据库的插入方法，sqlite遗留方法
    public void insert(PhoneMsg psg){
        DBHelper helper = new DBHelper(getApplicationContext(), "test.db", null,DB_VERSION);
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("INSERT INTO person VALUES (NULL,? , ?, ?, ?)",
                new Object[ ] { psg.getName(), psg.getStart_time(),psg.getEnd_time(),psg.getAll_use_time()} );
        db.close();
        Toast.makeText(getApplicationContext(), " 记录添加成功 ", Toast.LENGTH_SHORT).show();
    }
}
