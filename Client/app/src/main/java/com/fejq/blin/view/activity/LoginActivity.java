package com.fejq.blin.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;

import com.fejq.blin.R;
import com.fejq.blin.databinding.ActivityLoginBinding;
import com.fejq.blin.viewModel.LoginViewModel;

public class LoginActivity extends AppCompatActivity
{
    LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel=new LoginViewModel(this);

        ActivityLoginBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel.getUserInfo().setUserName("Bob");
        viewModel.getUserInfo().setPassword("123");
        viewDataBinding.setViewModle(viewModel);
    }


}