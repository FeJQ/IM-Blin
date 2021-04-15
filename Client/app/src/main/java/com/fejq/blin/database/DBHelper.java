package com.fejq.blin.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fejq.blin.App;

public class DBHelper extends SQLiteOpenHelper
{
    private static DBHelper dbUtil;

    public static DBHelper getInstance()
    {
        if(dbUtil==null)
        {
            dbUtil = new DBHelper(App.getContext(), "blin.db", null, 1);
        }
        return dbUtil;
    }


    //带有全部参数的构造函数，此构造函数是必须需要的。Eclipse和Android Studio均有自动填充功能
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    /**
     * 数据库第一次创建时被调用
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table user(Id integer primary key autoincrement,userId integer,userName varchar(50),password varchar(50),token varchar(255),mainUser boolean)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

}
