package com.example.lab_test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends Activity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)//安卓5.0以上的功能

    private Button bt_add;
    private EditText et_item;
    @SuppressLint("ResourceType")
    private ApplicationAdapter mAdapter;
    private List<ApplicationInfoo> apps=new ArrayList<>();
    private List<ShowMsg> showMsgs=new ArrayList<>();
   private List<PhoneMsg> phoneMsgslist=new ArrayList<>();
   private ListView ls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_add = (Button)findViewById(R.id.bt_add);
        et_item = (EditText)findViewById(R.id.et_item);
        ls=findViewById(R.id.lv1);

        /*
        **将存入数据库中的值取出来并到前端进行显示
         */
        BmobQuery<PhoneMsg> query=new BmobQuery<>();
        //query.order("-all_use_time");
        query.findObjects(new FindListener<PhoneMsg>() {
            @Override
            public void done(List<PhoneMsg> list, BmobException e) {
                if(e!=null){
                    Toast.makeText(getApplicationContext(),"查询失败",Toast.LENGTH_SHORT).show();
                }

              int len=list.size();
              Log.e("==================ccc", "长度1:"+len);
              for(int i=0;i<len;i++){
                  phoneMsgslist.add(list.get(i));//将数据库中的内容取到本地来，并将他们存储起来
              }

                apps = loadAppInfomation(getApplicationContext());//取出来了apps和phonemsgs之后，来进行处理，获得即将进行展示的数据集，即showMsgs.
        /*
        **进行两个类的综合，即来进行数据处理，最后进行展示
        * *两层循环进行展示，相当于两张表进行综合
         */
                //int len=phoneMsgslist.size();
                int len2=apps.size();
                Log.e("==================aaa", "长度1:"+len+"长度2"+len2);
                Log.e("==================bbb", "长度:"+len2);
                for(int m=0;m<len;m++)
                    for(int k=0;k<len2;k++){
                        if(phoneMsgslist.get(m).getName().equals(apps.get(k).getName())){
                            ShowMsg show=new ShowMsg(apps.get(k).getName(),
                                    phoneMsgslist.get(m).getStart_time(),
                                    phoneMsgslist.get(m).getEnd_time(),
                                    phoneMsgslist.get(m).getAll_use_time(),
                                    apps.get(k).getIcon());//进行展示对象的初始化
                            showMsgs.add(show);
                        }
                    }

                mAdapter = new ApplicationAdapter(getApplicationContext(), showMsgs);
                ls.setAdapter(mAdapter);
                ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        setTitle("你点击第"+position+"行");
                        //可添加其他事件
                    }
                });
            }
        });
    }
    //填充数据
    private List<ApplicationInfoo> loadAppInfomation(Context context) {
        List<ApplicationInfoo> apps = new ArrayList<ApplicationInfoo>();
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        Collections.sort(infos, new ResolveInfo.DisplayNameComparator(pm));
        if(infos != null) {
            apps.clear();
            for(int i=0; i<infos.size(); i++) {
                ApplicationInfoo app = new ApplicationInfoo();
                ResolveInfo info = infos.get(i);
                app.setName(info.loadLabel(pm).toString());
                app.setIcon(info.loadIcon(pm));
                app.setIntent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
                apps.add(app);
            }
        }
        return apps;
    }

//    private List<PhoneMsg> LoadMsg(){
//        List<PhoneMsg> list2=new ArrayList<>();
//        BmobQuery<PhoneMsg> query=new BmobQuery<PhoneMsg>();
//        query.findObjects(new FindListener<PhoneMsg>() {
//            @Override
//            public void done(List<PhoneMsg> list, BmobException e) {
//
//                if(e==null){
//                int len=list.size();
//                Log.e("==================ddd", "长度1:"+len);
//                for(int i=0;i<len;i++){
//                    list2.add(list.get(i));//将数据库中的内容取到本地来，并将他们存储起来
//
//                }
//            }
//            else {
//                    Log.e("==================ddd", ""+e);
//                }
//            }
//        });
//        return list2;
//    }



