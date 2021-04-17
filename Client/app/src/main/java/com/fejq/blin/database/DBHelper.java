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


    //带有全部参数的构造函数，此构造函数是必须需要的。
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
        String sql="CREATE TABLE user (" +
                "    Id       INTEGER       PRIMARY KEY AUTOINCREMENT," +
                "    userId   INTEGER," +
                "    userName VARCHAR (50)," +
                "    password VARCHAR (50)," +
                "    token    VARCHAR (255)," +
                "    mainUser BOOLEAN" +
                ");";
        db.execSQL(sql);

        sql="CREATE TABLE chatList (" +
                "    Id              INTEGER       PRIMARY KEY AUTOINCREMENT," +
                "    userId          INTEGER," +
                "    chatImg         VARCHAR (255)," +
                "    chatId          INTEGER," +
                "    chatTypeId      INTEGER," +
                "    chatName        VARCHAR (255)," +
                "    lastMessage     VARCHAR (255)," +
                "    lastMessageTime DATETIME," +
                "    nonReadCount    INTEGER" +
                ");";
        db.execSQL(sql);

        sql="CREATE TABLE chatType (" +
                "    Id       INTEGER PRIMARY KEY," +
                "    typeName VARCHAR" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

}
