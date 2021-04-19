package com.fejq.blin.model.message;

import com.fejq.blin.common.Action;
import com.fejq.blin.config.Const;
import com.fejq.blin.model.Client;
import com.fejq.blin.net.MessageProtocol;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;


import io.netty.channel.ChannelHandlerContext;

public class MessageTask
{
    /**
     * 检测消息队列,并发送消息
     *
     * @param ctx 上下文
     */
    public static void sendMessageLooper(ChannelHandlerContext ctx)
    {
        new Thread(() -> {
            while (Client.getInstance().sendable())
            {
                Request request = Client.getInstance().popMessage();
                if (request == null)
                {
                    try
                    {
                        Thread.sleep(Const.IDLE_TIME);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    continue;
                }
                new Thread(() -> {
                    //Log.i("uuid", request.getUuid());
                    // 发送消息
                    MessageProtocol messageProtocol = new MessageProtocol();
                    byte[] bytes = request.getContent().getBytes(Charset.forName("utf-8"));
                    messageProtocol.setLength(bytes.length);
                    messageProtocol.setContent(bytes);
                    ctx.writeAndFlush(messageProtocol);

                    // 等待响应
                    long startTimeMillis = System.currentTimeMillis();
                    Request.OnRecvListener onRecvListener = request.getOnRecvListener();

                    while (onRecvListener != null)
                    {
                        try
                        {
                            if (System.currentTimeMillis() - startTimeMillis >= Const.TIMEOUT)
                            {
                                // 超时
                                int code = -1;
                                String msg = "请求超时";
                                JSONObject data = new JSONObject();
                                onRecvListener.onRecv(code, msg, data);
                                break;
                            }
                            JSONObject status = Client.getInstance().getResponseMap().get(request.getUuid());
                            if (status == null)
                            {
                                Thread.sleep(Const.IDLE_TIME);
                                continue;
                            }
                            int code = status.getInt("code");
                            String msg = status.getString("message");
                            JSONObject data = status.getJSONObject("data");
                            onRecvListener.onRecv(code, msg, data);
                            Client.getInstance().getResponseMap().remove(request.getUuid());
                            break;
                        }
                        catch (Exception e)
                        {
                            //e.printStackTrace();
                            String s = e.getMessage();
                        }
                    }
                }).start();
            }
        }).start();
    }

    // 接收到好友发来的消息回调
    public interface OnRecvFriendMessageListener
    {
        void onRecvFriendMessage(String uuid, JSONObject status) throws JSONException;
    }
    private static OnRecvFriendMessageListener onRecvFriendMessageListener;
    public static void setOnRecvFriendMessageListener(OnRecvFriendMessageListener onRecvFriendMessageListener)
    {
        onRecvFriendMessageListener = onRecvFriendMessageListener;
    }

    /**
     * 处理服务器主动发来的消息
     * 主动发送的消息与响应消息相对,是在客户端没有发送请求的情况下,服务器主动向客户端发送的消息
     * 如,联系人发来消息,如果我在线,则服务器会主动将消息推送过来,我将这一类消息称为主动消息,
     * 我使用大于5000的值来标识这类消息的code
     */
    public static void receiveMessageLooper()
    {
        new Thread(() -> {
            Map<String, JSONObject> recvMap = Client.getInstance().getRecvMap();
            Iterator<Map.Entry<String, JSONObject>> iterator = recvMap.entrySet().iterator();
            while (iterator.hasNext())
            {
                Map.Entry<String, JSONObject> entry = iterator.next();
                String uuid = entry.getKey();
                JSONObject status = entry.getValue();
                try
                {
                    int code = status.getInt("code");
                    switch (code)
                    {
                        case Action.RECEIVED_FRIEND_MESSAGE:
                            onRecvFriendMessageListener.onRecvFriendMessage(entry.getKey(), entry.getValue());
                            break;
                        default:
                            break;
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                iterator.remove();
            }
            try
            {
                Thread.sleep(Const.IDLE_TIME);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }).start();
    }
}
