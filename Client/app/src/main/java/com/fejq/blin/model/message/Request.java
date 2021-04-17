package com.fejq.blin.model.message;

import android.graphics.Paint;

import com.fejq.blin.common.Action;
import com.fejq.blin.common.MessageType;
import com.fejq.blin.model.Client;
import com.fejq.blin.model.entity.User;
import com.fejq.utils.UuidUtil;


import org.json.JSONException;
import org.json.JSONObject;


public  class Request
{
    private String uuid;
    public String getUuid()
    {
        return uuid;
    }
    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    private String content;
    public String getContent()
    {
        return content;
    }

    // 接收到响应的回调接口
    private OnRecvListener onRecvListener;
    public OnRecvListener getOnRecvListener()
    {
        return onRecvListener;
    }
    public void setOnRecvListener(OnRecvListener onRecvListener)
    {
        this.onRecvListener = onRecvListener;
    }

    public Request()
    {
        String uuid = UuidUtil.make();
        this.setUuid(uuid);
    }


    /**
     * 发送消息
     */
    public void send()
    {
        send(null);
    }

    /**
     * 发送消息
     *
     * @param onRecvListener 处理服务器响应的回调函数
     */
    public void send(OnRecvListener onRecvListener)
    {
        //String msg=this.toString();
        // msg.encrypt()
        Client.getInstance().pushMessage(this);
        setOnRecvListener(onRecvListener);
    }


    /**
     * 用户名密码登录
     * @param user 用户(userName,password
     * @return
     */
    public Request login(User user)
    {
        try
        {
            JSONObject root = new JSONObject();
            root.put("action", Action.LOGIN_FIRST);
            root.put("uuid", this.getUuid());
            JSONObject data = new JSONObject();
            data.put("userName", user.getUserName());
            data.put("password", user.getPassword());
            root.put("data", data);
            content = root.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * token自动登录
     * @param user 用户(userId,token
     * @return
     */
    public Request loginWithToken(User user)
    {
        try
        {
            JSONObject root = new JSONObject();
            root.put("action", Action.LOGIN_TOKEN);
            root.put("uuid", this.getUuid());
            JSONObject data = new JSONObject();
            data.put("userId", user.getUserId());
            data.put("token", user.getToken());
            root.put("data", data);
            content = root.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 注册账户
     * @param user 用户(userName,password
     * @return
     */
    public Request register(User user)
    {
        try
        {
            JSONObject root = new JSONObject();
            root.put("action", Action.REGISTER);
            root.put("uuid", this.getUuid());
            JSONObject data = new JSONObject();
            data.put("userName", user.getUserName());
            data.put("password", user.getPassword());
            root.put("data", data);
            content = root.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 发送文字私聊信息
     * @param sender 发送者(token,userId
     * @param reveiver 接收者(userId
     * @param text 内容
     * @return
     */
    public Request sendTextMessageToUser(User sender, User reveiver, String text)
    {
        try
        {
            JSONObject root = new JSONObject();
            root.put("action", Action.SEND_SINGLE);
            root.put("uuid", this.getUuid());
            JSONObject data=new JSONObject();
            data.put("token",sender.getToken());
            data.put("type", MessageType.TEXT);
            data.put("senderId", sender.getUserId());
            data.put("receiverId", reveiver.getUserId());
            data.put("content",text);
            root.put("data",data);
            content=root.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }


    /**
     * 获取用户信息
     * @param user 用户(userId,token
     * @return
     */
    public Request getUserInfo(User user)
    {
        try
        {
            JSONObject root = new JSONObject();
            root.put("action", Action.SEND_SINGLE);
            root.put("uuid", this.getUuid());
            JSONObject data=new JSONObject();
            data.put("token",user.getToken());
            data.put("userId",user.getUserId());
            root.put("data",data);
            content=root.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 获取聊天列表
     * @param user 用户(token,userId
     * @return
     */
    public Request getChatList(User user)
    {
        try
        {
            JSONObject root = new JSONObject();
            root.put("action", Action.GET_RECENT_CHAT);
            root.put("uuid", this.getUuid());
            JSONObject data=new JSONObject();
            data.put("token",user.getToken());
            data.put("userId",user.getUserId());
            root.put("data",data);
            content=root.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 获取聊天记录
     * @param me 用户(Id,token
     * @param chatId 对方Id(联系人或群组Id
     * @param type 群组 or 联系人
     * @return
     */
    public Request getChatHistory(User me,int chatId,String chatType)
    {
        try
        {
            JSONObject root = new JSONObject();
            root.put("action", Action.GET_CHAT_HISTORY);
            root.put("uuid", this.getUuid());
            JSONObject data = new JSONObject();
            data.put("token", me.getToken());
            data.put("userId", me.getUserId());
            data.put("chatId", chatId);
            data.put("chatType", chatType);
            root.put("data", data);
            content=root.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }




    public interface OnRecvListener
    {
        void onRecv(int code, String message, JSONObject data) throws JSONException;
    }

}
