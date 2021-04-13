package com.fejq.blin.model.message;

import org.json.JSONObject;

public class LoginTokenMessage extends Message
{
    public LoginTokenMessage(String token)
    {
        super();
        this.setToken(token);
    }

    @Override
    public String toString()
    {
        try
        {
            JSONObject root = new JSONObject();
            root.put("action", Action.LOGIN_TOKEN);
            root.put("uuid", this.getUuid());
            JSONObject data=new JSONObject();
            data.put("token", this.getToken());
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
