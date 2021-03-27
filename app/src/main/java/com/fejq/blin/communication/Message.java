package com.fejq.blin.communication;

public class Message
{
    enum MessageType
    {
        LOGIN,
        REGISTER,
    }

    class UserInfoPack
    {
        String userName;
        String password;
        String backMessage;
    }


}
