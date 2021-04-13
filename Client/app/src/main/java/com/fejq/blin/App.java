package com.fejq.blin;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

public class App extends Application
{
    private static Context context;


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