//    public void initDatabase(){
//
//
//        PackageManager pm=getPackageManager();
//        List<PackageInfo> list2=pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
//        int j=0;
//        List<String> ls1=new ArrayList<>();
////        for (PackageInfo packageInfo : list2) {
////            //得到手机上已经安装的应用的名字,即在AndriodMainfest.xml中的app_name。
////            String appName=packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
////            //得到手机上已经安装的应用的图标,即在AndriodMainfest.xml中的icon,即获取安装的应用的图标。
////            Drawable drawable = packageInfo.applicationInfo.loadIcon(getPackageManager());
////            //得到应用所在包的名字,即在AndriodMainfest.xml中的package的值。
////            String packageName=packageInfo.packageName;
////            //获取手机的应用的初次安装时间
////            String firsttime=packageInfo.firstInstallTime+"";
////            //判断是否是用户的应用程序或者系统的应用程序：
//////            if(!isSystemApp(packageInfo) && !isSystemUpdateApp(packageInfo)){
//////
//////                appInfos.add(appInfo);
//////
//////                Log.i("pxj", "yuanshi packageName is:::"+packageInfoString);
//////
//////            }
//////
//////            public boolean isSystemApp(PackageInfo pInfo) {
//////
//////                return ((pInfo.applicationInfo.flags & ApplicationInfoo.FLAG_SYSTEM) != 0);
//////
//////            }
//////
//////
//////
//////            public boolean isSystemUpdateApp(PackageInfo pInfo) {
//////
//////                return ((pInfo.applicationInfo.flags & ApplicationInfoo.FLAG_UPDATED_SYSTEM_APP) != 0);
//////
//////            }
////            Log.e("=======aaa", "应用的名字:"+appName);
////            Log.e("=======bbbb", "应用的包名字:"+packageName);
////             //ls1.add("应用名称："+appName+"==第一次安装时间"+firsttime);
////            j++;
////
////        }
//
//    /*
//    *使用不同的而组件来判断运行的时间
//     */
//        Calendar beginCal = Calendar.getInstance();
//        beginCal.add(Calendar.DAY_OF_MONTH, -5);//相当于把天数减去5，即为得到的天数
//        Calendar endCal = Calendar.getInstance();
//        UsageStatsManager manager=(UsageStatsManager)getApplicationContext().getSystemService(USAGE_STATS_SERVICE);
//        /**
//         * 最近两周启动过所用app的List
//         * queryUsageStats第一个参数是根据后面的参数获取合适数据的来源，有按天，按星期，按月，按年等。
//         *  UsageStatsManager.INTERVAL_BEST 最好的匹配，不推荐使用
//         *   UsageStatsManager.INTERVAL_DAILY 按天
//         *   UsageStatsManager.INTERVAL_WEEKLY 按星期
//         *   UsageStatsManager.INTERVAL_MONTHLY 按月
//         *   UsageStatsManager.INTERVAL_YEARLY 按年
//         */
//        //即可按照天数来显示
//        List<UsageStats> stats=manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,beginCal.getTimeInMillis(),endCal.getTimeInMillis());
//        final int i=0;
//        for(UsageStats us:stats){
//            try {
//                StringBuilder sb=new StringBuilder();
//                pm=getApplicationContext().getPackageManager();
//                final ApplicationInfo applicationInfo=pm.getApplicationInfo(us.getPackageName(),PackageManager.GET_META_DATA);
//                if((applicationInfo.flags&applicationInfo.FLAG_SYSTEM)<=0){
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String t1= null;
//                    t1 = format.format(new Date(us.getLastTimeUsed()));//最后使用的时间
//                    //t1 = format.format(new Date(us.getLastTimeStamp()));//获取时间戳
//                    String t2=null;
//                    t2=format.format(new Date(us.getFirstTimeStamp()));//第一次使用的时间
//                    // sb.append(pm.getApplicationLabel(applicationInfo)+"--第一次使用\t"+t2+"--最后一次使用\t"+t1+"--总时间\t"+us.getTotalTimeInForeground()+"\n");
//                    sb.append(pm.getApplicationLabel(applicationInfo)+"--\t"+t2+"--\t"+t1+"--\t"+us.getTotalTimeInForeground()+"\n");
//                    ls1.add(sb.toString());
//
//
//
//                    //将其封装进入自定义的类，然后装入数据库之中
//                    PhoneMsg psg=new PhoneMsg(new String((String) pm.getApplicationLabel(applicationInfo)),t2,t1,us.getTotalTimeInForeground());
//                    //insert(psg);
//                    //直接装入Bmob数据库即可
//                    final PackageManager finalPm = pm;
//                    psg.save(new SaveListener<String>() {
//                        @Override
//                        public void done(String s, BmobException e) {
//                            if(e==null){
//                                Toast.makeText(getApplicationContext(),"添加成功"+new String((String) finalPm.getApplicationLabel(applicationInfo)),Toast.LENGTH_LONG).show();
//                            }else{
//                                Toast.makeText(getApplicationContext(),"添加失败"+new String((String) finalPm.getApplicationLabel(applicationInfo)),Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
////        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
////                MainActivity.this,
////                android.R.layout.simple_list_item_single_choice,
////                ls1 );//
////        li.setAdapter(adapter);
////        li.setChoiceMode(ListView.CHOICE_MODE_SINGLE); //单选
//        //
//        // sqlite数据库遗留操作。
//        // db.close(); //操作完成后关闭数据库连接
//    }
























    //将drawable文件转化为bitmap对象
