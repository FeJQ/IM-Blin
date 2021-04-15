package com.fejq.blin;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.fejq.blin.database.DBHelper;

public class App extends Application
{
    private static Context context;
    public static SQLiteDatabase database = DBHelper.getInstance().getWritableDatabase();

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this;
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
    }

    public static Context getContext()
    {
        return context;
    }
}
