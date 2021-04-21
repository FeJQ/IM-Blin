package com.fejq.blin.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.fejq.blin.App;
import com.fejq.blin.model.Client;
import com.fejq.blin.model.entity.Chat;
import com.fejq.blin.model.entity.User;

import java.util.List;

public class Service
{
    /**
     * 存储或更新用户信息
     *
     * @param user
     */
    public static void saveUserInfo(User user)
    {
        Client.getInstance().setCurrentUserId(user.getUserId());
        Client.getInstance().setToken(user.getToken());

//        App.database.execSQL("update user set mainUser=false");
//        Cursor cursor = App.database.query("user", null, "userId=?", new String[]{String.valueOf(user.getUserId())}, null, null, null);
//        if (cursor.getCount() == 0)
//        {
//            // 添加用户id,token到数据库
//            ContentValues values = new ContentValues();
//            values.put("userId", user.getUserId());
//            values.put("token", user.getToken());
//            values.put("mainUser", true);
//            App.database.insert("user", null, values);
//        }
//        else
//        {
//            App.database.execSQL(String.format("update user set token=%s,mainUser=%b where userId=%d", user.getToken(), true,user.getUserId()));
//        }

    }

//    public static User getMainUser()
//    {
//        Cursor cursor = App.database.query("user", null, "mainUser=?", new String[]{"true"}, null, null, null, null);
//        if (cursor.getCount() == 0)
//            return null;
//        int userId = cursor.getInt(cursor.getColumnIndex("userId"));
//        String userName = cursor.getString(cursor.getColumnIndex("userName"));
//        String token = cursor.getString(cursor.getColumnIndex("token"));
//        User user = new User(userId, userName, token);
//        return user;
//    }


}
