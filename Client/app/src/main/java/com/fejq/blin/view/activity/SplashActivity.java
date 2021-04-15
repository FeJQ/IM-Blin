package com.fejq.blin.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.fejq.blin.R;
import com.fejq.blin.viewModel.SplashViewModel;

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
        if (splashViewModel.checkUserToken())
        {
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