package com.fejq.blin.net;

import android.util.Log;

import com.fejq.blin.config.Const;
import com.fejq.blin.model.Client;
import com.fejq.blin.model.message.MessageDecoder;
import com.fejq.blin.model.message.MessageEncoder;
import com.fejq.blin.model.message.Request;

import org.json.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class TcpClientChannelInitializer extends ChannelInitializer
{
    @Override
    protected void initChannel(Channel channel) throws Exception
    {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new MessageEncoder());
        pipeline.addLast(new MessageDecoder());
        pipeline.addLast(new ClientChannelInboundHandler());
    }
}
