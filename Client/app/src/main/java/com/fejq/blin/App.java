package com.fejq.blin;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fejq.blin.database.DBHelper;

public class App extends Application
{
    private static Context context;


    @Override
    public void onCreate()
    {
        super.onCreate();
        if(context==null)
        {
            context = getApplicationContext();
        }
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        context=null;
    }

    public static Context getContext()
    {
        return context;
    }
}
