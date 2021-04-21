package com.fejq.blin.view.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.fejq.blin.R;
import com.fejq.blin.viewModel.SplashViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SplashActivity extends AppCompatActivity
{
   SplashViewModel splashViewModel=new SplashViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        showLogo();
        checkUserInfo();
    }

    private void showLogo()
    {

    }

    private void checkUserInfo()
    {
        if (false)
        {
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            //Intent intent = new Intent(this, RecentChatActivity.class);
            //startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

}