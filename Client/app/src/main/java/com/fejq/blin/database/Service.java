package com.fejq.blin.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.fejq.blin.App;
import com.fejq.blin.model.entity.User;

public class Service
{
    public static void saveUserInfo(User user)
    {
        App.database.execSQL("update user set mainUser=false");
        // 添加用户id,token到数据库
        ContentValues values = new ContentValues();
        values.put("userId", user.getUserId());
        values.put("token", user.getToken());
        values.put("mainUser", true);
        App.database.insert("user", null, values);
    }

    public static User getMainUser()
    {
        Cursor cursor = App.database.query("user", null, "mainUser=?", new String[]{"true"}, null, null, null, null);
        if (cursor.getCount() == 0)
            return null;
        int userId = cursor.getInt(cursor.getColumnIndex("userId"));
        String userName = cursor.getString(cursor.getColumnIndex("userName"));
        String token = cursor.getString(cursor.getColumnIndex("token"));
        User user = new User(userId, userName, token);
        return user;
    }
}
