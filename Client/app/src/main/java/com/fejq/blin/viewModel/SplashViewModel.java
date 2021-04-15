package com.fejq.blin.viewModel;

import android.content.Context;

import com.fejq.blin.App;
import com.fejq.blin.database.Service;
import com.fejq.blin.model.entity.User;

public class SplashViewModel
{
    public boolean checkUserToken()
    {
        Context context = App.getContext();
        User mainUser = Service.getMainUser();
        if(mainUser==null || mainUser.getToken()==null)
        {
            return false;
        }
        return true;
    }
}
