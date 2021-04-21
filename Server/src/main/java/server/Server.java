package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.Getter;
import util.DBHelper;

import javax.net.ssl.KeyManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.security.KeyStore;

public class Server
{
    private final int port;

    public static boolean isSsl=true;


    public Server(int port)
    {
        this.port = port;
    }


    public void start()
    {



        // 处理连接的loop group
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 处理其他业务的loop group
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new SocketChannelInitializer());


            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("服务器启动成功...");
            ChannelFuture future1 = future.channel().closeFuture().sync();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
