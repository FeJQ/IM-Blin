package com.fejq.blin.net;

import android.util.Log;

import com.fejq.blin.config.Const;
import com.fejq.blin.model.Client;
import com.fejq.blin.model.message.MessageTask;
import com.fejq.blin.model.message.Request;

import org.json.JSONObject;

import java.nio.charset.Charset;

import javax.net.ssl.SSLEngine;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class ClientChannelInboundHandler extends SimpleChannelInboundHandler<MessageProtocol>
{


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception
    {
        Log.i("Blin", "channelRead0 收到服务器消息:" + msg.getContent());
    }



    /**
     * 通道被激活
     *
     * @param ctx 上下文
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
        Client.getInstance().setConnected(true);
        Client.getInstance().setSendable(true);
        Log.e("Blin", "服务器连接成功");

        // 检测消息队列,并发送消息
        MessageTask.getInstance().sendMessageLooper(ctx);
        // 处理服务器主动发送的消息
        MessageTask.getInstance().receiveMessageLooper();
    }


    /**
     * 收到服务器的消息
     *
     * @param ctx 上线问
     * @param msg 消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        if (!(msg instanceof MessageProtocol))
        {
            return;
        }
        MessageProtocol messageProtocol = (MessageProtocol) msg;
        String message = new String(messageProtocol.getContent(), Charset.forName("utf-8"));

        // 解析收到的消息
        JSONObject root = null;
        try
        {
            root = new JSONObject(message);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }
        String uuid = root.getString("uuid");
        JSONObject status = root.getJSONObject("status");
        int code = status.getInt("code");
        if (code > 5000)
        {
            // 将收到的消息添加到Map
            Client.getInstance().getRecvMap().put(uuid, status);
        }
        else
        {
            // 将收到的响应添加到Map
            Client.getInstance().getResponseMap().put(uuid, status);
        }
    }

    /**
     * 发生异常
     *
     * @param ctx   上下文
     * @param cause 异常信息
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        System.out.println(cause.getMessage());
        Client.getInstance().setSendable(false);
        ctx.close();
    }
}
