package com.example.lab3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lihongzheng on 2020/10/26.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //数据库第一次被创建时将调用onCreate
        //通常将创建表的操作放在这里
        db.execSQL("CREATE TABLE IF NOT EXISTS PhoneMsg (\"\n" +
                "+ \"id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\"\n" +
                "+ \"name VARCHAR(50),\"\n" +
                "+ \"start_time VARCHAR(50),\"\n" +
                "+ \"end_time VARCHAR(50),\"\n" +
                "+ \"all_use_time bigint)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS PhoneMsg"); //删除数据表，谨慎使用
        onCreate(db); //重新建表
    }
}
