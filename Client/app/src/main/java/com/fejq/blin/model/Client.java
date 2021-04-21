package com.fejq.blin.model;

import com.fejq.blin.common.MessageQueue;
import com.fejq.blin.model.message.Request;
import com.fejq.blin.net.TcpClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Client extends TcpClient
{
    public static boolean isSsl=true;
    // 客户端收到的消息Map
    private Map<String, JSONObject> recvMap;
    public Map<String, JSONObject> getRecvMap()
    {
        return recvMap;
    }

    // 客户端收到的响应Map
    private Map<String, JSONObject> responseMap;
    public Map<String, JSONObject> getResponseMap()
    {
        return responseMap;
    }

    // 客户端待发送的消息队列
    private MessageQueue<Request> messageQueue;
    public void pushMessage(Request request)
    {
        messageQueue.push(request);
    }
    public Request popMessage()
    {
        return messageQueue.next();
    }

    // 标志客户端是否可以向服务器发送消息,如果为false,则不会对 发送消息队列 进行处理
    private boolean sendable = true;
    public boolean sendable()
    {
        return sendable;
    }
    public void setSendable(boolean val)
    {
        sendable = val;
    }

    // Client唯一实例
    private static Client client;
    private Client()
    {
        recvMap=new HashMap<>();
        messageQueue = new MessageQueue();
        responseMap=new HashMap<>();
    }
    // 获取Client唯一实例
    public static Client getInstance()
    {
        if(client==null)
        {
            client=new Client();
        }
        Client.getTcpClient().connect();
        return client;
    }

    // TcpClient唯一实例
    private static TcpClient tcpClient;
    public static TcpClient getTcpClient()
    {
        if (tcpClient == null)
        {
            tcpClient = new TcpClient();
        }
        return tcpClient;
    }

    // 全局保存用户Id
    private int currentUserId;
    public int getCurrentUserId()
    {
        return currentUserId;
    }
    public void setCurrentUserId(int currentUserId)
    {
        this.currentUserId = currentUserId;
    }

    // 全局保存用户token
    private String token;
    public String getToken()
    {
        return token;
    }
    public void setToken(String token)
    {
        this.token = token;
    }







}
