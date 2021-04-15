package com.fejq.blin.net;

import com.fejq.blin.config.Const;

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
                // 等待与服务器断开连接
                channelFuture.channel().closeFuture().sync();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            finally
            {
                eventExecutors.shutdownGracefully();
            }
        }).start();

    }
}
