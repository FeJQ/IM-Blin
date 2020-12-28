package com.fejq.communication;

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
