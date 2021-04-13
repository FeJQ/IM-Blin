package com.fejq.blin.model.message;

import com.fejq.blin.model.User;
import com.fejq.utils.UuidUtil;

import org.json.JSONObject;

public class RegisterMessage extends Message
{
    String userName;
    String password;

    public RegisterMessage(User user)
    {
        super();
        String uuid = UuidUtil.make();
        this.setUuid(uuid);
        this.userName = user.getUserName();
        this.password = user.getPassword();
    }

    @Override
    public String toString()
    {
        try
        {
            JSONObject root = new JSONObject();
            root.put("action", Action.REGISTER);
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
