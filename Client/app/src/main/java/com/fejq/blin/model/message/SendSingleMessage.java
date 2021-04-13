package com.fejq.blin.model.message;

import org.json.JSONObject;

public class SendSingleMessage extends Message
{
    private String content;
    private int uerID;
    public SendSingleMessage(String token,String content,int userID)
    {
        super();
        this.setToken(token);
        this.content=content;
        this.uerID=userID;
    }

    @Override
    public String toString()
    {
        try
        {
            JSONObject root = new JSONObject();
            root.put("action", Action.SEND_SINGLE);
            root.put("uuid", this.getUuid());
            JSONObject data=new JSONObject();
            data.put("token", this.getToken());
            data.put("content",this.content);
            data.put("userID",this.uerID);
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
