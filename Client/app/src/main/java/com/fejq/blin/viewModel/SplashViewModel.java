package com.fejq.blin.viewModel;

import android.content.Context;
import android.content.SharedPreferences;

import com.fejq.blin.App;
import com.fejq.blin.R;

public class SplashViewModel
{
    public boolean checkUserToken()
    {
        Context context = App.getContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_preference_name), context.MODE_PRIVATE);

        String savedUserName = sharedPreferences.getString(context.getString(R.string.shared_user_name), null);
        String savedToken = sharedPreferences.getString(context.getString(R.string.shared_token), null);
        if (savedUserName == null || savedToken == null)
        {
            return false;
        }
        return false;
    }
}
