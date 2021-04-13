package com.fejq.blin.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fejq.blin.R;
import com.fejq.blin.view.fragment.TopFragment;
import com.fejq.blin.view.fragment.BottomFragment;

public class RecentChatActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_chat);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new TopFragment()).commitAllowingStateLoss();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new BottomFragment()).commitAllowingStateLoss();
    }
}