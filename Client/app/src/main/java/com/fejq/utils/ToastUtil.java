package com.fejq.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtil
{
    public static void showShortToast(Context context, String message) {
        Looper.prepare();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public static void showLongToast(Context context, String message) {
        Looper.prepare();
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        Looper.loop();
    }

    public static void showToastOnMainUi(final Activity context, final String message) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
