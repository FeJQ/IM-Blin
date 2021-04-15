package com.fejq.blin.model.entity;

import org.json.JSONObject;


public class User
{

    private int userId;
    private String userName;
    private String password;
    private String token;

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public User(){};

    public User(int userId,String userName,String token)
    {
        this.userId=userId;
        this.userName=userName;
        this.token=token;
    }


}
