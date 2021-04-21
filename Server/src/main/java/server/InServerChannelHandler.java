package server;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import server.session.SessionFactory;

import java.nio.charset.Charset;

public class InServerChannelHandler extends SimpleChannelInboundHandler<MessageProtocol>
{

    /**
     * 通道被激活时触发
     *
     * @param ctx 上下文
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("channelActive:" + ctx.channel().remoteAddress());
        SslHandler sslHandler = ctx.pipeline().get(SslHandler.class);
        if (sslHandler == null)
        {
            //channels.add(ctx.channel());
            String logMsg =
                    "RemoteIP: " + ctx.channel().remoteAddress().toString() +
                            ".\n";
            System.out.println(logMsg);
            return;
        }
        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                new GenericFutureListener<Future<Channel>>()
                {
                    @Override
                    public void operationComplete(Future<Channel> future) throws Exception
                    {
                        // channels.add(ctx.channel());
                        String logMsg =
                                "RemoteIP: " + ctx.channel().remoteAddress().toString() +
                                        ".\n" +
                                        "Session is protected by " +
                                        ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite() +
                                        " cipher suite.\n";
                        System.out.println(logMsg);
                    }
                });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("通道被关闭:" + ctx.channel().remoteAddress());
        SessionFactory.getSession().unbind(ctx);
    }

    /**
     * 添加处理器时触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("handlerAdded:" + ctx.channel().remoteAddress());
    }


    /**
     * 接收到客户端的消息时触发
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        //ByteBuf buf=(ByteBuf)msg;
        //System.out.println("channelRead:" + ctx.channel().remoteAddress() + "-" + msg);
        if (msg instanceof MessageProtocol)
        {
            new Thread(() -> {
                MessageHandler messageHandler = new MessageHandler((MessageProtocol) msg, ctx);
                String response = messageHandler.makeResponse();
                MessageProtocol messageProtocol = new MessageProtocol();
                byte[] bytes = response.getBytes(Charset.forName("utf-8"));
                messageProtocol.setLength(bytes.length);
                messageProtocol.setContent(bytes);
                // ctx.channel().writeAndFlush(messageProtocol);
                ctx.writeAndFlush(messageProtocol);
            }).start();
        }
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception
    {
        System.out.println("channelRead0:" + ctx.channel().remoteAddress() + "-" + msg);
    }

}
