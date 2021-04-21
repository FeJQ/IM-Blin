package com.fejq.blin.net;

import com.fejq.blin.config.Const;
import com.fejq.blin.model.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TcpClient
{
    private boolean isConnected;

    public boolean isConnected()
    {
        return isConnected;
    }

    public void setConnected(boolean connected)
    {
        isConnected = connected;
    }

    public TcpClient()
    {

    }


    /**
     * 连接服务器
     */
    public void connect()
    {
        if (isConnected)
        {
            return;
        }
        new Thread(() -> {
            EventLoopGroup eventExecutors = new NioEventLoopGroup();
            try
            {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(eventExecutors)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.SO_KEEPALIVE, true)
                        .handler(new TcpClientChannelInitializer());

                ChannelFuture channelFuture = bootstrap.connect(Const.SERVER_ADDRESS, Const.PORT).sync();
                isConnected = true;
                // 等待与服务器断开连接
                channelFuture.channel().closeFuture().sync();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                eventExecutors.shutdownGracefully();
                isConnected = false;
                //Log.e("Blin", "服务器连接断开");
                Client.getInstance().setSendable(false);
            }
        }).start();

    }
}
