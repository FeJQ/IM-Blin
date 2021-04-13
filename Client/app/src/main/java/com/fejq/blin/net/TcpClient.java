package com.fejq.blin.net;

import android.util.Log;

import com.fejq.blin.common.MessageQueue;
import com.fejq.blin.config.Const;
import com.fejq.blin.model.message.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TcpClient
{
    public TcpClient()
    {

    }


    /**
     * 连接服务器
     */
    public void connect()
    {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try
        {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new TcpClientChannelInitializer());

            ChannelFuture channelFuture = bootstrap.connect(Const.SERVER_ADDRESS, Const.PORT).sync();
            // 等待与服务器断开连接
            channelFuture.channel().closeFuture().addListener(future -> {
                Log.i("Netty","通道被关闭");
                eventExecutors.shutdownGracefully();
            });
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