//    public static Bitmap drawableToBitamp(Drawable drawable)
//    {
//        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),
//                drawable.getOpacity()!= PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
//        drawable.draw(canvas);
//        return bitmap;
//    }

    //初始化
//    private void initListView()   {
//        listItems = new ArrayList<HashMap<String, Object>>();
//        PackageManager pm=getPackageManager();
//        List<PackageInfo> list2=pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
//        int j=0;
//        List<String> ls1=new ArrayList<>();
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
////                return ((pInfo.applicationInfo.flags & ApplicationInfoo.FLAG_SYSTEM) != 0);
////
////            }
////
////
////
////            public boolean isSystemUpdateApp(PackageInfo pInfo) {
////
////                return ((pInfo.applicationInfo.flags & ApplicationInfoo.FLAG_UPDATED_SYSTEM_APP) != 0);
////
////            }
//            //imageView.setImageDrawable(drawable);
//            Bitmap bt=drawableToBitamp(drawable);
//            Log.e("=======aaa", "应用的名字:"+appName);
//            Log.e("=======bbbb", "应用的包名字:"+packageName);
//            Log.e("=======ccc", "应用的图标:"+bt);
//
//            //ls1.add("应用名称："+appName+"==第一次安装时间"+firsttime);
//            j++;
//            //imageView.setImageBitmap(bt);
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("ItemTitle", "名字 "+j+appName);    //文字
//            map.put("ItemImage",bt);   //图片
//            listItems.add(map);
//        }
//
//
//
//        for(int i=0;i<10;i++)    {
//
//        }
//        //生成适配器的Item和动态数组对应的元素
//        listItemAdapter = new SimpleAdapter(this,listItems,   // listItems数据源
//                R.layout.item,  //ListItem的XML布局实现
//                new String[] {"ItemTitle", "ItemImage"},     //动态数组与ImageItem对应的子项
//                new int[ ] {R.id.tv11, R.id.imgg}      //list_item.xml布局文件里面的一个ImageView的ID,一个TextView 的ID
//        );
//    }

//    class ClickEvent implements View.OnClickListener {
//        @Override
//        public void onClick (View v)  {
//            // 向ListView里添加一项
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("ItemTitle", "Music： "+ et_item.getText().toString());
//            map.put("ItemImage", R.drawable.music);     //每次都放入同样的图片资源ID
//            listItems.add(map);
//            //重新设置适配器
//            MainActivity.this.setListAdapter(listItemAdapter);
//        }
//    }
}
