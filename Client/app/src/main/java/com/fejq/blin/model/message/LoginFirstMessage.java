package com.fejq.blin.model.message;

import com.fejq.blin.common.MessageQueue;
import com.fejq.blin.model.User;

import org.json.JSONObject;

public class LoginFirstMessage extends Message
{
    String userName;
    String password;

    public LoginFirstMessage(User user)
    {
        super();

        this.userName = user.getUserName();
        this.password = user.getPassword();
    }

    @Override
    public String toString()
    {
        try
        {
            JSONObject root = new JSONObject();
            root.put("action", Action.LOGIN_FIRST);
            root.put("uuid", this.getUuid());
            JSONObject data=new JSONObject();
            data.put("userName", userName);
            data.put("password", password);
            root.put("data",data);
            return root.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


}
