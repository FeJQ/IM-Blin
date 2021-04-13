import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Server
{
    private final int port;
    public static boolean isSSL;

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
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        protected void initChannel(SocketChannel socketChannel) throws Exception
                        {
                            System.out.println("客户端连接");
                            ChannelPipeline pipeline = socketChannel.pipeline();
//                            if (isSSL) {
//                                SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
//                                engine.setUseClientMode(false);
//                                pipeline.addLast("ssl", new SslHandler(engine));
//                            }
                            // http-request解码器
                            // http服务器端对request解码
                            pipeline.addLast("decoder", new StringDecoder());
                            // http-response解码器
                            // http服务器端对response编码
                            pipeline.addLast("encoder", new StringEncoder());
                            // 添加 handler
                            pipeline.addLast("handler", new ServerChannelHandler());

                        }
                    });

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
