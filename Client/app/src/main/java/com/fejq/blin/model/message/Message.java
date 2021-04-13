package com.fejq.blin.model.message;

import android.content.Intent;

import com.fejq.blin.config.Const;
import com.fejq.blin.model.Client;
import com.fejq.utils.UuidUtil;


import org.json.JSONException;
import org.json.JSONObject;


public abstract class Message
{



    // 接收到响应的回调接口
    private OnRecvListener onRecvListener;

    private void setOnRecvListener(OnRecvListener onRecvListener)
    {
        this.onRecvListener = onRecvListener;
    }

    public OnRecvListener getOnRecvListener()
    {
        return onRecvListener;
    }

    // 封装数据成json字符串
    public abstract String toString();

    public Message()
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


    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    private String uuid;
    private String token;

    public interface OnRecvListener
    {
        void onRecv(int code, String message,JSONObject data) throws JSONException;
    }

}
