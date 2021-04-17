package com.fejq.blin.viewModel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.fejq.blin.App;
import com.fejq.blin.R;
import com.fejq.blin.database.Service;
import com.fejq.blin.model.Client;
import com.fejq.blin.model.message.Request;
import com.fejq.blin.model.entity.User;
import com.fejq.blin.view.activity.LoginActivity;
import com.fejq.blin.view.activity.MainActivity;
import com.fejq.utils.ToastUtil;

public class LoginViewModel
{
    private User user = new User();
    private LoginActivity context;

    public LoginViewModel(LoginActivity activity)
    {
        context = activity;
    }

    public void onLogin()
    {
        try
        {
            if (!onLoginCheck())
            {
                ToastUtil.showShortToast(context, "用户名或密码格式错误");
                return;
            }
            Request request = new Request().login(user);
            request.send((code, msg, data) -> {
                if (code == 0)
                {
                    int userId = data.getInt("userId");
                    String token = data.getString("token");
                    User user = new User();
                    user.setUserId(userId);
                    user.setToken(token);
                    Service.saveUserInfo(user);

                    Intent intent=new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    ToastUtil.showShortToast(context, msg);
                }
                else
                {
                    ToastUtil.showShortToast(context, msg);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean onLoginCheck()
    {
        if (user.getUserName().isEmpty() || user.getPassword().isEmpty())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void saveUserInfo()
    {
        //实例化SharedPreferences对象（第一步）
        SharedPreferences sp = App.getContext().getSharedPreferences(App.getContext().getString(R.string.shared_preference_name), Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = sp.edit();
        //用putString的方法保存数据
        editor.putString("userName", user.getUserName());
        editor.putString("token", user.getToken());
        //提交当前数据
        editor.commit();
    }

    public User getUserInfo()
    {
        return user;
    }
}