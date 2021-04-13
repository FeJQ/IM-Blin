package com.fejq.blin.model;

import com.fejq.blin.common.MessageQueue;
import com.fejq.blin.model.message.Message;
import com.fejq.blin.net.TcpClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Client extends TcpClient
{
    // 客户端收到的消息Map
    private Map<String, JSONObject> recvMap;

    public Map<String, JSONObject> getRecvMap()
    {
        return recvMap;
    }

    // 客户端待发送的消息队列
    private MessageQueue<Message> messageQueue;

    // 添加消息到消息队列
    public void pushMessage(Message message)
    {
        messageQueue.push(message);
    }

    // 从消息队列中取出消息
    public Message popMessage()
    {
        return messageQueue.next();
    }

    // 标志客户端是否可以向服务器发送消息
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
        messageQueue = new MessageQueue();
        recvMap=new HashMap<>();
    }
    // 获取Client唯一实例
    public static Client getInstance()
    {
        if(client==null)
        {
            client=new Client();
            Client.getTcpClient().connect();
        }
        return client;
    }



    private static TcpClient tcpClient;

    public static TcpClient getTcpClient()
    {
        if (tcpClient == null)
        {
            tcpClient = new TcpClient();
        }
        return tcpClient;
    }

}
