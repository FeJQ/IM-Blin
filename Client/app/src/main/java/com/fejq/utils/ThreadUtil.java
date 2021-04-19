package com.fejq.utils;

import android.os.Looper;

public class ThreadUtil
{
    public boolean isMainThread()
    {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }
}
